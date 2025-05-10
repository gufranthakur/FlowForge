package flowforge.core.ui.MenuBar;

import flowforge.FlowForge;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppMenuBar extends JMenuBar {

    private FlowForge flowForge;
    private GraphicsEnvironment environment;
    private GraphicsDevice device;

    private JMenu aboutMenu;
    private JMenu projectMenu;
    private JMenuItem newProjectItem, openProjectItem, exitProjectItem,
            saveProjectItem, saveAsProjectItem,
            projectPropertiesItem, settingsItem;

    private JMenuItem aboutItem;
    private JMenu viewMenu;
    private JMenuItem fullscreenItem, presentationModeItem,
            showHideSideBar, showHideConsole, showHideToolbar;

    private boolean isFullScreen, isPresentationMode;

    public AppMenuBar(FlowForge flowForge) {
        this.flowForge = flowForge;

        environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
    }

    public void init() {
        // Initialize main menu items
        aboutMenu = new JMenu("About");
        projectMenu = new JMenu("Project");
        viewMenu = new JMenu("View");

        //about
        aboutItem = new JMenuItem("About FlowForge");

        // Initialize Project menu items
        newProjectItem = new JMenuItem("New Project");
        openProjectItem = new JMenuItem("Open Project");
        saveProjectItem = new JMenuItem("Save");
        saveAsProjectItem = new JMenuItem("Save As");
        projectPropertiesItem = new JMenuItem("Project Properties");
        settingsItem = new JMenuItem("Settings");
        exitProjectItem = new JMenuItem("Exit");

        // Initialize View menu items
        fullscreenItem = new JMenuItem("Fullscreen");
        presentationModeItem = new JMenuItem("Presentation Mode");
        showHideSideBar = new JMenuItem("Toggle Sidebar");
        showHideConsole = new JMenuItem("Toggle Console");
        showHideToolbar = new JMenuItem("Toggle Toolbar");

    }

    public void addComponent() {
        this.add(aboutMenu);
        aboutMenu.add(aboutItem);

        projectMenu.add(newProjectItem);
        projectMenu.add(openProjectItem);
        projectMenu.addSeparator();
        projectMenu.add(saveProjectItem);
        projectMenu.add(saveAsProjectItem);
        projectMenu.addSeparator();
        //projectItem.add(projectPropertiesItem);
        //projectItem.add(settingsItem);
        //projectItem.addSeparator();
        projectMenu.add(exitProjectItem);

        viewMenu.add(fullscreenItem);
        viewMenu.add(presentationModeItem);
        viewMenu.addSeparator();
        viewMenu.add(showHideSideBar);
        viewMenu.add(showHideConsole);
        viewMenu.add(showHideToolbar);

        flowForge.setJMenuBar(this);
    }

    public void initListeners() {
        aboutItem.addActionListener(e -> {
            flowForge.startPanel.setVisible(false);
            flowForge.add(flowForge.aboutPanel.getRootPanel(), BorderLayout.CENTER);
        });

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
            }
            flowForge.console.printSaveStatement();
        });

        exitProjectItem.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(
                    null, "Any unsaved changes will be lost", "Exit Program?", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                getNewInstance(false);
                flowForge.dispose();
            }
        });

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
            var console = flowForge.console.getRootPanel();
            console.setVisible(!console.isVisible());
        });

        showHideToolbar.addActionListener(e -> {
            var toolbar = flowForge.controlPanel.toolBar;
            toolbar.setVisible(!toolbar.isVisible());
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
        this.remove(aboutMenu);

        this.add(projectMenu);
        this.add(viewMenu);
    }
}