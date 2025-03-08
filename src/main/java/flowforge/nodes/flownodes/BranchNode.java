package flowforge.nodes.flownodes;

import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.comparators.*;
import flowforge.nodes.flownodes.logicgates.LogicGateNode;
import flowforge.nodes.variables.BooleanNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Implements conditional branching (if-else) functionality.
 * Routes execution flow based on a boolean condition from a connected node.
 */
public class BranchNode extends Node {

    private JRadioButton outputFalseButton;
    private ArrayList<Node> trueNodes = new ArrayList<>();
    private ArrayList<Node> falseNodes = new ArrayList<>();

    private ProgramPanel programPanel;

    /**
     * Creates a new branch node with true and false output paths.
     * @param title The display title for the node
     * @param programPanel Reference to the main program panel
     */
    public BranchNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(200, 100);
        this.pack();

        outputFalseButton = new JRadioButton();

        inputButton.setText("Input");
        inputXButton.setText("Condition");

        outputButton.setText("True");
        outputFalseButton.setText("False");
        outputXButton.setVisible(false);

        outputButton.addActionListener(e -> {
            for (Node node : programPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }
            if (outputButton.isSelected()) {
                programPanel.startConnection(this);
            }
        });

        outputFalseButton.addActionListener(e -> {
            for (Node node : programPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }
            if (outputFalseButton.isSelected()) {
                programPanel.startConnection(this);
            }
        });

        outputsPanel.add(outputFalseButton);
    }

    /**
     * Connects this node to a target node based on selected output type (true/false).
     * Adds the target to either trueNodes or falseNodes depending on which output is selected.
     * @param target The node to connect to
     */
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

    /**
     * Disconnects all input and output connections.
     * Clears both true and false connection lists.
     */
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

    /**
     * Executes the branch logic.
     * Evaluates the condition from the connected input node and
     * executes either the true or false branch accordingly.
     */
    @Override
    public void execute() {
        boolean condition = false;

        for (Node node : inputXNodes) {
            if (node != null) {
                switch (node) {
                    case BooleanNode booleanNode -> condition = booleanNode.getValue();
                    case EqualToNode equalToNode -> condition = equalToNode.getIsEqual();
                    case GreaterThanNode greaterThanNode -> condition = greaterThanNode.getIsGreater();
                    case LessThanNode lessThanNode -> condition = lessThanNode.getIsLess();
                    case GreaterThanOrEqualNode greaterThanOrEqualNode -> condition = greaterThanOrEqualNode.getIsGreaterOrEqual();
                    case LessThanOrEqualNode lessThanOrEqualNode -> condition = lessThanOrEqualNode.getIsLessOrEqual();
                    case NotEqualToNode notEqualToNode -> condition = notEqualToNode.getIsNotEqual();
                    case LogicGateNode logicGateNode -> condition = logicGateNode.getResult();
                    default -> { /* Unsupported condition node type */ }
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

    /**
     * Draws connections to true and false branch targets with distinct colors.
     * True path: blue gradient, False path: red gradient
     * @param g Graphics context to draw on
     */
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

    /**
     * Gets the connection point for the false output.
     * @return Point coordinates for false output connection
     */
    private Point getFalseOutputPoint() {
        return new Point(getX() + getWidth(), getY() + getHeight()*2/3);
    }

    /**
     * Gets all true branch target nodes.
     * Used during program saving to preserve branch structure.
     * @return List of nodes in the true branch
     */
    public ArrayList<Node> getTrueNodes() {
        return trueNodes;
    }

    /**
     * Gets all false branch target nodes.
     * Used during program saving to preserve branch structure.
     * @return List of nodes in the false branch
     */
    public ArrayList<Node> getFalseNodes() {
        return falseNodes;
    }

    /**
     * Adds a node to the true branch without UI interaction.
     * Used during program loading to reconstruct connections.
     * @param node Node to add to the true branch
     */
    public void addTrueNode(Node node) {
        if (node != null && !trueNodes.contains(node)) {
            trueNodes.add(node);
            node.inputNodes.add(this);
        }
    }

    /**
     * Adds a node to the false branch without UI interaction.
     * Used during program loading to reconstruct connections.
     * @param node Node to add to the false branch
     */
    public void addFalseNode(Node node) {
        if (node != null && !falseNodes.contains(node)) {
            falseNodes.add(node);
            node.inputNodes.add(this);
        }
    }
}