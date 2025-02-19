package flowforge.nodes;

import flowforge.core.MainPanel;

import java.awt.*;

public class StartNode extends Node{

    private MainPanel mainPanel;

    public StartNode(String title, MainPanel mainPanel) {
        super(title, mainPanel);
        this.setClosable(false);
        this.setResizable(false);
        this.mainPanel = mainPanel;

        inputButton.setVisible(false);
        inputXButton.setVisible(false);
        outputXButton.setVisible(true);

        contentPanel.add(outputsPanel, BorderLayout.CENTER);

        this.setSize(150, 100);
        this.setLocation(20, 300);
    }

    public void print(String message) {
        mainPanel.flowForge.console.print(message);
    }

    @Override
    public void execute() {
        mainPanel.flowForge.console.clear();
        print("Program Execution started");
        print("Total nodes : " + mainPanel.getNodeAmount());

        for (Node outputXNode : outputXNodes) if (outputXNode != null) outputXNode.execute();
        for (Node outputNode : outputNodes) outputNode.execute();

    }
}