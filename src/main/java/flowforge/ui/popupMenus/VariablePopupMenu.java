package flowforge.ui.popupMenus;

import flowforge.nodes.Node;
import flowforge.ui.panels.ControlPanel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.*;

public class VariablePopupMenu extends JPopupMenu {

    private ControlPanel controlPanel;
    private JMenuItem renameVariable, deleteVariable;
    private HashMap strings, integers, booleans, floats;

    public VariablePopupMenu(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;

        strings = controlPanel.flowForge.programPanel.strings;
        integers = controlPanel.flowForge.programPanel.integers;
        booleans = controlPanel.flowForge.programPanel.booleans;
        floats = controlPanel.flowForge.programPanel.floats;

        initUI();
    }

    private void initUI() {
        renameVariable = new JMenuItem("Rename");
        deleteVariable = new JMenuItem("Delete");

        deleteVariable.addActionListener(e -> {
            String key = (String) controlPanel.selectedVariableNode.getUserObject();

            if (key.equals("Strings") || key.equals("integers") || key.equals("booleans") || key.equals("floats"))
                return;

            strings.remove(key);
            integers.remove(key);
            booleans.remove(key);
            floats.remove(key);

            Iterator<Node> iterator = controlPanel.flowForge.programPanel.nodes.iterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                if (node.getTitle().equals(key)) {
                    iterator.remove();
                    controlPanel.flowForge.programPanel.removeNode(node);
                }
            }

            DefaultTreeModel model = (DefaultTreeModel) controlPanel.variableTree.getModel();
            model.removeNodeFromParent(controlPanel.selectedVariableNode);

        });

        renameVariable.addActionListener(e -> {
            String key = (String) controlPanel.selectedVariableNode.getUserObject();

            if (key.equals("Strings") || key.equals("Integers") || key.equals("Booleans") || key.equals("Floats"))
                return;

            String newKey = JOptionPane.showInputDialog(null, "Enter new Variable name", key);

            Enumeration<TreeNode> enumeration = controlPanel.variableRoot.depthFirstEnumeration();

            while (enumeration.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
                if (key.equals(node.getUserObject().toString())) {
                    JOptionPane.showMessageDialog(null,
                            "Variable name already in use", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (strings.containsKey(key)) {

                String value = (String) strings.remove(key);
                strings.put(newKey, value);

            } else if (integers.containsKey(key)) {

                Integer value = (Integer) integers.remove(key);
                integers.put(newKey, value);

            } else if (booleans.containsKey(key)) {

                Boolean value = (Boolean) booleans.remove(key);
                booleans.put(newKey, value);

            } else if (floats.containsKey(key)) {

                Float value = (Float) floats.remove(key);
                floats.put(newKey, value);

            }

            for (Node node : controlPanel.flowForge.programPanel.nodes) {
                if (node.getTitle().equals(key)) {
                    node.setTitle(newKey);
                }
            }

            controlPanel.selectedVariableNode.setUserObject(newKey);
            controlPanel.updateVariableTree();
        });

        this.add(renameVariable);
        this.add(deleteVariable);
    }



}
