package flowforge.nodes.flownodes.comparators;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.core.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import java.awt.*;

public class LessThanNode extends Node {
    private ProgramPanel programPanel;
    private boolean isLess = false;

    public LessThanNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        JLabel label = new JLabel(title);
        label.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 28));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.pack();
    }

    public boolean getIsLess() {
        return isLess;
    }

    public void setIsLess(boolean isLess) {
        this.isLess = isLess;
    }

    @Override
    public void execute() {
        if (inputXNodes.size() == 2) {
            Node firstNode = inputXNodes.get(0);
            Node secondNode = inputXNodes.get(1);

            if (firstNode.getClass() != secondNode.getClass()) {
                setIsLess(false);
                return;
            }

            if (firstNode instanceof IntegerNode) {
                Integer a = ((IntegerNode) firstNode).getValue();
                Integer b = ((IntegerNode) secondNode).getValue();
                setIsLess(a < b);
            } else if (firstNode instanceof BooleanNode) {
                Boolean a = ((BooleanNode) firstNode).getValue();
                Boolean b = ((BooleanNode) secondNode).getValue();
                setIsLess(!a && b);
            }
        } else {
            System.out.println("Error");
        }

        for (Node oNode : outputNodes) {
            oNode.execute();
        }
    }

}