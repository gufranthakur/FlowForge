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

    @Override
    public String compileToC() {
        String resultNodeName = outputXNodes.getFirst().getName();

        StringBuilder expression = new StringBuilder();

        for (int i = 0; i < inputXNodes.size(); i++) {
            if (i > 0) {
                expression.append(" * "); // Add "*" between node names
            }
            expression.append(inputXNodes.get(i).getName());
        }

         return resultNodeName + " = " + expression + ";";
    }


}
