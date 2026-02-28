package flowforge.nodes.flownodes.utils;

import flowforge.nodes.Node;
import flowforge.ui.panels.ProgramPanel;

import javax.swing.*;

public class RecursiveNode extends Node
{
    private ProgramPanel programPanel;

    public RecursiveNode(String title, ProgramPanel programPanel)
    {
        super(title, programPanel);
        this.programPanel = programPanel;

        this.nodeTheme = programPanel.flowForge.utilNodeTheme;
        inputButton.setVisible(true);
        outputButton.setVisible(true);

        inputXButton.setVisible(false);
        outputXButton.setVisible(false);
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


        synchronized (programPanel.stepExecutorLock)
        {
            try
            {
                programPanel.stepExecutorLock.wait(200);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }

        for (Node node : outputNodes)
        {
            if (node != null) node.execute(isStepExecution);
        }
    }

}