package flowforge.nodes.flownodes;

import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DelayNode extends Node {
    private ProgramPanel programPanel;
    public JSpinner delaySpinner;

    public DelayNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

        this.nodeTheme = new Color(138, 46, 14);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100);
        delaySpinner = new JSpinner(spinnerModel);
        delaySpinner.setPreferredSize(new Dimension(100, 30));

        inputButton.setVisible(true);
        outputButton.setVisible(true);
        outputXButton.setVisible(false);


        inputXButton.setText("Delay");

        wrapperPanel.setBackground(getBackground());
        wrapperPanel.add(delaySpinner);
        wrapperPanel.add(new JLabel("ms"));

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

        int delay = (Integer) delaySpinner.getValue();

        for (Node node : inputXNodes) {
            if (node != null) {
                if (node instanceof InputNode) delay = Integer.parseInt(((InputNode) node).inputValue);
                else if (node instanceof IntegerNode) delay = ((IntegerNode) node).getValue();
                else System.out.println("Error");
            }
        }

        synchronized (programPanel.stepExecutorLock) {
            try {
                programPanel.stepExecutorLock.wait(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute(isStepExecution);
        }
    }

}