package flowforge.core;

import flowforge.FlowForge;
import flowforge.ui.panels.Console;
import flowforge.ui.panels.ControlPanel;
import flowforge.ui.panels.ProgramPanel;

import javax.swing.*;

public class ForgeExecutor {

    private final FlowForge flowForge;
    private ProgramPanel programPanel;
    private ControlPanel controlPanel;
    private Console console;

    private boolean isExecuting = false;
    private SwingWorker<Void, Void> nodeExecutor;

    public ForgeExecutor(FlowForge flowForge) {
        this.flowForge = flowForge;
        this.programPanel = flowForge.programPanel;
        this.controlPanel = flowForge.controlPanel;
        this.console = flowForge.console;
    }

    public void execute() {
        programPanel.flowForge.console.clear();
        console.print("Program Execution started");
        console.print("Total nodes : " + programPanel.getNodeAmount());
        console.print("");

        isExecuting = true;

        nodeExecutor = new SwingWorker<>() {
            @Override
            protected Void doInBackground()  {
                isExecuting = true;
                programPanel.startNode.execute(false);
                return null;
            }
            @Override
            protected void done() {
                console.getRootPanel().setVisible(true);
                console.print("");
                stopExecution(false);
            }
        };

        nodeExecutor.execute();
    }

    public void stopExecution(boolean manualStop) {
        ControlPanel controlPanel = flowForge.controlPanel;
        Console console = flowForge.console;

        if (!manualStop) {
            controlPanel.runStopButton.setText("▶ Run");
            controlPanel.runStopButton.setBackground(flowForge.theme);
            console.print("Execution completed successfully");
        } else {
            nodeExecutor.cancel(true);
            console.print("Execution Stopped");
        }

        isExecuting = false;
    }


    public boolean getIsExecuting() {
        return isExecuting;
    }


    public void executeByStep() {
        programPanel.flowForge.console.clear();
        console.print("Step Execution started");
        console.print("Total nodes : " + programPanel.getNodeAmount());
        console.print("");

        SwingWorker<Void, Void> nodeStepExecutor = new SwingWorker<>() {
            @Override
            protected Void doInBackground()  {
                synchronized (programPanel.stepExecutorLock) {
                    programPanel.startNode.execute(true);
                }
                return null;
            }
            @Override
            protected void done() {
                console.print("");
                console.print("Step Execution completed");
                console.print("Click on the stop button to exit out of execution mode");
            }
        };

        nodeStepExecutor.execute();
    }

}
