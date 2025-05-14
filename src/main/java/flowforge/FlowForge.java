/*
 * Copyright 2025 Gufran Thakur
 *
 * Licensed under the MIT License. See the LICENSE file for details.
 */
package flowforge;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import flowforge.core.*;
import flowforge.core.ui.MenuBar.AboutPanel;
import flowforge.core.ui.MenuBar.AppMenuBar;
import flowforge.core.ui.MenuBar.ChangeLogPanel;
import flowforge.core.ui.panels.Console;
import flowforge.core.ui.panels.ControlPanel;
import flowforge.core.ui.panels.ProgramPanel;
import flowforge.core.ui.panels.StartPanel;

import javax.swing.*;
import java.awt.*;

public class FlowForge extends JFrame implements Runnable{

    public DataManager dataManager;
    public String projectFilePath;

    public JPanel programPanelContainer;
    public StartPanel startPanel;

    public AppMenuBar menuBar;
    public AboutPanel aboutPanel;
    public ChangeLogPanel changeLogPanel;

    public ControlPanel controlPanel;
    public ProgramPanel programPanel;
    public Console console;

    public Color theme = new Color(26, 77, 236).brighter();

    private boolean isRunning = false;


    public FlowForge() {
        this.setTitle("FlowForge");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() {
        console = new Console(this);
        startPanel = new StartPanel(this);
        controlPanel = new ControlPanel(this);
        programPanel = new ProgramPanel(this);
        dataManager = new DataManager(programPanel);

        menuBar = new AppMenuBar(this);
        aboutPanel = new AboutPanel(this);
        changeLogPanel = new ChangeLogPanel(this);

        programPanelContainer = new JPanel(null);

        controlPanel.init();
        menuBar.init();
        menuBar.initListeners();

    }

    public void addComponent() {
        controlPanel.addComponent();
        menuBar.addComponent();

        this.add(startPanel, BorderLayout.CENTER);
        this.setVisible(true);

        isRunning = true;
    }

    public void execute() {
        SwingWorker<Void, Void> nodeExecutor = new SwingWorker<>() {
            @Override
            protected Void doInBackground()  {
                programPanel.startNode.execute(false);
                return null;
            }
            @Override
            protected void done() {
                console.getRootPanel().setVisible(true);
            }
        };

        nodeExecutor.execute();
    }

    public void executeByStep() {
        SwingWorker<Void, Void> nodeStepExecutor = new SwingWorker<>() {
            @Override
            protected Void doInBackground()  {
                synchronized (programPanel.stepExecutorLock) {
                    programPanel.startNode.execute(true);
                }
                return null;
            }
            @Override
            protected void done() {
                console.getRootPanel().setVisible(true);
            }
        };

        nodeStepExecutor.execute();
    }


    public void launch() {
        programPanelContainer.add(programPanel);

        startPanel.setVisible(false);
        menuBar.launch();

        this.add(controlPanel.getRootPanel(), BorderLayout.WEST);
        this.add(programPanelContainer, BorderLayout.CENTER);
        this.add(console.getRootPanel(), BorderLayout.SOUTH);

        console.print("Welcome to FlowForge!");

        this.setExtendedState(MAXIMIZED_BOTH);
        this.repaint();
        this.revalidate();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / 120;
        double timePerUpdate = 1000000000.0 / 60;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (isRunning) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                programPanel.moveCamera();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                programPanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                frames = 0;
                updates = 0;

            }
        }

    }

    public static Thread flowForgeThread;

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatMacDarkLaf());
        FlatJetBrainsMonoFont.install();
        //FlatInspector.install( "ctrl shift V" );

        SwingUtilities.invokeLater(() -> {
            FlowForge flowForge = new FlowForge();
            flowForge.init();
            flowForge.addComponent();

            flowForgeThread = new Thread(flowForge);
            flowForgeThread.start();
        });


    }


}
