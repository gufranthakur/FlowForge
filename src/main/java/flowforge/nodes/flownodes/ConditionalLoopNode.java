package flowforge.nodes.flownodes;

import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.comparators.*;
import flowforge.nodes.variables.BooleanNode;

import javax.swing.*;
import java.awt.*;

public class ConditionalLoopNode extends Node {
    private ProgramPanel programPanel;
    private int iteration;

    public ConditionalLoopNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(170, 90);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        inputButton.setVisible(true);
        outputButton.setVisible(true);
        outputXButton.setVisible(false);

        inputXButton.setText("Condition");

        wrapperPanel.add(resetConnectionsButton);
        contentPanel.add(wrapperPanel, BorderLayout.NORTH);
    }

    @Override
    public void compile() {
        for (Node node : inputXNodes) {
            if (node !=null) {
                if (!(node instanceof BooleanNode || node instanceof EqualToNode ||
                        node instanceof GreaterThanNode || node instanceof LessThanNode ||
                        node instanceof GreaterThanOrEqualNode || node instanceof LessThanOrEqualNode ||
                        node instanceof NotEqualToNode)) {
                    programPanel.flowForge.console.throwError("Expected external input : Boolean output \n" +
                            "Found " + node.getTitle() + " Node", node);
                }
            }
        }
        for (Node node : outputXNodes) if (node != null) node.compile();
        for (Node node : outputNodes) if (node != null) node.compile();
    }

    @Override
    public void execute() {
        if (inputXNodes.size() != 1) return;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                Node node = inputXNodes.get(0);
                if (node instanceof BooleanNode boolNode) {
                    while (boolNode.getBooleanValue()) {
                        for (Node outputNode : outputNodes) {
                            if (outputNode != null) {
                                outputNode.execute();
                            }
                        }

                    }
                }
                return null;
            }
        };
        worker.execute();
        System.out.println(compileToC());
    }

    @Override
    public String compileToC() {

        StringBuilder expression = new StringBuilder();

        expression.append("while(" + inputXNodes.getFirst().getName() + ") { \n");
        for (Node node : outputNodes) {
            expression.append("\t" + node.compileToC() + "\n");
        }
        expression.append("}");

        return expression.toString();
    }

}