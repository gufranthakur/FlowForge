package flowforge.nodes.flownodes;

import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.comparators.*;
import flowforge.nodes.flownodes.logicgates.LogicGateNode;
import flowforge.nodes.variables.BooleanNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class BranchNode extends Node {

    private ArrayList<Node> trueNodes = new ArrayList<>();
    private ArrayList<Node> falseNodes = new ArrayList<>();

    public Color trueConnectionColor = new Color(62, 80, 236);
    public Color trueConnectionColor2 = new Color(55, 155, 236);

    public Color falseConnectionColor = new Color(255, 45, 118);
    public Color falseConnectionColor2 = new Color(234, 21, 22);

    public boolean isBeingTrueConnected = false;
    public boolean isBeingFalseConnected = false;

    private ProgramPanel programPanel;

    public BranchNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(200, 100);
        this.pack();

        inputButton.setText("Input");
        inputXButton.setText("Condition");

        outputButton.setText("True");
        outputXButton.setText("False");

        outputButton.addActionListener(e -> {

            isBeingFalseConnected = false;
            isBeingTrueConnected = true;

            for (Node node : programPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }

            programPanel.startConnection(this);

        });

        outputXButton.addActionListener(e -> {
            programPanel.selectedNode = this;

            isBeingFalseConnected = true;
            isBeingTrueConnected = false;

            for (Node node : programPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }

            programPanel.startConnection(this);

        });

    }

    @Override
    public void connectTo(Node target) {
        trueNodes.add(target);
        target.inputNodes.add(this);
        outputButton.setSelected(true);

    }

    @Override
    public void connectToX(Node target) {
        falseNodes.add(target);
        target.inputNodes.add(this);
        outputXButton.setSelected(true);
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
        outputXButton.setSelected(false);
    }

    /**
     * Executes the branch logic.
     * Evaluates the condition from the connected input node and
     * executes either the true or false branch accordingly.
     */
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
                    node.setBorder(new EmptyBorder(3, 3, 3, 3));
                }
                this.setBorder(new LineBorder(new Color(255, 126, 23), 3));
            });
        }
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
                    node.execute(isStepExecution);
                }
            }
        } else {
            for (Node node : falseNodes) {
                if (node != null) {
                    node.execute(isStepExecution);
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

        for (Node node : trueNodes) {
            Point start = getOutputPoint();
            Point end = node.getInputPoint();
            drawCurvedGradientLine(g, start, end, new Color(31, 216, 241), new Color(37, 114, 205));
        }

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