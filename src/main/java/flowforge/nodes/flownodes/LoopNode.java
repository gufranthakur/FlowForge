package flowforge.nodes.flownodes;

import flowforge.core.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoopNode extends Node {
    private ProgramPanel programPanel;
    public JSpinner loopSpinner;
    private Integer iterationValue;
    private int loops;

    public LoopNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(260, 150);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        loopSpinner = new JSpinner(spinnerModel);
        loopSpinner.setPreferredSize(new Dimension(100, 30));

        inputButton.setVisible(true);
        outputButton.setVisible(true);
        outputXButton.setVisible(true);

        inputXButton.setText("times");
        outputXButton.setText("Iteration Value");

        wrapperPanel.add(resetConnectionsButton);
        wrapperPanel.add(loopSpinner);
        wrapperPanel.add(new JLabel("times"));

        resetConnectionsButton.setVisible(false);

        contentPanel.add(wrapperPanel, BorderLayout.NORTH);
    }

    public Integer getIterationValue() {
        return iterationValue;
    }

    public void setIterationValue(Integer iterationValue) {
        this.iterationValue = iterationValue;
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
        loops = (Integer) loopSpinner.getValue();

        for (Node node : inputXNodes) {
            if (node != null) {
                if (node instanceof InputNode) loops = Integer.parseInt(((InputNode) node).inputValue);
                else if (node instanceof IntegerNode) loops = ((IntegerNode) node).getValue();
                else loops = (Integer) loopSpinner.getValue();
            }
        }


        for (int i = 0; i < loops; i++) {
            for (Node node : outputNodes) {
                setIterationValue(i);
                if (node != null) node.execute(isStepExecution);
            }
        }


    }

}