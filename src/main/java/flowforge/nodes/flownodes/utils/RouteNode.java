package flowforge.nodes.flownodes.utils;

import flowforge.core.ui.panels.ProgramPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RouteNode extends Node {

    private ProgramPanel programPanel;

    public RouteNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

        inputXButton.setVisible(false);
        outputXButton.setVisible(false);

        this.setSize(200, 100);
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

        for (Node node : outputNodes) {
            node.execute(isStepExecution);
        }
    }
}
