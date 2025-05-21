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
        this.setSize(200, 100);


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
                    node.setBorder(new EmptyBorder(3, 3, 3, 3));
                }
                this.setBorder(new LineBorder(new Color(255, 126, 23), 3));
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