package flowforge.nodes.flownodes;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.nodes.variables.FloatNode;
import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.arithmetic.*;
import flowforge.nodes.flownodes.comparators.*;
import flowforge.nodes.flownodes.logicgates.LogicGateNode;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;
import flowforge.nodes.variables.StringNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PrintNode extends Node {

    public JTextField textField;
    private final ProgramPanel programPanel;

    public PrintNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        outputButton.setVisible(true);
        outputXButton.setVisible(false);

        this.nodeTheme = new Color(6, 79, 172);

        textField = new JTextField();
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Print...");
        topPanel.add(textField);

    }

    public void print(String text) {
        programPanel.flowForge.console.print(text);
    }

    @Override
    public void execute(boolean isStepExecution) {
        if (isStepExecution) {
            synchronized (programPanel.stepExecutorLock) {
                try {
                    programPanel.stepExecutorLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            SwingUtilities.invokeLater(() -> {
                for (Node node : programPanel.nodes) {
                    node.restoreBorder();
                }
                this.setStepExecutedBorder();
            });
        }

        if (!inputXNodes.isEmpty()) {
            for (Node inputXNode : inputXNodes) {
                switch (inputXNode) {
                    case IntegerNode integerNode -> print(textField.getText() + integerNode.getValue());
                    case StringNode stringNode -> print(textField.getText() + stringNode.getValue());
                    case BooleanNode booleanNode -> print(textField.getText() + booleanNode.getValue());
                    case FloatNode floatNode -> print(textField.getText() + floatNode.getValue());
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
                    case EvalNode evalNode -> print(textField.getText() + evalNode.getResult());
                    default -> print("ERROR");
                }
            }
        } else {
            print(textField.getText());
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute(isStepExecution);
        }

    }


}