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

        textField = new JFormattedTextField();

        contentPanel.add(textField, BorderLayout.NORTH);

    }

    @Override
    public void execute() {
        System.out.println("print node executed");
        flowPanel.flowForge.console.print(textField.getText());
    }
}