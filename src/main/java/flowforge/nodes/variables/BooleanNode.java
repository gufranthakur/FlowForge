package flowforge.nodes.variables;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.core.FlowPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BooleanNode extends Node {

    private FlowPanel flowPanel;
    public JCheckBox checkBox;

    public BooleanNode(String title, FlowPanel flowPanel, Boolean booleanValue) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        this.setSize(240, 70);

        checkBox = new JCheckBox("Set value : False");

        checkBox.addActionListener(e -> {
            if (!checkBox.isSelected()) {
                checkBox.setText("Set value : False");
            } else {
                checkBox.setText("Set value : True");
            }
        });

        inputButton.setVisible(false);
        outputButton.setVisible(false);

        inputXButton.setText("Set");
        outputXButton.setText("Get");

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperPanel.add(checkBox);

        contentPanel.add(wrapperPanel, BorderLayout.NORTH);

        this.setSize(260, 90);
    }

    @Override
    public void execute() {
        for (Node node : inputXNodes) {
            if (node != null) {
                setBooleanValue(checkBox.isSelected());
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }
    }

    public Boolean getBooleanValue() {
        return flowPanel.booleans.get(title);
    }

    public void setBooleanValue(Boolean booleanValue) {
        flowPanel.booleans.put(title, booleanValue);
        System.out.println(flowPanel.booleans);
    }
}