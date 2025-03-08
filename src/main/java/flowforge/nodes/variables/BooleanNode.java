package flowforge.nodes.variables;

import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.InputNode;

import javax.swing.*;
import java.awt.*;

public class BooleanNode extends Node {

    private ProgramPanel programPanel;
    public JCheckBox checkBox;

    public BooleanNode(String title, ProgramPanel programPanel, Boolean booleanValue) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(240, 90);

        checkBox = new JCheckBox("Set value : False");

        checkBox.addActionListener(e -> {
            if (!checkBox.isSelected()) {
                checkBox.setText("Set value : False");
            } else {
                checkBox.setText("Set value : True");
            }
        });

        inputButton.setVisible(false);
        outputButton.setVisible(false);

        inputXButton.setText("Set");
        outputXButton.setText("Get");

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperPanel.add(checkBox);

        contentPanel.add(wrapperPanel, BorderLayout.NORTH);

        this.setSize(260, 100);
    }

    @Override
    public void execute() {
        for (Node node : inputXNodes) {
            if (node != null) {
                if (node instanceof InputNode) System.out.println("Error");
                else setBooleanValue(checkBox.isSelected());
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }

        System.out.println(compileToC());
    }

    public Boolean getValue() {
        return programPanel.booleans.get(title);
    }

    public void setBooleanValue(Boolean booleanValue) {
        programPanel.booleans.put(title, booleanValue);
        System.out.println(programPanel.booleans);
    }
    @Override
    public String compileToC() {
        String varName = "bool_" + title;

        StringBuilder code = new StringBuilder();
        code.append("boolean " + varName + " = " + getValue());

        return code.toString();

    }
}