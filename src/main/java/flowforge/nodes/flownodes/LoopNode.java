package flowforge.nodes.flownodes;

import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.IntegerNode;

import javax.swing.*;
import java.awt.*;

public class LoopNode extends Node {
    private ProgramPanel programPanel;
    private JSpinner loopSpinner;
    private Integer iterationValue;
    private int loops;

    public LoopNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(260, 100);

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

        contentPanel.add(wrapperPanel, BorderLayout.NORTH);
    }

    public Integer getIterationValue() {
        return iterationValue;
    }

    public void setIterationValue(Integer iterationValue) {
        this.iterationValue = iterationValue;
    }

    @Override
    public void compile() {
        for (Node node : inputXNodes) {
            if (node != null) if (!(node instanceof IntegerNode))
                programPanel.flowForge.console.throwError("Invalid variable being passed to Loop node. \n" +
                        "Expected Integer node, found " + node.getTitle() + "Node", node);
        }
        for (Node node : outputXNodes) if (node != null) node.compile();
        for (Node node : outputNodes) if (node != null) node.compile();
    }

    @Override
    public void execute() {
        loops = (Integer) loopSpinner.getValue();

        for (Node node : inputXNodes) {
            if (node != null) {
                if (node instanceof InputNode) loops = Integer.parseInt(((InputNode) node).getInputString());
                else if (node instanceof IntegerNode) loops = ((IntegerNode) node).getIntValue();
                else loops = (Integer) loopSpinner.getValue();
            }
        }

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground(){
                for (int i = 0; i < loops; i++) {
                    for (Node node : outputNodes) {
                        setIterationValue(i);
                        if (node != null) node.execute();
                    }
                }
                return null;
            }
        };
        worker.execute();
        System.out.println(compileToC());
    }

    boolean b = false;
    @Override
    public String compileToC() {
        StringBuilder expression = new StringBuilder();

        if (!inputXNodes.isEmpty()) {
            expression.append("for (int i = 0; i < " + inputXNodes.getFirst().getTitle() + "; i++ { \n");
            for (Node node : outputNodes) {
                if (!b)expression.append("\t" + node.compileToC() + "\n");
                b = true;

            }
        } else {
            expression.append("for (int i = 0; i < " + loopSpinner.getValue().toString() + "; i++ { \n");
            for (Node node : outputNodes) {
                if (!b)expression.append("\t" + node.compileToC() + "\n");
                b = true;

            }
        }


        expression.append("}");

        return expression.toString();
    }
}