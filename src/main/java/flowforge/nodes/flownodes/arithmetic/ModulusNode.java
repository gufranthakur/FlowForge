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

public class ModulusNode extends Node {

    private ProgramPanel programPanel;
    private float result;

    public ModulusNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

        this.nodeTheme = programPanel.flowForge.arithmeticNodeTheme;

        inputXButton.setText("a % b");
        outputXButton.setText("Remainder");
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

        if (dividend != null && divisor != null) {
            if (divisor == 0.0f) {
                result = Float.NaN;
                System.err.println("Warning: Modulus by zero in ModulusNode");
            } else {
                result = dividend % divisor;
            }
        } else {
            result = 0.0f;
            System.err.println("Warning: Invalid input types for ModulusNode");
        }

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute(isStepExecution);
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute(isStepExecution);
        }
    }
}
