package flowforge.nodes.flownodes.arithmetic;


import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.PrintNode;
import flowforge.nodes.variables.IntegerNode;

public class MultiplyNode extends Node {

    private ProgramPanel programPanel;
    private Integer result;

    public MultiplyNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(210, 150);

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
    public void execute() {
        // Initialize with 1 (multiplicative identity)
        int res = 1;

        // Process all input nodes
        boolean hasInputs = false;
        for (Node node : inputXNodes) {
            if (node != null && node instanceof IntegerNode) {
                res *= ((IntegerNode) node).getValue();
                hasInputs = true;
            }
        }

        // If no valid inputs were found, set result to 0
        if (!hasInputs) {
            res = 0;
        }

        setResult(res);

        // Execute output nodes
        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute();
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute();
        }
    }
}