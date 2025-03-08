package flowforge.nodes.flownodes.arithmetic;

import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.PrintNode;
import flowforge.nodes.variables.IntegerNode;

public class ModulusNode extends Node {

    private ProgramPanel programPanel;
    private Integer result;

    public ModulusNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(230, 150);

        inputXButton.setText("Divisor / Dividend");
        outputXButton.setText("Remainder");
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public void execute() {
        if (inputXNodes.size() != 2) return;

        Integer dividend = null;
        Integer divisor = null;

        Node firstNode = inputXNodes.get(0);
        Node secondNode = inputXNodes.get(1);

        if (firstNode instanceof IntegerNode && secondNode instanceof IntegerNode) {
            dividend = ((IntegerNode) firstNode).getValue();
            divisor = ((IntegerNode) secondNode).getValue();
        }

        if (dividend != null && divisor != null && divisor != 0) {
            result = dividend % divisor;
        } else {
            result = 0;
        }

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute();
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute();
        }
    }
}