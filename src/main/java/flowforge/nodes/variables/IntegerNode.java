package flowforge.nodes.variables;

import flowforge.core.FlowPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import java.awt.*;

public class IntegerNode extends Node {
    private FlowPanel flowPanel;
    public JTextField textField;

    private Integer intValue;

    public IntegerNode(String title, FlowPanel flowPanel, Integer intValue) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        this.intValue = intValue;
        this.setSize(200, 80);

        textField = new JTextField();

        inputButton.setText("Set");
        outputButton.setText("Get");
        contentPanel.add(textField, BorderLayout.CENTER);
    }

    @Override
    public void execute() {

    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }
}
