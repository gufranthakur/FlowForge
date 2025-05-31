package flowforge.nodes.variables;

import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.InputNode;
import flowforge.nodes.flownodes.arithmetic.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class FloatNode extends Node {
    private ProgramPanel programPanel;
    public JSpinner spinner;

    public FloatNode(String title, ProgramPanel programPanel, Float floatValue) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.nodeTheme = programPanel.flowForge.variableNodeTheme;

        SpinnerNumberModel model = new SpinnerNumberModel(1.0, 0.0, 10.0, 0.1);

        spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.NumberEditor(spinner, "0.0"));
        spinner.setPreferredSize(new Dimension(100, 30));

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
                    case InputNode inputNode -> setFloatValue(Float.parseFloat(inputNode.inputValue));
                    case AddNode addNode -> setFloatValue(addNode.getResult());
                    case SubtractNode subtractNode -> setFloatValue(subtractNode.getResult());
                    case MultiplyNode multiplyNode -> setFloatValue(multiplyNode.getResult());
                    case DivideNode divideNode -> setFloatValue(divideNode.getResult());
                    case ModulusNode modulusNode -> setFloatValue(modulusNode.getResult());
                    default -> setFloatValue(((Double) spinner.getValue()).floatValue());


                }
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute(isStepExecution);
        }

    }

    public float getValue() {
        return programPanel.floats.get(title);
    }

    public void setFloatValue(float floatValue) {
        programPanel.floats.put(title, floatValue);
        System.out.println(programPanel.floats);
    }



}