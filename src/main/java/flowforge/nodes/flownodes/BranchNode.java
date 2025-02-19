package flowforge.nodes.flownodes;

import flowforge.core.FlowPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.comparators.EqualToNode;
import flowforge.nodes.variables.BooleanNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BranchNode extends Node {

    private JRadioButton outputFalseButton;
    private ArrayList<Node> trueNodes = new ArrayList<>();
    private ArrayList<Node> falseNodes = new ArrayList<>();

    public BranchNode(String title, FlowPanel flowPanel) {
        super(title, flowPanel);
        this.setSize(200, 100);

        outputFalseButton = new JRadioButton();

        inputButton.setText("Input");
        inputXButton.setText("Condition");

        outputButton.setText("True");
        outputFalseButton.setText("False");
        outputXButton.setVisible(false);

        outputButton.addActionListener(e -> {
            for (Node node : flowPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }
            if (outputButton.isSelected()) {
                flowPanel.startConnection(this);
            }
        });

        outputFalseButton.addActionListener(e -> {
            for (Node node : flowPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }
            if (outputFalseButton.isSelected()) {
                flowPanel.startConnection(this);
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
                if (node instanceof BooleanNode) condition = ((BooleanNode)node).getBooleanValue();
                else if (node instanceof EqualToNode) condition = ((EqualToNode) node).getIsEqual();
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
            drawGradientLine(g, start, end, new Color(31, 216, 241), new Color(37, 114, 205));
        }

        // Draw False connections
        for (Node node : falseNodes) {
            Point start = getFalseOutputPoint();
            Point end = node.getInputPoint();
            drawGradientLine(g, start, end, new Color(244, 34, 160), new Color(239, 36, 36));
        }
    }

    private Point getFalseOutputPoint() {
        return new Point(getX() + getWidth(), getY() + getHeight()*2/3);
    }
}