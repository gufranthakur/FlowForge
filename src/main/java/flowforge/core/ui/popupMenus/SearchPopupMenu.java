package flowforge.core.ui.popupMenus;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.core.ui.panels.ProgramPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SearchPopupMenu extends JPopupMenu {

    private ProgramPanel programPanel;

    private List<String> nodesList;
    private JTree searchTree;
    private DefaultMutableTreeNode root;

    public SearchPopupMenu(ProgramPanel programPanel) {
        this.programPanel = programPanel;

        initList();
        initTree();
        initListeners();
        initUI();
    }
    private void initList() {
        nodesList = new ArrayList<>();
        nodesList.add("Print");
        nodesList.add("Branch");
        nodesList.add("Input");
        nodesList.add("Delay");
        nodesList.add("Loop");
        nodesList.add("Conditional-Loop");

        nodesList.add("Add");
        nodesList.add("Subtract");
        nodesList.add("Multiply");
        nodesList.add("Divide");
        nodesList.add("Modulus");
        nodesList.add("Random");

        nodesList.add("Equals to");
        nodesList.add("Greater than");
        nodesList.add("Less than");
        nodesList.add("Greater than or equal to");
        nodesList.add("Less than or equal to");
        nodesList.add("Not equal to");

        nodesList.add("NOT");
        nodesList.add("AND");
        nodesList.add("OR");
        nodesList.add("NAND");
        nodesList.add("NOR");
        nodesList.add("XOR");

        nodesList.add("Route");
        nodesList.add("Recurse");
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
                addNodeThroughSearch();
            }
        });
        searchTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addNodeThroughSearch();
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

        this.add(contentPanel);

        this.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // When the popup menu is about to be shown
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                programPanel.selectedNode.inputButton.setSelected(false);
                programPanel.selectedNode.outputButton.setSelected(false);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                programPanel.selectedNode.inputButton.setSelected(false);
                programPanel.selectedNode.outputButton.setSelected(false);
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

    private void addNodeThroughSearch() {
        programPanel.flowForge.controlPanel.getSelectedNodeAtTree(searchTree, false);
        getThis().setVisible(false);


        if (programPanel.selectedNode.isBeingConnected) {
            programPanel.startConnection(programPanel.selectedNode);
            programPanel.finishConnection(programPanel.nodes.getLast());
        }
        if (programPanel.selectedNode.isBeingXConnected) {
            programPanel.startXConnection(programPanel.selectedNode);
            programPanel.finishXConnection(programPanel.nodes.getLast());
        }
        programPanel.selectedNode.outputButton.setSelected(false);
        programPanel.selectedNode.outputXButton.setSelected(false);

        programPanel.selectedNode = null;
    }

    private JPopupMenu getThis() {
        return this;
    }

}
