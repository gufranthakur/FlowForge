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

        // Root panel
        rootPanel = new JPanel(new BorderLayout(5, 5));
        rootPanel.setMinimumSize(new Dimension(flowForge.getWidth(), 200));
        rootPanel.setPreferredSize(new Dimension(flowForge.getWidth(), 250));

        // Toolbar
        toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setBackground(flowForge.getBackground().darker());

        consoleLabel = new JLabel("Console");
        consoleLabel.setFont(new Font(consoleLabel.getFont().getName(), Font.BOLD, 16));
        toolbar.add(consoleLabel, BorderLayout.WEST);

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtons.setOpaque(false);

        clearButton = new JButton("↺");
        clearButton.setFont(new Font(clearButton.getFont().getName(), Font.PLAIN, 16));
        clearButton.setBackground(toolbar.getBackground().brighter());
        rightButtons.add(clearButton);

        closeButton = new JButton("X");
        closeButton.setFont(new Font(closeButton.getFont().getName(), Font.PLAIN, 16));
        closeButton.setFocusable(false);
        closeButton.setBackground(toolbar.getBackground().brighter());
        rightButtons.add(closeButton);

        toolbar.add(rightButtons, BorderLayout.EAST);

        // Console text area
        consoleTextPane = new JTextPane();
        consoleTextPane.setEditable(false);
        consoleTextPane.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 16));

        scrollPane = new JScrollPane(consoleTextPane);

        // Add to root panel
        rootPanel.add(toolbar, BorderLayout.NORTH);
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        // Actions
        closeButton.addActionListener(e -> minimize());
        clearButton.addActionListener(e -> consoleTextPane.setText(""));
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
