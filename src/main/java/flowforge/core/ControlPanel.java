package flowforge.core;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.FlowForge;
import flowforge.nodes.PrintNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
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
    private JComboBox variableBox;
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

        variableNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Variable name");

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
                    flowForge.flowPanel.add(new PrintNode("Print", flowForge.flowPanel));
                }
            }
        });

        addButton.addActionListener(e -> {

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



    public JPanel getRootPanel() {
        return rootPanel;
    }
}
