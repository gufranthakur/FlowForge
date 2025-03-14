package flowforge.core;

import flowforge.FlowForge;
import flowforge.nodes.Node;

import javax.swing.*;
import java.awt.*;

public class Console {
    private JPanel rootPanel;
    private JPanel toolbar;
    private JButton minimiseButton;
    private JScrollPane scrollPane;
    private JTextPane consoleTextPane;

    public Console(FlowForge flowForge) {
        rootPanel.setPreferredSize(new Dimension(flowForge.getWidth(), 250));
        toolbar.setBackground(flowForge.getBackground().darker());
        minimiseButton.setBackground(toolbar.getBackground());

        minimiseButton.addActionListener(e -> {
            rootPanel.setVisible(false);
        });
    }

    public void print(String value) {
        consoleTextPane.setText(consoleTextPane.getText() + value + "\n");
        consoleTextPane.setCaretPosition(consoleTextPane.getDocument().getLength());
    }

    public void clear() {
        consoleTextPane.setText("");
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void throwError(String error, Node node) {
        consoleTextPane.setForeground(Color.RED);
        print("Error at Node : " + node.getTitle() + " : " + error);
    }

}
