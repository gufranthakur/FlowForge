package flowforge.nodes.flownodes.arithmetic;


import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.PrintNode;
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
    public void execute() {
        if (inputXNodes.isEmpty()) return;
        int difference = ((IntegerNode)inputXNodes.get(0)).getValue();

        for (int i = 1; i < inputXNodes.size(); i++) {
            Node node = inputXNodes.get(i);
            if (node != null && node instanceof IntegerNode) {
                difference -= ((IntegerNode)node).getValue();
            }
        }

        setResult(difference);

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute();
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute();
        }
    }


}
