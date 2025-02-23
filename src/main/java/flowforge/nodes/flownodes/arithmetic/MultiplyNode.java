package flowforge.nodes.flownodes.arithmetic;


import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.IntegerNode;

public class MultiplyNode extends Node {

    private ProgramPanel programPanel;
    private Integer result;

    public MultiplyNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

        inputXButton.setText("Integers");
        outputXButton.setText("Result");
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
                programPanel.flowForge.console.throwError("Invalid variable being passed to Multiply node. \n" +
                        "Expected Integer node, found " + node.getTitle() + " Node", node);
        }
        for (Node node : outputXNodes) if (node != null) node.compile();
        for (Node node : outputNodes) if (node != null) node.compile();
    }

    @Override
    public void execute() {
        int res = 0;
        if (inputXNodes.getFirst() != null && inputXNodes.get(0) instanceof IntegerNode)
            res = ((IntegerNode) inputXNodes.get(0)).getIntValue();


        for (Node node : inputXNodes) {
            if (node != null) if (node instanceof IntegerNode) {
                res *= ((IntegerNode) node).getIntValue();
            }
        }

        setResult(res);

        for (Node node : outputXNodes) {
            if (node != null) node.execute();
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute();
        }
    }

}
