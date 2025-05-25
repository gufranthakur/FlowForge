package flowforge.ui.panels;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.FlowForge;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.*;
import flowforge.nodes.flownodes.arithmetic.*;
import flowforge.nodes.flownodes.comparators.*;
import flowforge.nodes.flownodes.logicgates.LogicGateNode;
import flowforge.nodes.flownodes.utils.RecursiveNode;
import flowforge.nodes.flownodes.utils.RouteNode;
import flowforge.nodes.variables.BooleanNode;
import flowforge.nodes.variables.FloatNode;
import flowforge.nodes.variables.IntegerNode;
import flowforge.nodes.variables.StringNode;
import flowforge.ui.popupMenus.VariablePopupMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Enumeration;

public class ControlPanel {
    private JPanel rootPanel;
    private JButton addNodeButton;
    private JPanel nodesListPanel;
    private JTextField variableNameField;
    public JComboBox variableBox;
    private JPanel variableListPanel;
    public JPanel toolBar;
    private JButton consoleButton;
    public JButton runStopButton;
    private JButton addButton;
    private JTabbedPane rootTabbedPane;
    private JPanel nodeControlPanel;
    private JScrollPane nodesScrollPanel;
    private JPanel variableControlPanel;
    private JScrollPane variableScrollPanel;
    private JPanel propertiesPanel;
    private JPanel detailsPanel;
    private JPanel connectionPanel;
    private JLabel connectionsDisplayLabel;
    private JLabel nodeNameLabel;
    private JLabel nodeSerialNoLabel;
    private JLabel nodeLocationLabel;
    private JLabel nodeSizeLabel;
    private JLabel inputConnectionsLabel;
    private JLabel outputConnectionsLabel;
    private JLabel inputXConnectionsLabel;
    private JLabel outputXConnectionLabel;
    public JButton runWithStepsButton;
    public JButton stopButton;
    private JComboBox nodeBox;

    public FlowForge flowForge;

    public JTree functionsTree;
    public DefaultMutableTreeNode root;
    private DefaultMutableTreeNode flowNode, comparators, logicGates, arithmeticNode, utilityNode;
    private DefaultMutableTreeNode print, branch, input, delay, loop, conditionalLoop, //Flow Nodes
            equalTo, greaterThan, lessThan, // Comparator Nodes
            greaterThanEqualTo, lessThanEqualTo, notEqualTo, //Comparator Nodes
            notGate, andGate, orGate, nandGate, norGate, xorGate,// Logic Gate Nodes
            add, subtract, multiply, divide, modulus, random, eval, // Arithmetic Nodes
            route, recursive; //Utility Nodes

    public JTree variableTree;
    public DefaultMutableTreeNode variableRoot;
    private DefaultMutableTreeNode integerNode, stringNode, booleanNode, floatNode;

    public DefaultMutableTreeNode selectedVariableNode;
    public VariablePopupMenu variablePopupMenu;

    private boolean programIsRunning = false;

    public ControlPanel(FlowForge flowForge) {
        this.flowForge = flowForge;
        rootPanel.setOpaque(true);

        root = new DefaultMutableTreeNode("Flow Functions");
        functionsTree = new JTree(root);
        functionsTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));

        variableRoot = new DefaultMutableTreeNode("Variables");
        variableTree = new JTree(variableRoot);
        variableTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));
        variablePopupMenu = new VariablePopupMenu(this);
    }

    public void init() {
        detailsPanel.setBackground(rootPanel.getBackground().brighter());
        connectionPanel.setBackground(rootPanel.getBackground().brighter());
        runStopButton.setBackground(flowForge.theme);

        for (String s : Arrays.asList("Integer", "String", "Boolean", "Float")) {
            variableBox.addItem(s);
        }
        initFunctionNodes();
        initVariableNodes();
        initListeners();
    }

    public void initFunctionNodes() {
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
                greaterThanEqualTo = new DefaultMutableTreeNode("Greater than or equal to");
                lessThanEqualTo = new DefaultMutableTreeNode("Less than or equal to");
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
                modulus = new DefaultMutableTreeNode("Modulus");
                random = new DefaultMutableTreeNode("Random");
                eval = new DefaultMutableTreeNode("Eval");

            utilityNode = new DefaultMutableTreeNode("Utility");
                route = new DefaultMutableTreeNode("Route");
                recursive = new DefaultMutableTreeNode("Recurse");
    }

    public void initVariableNodes() {
        integerNode = new DefaultMutableTreeNode("Integers");
        stringNode = new DefaultMutableTreeNode("Strings");
        booleanNode = new DefaultMutableTreeNode("Booleans");
        floatNode = new DefaultMutableTreeNode("Floats");
    }

    public void initListeners() {
        consoleButton.addActionListener(e -> {
            flowForge.console.getRootPanel().setVisible(!flowForge.console.getRootPanel().isVisible());
        });

        runStopButton.addActionListener(e -> {
            if (!flowForge.forgeExecutor.getIsExecuting()) {
                flowForge.forgeExecutor.execute();
                runStopButton.setText("Stop");
                runStopButton.setBackground(stopButton.getBackground());
            } else {
                flowForge.forgeExecutor.stopExecution(true);
                runStopButton.setText("▶ Run");
                runStopButton.setBackground(flowForge.theme);
            }

        });

        runWithStepsButton.addActionListener(e -> {
            if (!flowForge.programPanel.isExecutingSteps) {
                flowForge.programPanel.isExecutingSteps = true;

                runWithStepsButton.setText("Next Step");
                stopButton.setVisible(true);
                runStopButton.setEnabled(false);

                flowForge.forgeExecutor.executeByStep();

            } else {
                synchronized (flowForge.programPanel.stepExecutorLock) {
                    flowForge.programPanel.stepExecutorLock.notify();
                }
                flowForge.programPanel.requestFocus();
            }
        });

        stopButton.addActionListener(e -> {
            flowForge.programPanel.isExecutingSteps = false;

            runWithStepsButton.setText("▶ Run with Steps");
            stopButton.setVisible(false);
            runStopButton.setEnabled(true);

            for (Node node : flowForge.programPanel.nodes) {
                node.restoreBorder();
            }

        });


        functionsTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                getSelectedNodeAtTree(functionsTree, true);
            }
        });

        variableTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) variableTree.getLastSelectedPathComponent();
                selectedVariableNode = selectedNode;

                if (SwingUtilities.isRightMouseButton(e)) {
                    if (selectedNode.isLeaf()) {
                        variablePopupMenu.show(variableTree, e.getX() + 10, e.getY() + 10);
                    }
                } else {
                    if (selectedNode != null && selectedNode.getParent() != null) {
                        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();

                        String variableName = (String) selectedNode.getUserObject();
                        if (parentNode.getUserObject().equals("Integers")) {
                            flowForge.programPanel.addNewNode(new IntegerNode(variableName, flowForge.programPanel, 0), true);
                        } else if (parentNode.getUserObject().equals("Strings")) {
                            flowForge.programPanel.addNewNode(new StringNode(variableName, flowForge.programPanel, ""), true);
                        } else if (parentNode.getUserObject().equals("Booleans")) {
                            flowForge.programPanel.addNewNode(new BooleanNode(variableName, flowForge.programPanel, false), true);
                        } else if (parentNode.getUserObject().equals("Floats")) {
                            flowForge.programPanel.addNewNode(new FloatNode(variableName, flowForge.programPanel, 0.0f), true);
                        }
                    }
                }

            }
        });

        addButton.addActionListener(e -> {
            String variableName = JOptionPane.showInputDialog("Enter variable name");
            if (variableName.isEmpty() || variableName == null) return;

            Enumeration<TreeNode> enumeration = variableRoot.depthFirstEnumeration();

            while (enumeration.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
                if (variableName.equals(node.getUserObject().toString())) {
                    JOptionPane.showMessageDialog(null,
                            "Variable name already in use", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            flowForge.programPanel.addVariable(variableName, null);

            loadVariables();
            refreshTree();
        });
    }

    public void getSelectedNodeAtTree(JTree tree, boolean inCenter) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode == null) return;
        switch (selectedNode.getUserObject().toString()) {
            case "Print" : flowForge.programPanel.addNewNode(new PrintNode("Print", flowForge.programPanel), inCenter); break;
            case "Branch" : flowForge.programPanel.addNewNode(new BranchNode("Branch", flowForge.programPanel), inCenter); break;
            case "Equals to" : flowForge.programPanel.addNewNode(new EqualToNode("Equals to", flowForge.programPanel), inCenter); break;
            case "Greater than" : flowForge.programPanel.addNewNode(new GreaterThanNode("Greater than", flowForge.programPanel), inCenter); break;
            case "Less than" : flowForge.programPanel.addNewNode(new LessThanNode("Less than", flowForge.programPanel), inCenter); break;
            case "Greater than or equal to" : flowForge.programPanel.addNewNode(new GreaterThanOrEqualNode("Greater than equal to", flowForge.programPanel), inCenter); break;
            case "Less than or equal to" : flowForge.programPanel.addNewNode(new LessThanOrEqualNode("Less than equal to", flowForge.programPanel), inCenter); break;
            case "Not equal to" : flowForge.programPanel.addNewNode(new NotEqualToNode("Not equal to", flowForge.programPanel), inCenter); break;
            case "NOT" : flowForge.programPanel.addNewNode(new LogicGateNode("NOT", flowForge.programPanel, "NOT"), inCenter); break;
            case "AND" : flowForge.programPanel.addNewNode(new LogicGateNode("AND", flowForge.programPanel, "AND"), inCenter); break;
            case "OR" : flowForge.programPanel.addNewNode(new LogicGateNode("OR", flowForge.programPanel, "OR"), inCenter); break;
            case "NAND" : flowForge.programPanel.addNewNode(new LogicGateNode("NAND", flowForge.programPanel, "NAND"), inCenter); break;
            case "NOR" : flowForge.programPanel.addNewNode(new LogicGateNode("NOR", flowForge.programPanel, "NOR"), inCenter); break;
            case "XOR" : flowForge.programPanel.addNewNode(new LogicGateNode("XOR", flowForge.programPanel, "XOR"), inCenter); break;
            case "Input" : flowForge.programPanel.addNewNode(new InputNode("Input", flowForge.programPanel), inCenter); break;
            case "Delay" : flowForge.programPanel.addNewNode(new DelayNode("Delay", flowForge.programPanel), inCenter); break;
            case "Loop" : flowForge.programPanel.addNewNode(new LoopNode("Loop", flowForge.programPanel), inCenter); break;
            case "Conditional-Loop" : flowForge.programPanel.addNewNode(new ConditionalLoopNode("Conditional Loop", flowForge.programPanel), inCenter); break;
            case "Add" : flowForge.programPanel.addNewNode(new AddNode("Add", flowForge.programPanel), inCenter); break;
            case "Subtract" : flowForge.programPanel.addNewNode(new SubtractNode("Subtract", flowForge.programPanel), inCenter); break;
            case "Multiply" : flowForge.programPanel.addNewNode(new MultiplyNode("Multiply", flowForge.programPanel), inCenter); break;
            case "Divide" : flowForge.programPanel.addNewNode(new DivideNode("Divide", flowForge.programPanel), inCenter); break;
            case "Modulus" : flowForge.programPanel.addNewNode(new ModulusNode("Modulus", flowForge.programPanel), inCenter); break;
            case "Random" : flowForge.programPanel.addNewNode(new RandomNode("Random", flowForge.programPanel), inCenter); break;
            case "Route" : flowForge.programPanel.addNewNode(new RouteNode("Route", flowForge.programPanel), inCenter); break;
            case "Recurse" : flowForge.programPanel.addNewNode(new RecursiveNode("Recurse", flowForge.programPanel), inCenter); break;
            case "Eval" : flowForge.programPanel.addNewNode(new EvalNode("Eval", flowForge.programPanel), inCenter); break;
        }
    }


    public void addComponent() {
        root.add(print);
        root.add(branch);
        root.add(input);
        root.add(delay);
        root.add(loop);
        root.add(conditionalLoop);
        root.add(arithmeticNode);
            arithmeticNode.add(add);
            arithmeticNode.add(subtract);
            arithmeticNode.add(multiply);
            arithmeticNode.add(divide);
            arithmeticNode.add(modulus);
            arithmeticNode.add(random);
            arithmeticNode.add(eval);
        root.add(utilityNode);
            utilityNode.add(route);
            utilityNode.add(recursive);
        root.add(comparators);
            comparators.add(equalTo);
            comparators.add(greaterThan);
            comparators.add(lessThan);
            comparators.add(greaterThanEqualTo);
            comparators.add(lessThanEqualTo);
            comparators.add(notEqualTo);
        root.add(logicGates);
            logicGates.add(notGate);
            logicGates.add(andGate);
            logicGates.add(orGate);
            logicGates.add(nandGate);
            logicGates.add(norGate);
            logicGates.add(xorGate);

        variableRoot.add(integerNode);
        variableRoot.add(stringNode);
        variableRoot.add(booleanNode);
        variableRoot.add(floatNode);

        nodesListPanel.add(functionsTree, BorderLayout.CENTER);
        variableListPanel.add(variableTree, BorderLayout.CENTER);

        functionsTree.expandRow(0);
        variableTree.expandRow(0);
    }

    public void loadVariables() {

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

    public void refreshTree() {
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

    public void updateVariableTree() {
        loadVariables();
        refreshTree();
    }

    public void updatePropertiesPanel(Node selectedNode) {
        nodeNameLabel.setText("Node Name : " + selectedNode.getTitle());
        nodeSerialNoLabel.setText("Node ID : " + flowForge.programPanel.nodes.indexOf(selectedNode));
        nodeLocationLabel.setText("Location : " + selectedNode.getX() + ", " + selectedNode.getY());
        nodeSizeLabel.setText("Size : " + selectedNode.getWidth() + ", " + selectedNode.getHeight());

        inputConnectionsLabel.setText(returnIOConnectionList(selectedNode, "Input"));
        outputConnectionsLabel.setText(returnIOConnectionList(selectedNode, "Output"));
        inputXConnectionsLabel.setText(returnIOConnectionList(selectedNode, "InputX"));
        outputXConnectionLabel.setText(returnIOConnectionList(selectedNode, "OutputX"));


    }

    public String returnIOConnectionList(Node selectedNode, String connectionType) {
        StringBuilder builder = new StringBuilder();

        builder.append("[");
        switch (connectionType) {
            case "Input" :
                for (Node node : selectedNode.inputNodes) builder.append(node.getTitle()).append(", ");
                break;
            case "Output" :
                for (Node node : selectedNode.outputNodes) builder.append(node.getTitle()).append(", ");
                break;
            case "InputX" :
                for (Node node : selectedNode.inputXNodes) builder.append(node.getTitle()).append(", ");
                break;
            case "OutputX" :
                for (Node node : selectedNode.outputXNodes)
                    builder.append(node.getTitle()).append(", ");
                break;
        }
        try {
            builder.deleteCharAt(builder.length() - 2);
            builder.append("]");
        } catch (StringIndexOutOfBoundsException e) {
            return "[]";
        }
        return builder.toString();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
