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
    private JComboBox comboBox1;
    private JButton addArrayButton;
    private JScrollPane arrayScrollPane;
    private JPanel arrayListPanel;
    private JComboBox nodeBox;

    public FlowForge flowForge;

    public JTree functionsTree;
    public DefaultMutableTreeNode root;
    private DefaultMutableTreeNode flowNode, comparators, logicGates, arithmeticNode, utilityNode;
    private DefaultMutableTreeNode print, branch, input, delay, loop, conditionalLoop,
            equalTo, greaterThan, lessThan,
            greaterThanEqualTo, lessThanEqualTo, notEqualTo,
            notGate, andGate, orGate, nandGate, norGate, xorGate,
            add, subtract, multiply, divide, modulus, random, eval,
            route, recursive;

    public JTree variableTree;
    public DefaultMutableTreeNode variableRoot;
    private DefaultMutableTreeNode integerNode, stringNode, booleanNode, floatNode;

    public JTree arrayTree;
    public DefaultMutableTreeNode arrayRoot;
    public DefaultMutableTreeNode intArrayNode, strArrayNode, boolArrayNode, floatArrayNode;

    public DefaultMutableTreeNode selectedVariableNode;
    public VariablePopupMenu variablePopupMenu;

    private boolean programIsRunning = false;

    public ControlPanel(FlowForge flowForge) {
        this.flowForge = flowForge;
        createUIComponents();
        this.rootPanel.setPreferredSize(new Dimension(350, flowForge.getHeight()));
        this.rootPanel.setMaximumSize(new Dimension(350, flowForge.getHeight()));
        rootPanel.setOpaque(true);

        root = new DefaultMutableTreeNode("Flow Functions");
        functionsTree = new JTree(root);
        functionsTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));

        variableRoot = new DefaultMutableTreeNode("Variables");
        variableTree = new JTree(variableRoot);
        variableTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));
        variablePopupMenu = new VariablePopupMenu(this);

        arrayRoot = new DefaultMutableTreeNode("Arrays");
        arrayTree = new JTree(arrayRoot);
        arrayTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));
    }

    private void createUIComponents() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        createToolBar();
        createTabbedPane();

        rootPanel.add(toolBar, BorderLayout.NORTH);
        rootPanel.add(rootTabbedPane, BorderLayout.CENTER);
    }

    private void createToolBar() {
        toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        runStopButton = new JButton("▶ Run");
        runStopButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        runStopButton.setBackground(new Color(-16401911));

        runWithStepsButton = new JButton("▶ Run with Steps");
        runWithStepsButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        runWithStepsButton.setBackground(new Color(-33257));

        stopButton = new JButton("■");
        stopButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        stopButton.setBackground(new Color(-53968));
        stopButton.setVisible(false);

        consoleButton = new JButton("📜");
        consoleButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

        toolBar.add(runStopButton);
        toolBar.add(runWithStepsButton);
        toolBar.add(stopButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(consoleButton);
    }

    private void createTabbedPane() {
        rootTabbedPane = new JTabbedPane();
        rootTabbedPane.setPreferredSize(new Dimension(240, 200));

        createNodeControlPanel();
        createVariableControlPanel();
        createPropertiesPanel();

        rootTabbedPane.addTab("Functions", nodeControlPanel);
        rootTabbedPane.addTab("Variables", variableControlPanel);
        rootTabbedPane.addTab("Properties", propertiesPanel);
    }

    private void createNodeControlPanel() {
        nodeControlPanel = new JPanel(new BorderLayout());
        nodeControlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel functionsLabel = new JLabel("Functions");
        functionsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        functionsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        nodesListPanel = new JPanel(new BorderLayout());
        nodesScrollPanel = new JScrollPane(nodesListPanel);

        nodeControlPanel.add(functionsLabel, BorderLayout.NORTH);
        nodeControlPanel.add(nodesScrollPanel, BorderLayout.CENTER);
    }

    private void createVariableControlPanel() {
        variableControlPanel = new JPanel();
        variableControlPanel.setLayout(new BoxLayout(variableControlPanel, BoxLayout.Y_AXIS));
        variableControlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel variablesLabel = new JLabel("Variables");
        variablesLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        variablesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel variableInputPanel = new JPanel(new FlowLayout());
        variableBox = new JComboBox();
        variableBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        addButton = new JButton("Add");
        addButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        addButton.setBackground(new Color(-12693268));

        variableInputPanel.add(variableBox);
        variableInputPanel.add(addButton);

        variableListPanel = new JPanel(new BorderLayout());
        variableScrollPanel = new JScrollPane(variableListPanel);

        JLabel arraysLabel = new JLabel("Arrays");
        arraysLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        arraysLabel.setHorizontalAlignment(SwingConstants.CENTER);
        arraysLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel arrayInputPanel = new JPanel(new FlowLayout());
        comboBox1 = new JComboBox();
        comboBox1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        addArrayButton = new JButton("Add");
        addArrayButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        addArrayButton.setBackground(new Color(-12704020));

        arrayInputPanel.add(comboBox1);
        arrayInputPanel.add(addArrayButton);

        arrayListPanel = new JPanel(new BorderLayout());
        arrayScrollPane = new JScrollPane(arrayListPanel);

        variableControlPanel.add(variablesLabel);
        variableControlPanel.add(variableInputPanel);
        variableControlPanel.add(variableScrollPanel);
        variableControlPanel.add(arraysLabel);
        variableControlPanel.add(arrayInputPanel);
        variableControlPanel.add(arrayScrollPane);
    }

    private void createPropertiesPanel() {
        propertiesPanel = new JPanel(new BorderLayout());
        propertiesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel propertiesLabel = new JLabel("Properties");
        propertiesLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        propertiesLabel.setHorizontalAlignment(SwingConstants.CENTER);

        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nodeNameLabel = new JLabel("Node Name : ");
        nodeNameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        nodeSerialNoLabel = new JLabel("Node Serial No.");
        nodeSerialNoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        nodeLocationLabel = new JLabel("Location : ");
        nodeLocationLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        nodeSizeLabel = new JLabel("Size : ");
        nodeSizeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        createConnectionPanel();

        connectionsDisplayLabel = new JLabel("");
        connectionsDisplayLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        detailsPanel.add(nodeNameLabel);
        detailsPanel.add(nodeSerialNoLabel);
        detailsPanel.add(nodeLocationLabel);
        detailsPanel.add(nodeSizeLabel);
        detailsPanel.add(connectionPanel);
        detailsPanel.add(connectionsDisplayLabel);
        detailsPanel.add(Box.createVerticalGlue());

        propertiesPanel.add(propertiesLabel, BorderLayout.NORTH);
        propertiesPanel.add(detailsPanel, BorderLayout.CENTER);
    }

    private void createConnectionPanel() {
        connectionPanel = new JPanel();
        connectionPanel.setLayout(new BoxLayout(connectionPanel, BoxLayout.Y_AXIS));

        JLabel connectionsTitle = new JLabel("Connections : ");
        connectionsTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        connectionsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel inputLabel = new JLabel("Input Connections : ");
        inputLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        inputConnectionsLabel = new JLabel("[]");
        inputConnectionsLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        JLabel outputLabel = new JLabel("Output Connections : ");
        outputLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        outputConnectionsLabel = new JLabel("[]");
        outputConnectionsLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        JLabel inputXLabel = new JLabel("Input-X Connections : ");
        inputXLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        inputXConnectionsLabel = new JLabel("[]");
        inputXConnectionsLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        JLabel outputXLabel = new JLabel("Output-X Connections : ");
        outputXLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        outputXConnectionLabel = new JLabel("[]");
        outputXConnectionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        connectionPanel.add(connectionsTitle);
        connectionPanel.add(inputLabel);
        connectionPanel.add(inputConnectionsLabel);
        connectionPanel.add(outputLabel);
        connectionPanel.add(outputConnectionsLabel);
        connectionPanel.add(inputXLabel);
        connectionPanel.add(inputXConnectionsLabel);
        connectionPanel.add(outputXLabel);
        connectionPanel.add(outputXConnectionLabel);
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
        eval = new DefaultMutableTreeNode("Eval");
        add = new DefaultMutableTreeNode("Add");
        subtract = new DefaultMutableTreeNode("Subtract");
        multiply = new DefaultMutableTreeNode("Multiply");
        divide = new DefaultMutableTreeNode("Divide");
        modulus = new DefaultMutableTreeNode("Modulus");
        random = new DefaultMutableTreeNode("Random");

        utilityNode = new DefaultMutableTreeNode("Utility");
        route = new DefaultMutableTreeNode("Route");
        recursive = new DefaultMutableTreeNode("Recurse");
    }

    public void initVariableNodes() {
        integerNode = new DefaultMutableTreeNode("Integers");
        stringNode = new DefaultMutableTreeNode("Strings");
        booleanNode = new DefaultMutableTreeNode("Booleans");
        floatNode = new DefaultMutableTreeNode("Floats");

        intArrayNode = new DefaultMutableTreeNode("Integers");
        strArrayNode = new DefaultMutableTreeNode("String");
        boolArrayNode = new DefaultMutableTreeNode("Boolean");
        floatArrayNode = new DefaultMutableTreeNode("Floats");
    }

    public void initListeners() {
        consoleButton.addActionListener(e -> {
            if (flowForge.console.isMinimized) flowForge.console.resizeToDefault();
            else flowForge.console.minimize();
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
        arithmeticNode.add(eval);
        arithmeticNode.add(add);
        arithmeticNode.add(subtract);
        arithmeticNode.add(multiply);
        arithmeticNode.add(divide);
        arithmeticNode.add(modulus);
        arithmeticNode.add(random);
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

        arrayRoot.add(intArrayNode);
        arrayRoot.add(strArrayNode);
        arrayRoot.add(boolArrayNode);
        arrayRoot.add(floatArrayNode);

        nodesListPanel.add(functionsTree, BorderLayout.CENTER);
        variableListPanel.add(variableTree, BorderLayout.CENTER);
        arrayListPanel.add(arrayTree, BorderLayout.CENTER);

        functionsTree.expandRow(0);
        variableTree.expandRow(0);
        arrayTree.expandRow(0);
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