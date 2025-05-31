package flowforge.nodes.flownodes.comparators;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GreaterThanNode extends Node {
    private ProgramPanel programPanel;
    private boolean isGreater = false;

    public GreaterThanNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.nodeTheme = programPanel.flowForge.comparatorNodeTheme;
        inputXButton.setText("Values");

        JLabel label = new JLabel(">");
        label.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 28));

        topPanel.setLayout(new FlowLayout());
        topPanel.add(label);
    }

    public boolean getIsGreater() {
        return isGreater;
    }

    public void setIsGreater(boolean isGreater) {
        this.isGreater = isGreater;
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
                setIsGreater(false);
                return;
            }

            if (firstNode instanceof IntegerNode) {
                Integer a = ((IntegerNode) firstNode).getValue();
                Integer b = ((IntegerNode) secondNode).getValue();
                setIsGreater(a > b);
            } else if (firstNode instanceof BooleanNode) {
                // For booleans, true is considered greater than false
                Boolean a = ((BooleanNode) firstNode).getValue();
                Boolean b = ((BooleanNode) secondNode).getValue();
                setIsGreater(a && !b);
            }
        } else {
            System.out.println("Error");
        }

        for (Node oNode : outputNodes) {
            oNode.execute(isStepExecution);
        }
    }

}