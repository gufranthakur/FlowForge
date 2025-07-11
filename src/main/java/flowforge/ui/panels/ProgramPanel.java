package flowforge.ui.panels;

import flowforge.FlowForge;
import flowforge.nodes.flownodes.BranchNode;
import flowforge.ui.popupMenus.NodePopupMenu;
import flowforge.ui.popupMenus.ProgramPanelPopupMenu;
import flowforge.ui.popupMenus.SearchPopupMenu;
import flowforge.nodes.Node;
import flowforge.nodes.StartNode;
import flowforge.ui.popupMenus.SearchXPopupMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProgramPanel extends JPanel implements KeyListener {

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
    private int cameraSpeed = 7;

    public NodePopupMenu nodePopupMenu;
    public SearchPopupMenu searchPopupMenu;
    public SearchXPopupMenu searchXPopupMenu;
    public ProgramPanelPopupMenu programPanelPopupMenu;

    private Point currentMouseLocation;
    public boolean isExecutingSteps = false;
    public final Object stepExecutorLock = new Object();

    public boolean showGrid = true;
    public boolean snapToGrid = true;

    private Color bgColor = new Color(30, 30, 30);


    public ProgramPanel(FlowForge flowForge) {
        this.flowForge = flowForge;
        this.setLocation(0, 0);
        this.setSize(3000, 5000);
        this.setBackground(bgColor);
        this.addKeyListener(this);
        this.setLayout(null);

        setDoubleBuffered(true);

        startNode = new StartNode("Start", this);
        addNode(startNode);

        nodePopupMenu = new NodePopupMenu(this);
        searchPopupMenu = new SearchPopupMenu(this);
        searchXPopupMenu = new SearchXPopupMenu(this);
        programPanelPopupMenu = new ProgramPanelPopupMenu(this);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();

                if (selectedNode == null) return;

                if (selectedNode instanceof BranchNode) {
                    if (((BranchNode) selectedNode).isBeingTrueConnected || ((BranchNode) selectedNode).isBeingFalseConnected) {
                        searchPopupMenu.show(getProgramPanel(), currentMouseLocation.x, currentMouseLocation.y);
                    }
                } else {
                    if (selectedNode.isBeingConnected) {
                        searchPopupMenu.show(getProgramPanel(), currentMouseLocation.x, currentMouseLocation.y);
                    } else if (selectedNode.isBeingXConnected) {
                        searchXPopupMenu.show(getProgramPanel(), currentMouseLocation.x, currentMouseLocation.y);
                    }
                }
                for (Node node : nodes) {
                    node.inputButton.setEnabled(true);
                    node.outputButton.setEnabled(true);
                    node.inputXButton.setEnabled(true);
                    node.outputXButton.setEnabled(true);
                }

            }
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (SwingUtilities.isRightMouseButton(e)) {
                    programPanelPopupMenu.displayMenu(e.getX() + 10, e.getY() + 10, showGrid, snapToGrid);
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                currentMouseLocation = e.getPoint();
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    cameraSpeed = 18;
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (selectedNode == null) return;

                    if (selectedNode instanceof BranchNode) {
                        ((BranchNode) selectedNode).isBeingTrueConnected = false;
                        ((BranchNode) selectedNode).isBeingFalseConnected = false;
                    } else {
                        selectedNode.isBeingConnected = false;
                        selectedNode.isBeingXConnected = false;
                    }


                }

            }
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    cameraSpeed = 7;
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

            node.setLocation(viewportCenterX - node.getNodeWidth() / 2,
                    viewportCenterY - node.getNodeHeight() / 2);
        } else {
            node.setLocation(currentMouseLocation);
        }
        this.add(node);
        node.repaint();
        node.revalidate();
    }

    public void startConnection(Node node) {
        sourceNode = node;
    }

    public void finishConnection(Node targetNode) {
        if (sourceNode != null && sourceNode != targetNode) {

            if (sourceNode instanceof BranchNode) {
                if (((BranchNode) sourceNode).isBeingTrueConnected) sourceNode.connectTo(targetNode);
                if (((BranchNode) sourceNode).isBeingFalseConnected) sourceNode.connectToX(targetNode);
            }

            sourceNode.connectTo(targetNode);

            sourceNode.outputButton.setSelected(true);
            targetNode.inputButton.setSelected(true);

            selectedNode.isBeingConnected = false;
            sourceNode = null;
        }
    }

    public void startXConnection(Node node) {
        sourceNode = node;
    }

    public void finishXConnection(Node targetNode) {
        if (sourceNode != null && sourceNode != targetNode) {
            sourceNode.connectToX(targetNode);

            sourceNode.outputXButton.setSelected(true);
            targetNode.inputXButton.setSelected(true);

            selectedNode.isBeingXConnected = false;
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

        g2D.setColor(new Color(60, 60, 60));
        if (showGrid) {
            int cellSize = 30;
            int width = getWidth();
            int height = getHeight();

            for (int x = 0; x <= width; x += cellSize) {
                g2D.drawLine(x, 0, x, height);
            }

            for (int y = 0; y <= height; y += cellSize) {
                g2D.drawLine(0, y, width, y);
            }
        }

        g2D.setStroke(new BasicStroke(2.0f));
        g2D.drawRect(2, 2, getWidth() - 2, getHeight() - 2);

        for (Node node : nodes) {
            node.drawConnection(g2D);
            node.drawXConnection(g2D);
        }

        if (selectedNode == null) return;

        if (selectedNode instanceof BranchNode) {
            if (((BranchNode) selectedNode).isBeingTrueConnected) {
                selectedNode.drawCurvedGradientLine(g2D, selectedNode.getOutputPoint(),
                        currentMouseLocation,
                        ((BranchNode) selectedNode).trueConnectionColor,
                        ((BranchNode) selectedNode).trueConnectionColor2);

            } else if (((BranchNode) selectedNode).isBeingFalseConnected) {
                selectedNode.drawCurvedGradientLine(g2D, selectedNode.getOutputPoint(),
                        currentMouseLocation,
                        ((BranchNode) selectedNode).falseConnectionColor,
                        ((BranchNode) selectedNode).falseConnectionColor2);
            }
        } else {
            if (selectedNode.isBeingConnected) {
                selectedNode.drawCurvedGradientLine(g2D, selectedNode.getOutputPoint(),
                        currentMouseLocation,
                        selectedNode.connectionColor, selectedNode.connectionColor2);
            }

            if (selectedNode.isBeingXConnected) {
                selectedNode.drawCurvedGradientLine(g2D, selectedNode.getOutputXPoint(),
                        currentMouseLocation,
                        selectedNode.connectionXColor, selectedNode.connectionXColor2);
            }
        }




    }


    public int getNodeAmount() {
        return nodes.size();
    }

    public void addVariable(String varName, String varType) {

        if (varType == null) {
            switch (Objects.requireNonNull(flowForge.controlPanel.variableBox.getSelectedItem()).toString()) {
                case "Integer" -> integers.put(varName, 0);
                case "String" -> strings.put(varName, "");
                case "Boolean" -> booleans.put(varName, false);
                case "Float" -> floats.put(varName, 0.0f);
            }
        } else {
            switch (varType) {
                case "Integer" -> integers.put(varName, 0);
                case "String" -> strings.put(varName, "");
                case "Boolean" -> booleans.put(varName, false);
                case "Float" -> floats.put(varName, 0.0f);
            }
        }

        searchXPopupMenu.reloadVariableTree();

    }

    public JPanel getProgramPanel() {
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
