package flowforge;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import flowforge.core.Console;
import flowforge.core.ControlPanel;
import flowforge.core.FlowPanel;

import javax.swing.*;
import java.awt.*;

public class FlowForge extends JFrame {

    public Console console;
    public Timer loop;

    public ControlPanel controlPanel;
    public FlowPanel flowPanel;

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

        flowPanel = new FlowPanel(this);

        controlPanel = new ControlPanel(this);
        controlPanel.init();

        loop = new Timer(50, e -> {
            flowPanel.repaint();
        });

    }

    public void addComponent() {
        controlPanel.addComponent();

        this.add(controlPanel.getRootPanel(), BorderLayout.WEST);
        this.add(flowPanel, BorderLayout.CENTER);
        this.add(console.getRootPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        loop.start();
    }

    public void run() {
        flowPanel.startNode.execute();
        console.getRootPanel().setVisible(true);
    }

    public void stop() {
        System.out.println("Stopped");
    }

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            FlowForge flowForge = new FlowForge();
            flowForge.init();
            flowForge.addComponent();
        });
    }

}
