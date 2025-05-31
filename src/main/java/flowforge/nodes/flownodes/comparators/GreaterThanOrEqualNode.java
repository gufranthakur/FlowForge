package flowforge.nodes.flownodes.comparators;


import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GreaterThanOrEqualNode extends Node {
    private ProgramPanel programPanel;
    private boolean isGreaterOrEqual = false;

    public GreaterThanOrEqualNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.nodeTheme = new Color(130, 140, 6);

        inputXButton.setText("Values");

        JLabel label = new JLabel(">=");
        label.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 28));

        topPanel.setLayout(new FlowLayout());
        topPanel.add(label);
    }

    public boolean getIsGreaterOrEqual() {
        return isGreaterOrEqual;
    }

    public void setIsGreaterOrEqual(boolean isGreaterOrEqual) {
        this.isGreaterOrEqual = isGreaterOrEqual;
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
        if (inputXNodes.size() == 2) {
            Node firstNode = inputXNodes.get(0);
            Node secondNode = inputXNodes.get(1);

            if (firstNode.getClass() != secondNode.getClass()) {
                setIsGreaterOrEqual(false);
                return;
            }

            if (firstNode instanceof IntegerNode) {
                Integer a = ((IntegerNode) firstNode).getValue();
                Integer b = ((IntegerNode) secondNode).getValue();
                setIsGreaterOrEqual(a >= b);
            } else if (firstNode instanceof BooleanNode) {
                Boolean a = ((BooleanNode) firstNode).getValue();
                Boolean b = ((BooleanNode) secondNode).getValue();
                setIsGreaterOrEqual(a || !b);
            }
        } else {
            System.out.println("Error");
        }

        for (Node oNode : outputNodes) {
            oNode.execute(isStepExecution);
        }
    }

}