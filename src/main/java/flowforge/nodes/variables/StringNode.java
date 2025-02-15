package flowforge.nodes.variables;

import flowforge.core.FlowPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import java.awt.*;

public class StringNode extends Node {

    private FlowPanel flowPanel;
    public JTextField textField;

    private String stringValue;

    public StringNode(String title, FlowPanel flowPanel, String stringValue) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        this.stringValue = stringValue;
        this.setSize(120, 30);

        textField = new JTextField();
        textField.addActionListener(e -> setStringValue(textField.getText()));

        inputButton.setVisible(false);
        outputButton.setVisible(false);

        inputXButton.setText("Set");
        outputXButton.setText("Get");
        contentPanel.add(textField, BorderLayout.CENTER);

        this.setSize(260, 60);
    }

    @Override
    public void execute() {
        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

}
