/*
 * Copyright 2025 Gufran Thakur
 *
 * Licensed under the MIT License. See the LICENSE file for details.
 */
package flowforge;

import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import flowforge.core.*;
import flowforge.core.panels.Console;
import flowforge.core.panels.ControlPanel;
import flowforge.core.panels.ProgramPanel;
import flowforge.core.panels.StartPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlowForge extends JFrame {

    public Timer loop;
    private Thread loopThread;
    public DataManager dataManager;
    public String projectFilePath;

    public JPanel programPanelContainer;
    public StartPanel startPanel;

    public AppMenuBar menuBar;

    public ControlPanel controlPanel;
    public ProgramPanel programPanel;
    public Console console;


    public Color theme = new Color(26, 77, 236);

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

        programPanelContainer = new JPanel(null);

        programPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                programPanel.requestFocusInWindow();

                for (Node node : programPanel.nodes) {
                    node.inputButton.setEnabled(true);
                    node.outputButton.setEnabled(true);
                    node.inputXButton.setEnabled(true);
                    node.outputXButton.setEnabled(true);
                }
            }
        });

        loop = new Timer(16, e -> {
            programPanel.repaint(programPanel.getVisibleRect());
            programPanel.moveCamera();
        });

        controlPanel.init();
        menuBar.init();
        menuBar.initListeners();

    }

    public void addComponent() {
        controlPanel.addComponent();
        menuBar.addComponent();

        this.add(startPanel, BorderLayout.CENTER);
        this.setVisible(true);

        loop.start();
    }

    public void execute() {

        SwingWorker<Void, Void> nodeExecutor = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                programPanel.startNode.execute();
                return null;
            }
            @Override
            protected void done() {
                console.getRootPanel().setVisible(true);
            }
        };

        nodeExecutor.execute();
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

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatMacDarkLaf());
        FlatInspector.install( "ctrl shift V" );

        SwingUtilities.invokeLater(() -> {
            FlowForge flowForge = new FlowForge();
            flowForge.init();
            flowForge.addComponent();
        });
    }

}
