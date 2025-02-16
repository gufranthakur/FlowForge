package flowforge.nodes;

import flowforge.core.FlowPanel;

import java.awt.*;

public class StartNode extends Node{

    private FlowPanel flowPanel;

    public StartNode(String title, FlowPanel flowPanel) {
        super(title, flowPanel);
        this.setClosable(false);
        this.setResizable(false);
        this.flowPanel = flowPanel;

        inputButton.setVisible(false);
        inputXButton.setVisible(false);
        outputXButton.setVisible(true);

        contentPanel.add(outputsPanel, BorderLayout.CENTER);

        this.setSize(150, 100);
        this.setLocation(20, 300);
    }

    @Override
    public void execute() {
        flowPanel.flowForge.console.clear();
        flowPanel.flowForge.console.print("Program Execution started");
        flowPanel.flowForge.console.print("Total nodes : " + flowPanel.getNodeAmount());

        for (Node outputXNode : outputXNodes) {
            if (outputXNode != null) outputXNode.execute();
        }
        for (Node outputNode : outputNodes) {
            if (outputNode == null) {
                flowPanel.flowForge.console.print("WARNING : No node is connected to the start node" );
            } else {
                outputNode.execute();
            }
        }

    }
}