package flowforge.nodes.flownodes.arithmetic;


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
    private Integer result;

    public SubtractNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(240, 180);
        inputXButton.setText("Integers");
        outputXButton.setText("Difference");
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
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
                this.setBorder(new LineBorder(new Color(255, 126, 23), 3));
            });
        }
        if (inputXNodes.isEmpty()) return;
        int difference = ((IntegerNode)inputXNodes.get(0)).getValue();

        for (int i = 1; i < inputXNodes.size(); i++) {
            Node node = inputXNodes.get(i);
            if (node != null && node instanceof IntegerNode) {
                difference -= ((IntegerNode)node).getValue();
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
