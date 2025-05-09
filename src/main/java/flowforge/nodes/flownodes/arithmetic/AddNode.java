package flowforge.nodes.flownodes.arithmetic;


import flowforge.core.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.PrintNode;
import flowforge.nodes.variables.IntegerNode;

public class AddNode extends Node {

    private ProgramPanel programPanel;
    private Integer result;

    public AddNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(210, 150);

        inputXButton.setText("Integers");
        outputXButton.setText("Sum");
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public void execute() {
        int sum = 0;

        for (Node node : inputXNodes) {
            if (node != null) if (node instanceof IntegerNode) {
                sum += ((IntegerNode) node).getValue();
            }
        }

        setResult(sum);

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute();
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute();
        }
    }

}
