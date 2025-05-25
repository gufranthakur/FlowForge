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

    private JMenuItem deleteNode, resetConnections, resize, highlight, comment;

    public NodePopupMenu(ProgramPanel programPanel) {
        this.programPanel = programPanel;

        initUI();
    }

    private void initUI() {
        resize = new JMenuItem("Resize");
        highlight = new JMenuItem("Highlight");
        comment = new JMenuItem("Add Comment");
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

        highlight.addActionListener(e -> {
             if (programPanel.selectedNode.isHighlighted) {
                 programPanel.selectedNode.isHighlighted = false;
             } else {
                 programPanel.selectedNode.isHighlighted = true;
             }
             programPanel.selectedNode.restoreBorder();
        });

        comment.addActionListener(e -> {
            if (programPanel.selectedNode.isCommented) {
                programPanel.selectedNode.isCommented = false;
                programPanel.selectedNode.setToolTipText(null);
            } else {

                JTextArea textArea = new JTextArea(10, 30);
                textArea.setLineWrap(true);
                textArea.setFont(textArea.getFont().deriveFont(14f));
                textArea.setWrapStyleWord(true);

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                int result = JOptionPane.showConfirmDialog(
                        null,
                        scrollPane,
                        "Enter your text",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    String input = textArea.getText();
                    programPanel.selectedNode.setToolTipText(input);
                    programPanel.selectedNode.comment = input;
                    programPanel.selectedNode.isCommented = true;
                }



            }
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
        this.add(highlight);
        this.add(comment);
        this.addSeparator();
        this.add(resetConnections);
        this.add(deleteNode);

    }


    public void display(Node node, int x, int y, boolean isMinimized, boolean isHighlighted, boolean isCommented) {
        this.show(node, x, y);

        if (isMinimized) resize.setText("Maximise");
        else resize.setText("Minimize");

        if (isHighlighted) highlight.setText("Clear Highlight");
        else highlight.setText("Highlight");

        if (isCommented) comment.setText("Remove Comment");
        else comment.setText("Add Comment");

        this.pack();
    }
}
