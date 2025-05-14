package flowforge.nodes.flownodes;

import flowforge.core.ui.panels.ProgramPanel;
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
        this.setSize(170, 150);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        inputButton.setVisible(true);
        outputButton.setVisible(true);
        outputXButton.setVisible(false);

        inputXButton.setText("Condition");

        wrapperPanel.add(resetConnectionsButton);
        contentPanel.add(wrapperPanel, BorderLayout.NORTH);
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