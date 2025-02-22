package flowforge.nodes.flownodes.arithmetic;

import flowforge.FlowForge;
import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;

public class AddNode extends Node {

    private ProgramPanel programPanel;
    private Integer result;

    public AddNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
    }



    @Override
    public void execute() {

    }

}
