package flowforge.nodes.variables;

import flowforge.core.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.InputNode;
import flowforge.nodes.flownodes.arithmetic.*;

import javax.swing.*;
import java.awt.*;

public class IntegerNode extends Node {
    private ProgramPanel programPanel;
    public JSpinner spinner;

    public IntegerNode(String title, ProgramPanel programPanel, Integer intValue) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(170, 100);

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
                switch (node) {
                    case InputNode inputNode -> setIntValue(Integer.valueOf(inputNode.inputValue));
                    case AddNode addNode -> setIntValue(addNode.getResult());
                    case SubtractNode subtractNode -> setIntValue(subtractNode.getResult());
                    case MultiplyNode multiplyNode -> setIntValue(multiplyNode.getResult());
                    case DivideNode divideNode -> setIntValue(divideNode.getResult());
                    case ModulusNode modulusNode -> setIntValue(modulusNode.getResult());
                    default -> setIntValue((Integer) spinner.getValue());
                }
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }

    }

    public Integer getValue() {
        return programPanel.integers.get(title);
    }

    public void setIntValue(Integer intValue) {
        programPanel.integers.put(title, intValue);
        System.out.println(programPanel.integers);
    }

}