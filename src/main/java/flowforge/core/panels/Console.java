package flowforge.core.panels;

import flowforge.FlowForge;
import flowforge.nodes.Node;

import javax.swing.*;
import java.awt.*;

public class Console {
    private JPanel rootPanel;
    private JPanel toolbar;
    private JButton closeButton;
    private JScrollPane scrollPane;
    private JTextPane consoleTextPane;

    private FlowForge flowForge;

    public Console(FlowForge flowForge) {
        this.flowForge = flowForge;

        rootPanel.setPreferredSize(new Dimension(flowForge.getWidth(), 250));
        toolbar.setBackground(flowForge.getBackground().darker());
        closeButton.setBackground(toolbar.getBackground());

        closeButton.addActionListener(e -> {
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

    public void printSaveStatement() {
        clear();
        print("Project saved succesfully at location : \n" + flowForge.projectFilePath);
    }

}
