package flowforge.nodes.flownodes;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.core.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.arithmetic.*;
import flowforge.nodes.flownodes.comparators.*;
import flowforge.nodes.flownodes.logicgates.LogicGateNode;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;
import flowforge.nodes.variables.StringNode;

import javax.swing.*;

public class PrintNode extends Node {

    public JTextField textField;
    private final ProgramPanel programPanel;

    public PrintNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        outputButton.setVisible(true);

        textField = new JTextField();
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Print...");
        topPanel.add(textField);

        this.setSize(200, 140);
        this.pack();
    }

    public void print(String text) {
        programPanel.flowForge.console.print(text);
    }

    @Override
    public void execute() {

        if (!inputXNodes.isEmpty()) {
            for (Node inputXNode : inputXNodes) {
                if (inputXNode != null) {
                    switch (inputXNode) {
                        case IntegerNode integerNode -> print(textField.getText() + integerNode.getValue());

                        case StringNode stringNode -> print(textField.getText() + stringNode.getValue());

                        case BooleanNode booleanNode -> print(textField.getText() + booleanNode.getValue());

                        case EqualToNode equalToNode -> print(textField.getText() + equalToNode.getIsEqual());

                        case GreaterThanNode greaterThanNode -> print(textField.getText() + greaterThanNode.getIsGreater());

                        case LessThanNode lessThanNode -> print(textField.getText() + lessThanNode.getIsLess());

                        case GreaterThanOrEqualNode greaterThanOrEqualNode -> print(textField.getText() + greaterThanOrEqualNode.getIsGreaterOrEqual());

                        case LessThanOrEqualNode lessThanOrEqualNode -> print(textField.getText() + lessThanOrEqualNode.getIsLessOrEqual());

                        case NotEqualToNode notEqualToNode -> print(textField.getText() + notEqualToNode.getIsNotEqual());

                        case LogicGateNode logicGateNode -> print(textField.getText() + logicGateNode.getResult());

                        case InputNode inputNode -> print(textField.getText() + inputNode.inputValue);

                        case LoopNode loopNode -> print(textField.getText() + loopNode.getIterationValue());

                        case AddNode addNode -> print(textField.getText() + addNode.getResult());

                        case SubtractNode subtractNode -> print(textField.getText() + subtractNode.getResult());

                        case MultiplyNode multiplyNode -> print(textField.getText() + multiplyNode.getResult());

                        case DivideNode divideNode -> print(textField.getText() + divideNode.getResult());

                        case ModulusNode modulusNode -> print(textField.getText() + modulusNode.getResult());

                        case RandomNode randomNode -> print(textField.getText() + randomNode.getResult());

                        default -> print("ERROR");
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