package flowforge.nodes.flownodes;

import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;

public class InputNode extends Node {

    private ProgramPanel programPanel;

    public InputNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
    }

    @Override
    public void execute() {

    }
}
