package flowforge.ui.panels;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import flowforge.FlowForge;

import javax.swing.*;
import java.awt.*;

public class Console {
    private JPanel rootPanel;
    private JPanel toolbar;
    private JButton closeButton;
    private JScrollPane scrollPane;
    private JTextPane consoleTextPane;
    private JLabel consoleLabel;
    private JButton clearButton;

    private FlowForge flowForge;

    public boolean isMinimized = false;

    public Console(FlowForge flowForge) {
        this.flowForge = flowForge;
        this.rootPanel.setMinimumSize(new Dimension(flowForge.getWidth(), 200));

        consoleTextPane.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 16));
        rootPanel.setPreferredSize(new Dimension(flowForge.getWidth(), 250));
        toolbar.setBackground(flowForge.getBackground().darker());
        closeButton.setBackground(toolbar.getBackground().brighter());
        clearButton.setBackground(toolbar.getBackground().brighter());

        closeButton.addActionListener(e -> {
            minimize();
        });
        clearButton.addActionListener(e -> {
            consoleTextPane.setText("");
        });
    }

    public void print(String value) {
        SwingUtilities.invokeLater(() -> {
            consoleTextPane.setText(consoleTextPane.getText() + value + "\n");
            consoleTextPane.setCaretPosition(consoleTextPane.getDocument().getLength());
        });
    }

    public void clear() {
        SwingUtilities.invokeLater(() -> consoleTextPane.setText(""));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void printSaveStatement() {
        clear();
        print("Project saved successfully at location : \n" + flowForge.projectFilePath);
    }

    public void resizeToDefault() {
        flowForge.consoleSplitPane.setDividerLocation(0.7);
        isMinimized = false;
    }

    public void minimize() {
        flowForge.consoleSplitPane.setDividerLocation(1.0);
        isMinimized = true;
    }


}
