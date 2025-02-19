package flowforge.nodes.flownodes;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.core.MainPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.comparators.*;
import flowforge.nodes.flownodes.logicgates.LogicGateNode;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;
import flowforge.nodes.variables.StringNode;

import javax.swing.*;

public class PrintNode extends Node {

    private final JTextField textField;
    private final MainPanel mainPanel;

    public PrintNode(String title, MainPanel mainPanel) {
        super(title, mainPanel);
        this.mainPanel = mainPanel;
        outputButton.setVisible(true);

        textField = new JTextField();
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Print...");
        topPanel.add(textField);

        this.setSize(200, 140);
        this.pack();
    }

    public void print(String text) {
        mainPanel.flowForge.console.print(text);
    }

    @Override
    public void execute() {

        if (!inputXNodes.isEmpty()) {
            for (Node inputXNode : inputXNodes) {
                if (inputXNode != null) {
                    switch (inputXNode) {
                        case IntegerNode integerNode -> print(integerNode.getIntValue() + textField.getText());

                        case StringNode stringNode -> print(stringNode.getStringValue() + textField.getText());

                        case BooleanNode booleanNode -> print(booleanNode.getBooleanValue() + textField.getText());

                        case EqualToNode equalToNode -> print(equalToNode.getIsEqual() + textField.getText());

                        case GreaterThanNode greaterThanNode -> print(greaterThanNode.getIsGreater() + textField.getText());

                        case LessThanNode lessThanNode -> print(lessThanNode.getIsLess() + textField.getText());

                        case GreaterThanOrEqualNode greaterThanOrEqualNode -> print(greaterThanOrEqualNode.getIsGreaterOrEqual() + textField.getText());

                        case LessThanOrEqualNode lessThanOrEqualNode -> print(lessThanOrEqualNode.getIsLessOrEqual() + textField.getText());

                        case NotEqualToNode notEqualToNode -> print(notEqualToNode.getIsNotEqual() + textField.getText());

                        case LogicGateNode logicGateNode -> print(logicGateNode.getResult() + textField.getText());

                        default -> {
                            print("ERROR");
                        }
                    }
                }
            }
        } else {

            print(textField.getText());
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }
    }
}