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


        contentPanel.add(textField, BorderLayout.NORTH);
    }

    @Override
    public void execute() {
        System.out.println("print node executed");
        flowPanel.flowForge.console.print(textField.getText());
        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }

        for (Node nodes : inputNodes) {
            if (nodes instanceof VariableNode) {
                flowPanel.flowForge.console.print(((VariableNode) nodes).textField.getText());
            }
        }

    }
}