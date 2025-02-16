package flowforge.nodes;

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
        topPanel.add(textField);

        this.setSize(200, 140);
    }

    public void print(String text) {
        flowPanel.flowForge.console.print(text);
    }

    @Override
    public void execute() {
        for (Node inputXNode : inputXNodes) {
            if (inputXNode != null) {
                if (inputXNode instanceof IntegerNode) {
                    print(((IntegerNode) inputXNode).getIntValue() + textField.getText());
                } else if (inputXNode instanceof StringNode) {
                    print(((StringNode) inputXNode).getStringValue() + textField.getText());
                }
            } else {
                print(textField.getText());
                for (Node nodes : outputNodes) {
                    if (nodes != null) nodes.execute();
                }
            }
        }

    }
}