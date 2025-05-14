package flowforge.nodes.flownodes;

import flowforge.core.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import java.awt.*;

public class DelayNode extends Node {
    private ProgramPanel programPanel;
    public JSpinner delaySpinner;

    public DelayNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(260, 150);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100);
        delaySpinner = new JSpinner(spinnerModel);
        delaySpinner.setPreferredSize(new Dimension(100, 30));

        inputButton.setVisible(true);
        outputButton.setVisible(true);
        outputXButton.setVisible(false);

        inputXButton.setText("Delay Value");

        wrapperPanel.add(resetConnectionsButton);
        wrapperPanel.add(delaySpinner);
        wrapperPanel.add(new JLabel("ms"));

        resetConnectionsButton.addActionListener(e -> {
            delaySpinner.setValue(1000);
        });

        contentPanel.add(wrapperPanel, BorderLayout.NORTH);
    }

    @Override
    public void execute() {
        int delay = (Integer) delaySpinner.getValue();

        for (Node node : inputXNodes) {
            if (node != null) {
                if (node instanceof InputNode) delay = Integer.parseInt(((InputNode) node).inputValue);
                else if (node instanceof IntegerNode) delay = ((IntegerNode) node).getValue();
                else System.out.println("Error");
            }
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute();
        }
    }
}