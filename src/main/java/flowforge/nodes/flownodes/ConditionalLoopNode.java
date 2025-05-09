package flowforge.nodes.flownodes;

import flowforge.core.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.BooleanNode;

import javax.swing.*;
import java.awt.*;

public class ConditionalLoopNode extends Node {
    private ProgramPanel programPanel;
    private int iteration;

    public ConditionalLoopNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(170, 150);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        inputButton.setVisible(true);
        outputButton.setVisible(true);
        outputXButton.setVisible(false);

        inputXButton.setText("Condition");

        wrapperPanel.add(resetConnectionsButton);
        contentPanel.add(wrapperPanel, BorderLayout.NORTH);
    }

    @Override
    public void execute() {
        if (inputXNodes.size() != 1) return;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                Node node = inputXNodes.get(0);
                if (node instanceof BooleanNode boolNode) {
                    while (boolNode.getValue()) {
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
    }

}