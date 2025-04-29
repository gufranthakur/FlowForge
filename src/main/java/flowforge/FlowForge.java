/*
 * Copyright 2025 Gufran Thakur
 *
 * Licensed under the MIT License. See the LICENSE file for details.
 */

package flowforge;

import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import flowforge.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FlowForge extends JFrame {

    public Timer loop;
    public DataManager dataManager;
    public String projectFilePath;

    public JPanel programPanelContainer;
    public StartPanel startPanel;

    public AppMenuBar menuBar;

    public ControlPanel controlPanel;
    public ProgramPanel programPanel;
    public Console console;

    public JPanel consoleOverlayPanel;

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

        // Modified: Use null layout for overlay panel
        consoleOverlayPanel = new JPanel(null);
        consoleOverlayPanel.setOpaque(false);
        consoleOverlayPanel.setVisible(true);

        programPanelContainer = new JPanel(new BorderLayout());
        programPanelContainer.add(programPanel, BorderLayout.CENTER);

        programPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                programPanel.requestFocusInWindow();
            }
        });

        loop = new Timer(5, e -> {
            programPanel.repaint();
            programPanel.moveCamera();
        });

        controlPanel.init();
        menuBar.init();
        menuBar.initListeners();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateConsolePosition();
            }
        });
    }

    private void updateConsolePosition() {
        if (console != null && console.getRootPanel() != null && consoleOverlayPanel != null) {
            switch (console.getCurrentDockPosition()) {
                case "SOUTH":
                    console.getRootPanel().setBounds(0, getHeight() - console.getPreferredHeight(),
                            getWidth(), console.getPreferredHeight());
                    break;
                case "EAST":
                    console.getRootPanel().setBounds(getWidth() - console.getPreferredWidth(), 0,
                            console.getPreferredWidth(), getHeight());
                    break;
                case "CENTER":
                    console.getRootPanel().setBounds(0, 0, getWidth(), getHeight());
                    break;
            }
            consoleOverlayPanel.revalidate();
            consoleOverlayPanel.repaint();
        }
    }

    public void addComponent() {
        controlPanel.addComponent();
        menuBar.addComponent();

        this.add(startPanel, BorderLayout.CENTER);
        this.setVisible(true);

        loop.start();
    }

    public void run() {
        programPanel.startNode.execute();
        console.getRootPanel().setVisible(true);
    }

    public void launch() {
        startPanel.setVisible(false);
        menuBar.launch();

        this.add(controlPanel.getRootPanel(), BorderLayout.WEST);
        this.add(programPanelContainer, BorderLayout.CENTER);

        consoleOverlayPanel.add(console.getRootPanel());
        console.getRootPanel().setBounds(0, getHeight() - 250, getWidth(), 250);

        this.setGlassPane(consoleOverlayPanel);
        consoleOverlayPanel.setVisible(true);

        this.setExtendedState(MAXIMIZED_BOTH);
        this.repaint();
        this.revalidate();

        console.print("Welcome to FlowForge!");
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatMacDarkLaf());
        FlatInspector.install("ctrl shift V");

        SwingUtilities.invokeLater(() -> {
            FlowForge flowForge = new FlowForge();
            flowForge.init();
            flowForge.addComponent();
        });
    }
}