package flowforge.nodes.flownodes.comparators;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.core.FlowPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;
import flowforge.nodes.variables.StringNode;

import javax.swing.*;
import java.awt.*;

public class EqualToNode extends Node {

    private FlowPanel flowPanel;
    private boolean isEqual = false;

    public EqualToNode(String title, FlowPanel flowPanel) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        JLabel label = new JLabel(title);
        label.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 28));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public boolean getIsEqual() {
        return isEqual;
    }

    public void setIsEqual(boolean isEqual) {
        this.isEqual = isEqual;
    }

    @Override
    public void execute() {
        if (inputXNodes.size() == 2) {
            Node firstNode = inputXNodes.get(0);
            Node secondNode = inputXNodes.get(1);

            if (firstNode.getClass() != secondNode.getClass()) {
                setIsEqual(false);
                return;
            }

            if (firstNode instanceof IntegerNode) {
                Integer a = ((IntegerNode) firstNode).getIntValue();
                Integer b = ((IntegerNode) secondNode).getIntValue();
                setIsEqual(a.equals(b));
            } else if (firstNode instanceof StringNode) {
                String a = ((StringNode) firstNode).getStringValue();
                String b = ((StringNode) secondNode).getStringValue();
                setIsEqual(a.equals(b));
            } else if (firstNode instanceof BooleanNode) {
                Boolean a = ((BooleanNode) firstNode).getBooleanValue();
                Boolean b = ((BooleanNode) secondNode).getBooleanValue();
                setIsEqual(a.equals(b));
            }
        } else {
            System.out.println("Error");
        }

        for (Node oNode : outputNodes) {
            oNode.execute();
        }


    }

}
