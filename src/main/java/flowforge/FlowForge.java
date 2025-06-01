/*
 * Copyright 2025 Gufran Thakur
 *
 * Licensed under the MIT License. See the LICENSE file for details.
 */
package flowforge;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import flowforge.core.*;
import flowforge.ui.MenuBar.AboutPanel;
import flowforge.ui.MenuBar.AppMenuBar;
import flowforge.ui.MenuBar.ChangeLogPanel;
import flowforge.ui.panels.Console;
import flowforge.ui.panels.ControlPanel;
import flowforge.ui.panels.ProgramPanel;
import flowforge.ui.panels.StartPanel;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

//The main Class of FlowForge. All the components have a reference to this clasn.
//In a way, this is the glue between all the components and panels.

public class FlowForge extends JFrame implements Runnable{

    //Saves and loads nodes into JSON file.
    public DataManager dataManager;
    public String projectFilePath;

    //Program panel container contains the program panel.
    //This is needed because the actual program panel uses null layout.
    public JPanel programPanelContainer;
    //The starting panel which contains open project and new project buttons.
    public StartPanel startPanel;

    //Everything about the menubar
    public AppMenuBar menuBar;
    public AboutPanel aboutPanel;
    public ChangeLogPanel changeLogPanel;

    //Control panel contains nodes, variables, properties, code executions and other things the user can operate with.
    public ControlPanel controlPanel;
    //Program panel is where the nodes are placed and executed
    public ProgramPanel programPanel;
    //A small in-built terminal that displays the output
    public Console console;

    public Color theme = new Color(26, 77, 236).brighter();
    public Color secondaryTheme = new Color(229, 117, 42);
    public Color arithmeticNodeTheme = new Color(73, 11, 142);
    public Color comparatorNodeTheme = new Color(130, 140, 6);
    public Color utilNodeTheme = new Color(5, 94, 92);
    public Color variableNodeTheme = new Color(108, 5, 5);

    //Responsible for execution and stopping execution of program.
    public ForgeExecutor forgeExecutor;

    private Thread flowForgeThread;
    private boolean isRunning = false;

    public FlowForge() {
        this.setTitle("FlowForge");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image icon = Toolkit.getDefaultToolkit().getImage("FlowForge-Logo.png");
        this.setIconImage(icon);
    }

    public void init() {
        //All core components of the app are initialized here. Every component has a reference to this class via the constructor
        flowForgeThread = new Thread(this);

        console = new Console(this);
        startPanel = new StartPanel(this);
        programPanel = new ProgramPanel(this);
        controlPanel = new ControlPanel(this);

        dataManager = new DataManager(programPanel);
        forgeExecutor = new ForgeExecutor(this);

        menuBar = new AppMenuBar(this);
        aboutPanel = new AboutPanel(this);
        changeLogPanel = new ChangeLogPanel(this);

        programPanelContainer = new JPanel(null);

        controlPanel.init();
        menuBar.init();
        menuBar.initListeners();

    }

    public void addComponent() {
        //Only the start panel is added for now. Other components get added when the user opens/creates a project.
        controlPanel.addComponent();
        menuBar.addComponent();

        this.add(startPanel, BorderLayout.CENTER);
        this.setVisible(true);

        isRunning = true;

        flowForgeThread.start();

    }

    public void launch() {
        //This is called when the user opens/creates a project. Start panel is removed and other components come into action
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
    //This one kinda doesnt work properly, still in testing.
    public boolean checkForUpdate() {

        try {
            String currentVersion = "1.7";
            URL versionUrl = new URL("https://flow-forge-website.vercel.app/version.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(versionUrl.openStream()));
            String latestVersion = reader.readLine().trim();
            reader.close();

            if (!latestVersion.equals(currentVersion)) {

                if (JOptionPane.showConfirmDialog(null,
                        "There is a new FlowForge version available. " + "\n" +
                                "You can download it manually by clicking on \"Yes\"",
                        "New version Available", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("https://flow-forge-website.vercel.app/"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (URISyntaxException ex) {
                        System.out.println("Exception caught : wrong URL");
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Please connect to an internet",
                    "Could not check for update", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    //Game loop, for smooth animations and responsiveness.
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

    //Main method.
    public static void main(String[] args){
        FlatMacDarkLaf.setup(); //Theme setup
        FlatJetBrainsMonoFont.install(); //Font installation

        //Swing is not thread safe, invoked in the EDT (Event dispatch thread)
        SwingUtilities.invokeLater(() -> {
            FlowForge flowForge = new FlowForge();
            flowForge.init();
            flowForge.addComponent();
        });

    }

}
