package flowforge.nodes.flownodes;

import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import java.awt.*;

public class DelayNode extends Node {
    private ProgramPanel programPanel;
    private JSpinner delaySpinner;

    public DelayNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(260, 100);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100);
        delaySpinner = new JSpinner(spinnerModel);
        delaySpinner.setPreferredSize(new Dimension(100, 30));

        inputButton.setVisible(true);
        outputButton.setVisible(true);
        outputXButton.setVisible(false);

        inputXButton.setText("InputX");

        wrapperPanel.add(resetConnectionsButton);
        wrapperPanel.add(delaySpinner);
        wrapperPanel.add(new JLabel("ms"));

        contentPanel.add(wrapperPanel, BorderLayout.NORTH);
    }

    @Override
    public void compile() {
        for (Node node : inputXNodes) {
            if (node != null) if (!(node instanceof IntegerNode))
                programPanel.flowForge.console.throwError("Invalid variable being passed to Delay node. \n" +
                        "Expected Integer node, found " + node.getTitle() + "Node", node);
        }
    }

    @Override
    public void execute() {
        int delay = (Integer) delaySpinner.getValue();

        for (Node node : inputXNodes) {
            if (node != null) {
                if (node instanceof InputNode) delay = Integer.parseInt(((InputNode) node).getInputString());
                else if (node instanceof IntegerNode) delay = ((IntegerNode) node).getIntValue();
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