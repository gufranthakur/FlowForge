package flowforge.nodes.flownodes.comparators;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.core.FlowPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import java.awt.*;

public class GreaterThanNode extends Node {
    private FlowPanel flowPanel;
    private boolean isGreater = false;

    public GreaterThanNode(String title, FlowPanel flowPanel) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        JLabel label = new JLabel(title);
        label.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 28));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public boolean getIsGreater() {
        return isGreater;
    }

    public void setIsGreater(boolean isGreater) {
        this.isGreater = isGreater;
    }

    @Override
    public void execute() {
        if (inputXNodes.size() == 2) {
            Node firstNode = inputXNodes.get(0);
            Node secondNode = inputXNodes.get(1);

            if (firstNode.getClass() != secondNode.getClass()) {
                setIsGreater(false);
                return;
            }

            if (firstNode instanceof IntegerNode) {
                Integer a = ((IntegerNode) firstNode).getIntValue();
                Integer b = ((IntegerNode) secondNode).getIntValue();
                setIsGreater(a > b);
            } else if (firstNode instanceof BooleanNode) {
                // For booleans, true is considered greater than false
                Boolean a = ((BooleanNode) firstNode).getBooleanValue();
                Boolean b = ((BooleanNode) secondNode).getBooleanValue();
                setIsGreater(a && !b);
            }
        } else {
            System.out.println("Error");
        }

        for (Node oNode : outputNodes) {
            oNode.execute();
        }
    }
}