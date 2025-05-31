package flowforge.nodes.variables;

import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.InputNode;
import flowforge.nodes.flownodes.arithmetic.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class IntegerNode extends Node {
    private ProgramPanel programPanel;
    public JSpinner spinner;

    public IntegerNode(String title, ProgramPanel programPanel, Integer intValue) {
        super(title, programPanel);
        this.programPanel = programPanel;

        this.nodeTheme = programPanel.flowForge.variableNodeTheme;

        spinner = new JSpinner();
        spinner.setPreferredSize(new Dimension(100, 30));
        resetConnectionsButton.setVisible(false);

        inputButton.setVisible(false);
        outputButton.setVisible(false);

        inputXButton.setText("Set");
        outputXButton.setText("Get");

        topPanel.add(spinner);

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
        for (Node node : inputXNodes) {
            if (node != null) {
                switch (node) {
                    case InputNode inputNode -> setIntValue(Integer.valueOf(inputNode.inputValue));
                    case AddNode addNode -> setIntValue((int) addNode.getResult());
                    case SubtractNode subtractNode -> setIntValue((int) subtractNode.getResult());
                    case MultiplyNode multiplyNode -> setIntValue((int) multiplyNode.getResult());
                    case DivideNode divideNode -> setIntValue((int) divideNode.getResult());
                    case ModulusNode modulusNode -> setIntValue((int) modulusNode.getResult());
                    default -> setIntValue((Integer) spinner.getValue());
                }
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute(isStepExecution);
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