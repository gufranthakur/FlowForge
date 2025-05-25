package flowforge.ui.popupMenus;

import flowforge.nodes.Node;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;
import flowforge.nodes.variables.StringNode;
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

            Node node = programPanel.selectedNode;

            if (!node.isMinimized) {
                node.setSize(150, 40);
                node.isMinimized = true;
                node.contentPanel.setVisible(false);
            } else {
                if (node instanceof StringNode
                        || node instanceof IntegerNode
                        || node instanceof BooleanNode) {
                    node.setSize(200, 100);
                    node.isMinimized = false;
                    node.contentPanel.setVisible(true);
                    return;
                }
                programPanel.selectedNode.setSize(200, 150);
                programPanel.selectedNode.isMinimized = false;
                node.contentPanel.setVisible(true);
            }
            node.repaint();

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
