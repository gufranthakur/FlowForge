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

public class MultiplyNode extends Node {

    private ProgramPanel programPanel;
    private float result;

    public MultiplyNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

        this.nodeTheme = new Color(107, 48, 177);

        inputXButton.setText("Numbers");
        outputXButton.setText("Result");
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

        float res = 1.0f;
        boolean hasInputs = false;

        for (Node node : inputXNodes) {
            if (node instanceof IntegerNode) {
                res *= ((IntegerNode) node).getValue();
                hasInputs = true;
            } else if (node instanceof FloatNode) {
                res *= ((FloatNode) node).getValue();
                hasInputs = true;
            }
        }

        if (!hasInputs) {
            res = 0.0f;
        }

        setResult(res);

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute(isStepExecution);
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute(isStepExecution);
        }
    }
}