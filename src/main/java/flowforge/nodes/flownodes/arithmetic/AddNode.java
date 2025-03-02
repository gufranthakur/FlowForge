package flowforge.nodes.flownodes.arithmetic;


import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.IntegerNode;

public class AddNode extends Node {

    private ProgramPanel programPanel;
    private Integer result;

    public AddNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

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
    public void compile() {
        for (Node node : inputXNodes) {
            if (node != null) if (!(node instanceof IntegerNode))
                programPanel.flowForge.console.throwError("Invalid variable being passed to Add node. \n" +
                        "Expected Integer node, found " + node.getTitle() + "Node", node);
        }
        for (Node node : outputXNodes) if (node != null) node.compile();
        for (Node node : outputNodes) if (node != null) node.compile();
    }

    @Override
    public void execute() {
        int sum = 0;

        for (Node node : inputXNodes) {
            if (node != null) if (node instanceof IntegerNode) {
                sum += ((IntegerNode) node).getIntValue();
            }
        }

        setResult(sum);

        for (Node node : outputXNodes) {
            if (node != null) node.execute();
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute();
        }
    }

    @Override
    public String compileToC() {

        String sumNodeName = outputXNodes.getFirst().getName();

        StringBuilder expression = new StringBuilder();

        for (int i = 0; i < inputXNodes.size(); i++) {

            if (i > 0) {
                expression.append(" + ");
            }
            expression.append(inputXNodes.get(i).getName());
        }

        return sumNodeName + " = " + expression + ";";
    }


}
