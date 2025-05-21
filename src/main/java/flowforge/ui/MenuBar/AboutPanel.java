package flowforge.ui.MenuBar;

import flowforge.FlowForge;

import javax.swing.*;
import java.awt.*;

public class AboutPanel {
    private JPanel rootPanel;
    private JPanel mainPanel;
    private JTextPane aboutTextPane;
    private JTextPane flowForgeIsOpenSourceTextPane;
    private JScrollPane scrollPane;
    private JPanel aboutPanel;
    private JPanel lisencingPanel;
    private JButton confirmButton;

    private FlowForge flowForge;

    public AboutPanel(FlowForge flowForge) {
        this.flowForge = flowForge;

        confirmButton.addActionListener(e -> {
            flowForge.remove(this.getRootPanel());
            flowForge.add(flowForge.startPanel, BorderLayout.CENTER);

            flowForge.menuBar.aboutPanelVisible = false;

            flowForge.revalidate();
            flowForge.repaint();
        });

        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

}
