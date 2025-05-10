package flowforge.core.ui.popupMenus;

import flowforge.core.ui.panels.ProgramPanel;

import javax.swing.*;

public class NodePopupMenu extends JPopupMenu {

    private ProgramPanel programPanel;

    private JMenuItem deleteNode, resetConnections, resize;

    public NodePopupMenu(ProgramPanel programPanel) {
        this.programPanel = programPanel;

        initUI();
    }

    private void initUI() {
        resize = new JMenuItem("Resize");
        resetConnections = new JMenuItem("Disconnect");
        deleteNode = new JMenuItem("Delete");

        resize.addActionListener(e -> programPanel.selectedNode.pack());
        resetConnections.addActionListener(e -> programPanel.selectedNode.disconnectAll());
        deleteNode.addActionListener(e ->  {
            programPanel.selectedNode.disconnectAll();
            programPanel.removeNode(programPanel.selectedNode);
        });

        this.add(resize);
        this.add(resetConnections);
        this.add(deleteNode);
    }

}
