package flowforge.ui.MenuBar;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import flowforge.FlowForge;
import flowforge.nodes.Node;
import flowforge.nodes.StartNode;
import flowforge.nodes.flownodes.BranchNode;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;
import flowforge.nodes.variables.StringNode;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AppMenuBar extends JMenuBar {

    private FlowForge flowForge;
    private GraphicsEnvironment environment;
    private GraphicsDevice device;

    private JMenu moreMenu;
        private JMenuItem aboutItem, chengelogItem, checkForUpdateItem;
    private JMenu projectMenu;
        private JMenuItem newProjectItem, openProjectItem, closeProjectItem,
            saveProjectItem, saveAsProjectItem, viewDocumentationItem, exitAppItem,
            projectPropertiesItem, settingsItem;

    private JMenu viewMenu;
        private JMenuItem fullscreenItem, presentationModeItem,
                showHideSideBar, showHideConsole, showHideToolbar;

    private JMenu codeMenu;
        private JMenuItem runItem, runWithStepsItem, stopExecutionItem,
                minimizeAllNodesItem, maximiseAllNodesItem, removeAllHighlightItem,
                clearAllConnectionsItem, removeAllNodesItem;


    public boolean aboutPanelVisible, changelogPanelVisible = false;
    private boolean isFullScreen, isPresentationMode;

    public AppMenuBar(FlowForge flowForge) {
        this.flowForge = flowForge;

        environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
    }

    public void init() {
        // Initialize main menu items
        moreMenu = new JMenu("More");
        projectMenu = new JMenu("Project");
        viewMenu = new JMenu("View");
        codeMenu = new JMenu("Code");

        //about
        aboutItem = new JMenuItem("About FlowForge");
        chengelogItem = new JMenuItem("View Changelog");
        checkForUpdateItem = new JMenuItem("Check for Updates");

        viewDocumentationItem = new JMenuItem("Documentation ↗");
        // Initialize Project menu items
        newProjectItem = new JMenuItem("New Project");
        openProjectItem = new JMenuItem("Open Project");
        closeProjectItem = new JMenuItem("Close Project");
        saveProjectItem = new JMenuItem("Save");
        saveAsProjectItem = new JMenuItem("Save As");

        projectPropertiesItem = new JMenuItem("Project Properties");
        settingsItem = new JMenuItem("Settings");
        exitAppItem = new JMenuItem("Exit");


        // Initialize View menu items
        fullscreenItem = new JMenuItem("Fullscreen");
        presentationModeItem = new JMenuItem("Presentation Mode");
        showHideSideBar = new JMenuItem("Toggle Sidebar");
        showHideConsole = new JMenuItem("Toggle Console");
        showHideToolbar = new JMenuItem("Toggle Toolbar");


        // Init codeMenu items
        runItem = new JMenuItem("Run");
        runWithStepsItem = new JMenuItem("Run with Steps");
        stopExecutionItem = new JMenuItem("Stop Execution");
        minimizeAllNodesItem = new JMenuItem("Minimize all nodes");
        maximiseAllNodesItem = new JMenuItem("Maximize all nodes");
        removeAllHighlightItem = new JMenuItem("Clear all Highlight");
        clearAllConnectionsItem = new JMenuItem("Clear all connections");
        removeAllNodesItem = new JMenuItem("Remove all Nodes");

    }

    public void addComponent() {
        this.add(moreMenu);
        moreMenu.add(aboutItem);
        moreMenu.add(chengelogItem);
        moreMenu.addSeparator();
        moreMenu.add(checkForUpdateItem);

        projectMenu.add(newProjectItem);
        projectMenu.add(openProjectItem);
        projectMenu.add(closeProjectItem);
        projectMenu.addSeparator();
        projectMenu.add(saveProjectItem);
        projectMenu.add(saveAsProjectItem);
        projectMenu.addSeparator();
        //projectItem.add(projectPropertiesItem);
        //projectItem.add(settingsItem);
        //projectItem.addSeparator();
        projectMenu.add(exitAppItem);
        projectMenu.addSeparator();
        projectMenu.add(viewDocumentationItem);

        viewMenu.add(fullscreenItem);
        viewMenu.add(presentationModeItem);
        viewMenu.addSeparator();
        viewMenu.add(showHideSideBar);
        viewMenu.add(showHideConsole);
        viewMenu.add(showHideToolbar);


        codeMenu.add(runItem);
        codeMenu.add(runWithStepsItem);
        codeMenu.add(stopExecutionItem);
        codeMenu.addSeparator();
        codeMenu.add(minimizeAllNodesItem);
        codeMenu.add(maximiseAllNodesItem);
        codeMenu.add(removeAllHighlightItem);
        codeMenu.addSeparator();
        codeMenu.add(clearAllConnectionsItem);
        codeMenu.add(removeAllNodesItem);


        flowForge.setJMenuBar(this);
    }

    public void initListeners() {
        initStartMenuListeners();
        initProjectMenuListeners();
        initViewMenuListeners();
        initCodeMenuItems();
    }

    private void initStartMenuListeners() {
        aboutItem.addActionListener(e -> {
            if (aboutPanelVisible) return;
            flowForge.remove(flowForge.startPanel);
            flowForge.add(flowForge.aboutPanel.getRootPanel(), BorderLayout.CENTER);

            aboutPanelVisible = true;

            flowForge.revalidate();
            flowForge.repaint();
        });

        chengelogItem.addActionListener(e -> {
            if (changelogPanelVisible) return;
            flowForge.remove(flowForge.startPanel);
            flowForge.add(flowForge.changeLogPanel.getRootPanel(), BorderLayout.CENTER);

            changelogPanelVisible = true;

            flowForge.revalidate();
            flowForge.repaint();
        });

        checkForUpdateItem.addActionListener(e -> {
            if (!flowForge.checkForUpdate()) {
                JOptionPane.showMessageDialog(null,
                        "FlowForge is up to date",
                        "No updates available", JOptionPane.INFORMATION_MESSAGE);
            }

        });
    }

    private void initProjectMenuListeners() {
        newProjectItem.addActionListener(e -> {
            flowForge.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getNewInstance(true);
        });

        openProjectItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("FlowForge Programs", "flow");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                flowForge.launch();
                flowForge.dataManager.loadProgram(filePath);
                flowForge.projectFilePath = filePath;

                flowForge.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                FlowForge newInstance = getNewInstance(true);

                newInstance.dataManager.loadProgram(filePath);
                newInstance.projectFilePath = filePath;
            }
        });

        saveProjectItem.addActionListener(e -> {
            flowForge.dataManager.saveProgram(flowForge.projectFilePath);
            flowForge.console.printSaveStatement();
        });

        saveAsProjectItem.addActionListener( e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".flow")) {
                    filePath += ".flow";
                }
                flowForge.dataManager.saveProgram(filePath);
                flowForge.projectFilePath = filePath;
                flowForge.console.printSaveStatement();
            }
        });

        closeProjectItem.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(
                    null, "Any unsaved changes will be lost",
                    "Close Project?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                getNewInstance(false);
                flowForge.dispose();
            }
        });

        exitAppItem.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null, "Any unsaved changes will be lost",
                    "Exit FlowForge?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
                System.exit(0);
            }
        });

        viewDocumentationItem.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("https://gufrans-organization.gitbook.io/flowforge-docs"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (URISyntaxException ex) {
                System.out.println("Exception caught : wrong URL");
            }
        });
    }

    private void initViewMenuListeners() {
        fullscreenItem.addActionListener(e -> {
            if (!isFullScreen) {
                flowForge.dispose();
                flowForge.setUndecorated(true);
                device.setFullScreenWindow(flowForge);
            } else {
                device.setFullScreenWindow(null);
                flowForge.dispose();
                flowForge.setUndecorated(false);
                flowForge.setVisible(true);
            }
            isFullScreen = !isFullScreen;
        });

        presentationModeItem.addActionListener(e -> {
            var controlPanel = flowForge.controlPanel.getRootPanel();
            var console = flowForge.console.getRootPanel();
            var toolbar = flowForge.controlPanel.toolBar;

            if (!isPresentationMode) {
                controlPanel.setVisible(false);
                console.setVisible(false);
                toolbar.setVisible(false);
            } else {
                controlPanel.setVisible(true);
                console.setVisible(true);
                toolbar.setVisible(true);
            }
            isPresentationMode = !isPresentationMode;
        });

        showHideSideBar.addActionListener(e -> {
            var controlPanel = flowForge.controlPanel.getRootPanel();
            controlPanel.setVisible(!controlPanel.isVisible());
        });

        showHideConsole.addActionListener(e -> {
            if (!flowForge.console.isMinimized) flowForge.console.minimize();
            else flowForge.console.resizeToDefault();

            flowForge.repaint();
            flowForge.revalidate();
        });

        showHideToolbar.addActionListener(e -> {
            var toolbar = flowForge.controlPanel.toolBar;
            toolbar.setVisible(!toolbar.isVisible());
        });

    }

    private void initCodeMenuItems() {
        runItem.addActionListener(e -> {
            flowForge.controlPanel.runStopButton.doClick();
        });

        runWithStepsItem.addActionListener(e -> {
            flowForge.controlPanel.runWithStepsButton.doClick();
        });

        stopExecutionItem.addActionListener(e -> {
            flowForge.controlPanel.stopButton.doClick();
        });

        minimizeAllNodesItem.addActionListener(e -> {
            for (Node node : flowForge.programPanel.nodes) {
                node.setSize(150, 40);
                node.isMinimized = true;
                node.contentPanel.setVisible(false);
            }
            flowForge.programPanel.repaint();
            flowForge.programPanel.revalidate();
        });

        maximiseAllNodesItem.addActionListener(e -> {
            for (Node node : flowForge.programPanel.nodes) {
                if (node instanceof StringNode
                        || node instanceof IntegerNode
                        || node instanceof BooleanNode
                        || node instanceof BranchNode) {
                    node.setSize(200, 100);
                    node.isMinimized = false;
                    node.contentPanel.setVisible(true);
                } else {
                    node.setSize(200, 150);
                    node.isMinimized = false;
                    node.contentPanel.setVisible(true);
                }

            }
            flowForge.programPanel.repaint();
            flowForge.programPanel.revalidate();
        });

        removeAllHighlightItem.addActionListener(e -> {
            for (Node node : flowForge.programPanel.nodes) {
                node.isHighlighted = false;
                node.restoreBorder();
            }
        });

        clearAllConnectionsItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "This action cannot be reversed.\nAll connections will be cleared", "Delete all connections?" ,
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                for (Node node : flowForge.programPanel.nodes) {
                    node.disconnectAll();
                }
            }

        });

        removeAllNodesItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "This action cannot be reversed.\nAll Nodes will be deleted", "Delete all Nodes?" ,
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                for (int i = flowForge.programPanel.nodes.size() - 1; i >= 0; i--) {
                    Node node = flowForge.programPanel.nodes.get(i);
                    if (!(node instanceof StartNode)) {
                        flowForge.programPanel.removeNode(node);
                    }
                }
            }
        });
    }

    private FlowForge getNewInstance(boolean launchOnCreation) {
        FlowForge newInstance = new FlowForge();
        newInstance.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        flowForge.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                newInstance.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            }
        });

        newInstance.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                flowForge.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            }
        });

        newInstance.init();
        newInstance.addComponent();

        if (launchOnCreation) newInstance.launch();

        return newInstance;
    }



    public void launch() {
        this.remove(moreMenu);

        this.add(projectMenu);
        this.add(viewMenu);
        this.add(codeMenu);
    }
}