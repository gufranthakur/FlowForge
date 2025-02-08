package flowforge.nodes;

import flowforge.core.FlowPanel;

import java.awt.*;

public class StartNode extends Node{

    public StartNode(String title, FlowPanel flowPanel) {
        super(title, flowPanel);
        this.setClosable(false);
        this.setResizable(false);

        inputButton.setVisible(false);

        contentPanel.add(outputButton, BorderLayout.CENTER);

        this.setSize(150, 70);
        this.setLocation(20, 300);
    }

    @Override
    public void execute() {
        System.out.println("Start node executed");
        outputNode.execute();
    }
}