package flowforge.nodes.flownodes.arithmetic;

import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.PrintNode;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Random;

public class RandomNode extends Node {

    private ProgramPanel programPanel;
    private int result;

    public RandomNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(220, 170);

        inputXButton.setText("Range");
        outputXButton.setText("Random Value");
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
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
        if (inputXNodes.size() != 2) return;

        Integer firstInt = null;
        Integer secondInt = null;

        Node firstNode = inputXNodes.get(0);
        Node secondNode = inputXNodes.get(1);

        if (firstNode instanceof IntegerNode && secondNode instanceof IntegerNode) {
            firstInt = ((IntegerNode) firstNode).getValue();
            secondInt = ((IntegerNode) secondNode).getValue();
        }

        if (firstInt == null || secondInt == null) return;

        Random random = new Random();

        if (firstInt < secondInt) {
            setResult(random.nextInt(firstInt, secondInt));
        } else if (firstInt > secondInt) {
            setResult(random.nextInt(secondInt, firstInt));
        } else {
            setResult(firstInt);
        }

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute(isStepExecution);
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute(isStepExecution);
        }
    }
}
