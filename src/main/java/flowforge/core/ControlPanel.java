package flowforge.core;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.FlowForge;
import flowforge.nodes.flownodes.PrintNode;
import flowforge.nodes.flownodes.BranchNode;
import flowforge.nodes.flownodes.comparators.*;
import flowforge.nodes.flownodes.logicgates.LogicGateNode;
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
    private DefaultMutableTreeNode commonNode, flowNode, basic, comparators, logicGates, arithmeticNode, arduinoNode;
    private DefaultMutableTreeNode print, branch, equalto, greaterThan, lessThan,
            greaterThanEqualTo, lessThanEqualTo, notEqualTo,
            notGate, andGate, orGate, nandGate, norGate, xorGate,
            add, subtract, multiply, divide;

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


        flowNode = new DefaultMutableTreeNode("Flow");

        print = new DefaultMutableTreeNode("Print");
        branch = new DefaultMutableTreeNode("Branch");

            comparators = new DefaultMutableTreeNode("Comparators");
                equalto = new DefaultMutableTreeNode("Equals to");
                greaterThan = new DefaultMutableTreeNode("Greater than");
                lessThan = new DefaultMutableTreeNode("Less than");
                greaterThanEqualTo = new DefaultMutableTreeNode("Greater than equal to");
                lessThanEqualTo = new DefaultMutableTreeNode("Less than equal to");
                notEqualTo = new DefaultMutableTreeNode("Not equal to");

            logicGates = new DefaultMutableTreeNode("Logic gates");
                notGate = new DefaultMutableTreeNode("NOT");
                andGate = new DefaultMutableTreeNode("AND");
                orGate = new DefaultMutableTreeNode("OR");
                nandGate = new DefaultMutableTreeNode("NAND");
                norGate = new DefaultMutableTreeNode("NOR");
                xorGate = new DefaultMutableTreeNode("XOR");

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
                switch (selectedNode.getUserObject().toString()) {
                    case "Print" : flowForge.mainPanel.addNode(new PrintNode("Print", flowForge.mainPanel));
                        break;
                    case "Branch" : flowForge.mainPanel.addNode(new BranchNode("Branch", flowForge.mainPanel));
                        break;
                    case "Equals to" : flowForge.mainPanel.addNode(new EqualToNode("Equals to", flowForge.mainPanel));
                        break;
                    case "Greater than" : flowForge.mainPanel.addNode(new GreaterThanNode("Greater than", flowForge.mainPanel));
                        break;
                    case "Less than" : flowForge.mainPanel.addNode(new LessThanNode("Less than", flowForge.mainPanel));
                        break;
                    case "Greater than or equal to" : flowForge.mainPanel.addNode(new GreaterThanOrEqualNode("Greater than equal to", flowForge.mainPanel));
                        break;
                    case "Less than or equal to" : flowForge.mainPanel.addNode(new LessThanOrEqualNode("Less than equal to", flowForge.mainPanel));
                        break;
                    case "Not equal to" : flowForge.mainPanel.addNode(new NotEqualToNode("Not equal to", flowForge.mainPanel));
                        break;
                    case "NOT" : flowForge.mainPanel.addNode(new LogicGateNode("NOT", flowForge.mainPanel, "NOT"));
                        break;
                    case "AND" : flowForge.mainPanel.addNode(new LogicGateNode("AND", flowForge.mainPanel, "AND"));
                        break;
                    case "OR" : flowForge.mainPanel.addNode(new LogicGateNode("OR", flowForge.mainPanel, "OR"));
                        break;
                    case "NAND" : flowForge.mainPanel.addNode(new LogicGateNode("NAND", flowForge.mainPanel, "NAND"));
                        break;
                    case "NOR" : flowForge.mainPanel.addNode(new LogicGateNode("NOR", flowForge.mainPanel, "NOR"));
                        break;
                    case "XOR" : flowForge.mainPanel.addNode(new LogicGateNode("XOR", flowForge.mainPanel, "XOR"));
                        break;
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
                        flowForge.mainPanel.addNode(new IntegerNode(variableName, flowForge.mainPanel, 0));
                    } else if (parentNode.getUserObject().equals("Strings")) {
                        flowForge.mainPanel.addNode(new StringNode(variableName, flowForge.mainPanel, ""));
                    } else if (parentNode.getUserObject().equals("Booleans")) {
                        flowForge.mainPanel.addNode(new BooleanNode(variableName, flowForge.mainPanel, false));
                    }
                }

            }
        });

        addButton.addActionListener(e -> {
            String variableName = JOptionPane.showInputDialog("Enter variable name");
            flowForge.mainPanel.addVariable(variableName);

            loadVariables();
            refreshTree();
        });
    }

    public void addComponent() {
        root.add(commonNode);
            commonNode.add(print);
        root.add(flowNode);
        flowNode.add(print);
        flowNode.add(branch);
            flowNode.add(arithmeticNode);
                arithmeticNode.add(add);
                arithmeticNode.add(subtract);
                arithmeticNode.add(multiply);
                arithmeticNode.add(divide);
            flowNode.add(comparators);
                comparators.add(equalto);
                comparators.add(greaterThan);
                comparators.add(lessThan);
                comparators.add(greaterThanEqualTo);
                comparators.add(lessThanEqualTo);
                comparators.add(notEqualTo);
            flowNode.add(logicGates);
                logicGates.add(notGate);
                logicGates.add(andGate);
                logicGates.add(orGate);
                logicGates.add(nandGate);
                logicGates.add(norGate);
                logicGates.add(xorGate);
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

        for (String key : flowForge.mainPanel.integers.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(key);
            integerNode.add(node);
        }
        for (String key : flowForge.mainPanel.strings.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(key);
            stringNode.add(node);
        }
        for (String key : flowForge.mainPanel.booleans.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(key);
            booleanNode.add(node);
        }
        for (String key : flowForge.mainPanel.floats.keySet()) {
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
