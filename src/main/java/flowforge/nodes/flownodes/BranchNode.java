package flowforge.nodes.flownodes;

import flowforge.core.MainPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.comparators.*;
import flowforge.nodes.flownodes.logicgates.LogicGateNode;
import flowforge.nodes.variables.BooleanNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BranchNode extends Node {

    private JRadioButton outputFalseButton;
    private ArrayList<Node> trueNodes = new ArrayList<>();
    private ArrayList<Node> falseNodes = new ArrayList<>();

    public BranchNode(String title, MainPanel mainPanel) {
        super(title, mainPanel);
        this.setSize(200, 100);
        this.pack();

        outputFalseButton = new JRadioButton();

        inputButton.setText("Input");
        inputXButton.setText("Condition");

        outputButton.setText("True");
        outputFalseButton.setText("False");
        outputXButton.setVisible(false);

        outputButton.addActionListener(e -> {
            for (Node node : mainPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }
            if (outputButton.isSelected()) {
                mainPanel.startConnection(this);
            }
        });

        outputFalseButton.addActionListener(e -> {
            for (Node node : mainPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }
            if (outputFalseButton.isSelected()) {
                mainPanel.startConnection(this);
            }
        });

        outputsPanel.add(outputFalseButton);
    }

    @Override
    public void connectTo(Node target) {
        if (outputButton.isSelected()) {
            trueNodes.add(target);
            target.inputNodes.add(this);
            outputButton.setSelected(false);
        } else if (outputFalseButton.isSelected()) {
            falseNodes.add(target);
            target.inputNodes.add(this);
            outputFalseButton.setSelected(false);
        }

    }

    @Override
    public void disconnectAll() {
        for (Node node : trueNodes) {
            node.inputNodes.remove(this);
        }
        for (Node node : falseNodes) {
            node.inputNodes.remove(this);
        }

        trueNodes.clear();
        falseNodes.clear();

        inputButton.setSelected(false);
        outputButton.setSelected(false);
        inputXButton.setSelected(false);
        outputFalseButton.setSelected(false);
    }

    @Override
    public void execute() {
        boolean condition = false;

        for (Node node : inputXNodes) {
            if (node != null) {
                switch (node) {
                    case BooleanNode booleanNode -> condition = booleanNode.getBooleanValue();

                    case EqualToNode equalToNode -> condition = equalToNode.getIsEqual();

                    case GreaterThanNode greaterThanNode -> condition = greaterThanNode.getIsGreater();

                    case LessThanNode lessThanNode -> condition = lessThanNode.getIsLess();

                    case GreaterThanOrEqualNode greaterThanOrEqualNode -> condition = greaterThanOrEqualNode.getIsGreaterOrEqual();

                    case LessThanOrEqualNode lessThanOrEqualNode -> condition = lessThanOrEqualNode.getIsLessOrEqual();

                    case NotEqualToNode notEqualToNode -> condition = notEqualToNode.getIsNotEqual();

                    case LogicGateNode logicGateNode -> condition = logicGateNode.getResult();

                    default -> {
                        System.out.println("Error");
                    }
                }
            }
        }

        if (condition) {
            for (Node node : trueNodes) {
                if (node != null) {
                    node.execute();
                }
            }
        } else {
            for (Node node : falseNodes) {
                if (node != null) {
                    node.execute();
                }
            }
        }
    }

    @Override
    public void drawConnection(Graphics2D g) {
        // Draw True connections
        for (Node node : trueNodes) {
            Point start = getOutputPoint();
            Point end = node.getInputPoint();
            drawCurvedGradientLine(g, start, end, new Color(31, 216, 241), new Color(37, 114, 205));
        }

        // Draw False connections
        for (Node node : falseNodes) {
            Point start = getFalseOutputPoint();
            Point end = node.getInputPoint();
            drawCurvedGradientLine(g, start, end, new Color(244, 34, 160), new Color(239, 36, 36));
        }
    }

    private Point getFalseOutputPoint() {
        return new Point(getX() + getWidth(), getY() + getHeight()*2/3);
    }
}