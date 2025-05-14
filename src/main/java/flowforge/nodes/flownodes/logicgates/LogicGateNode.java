package flowforge.nodes.flownodes.logicgates;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.core.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.BooleanNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LogicGateNode extends Node {

    private JLabel label;
    private ProgramPanel programPanel;
    private boolean result = false;
    private String gateType;

    public LogicGateNode(String title, ProgramPanel programPanel, String gateType) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.gateType = gateType.toUpperCase();

        label = new JLabel(gateType);
        label.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 28));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(label, BorderLayout.CENTER);

        this.setSize(240, 130);
    }

    public String getGateType() {
        return gateType;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
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
        switch(gateType) {
            case "NOT":
                executeNot();
                break;
            case "AND":
                executeTwo((a, b) -> a && b);
                break;
            case "OR":
                executeTwo((a, b) -> a || b);
                break;
            case "NAND":
                executeTwo((a, b) -> !(a && b));
                break;
            case "NOR":
                executeTwo((a, b) -> !(a || b));
                break;
            case "XOR":
                executeTwo((a, b) -> a ^ b);
                break;
            default:
                System.out.println("Error: Invalid gate type");
                return;
        }

        for (Node oNode : outputNodes) {
            oNode.execute(isStepExecution);
        }
    }

    private void executeNot() {
        if (inputXNodes.size() != 1) {
            System.out.println("Error: NOT gate needs exactly 1 input");
            return;
        }

        Node inputNode = inputXNodes.get(0);
        if (inputNode instanceof BooleanNode) {
            Boolean a = ((BooleanNode) inputNode).getValue();
            setResult(!a);
        } else {
            System.out.println("Error: Input must be boolean");
        }
    }

    private void executeTwo(BooleanOperator operator) {
        if (inputXNodes.size() != 2) {
            System.out.println("Error: Gate needs exactly 2 inputs");
            return;
        }

        Node firstNode = inputXNodes.get(0);
        Node secondNode = inputXNodes.get(1);

        if (firstNode instanceof BooleanNode && secondNode instanceof BooleanNode) {
            Boolean a = ((BooleanNode) firstNode).getValue();
            Boolean b = ((BooleanNode) secondNode).getValue();
            setResult(operator.apply(a, b));
        } else {
            System.out.println("Error: Inputs must be boolean");
        }
    }

    @FunctionalInterface
    private interface BooleanOperator {
        boolean apply(boolean a, boolean b);
    }

}