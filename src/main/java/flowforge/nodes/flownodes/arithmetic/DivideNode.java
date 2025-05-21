package flowforge.nodes.flownodes.arithmetic;

import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.PrintNode;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DivideNode extends Node {

    private ProgramPanel programPanel;
    private Integer result;

    public DivideNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(230, 150);

        inputXButton.setText("Divisor / Dividend");
        outputXButton.setText("Quotient");
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

        Integer dividend = null;
        Integer divisor = null;

        Node firstNode = inputXNodes.get(0);
        Node secondNode = inputXNodes.get(1);

        if (firstNode instanceof IntegerNode && secondNode instanceof IntegerNode) {
            dividend = ((IntegerNode) firstNode).getValue();
            divisor = ((IntegerNode) secondNode).getValue();
        }

        if (dividend != null && divisor != null && divisor != 0) {
            result = dividend / divisor;
        } else {
            result = 0;
        }

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute(isStepExecution);
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute(isStepExecution);
        }

    }

}