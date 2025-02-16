package flowforge.nodes;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.core.FlowPanel;
import flowforge.nodes.variables.IntegerNode;
import flowforge.nodes.variables.StringNode;

import javax.swing.*;

public class PrintNode extends Node{

    private final JTextField textField;
    private final FlowPanel flowPanel;

    public PrintNode(String title, FlowPanel flowPanel) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        outputButton.setVisible(true);

        textField = new JTextField();
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Print...");
        topPanel.add(textField);

        this.setSize(200, 140);
    }

    public void print(String text) {
        flowPanel.flowForge.console.print(text);
    }

    @Override
    public void execute() {

        if (!inputXNodes.isEmpty()) {
            for (Node inputXNode : inputXNodes) {
                if (inputXNode != null) {
                    if (inputXNode instanceof IntegerNode) {
                        print(((IntegerNode) inputXNode).getIntValue() + textField.getText());
                    } else if (inputXNode instanceof StringNode) {
                        print(((StringNode) inputXNode).getStringValue() + textField.getText());
                    }
                }
            }
        } else {

            print(textField.getText());
        }

        // Execute any connected output nodes
        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }
    }
}