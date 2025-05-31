package flowforge.nodes.flownodes.utils;

import flowforge.ui.panels.ProgramPanel;
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

        this.nodeTheme = new Color(5, 94, 92);

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
                    node.restoreBorder();
                }
                this.setStepExecutedBorder();
            });
        }

        for (Node node : outputNodes) {
            node.execute(isStepExecution);
        }
    }
}
