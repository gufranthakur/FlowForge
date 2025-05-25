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

public class AddNode extends Node {

    private ProgramPanel programPanel;
    private float result;

    public AddNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(210, 150);

        inputXButton.setText("Integers");
        outputXButton.setText("Sum");
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
                    node.setBorder(new EmptyBorder(3, 3, 3, 3));
                }
                this.setStepExecutedBorder();
            });
        }

        float sum = 0;

        for (Node node : inputXNodes) {
            if (node != null) if (node instanceof IntegerNode) {
                sum += ((IntegerNode) node).getValue();
            } else if (node instanceof FloatNode) {
                sum += ((FloatNode) node).getValue();
            }
        }

        setResult(sum);
        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute(isStepExecution);
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute(isStepExecution);
        }
    }


}
