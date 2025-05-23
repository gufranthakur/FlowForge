package flowforge.ui.popupMenus;

import flowforge.nodes.variables.BooleanNode;
import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.StartNode;

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

        resize.addActionListener(e -> {
            programPanel.selectedNode.setSize(150, 30);
            programPanel.selectedNode.isMinimized = true;
        });
        resetConnections.addActionListener(e -> programPanel.selectedNode.disconnectAll());
        deleteNode.addActionListener(e ->  {
            if (programPanel.selectedNode instanceof StartNode) {
                JOptionPane.showMessageDialog(null, "Start Node cannot be deleted",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            programPanel.selectedNode.disconnectAll();
            programPanel.removeNode(programPanel.selectedNode);
        });

        this.add(resize);
        this.add(resetConnections);
        this.add(deleteNode);
    }

}
