package flowforge.nodes.flownodes.arithmetic;


import flowforge.core.ui.panels.ProgramPanel;
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
        int res = 1;
        boolean hasInputs = false;
        for (Node node : inputXNodes) {
            if (node != null && node instanceof IntegerNode) {
                res *= ((IntegerNode) node).getValue();
                hasInputs = true;
            }
        }
        if (!hasInputs) {
            res = 0;
        }

        setResult(res);

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute();
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute();
        }
    }
}