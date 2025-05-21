package flowforge.ui.MenuBar;

import flowforge.FlowForge;

import javax.swing.*;
import java.awt.*;

public class ChangeLogPanel {
    private JPanel rootPanel;
    private JScrollPane contentScrollPane;
    private JPanel contentPanel;
    private JButton backButton;
    private JTextPane textPane;

    public ChangeLogPanel(FlowForge flowForge) {
        backButton.setBackground(flowForge.theme);
        backButton.addActionListener(e -> {
            flowForge.remove(this.getRootPanel());
            flowForge.add(flowForge.startPanel, BorderLayout.CENTER);

            flowForge.menuBar.changelogPanelVisible = false;

            flowForge.revalidate();
            flowForge.repaint();
        });

        contentScrollPane.getVerticalScrollBar().setUnitIncrement(14);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

}
