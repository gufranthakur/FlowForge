package flowforge;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import flowforge.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FlowForge extends JFrame {

    public Console console;
    public Timer loop;
    public DataManager dataManager;

    public JPanel programPanelContainer;
    public StartPanel startPanel;

    public ControlPanel controlPanel;
    public ProgramPanel programPanel;

    public String projectFilePath;

    public Color theme = new Color(26, 77, 236);

    public FlowForge() {
        this.setTitle("FlowForge");
        this.setSize(1200, 700);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() {
        console = new Console(this);
        startPanel = new StartPanel(this);
        programPanel = new ProgramPanel(this);
        dataManager = new DataManager(programPanel);

        programPanelContainer = new JPanel(null);

        programPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                programPanel.requestFocusInWindow();
            }
        });

        controlPanel = new ControlPanel(this);
        controlPanel.init();

        loop = new Timer(5, e -> {
            programPanel.repaint();
            programPanel.moveCamera();

        });
    }

    public void addComponent() {
        controlPanel.addComponent();

        this.add(startPanel, BorderLayout.CENTER);
        this.setVisible(true);

        loop.start();
    }

    public void compile() {
        console.getRootPanel().setVisible(true);
    }

    public void run() {
        programPanel.startNode.execute();
        console.getRootPanel().setVisible(true);
    }

    public void launch() {
        programPanelContainer.add(programPanel);

        startPanel.setVisible(false);

        this.add(controlPanel.getRootPanel(), BorderLayout.WEST);
        this.add(programPanelContainer, BorderLayout.CENTER);
        this.add(console.getRootPanel(), BorderLayout.SOUTH);

        console.print("Welcome to FlowForge!");

        this.repaint();
        this.revalidate();
    }

    public static void main(String[] args)  {
        FlatMacDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            FlowForge flowForge = new FlowForge();
            flowForge.init();
            flowForge.addComponent();
        });
    }

}
