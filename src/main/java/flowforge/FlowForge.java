package flowforge;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import flowforge.core.Console;
import flowforge.core.ControlPanel;
import flowforge.core.ProgramPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FlowForge extends JFrame {

    public Console console;
    public Timer loop;

    public JPanel programPanelContainer;
    public ControlPanel controlPanel;
    public ProgramPanel programPanel;

    public Color theme = new Color(26, 77, 236);
    public Color errorTheme = new Color(198, 17, 17);

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

        programPanelContainer = new JPanel(null);
        programPanel = new ProgramPanel(this);
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

        programPanelContainer.add(programPanel);

        this.add(controlPanel.getRootPanel(), BorderLayout.WEST);
        this.add(programPanelContainer, BorderLayout.CENTER);
        this.add(console.getRootPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        loop.start();
    }

    public void run() {
        programPanel.startNode.execute();
        console.getRootPanel().setVisible(true);
    }

    public void stop() {
        System.out.println("Stopped");
        console.getRootPanel().setVisible(false);
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
