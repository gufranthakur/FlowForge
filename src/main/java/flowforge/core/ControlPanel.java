package flowforge.core;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.FlowForge;
import flowforge.nodes.flownodes.*;
import flowforge.nodes.flownodes.arithmetic.AddNode;
import flowforge.nodes.flownodes.arithmetic.MultiplyNode;
import flowforge.nodes.flownodes.arithmetic.SubtractNode;
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
    private JPanel nodesListPanel;
    private JTextField variableNameField;
    public JComboBox variableBox;
    private JPanel variableListPanel;
    private JPanel executePanel;
    private JButton compileButton;
    private JButton runStopButton;
    private JButton addButton;
    private JTabbedPane rootTabbedPane;
    private JPanel nodeControlPanel;
    private JScrollPane nodesScrollPanel;
    private JPanel variableControlPanel;
    private JScrollPane variableScrollPanel;
    private JPanel propertiesPanel;
    private JComboBox nodeBox;

    private FlowForge flowForge;

    private JTree functionsTree;
    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode commonNode, flowNode, basic, comparators, logicGates, arithmeticNode, arduinoNode;
    private DefaultMutableTreeNode print, branch, input, delay, loop, conditionalLoop,
            equalTo, greaterThan, lessThan,
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
        compileButton.setBackground(flowForge.theme);

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
        input = new DefaultMutableTreeNode("Input");
        delay = new DefaultMutableTreeNode("Delay");
        loop = new DefaultMutableTreeNode("Loop");
        conditionalLoop = new DefaultMutableTreeNode("Conditional-Loop");

            comparators = new DefaultMutableTreeNode("Comparators");
                equalTo = new DefaultMutableTreeNode("Equals to");
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
        compileButton.addActionListener(e -> {

        });

        runStopButton.addActionListener(e -> {
            flowForge.run();
        });

        functionsTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) functionsTree.getLastSelectedPathComponent();
                switch (selectedNode.getUserObject().toString()) {
                    case "Print" : flowForge.programPanel.addNode(new PrintNode("Print", flowForge.programPanel));
                        break;
                    case "Branch" : flowForge.programPanel.addNode(new BranchNode("Branch", flowForge.programPanel));
                        break;
                    case "Equals to" : flowForge.programPanel.addNode(new EqualToNode("Equals to", flowForge.programPanel));
                        break;
                    case "Greater than" : flowForge.programPanel.addNode(new GreaterThanNode("Greater than", flowForge.programPanel));
                        break;
                    case "Less than" : flowForge.programPanel.addNode(new LessThanNode("Less than", flowForge.programPanel));
                        break;
                    case "Greater than or equal to" : flowForge.programPanel.addNode(new GreaterThanOrEqualNode("Greater than equal to", flowForge.programPanel));
                        break;
                    case "Less than or equal to" : flowForge.programPanel.addNode(new LessThanOrEqualNode("Less than equal to", flowForge.programPanel));
                        break;
                    case "Not equal to" : flowForge.programPanel.addNode(new NotEqualToNode("Not equal to", flowForge.programPanel));
                        break;
                    case "NOT" : flowForge.programPanel.addNode(new LogicGateNode("NOT", flowForge.programPanel, "NOT"));
                        break;
                    case "AND" : flowForge.programPanel.addNode(new LogicGateNode("AND", flowForge.programPanel, "AND"));
                        break;
                    case "OR" : flowForge.programPanel.addNode(new LogicGateNode("OR", flowForge.programPanel, "OR"));
                        break;
                    case "NAND" : flowForge.programPanel.addNode(new LogicGateNode("NAND", flowForge.programPanel, "NAND"));
                        break;
                    case "NOR" : flowForge.programPanel.addNode(new LogicGateNode("NOR", flowForge.programPanel, "NOR"));
                        break;
                    case "XOR" : flowForge.programPanel.addNode(new LogicGateNode("XOR", flowForge.programPanel, "XOR"));
                        break;
                    case "Input" : flowForge.programPanel.addNode(new InputNode("Input", flowForge.programPanel));
                        break;
                    case "Delay" : flowForge.programPanel.addNode(new DelayNode("Delay", flowForge.programPanel));
                        break;
                    case "Loop" : flowForge.programPanel.addNode(new LoopNode("Loop", flowForge.programPanel));
                        break;
                    case "Conditional-Loop" : flowForge.programPanel.addNode(new ConditionalLoopNode("Conditional Loop", flowForge.programPanel));
                        break;
                    case "Add" : flowForge.programPanel.addNode(new AddNode("Add", flowForge.programPanel));
                        break;
                    case "Subtract" : flowForge.programPanel.addNode(new SubtractNode("Subtract", flowForge.programPanel));
                        break;
                    case "Multiply" : flowForge.programPanel.addNode(new MultiplyNode("Multiply", flowForge.programPanel));
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
                        flowForge.programPanel.addNode(new IntegerNode(variableName, flowForge.programPanel, 0));
                    } else if (parentNode.getUserObject().equals("Strings")) {
                        flowForge.programPanel.addNode(new StringNode(variableName, flowForge.programPanel, ""));
                    } else if (parentNode.getUserObject().equals("Booleans")) {
                        flowForge.programPanel.addNode(new BooleanNode(variableName, flowForge.programPanel, false));
                    }
                }

            }
        });

        addButton.addActionListener(e -> {
            String variableName = JOptionPane.showInputDialog("Enter variable name");
            flowForge.programPanel.addVariable(variableName);

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
        flowNode.add(input);
        flowNode.add(delay);
        flowNode.add(loop);
        flowNode.add(conditionalLoop);
            flowNode.add(arithmeticNode);
                arithmeticNode.add(add);
                arithmeticNode.add(subtract);
                arithmeticNode.add(multiply);
                arithmeticNode.add(divide);
            flowNode.add(comparators);
                comparators.add(equalTo);
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

        for (String key : flowForge.programPanel.integers.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(key);
            integerNode.add(node);
        }
        for (String key : flowForge.programPanel.strings.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(key);
            stringNode.add(node);
        }
        for (String key : flowForge.programPanel.booleans.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(key);
            booleanNode.add(node);
        }
        for (String key : flowForge.programPanel.floats.keySet()) {
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
