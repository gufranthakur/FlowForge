package flowforge.nodes.flownodes.arithmetic;


import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.IntegerNode;

public class SubtractNode extends Node {
    private ProgramPanel programPanel;
    private Integer result;

    public SubtractNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        inputXButton.setText("Integers");
        outputXButton.setText("Difference");
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public void compile() {
        for (Node node : inputXNodes) {
            if (node != null) if (!(node instanceof IntegerNode))
                programPanel.flowForge.console.throwError("Invalid variable being passed to Subtract node. \n" +
                        "Expected Integer node, found " + node.getTitle() + "Node", node);
        }
        for (Node node : outputXNodes) if (node != null) node.compile();
        for (Node node : outputNodes) if (node != null) node.compile();
    }

    @Override
    public void execute() {
        if (inputXNodes.isEmpty()) return;
        int difference = ((IntegerNode)inputXNodes.get(0)).getIntValue();

        for (int i = 1; i < inputXNodes.size(); i++) {
            Node node = inputXNodes.get(i);
            if (node != null && node instanceof IntegerNode) {
                difference -= ((IntegerNode)node).getIntValue();
            }
        }

        setResult(difference);

        for (Node node : outputXNodes) {
            if (node != null) node.execute();
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute();
        }
    }
}
