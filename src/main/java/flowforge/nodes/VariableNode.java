package flowforge.nodes;

import flowforge.core.FlowPanel;

import javax.swing.*;
import java.awt.*;

public class VariableNode extends Node{

    private FlowPanel flowPanel;
    public JTextField textField;

    private String stringValue;
    private Integer intValue;
    private Boolean booleanValue;

    public VariableNode(String title, FlowPanel flowPanel, String stringValue) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        this.stringValue = stringValue;
        this.setSize(120, 30);

        textField = new JTextField();
        textField.addActionListener(e -> getText());

        inputButton.setText("Set");
        outputButton.setText("Get");
        contentPanel.add(textField, BorderLayout.CENTER);
    }
    public VariableNode(String title, FlowPanel flowPanel, Integer intValue) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        this.intValue = intValue;
        this.setSize(200, 80);

        textField = new JTextField();

        inputButton.setText("Set");
        outputButton.setText("Get");
        contentPanel.add(textField, BorderLayout.CENTER);
    }
    public VariableNode(String title, FlowPanel flowPanel, Boolean booleanValue) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        this.booleanValue = booleanValue;
        this.setSize(200, 60);

        textField = new JTextField();

        inputButton.setText("Set");
        outputButton.setText("Get");
        contentPanel.add(textField, BorderLayout.CENTER);
    }

    public String getText() {
        stringValue = textField.getText();
        return stringValue;
    }


    @Override
    public void execute() {

    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
}
