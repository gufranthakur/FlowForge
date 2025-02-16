package flowforge.nodes.variables;

import flowforge.core.FlowPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import java.awt.*;

public class StringNode extends Node {

    private FlowPanel flowPanel;
    public JTextField textField;

    public StringNode(String title, FlowPanel flowPanel, String stringValue) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        this.setSize(120, 30);

        textField = new JTextField();

        inputButton.setVisible(false);
        outputButton.setVisible(false);

        inputXButton.setText("Set");
        outputXButton.setText("Get");
        contentPanel.add(textField, BorderLayout.CENTER);

        this.setSize(260, 90);
    }

    @Override
    public void execute() {

        for (Node node : inputXNodes) {
            if (node != null) {
                setStringValue(textField.getText());
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }
    }

    public String getStringValue() {
        return flowPanel.strings.get(title);
    }

    public void setStringValue(String stringValue) {
        flowPanel.strings.put(title, stringValue);
        System.out.println(flowPanel.strings);
    }

}
