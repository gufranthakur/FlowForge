package flowforge.nodes.variables;

import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.StartNode;
import flowforge.nodes.flownodes.InputNode;
import flowforge.nodes.flownodes.arithmetic.AddNode;
import flowforge.nodes.flownodes.arithmetic.MultiplyNode;
import flowforge.nodes.flownodes.arithmetic.SubtractNode;

import javax.swing.*;
import java.awt.*;

public class IntegerNode extends Node {
    private ProgramPanel programPanel;
    public JSpinner spinner;

    public IntegerNode(String title, ProgramPanel programPanel, Integer intValue) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(170, 90);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        spinner = new JSpinner();

        inputButton.setVisible(false);
        outputButton.setVisible(false);

        inputXButton.setText("Set");
        outputXButton.setText("Get");

        wrapperPanel.add(spinner);
        contentPanel.add(wrapperPanel, BorderLayout.NORTH);
    }

    @Override
    public void execute() {
        for (Node node : inputXNodes) {
            if (node != null) {
                if (node instanceof InputNode) setIntValue(Integer.valueOf(((InputNode) node).getInputString()));
                else if (node instanceof AddNode) setIntValue(((AddNode) node).getResult());
                else if (node instanceof SubtractNode) setIntValue(((SubtractNode) node).getResult());
                else setIntValue((Integer) spinner.getValue());
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }

        System.out.println(compileToC());
    }

    public Integer getIntValue() {
        return programPanel.integers.get(title);
    }

    public void setIntValue(Integer intValue) {
        programPanel.integers.put(title, intValue);
        System.out.println(programPanel.integers);
    }
    @Override
    public String compileToC() {
        // For a variable node, we need to declare it and initialize it
        String varName = "int_" + title;
        int value = getIntValue();

        StringBuilder code = new StringBuilder();
        code.append("int ").append(varName).append(" = ").append(value).append(";\n");

        return code.toString();
    }
}