package flowforge.nodes.flownodes.utils;

import flowforge.nodes.Node;
import flowforge.ui.panels.ProgramPanel;

import javax.swing.*;

public class RouteNode extends Node
{

    private ProgramPanel programPanel;

    public RouteNode(String title, ProgramPanel programPanel)
    {
        super(title, programPanel);
        this.programPanel = programPanel;

        this.nodeTheme = programPanel.flowForge.utilNodeTheme;

        inputXButton.setVisible(false);
        outputXButton.setVisible(false);

        this.setSize(200, 100);
    }

    @Override
    public void execute(boolean isStepExecution)
    {

        if (isStepExecution)
        {
            synchronized (programPanel.stepExecutorLock)
            {
                try
                {
                    programPanel.stepExecutorLock.wait();
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
            SwingUtilities.invokeLater(() ->
            {
                for (Node node : programPanel.nodes)
                {
                    node.restoreBorder();
                }
                this.setStepExecutedBorder();
            });
        }

        for (Node node : outputNodes)
        {
            node.execute(isStepExecution);
        }
    }
}
