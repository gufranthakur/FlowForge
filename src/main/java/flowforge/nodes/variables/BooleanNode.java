package flowforge.nodes.variables;

import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.InputNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BooleanNode extends Node {

    private ProgramPanel programPanel;
    public JCheckBox checkBox;

    public BooleanNode(String title, ProgramPanel programPanel, Boolean booleanValue) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(200, 100);

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
        wrapperPanel.setBackground(getBackground());
        wrapperPanel.add(checkBox);

        contentPanel.add(wrapperPanel, BorderLayout.NORTH);


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
        for (Node node : inputXNodes) {
            if (node != null) {
                if (node instanceof InputNode) System.out.println("Error");
                else setBooleanValue(checkBox.isSelected());
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute(isStepExecution);
        }

    }

    public Boolean getValue() {
        return programPanel.booleans.get(title);
    }

    public void setBooleanValue(Boolean booleanValue) {
        programPanel.booleans.put(title, booleanValue);
        System.out.println(programPanel.booleans);
    }



}