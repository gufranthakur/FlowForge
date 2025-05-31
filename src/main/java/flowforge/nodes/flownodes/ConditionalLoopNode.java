package flowforge.nodes.flownodes;

import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.BooleanNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ConditionalLoopNode extends Node {
    private ProgramPanel programPanel;
    private int iteration;

    public ConditionalLoopNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

        this.nodeTheme = new Color(11, 35, 131);

        inputButton.setVisible(true);
        outputButton.setVisible(true);
        outputXButton.setVisible(false);


        inputXButton.setText("Condition");

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
        if (inputXNodes.size() != 1) return;


        Node node = inputXNodes.get(0);
        if (node instanceof BooleanNode boolNode) {
            while (boolNode.getValue()) {
                for (Node outputNode : outputNodes) {
                    if (outputNode != null) {
                        outputNode.execute(isStepExecution);
                    }
                }

            }
        }


    }


}