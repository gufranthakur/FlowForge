package flowforge.ui.popupMenus;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;
import flowforge.nodes.variables.StringNode;
import flowforge.ui.panels.ProgramPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SearchXPopupMenu extends JPopupMenu {

    private ProgramPanel programPanel;

    private List<String> nodesList;
    private JTree searchTree;
    private DefaultMutableTreeNode root;


    public SearchXPopupMenu(ProgramPanel programPanel) {
        this.programPanel = programPanel;

        initList();
        initTree();
        initListeners();
        initUI();
    }
    private void initList() {
        nodesList = new ArrayList<>();
        nodesList.add("Create Integer");
        nodesList.add("Create String");
        nodesList.add("Create Boolean");

        nodesList.addAll(programPanel.strings.keySet());
        nodesList.addAll(programPanel.integers.keySet());
        nodesList.addAll(programPanel.booleans.keySet());
    }


    private void initTree() {
        root = new DefaultMutableTreeNode("Results");
        for (String s : nodesList) {
            root.add(new DefaultMutableTreeNode(s));
        }

        searchTree = new JTree(root);
        searchTree.setRootVisible(false);

    }

    private void initListeners() {
        searchTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) searchTree.getLastSelectedPathComponent();
                addNodeThroughSearch(selectedNode);
            }
        });
        searchTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) searchTree.getLastSelectedPathComponent();
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addNodeThroughSearch(selectedNode);
                }
            }
        });

    }

    private void initUI() {
        JPanel contentPanel = new JPanel();
        JTextField searchField = new JTextField();

        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setPreferredSize(new Dimension(300, 300));

        searchTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));
        searchTree.setPreferredSize(new Dimension(300, 50));
        searchTree.setMaximumSize(new Dimension(300, 50));
        searchTree.setBackground(getBackground());

        searchField.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter(searchField.getText());
            }
        });

        contentPanel.add(searchField, BorderLayout.NORTH);
        contentPanel.add(searchTree, BorderLayout.CENTER);

        JLabel label = new JLabel("Press ESC twice to escape");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        contentPanel.add(label, BorderLayout.SOUTH);

        this.setBackground(new Color(30, 30, 30));
        contentPanel.setBackground(getBackground());
        searchTree.setBackground(getBackground());

        this.add(contentPanel);

        this.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // When the popup menu is about to be shown
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                programPanel.selectedNode.inputButton.setSelected(true);
                programPanel.selectedNode.outputButton.setSelected(true);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                programPanel.selectedNode.inputButton.setSelected(true);
                programPanel.selectedNode.outputButton.setSelected(true);
            }
        });
    }

    private void filter(String search) {
        search = search.trim().toLowerCase();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Functions");
        for (String s : nodesList) {
            if (s.toLowerCase().contains(search) || search.isEmpty()) {
                root.add(new DefaultMutableTreeNode(s));
            }
        }

        searchTree.setModel(new DefaultTreeModel(root));
        searchTree.setRootVisible(false);

        revalidate();
    }

    private void addNodeThroughSearch(DefaultMutableTreeNode selectedNode) {

        if (selectedNode == null) return;

        String selected = selectedNode.getUserObject().toString();

        switch (selected) {
            case "Create Integer" : {
                String varName = JOptionPane.showInputDialog(null, "Enter Variable name");
                if (!variableAlreadyExists(varName)) {
                    programPanel.addVariable(varName, "Integer");
                    programPanel.addNewNode(new IntegerNode(varName, programPanel, 0), false);
                    reloadVariableTree();
                }
            }
            break;
            case "Create String" : {
                String varName = JOptionPane.showInputDialog(null, "Enter Variable name");
                if (!variableAlreadyExists(varName)) {
                    programPanel.addVariable(varName, "String");
                    programPanel.addNewNode(new StringNode(varName, programPanel, ""), false);
                    reloadVariableTree();
                }
            }
            break;
            case "Create Boolean" : {
                String varName = JOptionPane.showInputDialog(null, "Enter Variable name");
                if (!variableAlreadyExists(varName)) {
                    programPanel.addVariable(varName, "Boolean");
                    programPanel.addNewNode(new BooleanNode(varName, programPanel, false), false);
                    reloadVariableTree();
                }
            }
            break;
        }


        if (programPanel.integers.containsKey(selected)) {
            programPanel.addNewNode(new IntegerNode(selected, programPanel, 0), false);
        } else if (programPanel.strings.containsKey(selected)) {
            programPanel.addNewNode(new StringNode(selected, programPanel, ""), false);
        } else if (programPanel.booleans.containsKey(selected)) {
            programPanel.addNewNode(new BooleanNode(selected, programPanel, false), false);
        }

        getThis().setVisible(false);

        if (programPanel.selectedNode.isBeingXConnected) {
            programPanel.startXConnection(programPanel.selectedNode);
            programPanel.finishXConnection(programPanel.nodes.getLast());
        }

        programPanel.selectedNode = null;
    }

    public boolean variableAlreadyExists(String varName) {
        Enumeration<TreeNode> enumeration = programPanel.flowForge.controlPanel.variableRoot.depthFirstEnumeration();

        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
            if (varName.equals(node.getUserObject().toString())) {
                JOptionPane.showMessageDialog(null,
                        "Variable name already in use", "Error", JOptionPane.ERROR_MESSAGE);
                return true;
            }
        }
        return false;
    }

    private void reloadVariableTree() {
        programPanel.flowForge.controlPanel.loadVariables();
        programPanel.flowForge.controlPanel.refreshTree();

        repopulateSearchTree();
    }

    private void repopulateSearchTree() {
        DefaultTreeModel model = (DefaultTreeModel) searchTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        model.reload();

        initList();

        for (String s : nodesList) {
            root.add(new DefaultMutableTreeNode(s));
        }

        model.reload();
        repaint();
    }


    private JPopupMenu getThis() {
        return this;
    }

}
