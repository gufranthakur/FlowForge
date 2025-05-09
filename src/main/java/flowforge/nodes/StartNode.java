package flowforge.nodes;

import flowforge.core.ui.panels.ProgramPanel;

import java.awt.*;

public class StartNode extends Node{

    private ProgramPanel programPanel;

    public StartNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.setClosable(false);
        this.setResizable(false);
        this.programPanel = programPanel;

        inputButton.setVisible(false);
        inputXButton.setVisible(false);
        outputXButton.setVisible(true);

        contentPanel.add(outputsPanel, BorderLayout.CENTER);

        this.pack();
        this.setLocation(20, 300);
    }

    public void print(String message) {
        programPanel.flowForge.console.print(message);
    }

    @Override
    public void execute() {
        programPanel.flowForge.console.clear();
        print("Program Execution started");
        print("Total nodes : " + programPanel.getNodeAmount());

        System.out.println(outputNodes.size());

        for (Node outputXNode : outputXNodes) if (outputXNode != null) outputXNode.execute();
        for (Node outputNode : outputNodes) outputNode.execute();

    }
}