package flowforge.core.ui.panels;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.FlowForge;
import flowforge.nodes.Node;
import flowforge.nodes.StartNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgramPanel extends JDesktopPane implements KeyListener {

    public FlowForge flowForge;
    public StartNode startNode;
    private Node sourceNode;

    public List<Node> nodes = new ArrayList<>();
    public Node selectedNode;

    public HashMap<String, Integer> integers = new HashMap<>(20);
    public HashMap<String, String> strings = new HashMap<>(20);
    public HashMap<String, Boolean> booleans = new HashMap<>(20);
    public HashMap<String, Float> floats = new HashMap<>(20);

    private boolean isUp, isDown, isLeft, isRight;

    private int cameraSpeed = 5;

    private JPopupMenu nodePopupMenu;
    private JMenuItem deleteNode, resetConnections, resize;
    private JPopupMenu addNodePopupMenu;

    private List<String> nodesList;
    private JTree searchTree;
    private DefaultMutableTreeNode root;

    private Point currentMouseLocation;

    private BufferedImage gridImage;

    public ProgramPanel(FlowForge flowForge) {
        this.flowForge = flowForge;
        this.setLocation(0, 0);
        this.setSize(3000, 3000);
        this.setBackground(new Color(30, 30, 30));
        this.addKeyListener(this);

        setDoubleBuffered(true);

        startNode = new StartNode("Start", this);
        addNode(startNode);

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

        root = new DefaultMutableTreeNode("Results");
        for (String s : nodesList) {
            root.add(new DefaultMutableTreeNode(s));
        }

        searchTree = new JTree(root);
        searchTree.setRootVisible(false);

        nodePopupMenu = new JPopupMenu();
        addNodePopupMenu = new JPopupMenu();

        initNodePopupMenu();
        initAddNodePopupMenu();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();

                if (selectedNode == null) return;
                if (selectedNode.isBeingConnected || selectedNode.isBeingXConnected) {
                    addNodePopupMenu.show(getDesktopPane(), currentMouseLocation.x, currentMouseLocation.y);
                }

                for (Node node : nodes) {
                    node.inputButton.setEnabled(true);
                    node.outputButton.setEnabled(true);
                    node.inputXButton.setEnabled(true);
                    node.outputXButton.setEnabled(true);
                }

            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                currentMouseLocation = e.getPoint();
            }
        });

        searchTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                flowForge.controlPanel.getSelectedNodeAtTree(searchTree, false);
                addNodePopupMenu.setVisible(false);


                if (selectedNode.isBeingConnected) {
                    startConnection(selectedNode);
                    finishConnection(nodes.getLast());
                }
                if (selectedNode.isBeingXConnected) {
                    startXConnection(selectedNode);
                    finishXConnection(nodes.getLast());
                }
                selectedNode.outputButton.setSelected(false);
                selectedNode.outputXButton.setSelected(false);

                selectedNode = null;
            }
        });
        searchTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    flowForge.controlPanel.getSelectedNodeAtTree(searchTree, false);

                    addNodePopupMenu.setVisible(false);


                    if (selectedNode.isBeingConnected) {
                        startConnection(selectedNode);
                        finishConnection(nodes.getLast());
                    }
                    if (selectedNode.isBeingXConnected) {
                        startXConnection(selectedNode);
                        finishXConnection(nodes.getLast());
                    }
                    selectedNode.outputButton.setSelected(false);
                    selectedNode.outputXButton.setSelected(false);

                    selectedNode = null;
                }
            }
        });
    }

    public void addNode(Node node) {
        nodes.add(node);
        this.add(node);
    }

    public void addNewNode(Node node, boolean inCenter) {
        nodes.add(node);

        if (inCenter) {
            Rectangle visibleRect = this.getVisibleRect();
            int viewportCenterX = visibleRect.x + visibleRect.width / 2;
            int viewportCenterY = visibleRect.y + visibleRect.height / 2;

            // Set node position at the viewport center
            node.setLocation(viewportCenterX - node.getNodeWidth() / 2,
                    viewportCenterY - node.getNodeHeight() / 2);
        } else {
            node.setLocation(currentMouseLocation);
        }
        this.add(node);
    }

    public void startConnection(Node node) {
        sourceNode = node;
    }

    public void finishConnection(Node targetNode) {
        if (sourceNode != null && sourceNode != targetNode) {
            sourceNode.connectTo(targetNode);
            sourceNode.outputButton.setSelected(false);
            targetNode.inputButton.setSelected(false);
            sourceNode = null;
            repaint();
        }
    }

    public void startXConnection(Node node) {
        sourceNode = node;
    }

    public void finishXConnection(Node targetNode) {
        if (sourceNode != null && sourceNode != targetNode) {
            sourceNode.connectToX(targetNode);
            sourceNode.outputXButton.setSelected(false);
            targetNode.inputXButton.setSelected(false);
            sourceNode = null;
        }
    }

    public void removeNode(Node node) {
        nodes.remove(node);
        this.remove(node);
        repaint();
    }

    public void moveCamera() {
        if (isUp) {
            this.setLocation(this.getX(), this.getY() + cameraSpeed);

        }
        if (isDown) {
            this.setLocation(this.getX(), this.getY() - cameraSpeed);

        }
        if (isLeft) {
            this.setLocation(this.getX() + cameraSpeed, this.getY());

        }
        if (isRight) {
            this.setLocation(this.getX() - cameraSpeed, this.getY());
        }

    }

    public void moveCameraTo(int x, int y) {
        this.setLocation(-x, -y);
    }

    public void clearAll() {
        ArrayList<Node> nodesToRemove = new ArrayList<>(nodes);

        for (Node node : nodesToRemove) {
            removeNode(node);
        }

        integers.clear();
        strings.clear();
        booleans.clear();
        floats.clear();

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int cellSize = 30;
        int width = getWidth();
        int height = getHeight();

        g2D.setColor(new Color(60, 60, 60));


        for (int x = 0; x <= width; x += cellSize) {
            g2D.drawLine(x, 0, x, height);
        }

        for (int y = 0; y <= height; y += cellSize) {
            g2D.drawLine(0, y, width, y);
        }

        g2D.setStroke(new BasicStroke(2.0f));
        g2D.drawRect(2, 2, getWidth() - 2, getHeight() - 2);

        for (Node node : nodes) {
            node.drawConnection(g2D);
            node.drawXConnection(g2D);

        }

        if (selectedNode == null) return;

        if (selectedNode.isBeingConnected && selectedNode.outputButton.isSelected()) {
            selectedNode.drawCurvedGradientLine(g2D, selectedNode.getOutputPoint(),
                    currentMouseLocation,
                    selectedNode.connectionColor, selectedNode.connectionColor2);
        }

        if (selectedNode.isBeingXConnected && selectedNode.outputXButton.isSelected()) {
            selectedNode.drawCurvedGradientLine(g2D, selectedNode.getOutputXPoint(),
                    currentMouseLocation,
                    selectedNode.connectionXColor, selectedNode.connectionXColor2);
        }

    }


    public int getNodeAmount() {
        return nodes.size();
    }

    public void addVariable(String varName) {
        if (flowForge.controlPanel.variableBox.getSelectedItem().equals("Integer")) {
            integers.put(varName, 0);
        } else if (flowForge.controlPanel.variableBox.getSelectedItem().equals("String")) {
            strings.put(varName, "");
        } else if (flowForge.controlPanel.variableBox.getSelectedItem().equals("Boolean")) {
            booleans.put(varName, false);
        } else if (flowForge.controlPanel.variableBox.getSelectedItem().equals("Float")) {
            floats.put(varName, 0.0f);
        }
    }

    private void initNodePopupMenu() {
        resize = new JMenuItem("Resize");
        resetConnections = new JMenuItem("Disconnect");
        deleteNode = new JMenuItem("Delete");

        resize.addActionListener(e -> selectedNode.pack());
        resetConnections.addActionListener(e -> selectedNode.disconnectAll());
        deleteNode.addActionListener(e ->  {
            selectedNode.disconnectAll();
            removeNode(selectedNode);
        });

        nodePopupMenu.add(resize);
        nodePopupMenu.add(resetConnections);
        nodePopupMenu.add(deleteNode);
    }

    private void initAddNodePopupMenu() {
        addNodePopupMenu = new JPopupMenu();

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

        addNodePopupMenu.add(contentPanel);

        addNodePopupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // When the popup menu is about to be shown
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                selectedNode.inputButton.setSelected(false);
                selectedNode.outputButton.setSelected(false);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                selectedNode.inputButton.setSelected(false);
                selectedNode.outputButton.setSelected(false);
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

    public void showNodePopupMenu(Node node, MouseEvent e) {
        nodePopupMenu.show(node, e.getX() + 10, e.getY() + 10);

    }

    public JDesktopPane getDesktopPane() {
        return this;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w') isUp = true;
        if (e.getKeyChar() == 's') isDown = true;
        if (e.getKeyChar() == 'a') isLeft = true;
        if (e.getKeyChar() == 'd') isRight = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'w') isUp = false;
        if (e.getKeyChar() == 's') isDown = false;
        if (e.getKeyChar() == 'a') isLeft = false;
        if (e.getKeyChar() == 'd') isRight = false;
    }

}