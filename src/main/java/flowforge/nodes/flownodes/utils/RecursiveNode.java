package flowforge.nodes.flownodes.utils;

import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RecursiveNode extends Node {
    private ProgramPanel programPanel;

    public RecursiveNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

        this.nodeTheme = programPanel.flowForge.utilNodeTheme;
        inputButton.setVisible(true);
        outputButton.setVisible(true);

        inputXButton.setVisible(false);
        outputXButton.setVisible(false);
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


        synchronized (programPanel.stepExecutorLock) {
            try {
                programPanel.stepExecutorLock.wait(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute(isStepExecution);
        }
    }

}