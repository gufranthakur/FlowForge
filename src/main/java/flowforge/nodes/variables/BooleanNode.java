package flowforge.nodes.variables;

import flowforge.core.FlowPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import java.awt.*;

public class BooleanNode extends Node {

    private FlowPanel flowPanel;
    public JTextField textField;

    private Boolean booleanValue;

    public BooleanNode(String title, FlowPanel flowPanel, Boolean booleanValue) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        this.booleanValue = booleanValue;
        this.setSize(240, 70);

        textField = new JTextField();

        inputButton.setText("Set");
        outputButton.setText("Get");
        contentPanel.add(textField, BorderLayout.CENTER);
    }

    @Override
    public void execute() {

    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
}
