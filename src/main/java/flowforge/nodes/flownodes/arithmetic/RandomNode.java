package flowforge.nodes.flownodes.arithmetic;

import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.PrintNode;
import flowforge.nodes.variables.IntegerNode;

import java.util.Random;

public class RandomNode extends Node {

    private ProgramPanel programPanel;
    private int result;

    public RandomNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

        inputXButton.setText("Range");
        outputXButton.setText("Random Value");
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

        Integer firstInt = null;
        Integer secondInt = null;

        Node firstNode = inputXNodes.get(0);
        Node secondNode = inputXNodes.get(1);

        if (firstNode instanceof IntegerNode && secondNode instanceof IntegerNode) {
            firstInt = ((IntegerNode) firstNode).getValue();
            secondInt = ((IntegerNode) secondNode).getValue();
        }

        if (firstInt == null || secondInt == null) return;

        Random random = new Random();

        if (firstInt < secondInt) {
            setResult(random.nextInt(firstInt, secondInt));
        } else if (firstInt > secondInt) {
            setResult(random.nextInt(secondInt, firstInt));
        } else {
            setResult(firstInt);
        }

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute();
        }

        for (Node node : outputNodes) {
            if (node != null) node.execute();
        }
    }
}
