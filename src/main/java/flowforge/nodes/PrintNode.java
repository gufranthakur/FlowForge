package flowforge.nodes;

import flowforge.FlowForge;
import flowforge.core.FlowPanel;

import javax.swing.*;
import java.awt.*;

public class PrintNode extends Node{

    private JTextField textField;
    private FlowPanel flowPanel;

    public PrintNode(String title, FlowPanel flowPanel) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        outputButton.setVisible(true);

        textField = new JTextField();
        topPanel.add(textField);

        this.setSize(200, 140);
    }

    @Override
    public void execute() {
        System.out.println("print node executed");

        if (inputXNode != null && inputXNode instanceof VariableNode) {
            flowPanel.flowForge.console.print(((VariableNode) inputXNode).getStringValue());
            System.out.println("Printed from variable node");
        } else {
            flowPanel.flowForge.console.print(textField.getText());
            for (Node nodes : outputNodes) {
                if (nodes != null) nodes.execute();
            }
        }

    }
}