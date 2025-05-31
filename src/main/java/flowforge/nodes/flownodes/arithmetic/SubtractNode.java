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

public class SubtractNode extends Node {
    private ProgramPanel programPanel;
    private float result;

    public SubtractNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.nodeTheme = programPanel.flowForge.arithmeticNodeTheme;
        inputXButton.setText("Numbers");
        outputXButton.setText("Difference");
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

        if (inputXNodes.isEmpty()) return;

        float difference = 0.0f;

        // Get the first value
        Node firstNode = inputXNodes.get(0);
        if (firstNode instanceof IntegerNode) {
            difference = ((IntegerNode) firstNode).getValue();
        } else if (firstNode instanceof FloatNode) {
            difference = ((FloatNode) firstNode).getValue();
        }

        // Subtract all subsequent values
        for (int i = 1; i < inputXNodes.size(); i++) {
            Node node = inputXNodes.get(i);
            if (node instanceof IntegerNode) {
                difference -= ((IntegerNode) node).getValue();
            } else if (node instanceof FloatNode) {
                difference -= ((FloatNode) node).getValue();
            }
        }

        setResult(difference);

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute(isStepExecution);
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute(isStepExecution);
        }
    }
}
