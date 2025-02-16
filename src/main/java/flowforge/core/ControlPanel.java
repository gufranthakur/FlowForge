package flowforge.core;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.FlowForge;
import flowforge.nodes.PrintNode;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.IntegerNode;
import flowforge.nodes.variables.StringNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class ControlPanel {
    private JPanel rootPanel;
    private JTextField searchField;
    private JButton addNodeButton;
    private JPanel nodeControlPanel;
    private JScrollPane nodesScrollPanel;
    private JPanel nodesListPanel;
    private JPanel variableControlPanel;
    private JTextField variableNameField;
    public JComboBox variableBox;
    private JScrollPane variableScrollPanel;
    private JPanel variableListPanel;
    private JPanel executePanel;
    private JButton runButton;
    private JButton stopButton;
    private JButton addButton;
    private JComboBox nodeBox;

    private FlowForge flowForge;

    private JTree functionsTree;
    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode commonNode, basicNode, arithmeticNode, arduinoNode;
    private DefaultMutableTreeNode print, add, subtract, multiply, divide;

    private JTree variableTree;
    private DefaultMutableTreeNode variableRoot;
    private DefaultMutableTreeNode integerNode, stringNode, booleanNode, floatNode;
    
    public ControlPanel(FlowForge flowForge) {
        this.flowForge = flowForge;

        root = new DefaultMutableTreeNode("Functions");
        functionsTree = new JTree(root);
        functionsTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));

        variableRoot = new DefaultMutableTreeNode("Variables");
        variableTree = new JTree(variableRoot);
        variableTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));
    }

    public void init() {
        runButton.setBackground(flowForge.theme);
        stopButton.setBackground(flowForge.errorTheme);
        stopButton.setEnabled(false);

        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");

        for (String s : Arrays.asList("Integer", "String", "Boolean", "Float")) {
            variableBox.addItem(s);
        }
        initFunctionNodes();
        initVariableNodes();
        initListeners();
    }

    public void initFunctionNodes() {
        commonNode = new DefaultMutableTreeNode("Common");
        print = new DefaultMutableTreeNode("Print");

        basicNode = new DefaultMutableTreeNode("Basic");

        arithmeticNode = new DefaultMutableTreeNode("Arithmetic");
        add = new DefaultMutableTreeNode("Add");
        subtract = new DefaultMutableTreeNode("Subtract");
        multiply = new DefaultMutableTreeNode("Multiply");
        divide = new DefaultMutableTreeNode("Divide");

        arduinoNode = new DefaultMutableTreeNode("Arduino");
    }

    public void initVariableNodes() {
        integerNode = new DefaultMutableTreeNode("Integers");
        stringNode = new DefaultMutableTreeNode("Strings");
        booleanNode = new DefaultMutableTreeNode("Booleans");
        floatNode = new DefaultMutableTreeNode("Floats");
    }

    public void initListeners() {
        runButton.addActionListener(e -> {
            stopButton.setEnabled(true);
            runButton.setEnabled(false);

            flowForge.run();
        });

        stopButton.addActionListener(e -> {
            runButton.setEnabled(true);
            stopButton.setEnabled(false);

            flowForge.stop();
        });

        functionsTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) functionsTree.getLastSelectedPathComponent();
                if (selectedNode.getUserObject().equals("Print")) {
                    flowForge.flowPanel.addNode(new PrintNode("Print", flowForge.flowPanel));
                }
            }
        });

        variableTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) variableTree.getLastSelectedPathComponent();

                if (selectedNode != null && selectedNode.getParent() != null) {
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();

                    String variableName = (String) selectedNode.getUserObject();
                    if (parentNode.getUserObject().equals("Integers")) {
                        flowForge.flowPanel.addNode(new IntegerNode(variableName, flowForge.flowPanel, 0));
                    } else if (parentNode.getUserObject().equals("Strings")) {
                        flowForge.flowPanel.addNode(new StringNode(variableName, flowForge.flowPanel, ""));
                    } else if (parentNode.getUserObject().equals("Booleans")) {
                        flowForge.flowPanel.addNode(new BooleanNode(variableName, flowForge.flowPanel, false));
                    }
                }

            }
        });

        addButton.addActionListener(e -> {
            String variableName = JOptionPane.showInputDialog("Enter variable name");
            flowForge.flowPanel.addVariable(variableName);

            loadVariables();
            refreshTree();
        });
    }

    public void addComponent() {
        root.add(commonNode);
            commonNode.add(print);
        root.add(basicNode);
            basicNode.add(print);
        root.add(arithmeticNode);
            arithmeticNode.add(add);
            arithmeticNode.add(subtract);
            arithmeticNode.add(multiply);
            arithmeticNode.add(divide);
        root.add(arduinoNode);

        variableRoot.add(integerNode);
        variableRoot.add(stringNode);
        variableRoot.add(booleanNode);
        variableRoot.add(floatNode);

        nodesListPanel.add(functionsTree, BorderLayout.CENTER);
        variableListPanel.add(variableTree, BorderLayout.CENTER);
    }

    private void loadVariables() {

        integerNode.removeAllChildren();
        stringNode.removeAllChildren();
        floatNode.removeAllChildren();
        booleanNode.removeAllChildren();

        for (String key : flowForge.flowPanel.integers.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(key);
            integerNode.add(node);
        }
        for (String key : flowForge.flowPanel.strings.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(key);
            stringNode.add(node);
        }
        for (String key : flowForge.flowPanel.booleans.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(key);
            booleanNode.add(node);
        }
        for (String key : flowForge.flowPanel.floats.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(key);
            floatNode.add(node);
        }
    }

    private void refreshTree() {
        java.util.Enumeration<javax.swing.tree.TreePath> expanded = variableTree.getExpandedDescendants(new javax.swing.tree.TreePath(root));

        DefaultTreeModel model = (DefaultTreeModel) variableTree.getModel();
        model.reload();

        if (expanded != null) {
            while (expanded.hasMoreElements()) {
                javax.swing.tree.TreePath treePath = expanded.nextElement();
                variableTree.expandPath(treePath);
            }
        }
        variableTree.expandRow(0);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
