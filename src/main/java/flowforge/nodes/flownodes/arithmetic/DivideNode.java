package flowforge.nodes.flownodes.arithmetic;

import flowforge.nodes.variables.FloatNode;
import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.PrintNode;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DivideNode extends Node {

    private ProgramPanel programPanel;
    private float result;

    public DivideNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

        this.nodeTheme = new Color(86, 23, 163);

        inputXButton.setText("a / b");
        outputXButton.setText("Quotient");
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
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

        if (inputXNodes.size() != 2) return;

        Float dividend = null;
        Float divisor = null;

        Node firstNode = inputXNodes.get(0);
        Node secondNode = inputXNodes.get(1);

        // Handle all combinations of Integer and Float nodes
        if (firstNode instanceof IntegerNode && secondNode instanceof IntegerNode) {
            dividend = Float.valueOf(((IntegerNode) firstNode).getValue());
            divisor = Float.valueOf(((IntegerNode) secondNode).getValue());
        } else if (firstNode instanceof FloatNode && secondNode instanceof FloatNode) {
            dividend = ((FloatNode) firstNode).getValue();
            divisor = ((FloatNode) secondNode).getValue();
        } else if (firstNode instanceof IntegerNode && secondNode instanceof FloatNode) {
            dividend = Float.valueOf(((IntegerNode) firstNode).getValue());
            divisor = ((FloatNode) secondNode).getValue();
        } else if (firstNode instanceof FloatNode && secondNode instanceof IntegerNode) {
            dividend = ((FloatNode) firstNode).getValue();
            divisor = Float.valueOf(((IntegerNode) secondNode).getValue());
        }

        // Perform division with proper error handling
        if (dividend != null && divisor != null) {
            if (divisor == 0.0f) {
                // Handle division by zero
                result = Float.POSITIVE_INFINITY; // or Float.NaN depending on your preference
                System.err.println("Warning: Division by zero in DivideNode");
            } else {
                result = dividend / divisor;
            }
        } else {
            result = 0.0f;
            System.err.println("Warning: Invalid input types for DivideNode");
        }

        // Execute connected nodes
        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) {
                node.execute(isStepExecution);
            }
        }

        for (Node node : outputNodes) {
            if (node != null) {
                node.execute(isStepExecution);
            }
        }
    }
}