package flowforge.nodes.flownodes;

import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;

import javax.swing.*;

public class InputNode extends Node {

    private ProgramPanel programPanel;

    public InputNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        inputXButton.setVisible(false);
    }

    public String getInputString() {
        return JOptionPane.showInputDialog("Enter value");
    }

    @Override
    public void compile() {
        for (Node node : outputXNodes) if (node != null) node.compile();
        for (Node node : outputNodes) if (node != null) node.compile();
    }

    @Override
    public void execute() {
        for (Node node : outputXNodes) {
            node.execute();
        }

        for (Node node : outputNodes) {
            node.execute();
        }
    }
}
