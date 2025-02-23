package flowforge.nodes.flownodes.comparators;


import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import java.awt.*;

public class GreaterThanOrEqualNode extends Node {
    private ProgramPanel programPanel;
    private boolean isGreaterOrEqual = false;

    public GreaterThanOrEqualNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        JLabel label = new JLabel(title);
        label.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 28));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.pack();
    }

    public boolean getIsGreaterOrEqual() {
        return isGreaterOrEqual;
    }

    public void setIsGreaterOrEqual(boolean isGreaterOrEqual) {
        this.isGreaterOrEqual = isGreaterOrEqual;
    }

    @Override
    public void compile() {
        for (Node node : inputXNodes) {
            if (node != null) if (!(node instanceof IntegerNode))
                programPanel.flowForge.console.throwError("Invalid variable being passed to " + title + " node. \n" +
                        "Expected Integer node, found " + node.getTitle() + "Node", node);
        }
    }

    @Override
    public void execute() {
        if (inputXNodes.size() == 2) {
            Node firstNode = inputXNodes.get(0);
            Node secondNode = inputXNodes.get(1);

            if (firstNode.getClass() != secondNode.getClass()) {
                setIsGreaterOrEqual(false);
                return;
            }

            if (firstNode instanceof IntegerNode) {
                Integer a = ((IntegerNode) firstNode).getIntValue();
                Integer b = ((IntegerNode) secondNode).getIntValue();
                setIsGreaterOrEqual(a >= b);
            } else if (firstNode instanceof BooleanNode) {
                Boolean a = ((BooleanNode) firstNode).getBooleanValue();
                Boolean b = ((BooleanNode) secondNode).getBooleanValue();
                setIsGreaterOrEqual(a || !b);
            }
        } else {
            System.out.println("Error");
        }

        for (Node oNode : outputNodes) {
            oNode.execute();
        }
    }
}