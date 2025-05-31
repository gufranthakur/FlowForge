package flowforge.nodes;

import flowforge.ui.panels.ProgramPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StartNode extends Node{

    private ProgramPanel programPanel;

    public StartNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.setClosable(false);
        this.setResizable(false);
        this.programPanel = programPanel;

        inputButton.setVisible(false);
        inputXButton.setVisible(false);
        outputXButton.setVisible(true);

        contentPanel.add(outputsPanel, BorderLayout.CENTER);

        this.nodeTheme = new Color(5, 121, 9);

        this.pack();
        this.setLocation(20, 300);
    }

    public void print(String message) {
        programPanel.flowForge.console.print(message);
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

        System.out.println(outputNodes.size());

        for (Node outputXNode : outputXNodes) if (outputXNode != null) outputXNode.execute(isStepExecution);
        for (Node outputNode : outputNodes) outputNode.execute(isStepExecution);
    }


}