/*
 * Copyright 2025 Gufran Thakur
 *
 * Licensed under the MIT License. See the LICENSE file for details.
 */
package flowforge;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatInspector;
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

public class FlowForge extends JFrame {

    public Timer loop;
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

        loop = new Timer(1, e -> {
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
        SwingWorker<Void, Void> nodeExecutor = new SwingWorker<>() {
            @Override
            protected Void doInBackground()  {
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

    public void changeTheme(LookAndFeel lookAndFeel) {
        FlatAnimatedLafChange.showSnapshot();

        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }
        FlatLaf.updateUI();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();

        console.updateGUI();
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
        //FlatInspector.install( "ctrl shift V" );

        SwingUtilities.invokeLater(() -> {
            FlowForge flowForge = new FlowForge();
            flowForge.init();
            flowForge.addComponent();
        });
    }

}
