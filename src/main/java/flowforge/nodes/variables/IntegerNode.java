package flowforge.nodes.variables;

import flowforge.core.FlowPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import java.awt.*;

public class IntegerNode extends Node {
    private FlowPanel flowPanel;
    public JSpinner spinner;

    public IntegerNode(String title, FlowPanel flowPanel, Integer intValue) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        this.setSize(170, 90);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        spinner = new JSpinner();

        inputButton.setVisible(false);
        outputButton.setVisible(false);

        inputXButton.setText("Set");
        outputXButton.setText("Get");

        wrapperPanel.add(spinner);
        contentPanel.add(wrapperPanel, BorderLayout.NORTH);
    }

    @Override
    public void execute() {
        for (Node node : inputXNodes) {
            if (node != null) {
                setIntValue((Integer) spinner.getValue());
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }
    }

    public Integer getIntValue() {
        return flowPanel.integers.get(title);
    }

    public void setIntValue(Integer intValue) {
        flowPanel.integers.put(title, intValue);
        System.out.println(flowPanel.integers);
    }
}