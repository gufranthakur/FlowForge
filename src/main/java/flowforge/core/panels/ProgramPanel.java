package flowforge.core.panels;

import flowforge.FlowForge;
import flowforge.nodes.Node;
import flowforge.nodes.StartNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgramPanel extends JDesktopPane implements KeyListener {

    public FlowForge flowForge;
    public StartNode startNode;
    private Node sourceNode;

    public List<Node> nodes = new ArrayList<>();
    private Node selectedNode;

    public HashMap<String, Integer> integers = new HashMap<>(20);
    public HashMap<String, String> strings = new HashMap<>(20);
    public HashMap<String, Boolean> booleans = new HashMap<>(20);
    public HashMap<String, Float> floats = new HashMap<>(20);

    private boolean isUp, isDown, isLeft, isRight;
    private int cameraX, cameraY;
    private int cameraSpeed = 5;
    private Point dragStart;

    private JPopupMenu nodePopupMenu;
    private JMenuItem deleteNode, resetConnections, resize;

    public ProgramPanel(FlowForge flowForge) {
        this.flowForge = flowForge;
        this.setLocation(0, 0);
        this.setSize(3000, 3000);
        this.setBackground(new Color(30, 30, 30));
        this.addKeyListener(this);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    // Slow down movement a bit for smoother feel
                    int deltaX = (e.getX() - dragStart.x);
                    int deltaY = (e.getY() - dragStart.y);

                    setLocation(getX() + deltaX, getY() + deltaY);
                    cameraX -= deltaX;
                    cameraY -= deltaY;

                    dragStart = e.getPoint();
                    repaint();
                }
            }
        });
        setDoubleBuffered(true);

        cameraX = 0;
        cameraY = 0;

        startNode = new StartNode("Start", this);
        addNode(startNode);

        nodePopupMenu = new JPopupMenu();
        initNodePopupMenu();
    }

    public void addNode(Node node) {
        nodes.add(node);
        this.add(node);
    }

    public void addNewNode(Node node) {
        nodes.add(node);

        // Get the visible viewport center
        Rectangle visibleRect = this.getVisibleRect();
        int viewportCenterX = visibleRect.x + visibleRect.width/2;
        int viewportCenterY = visibleRect.y + visibleRect.height/2;

        // Set node position at the viewport center
        node.setLocation(viewportCenterX - node.getNodeWidth()/2,
                viewportCenterY - node.getNodeHeight()/2);

        this.add(node);
    }

    public void startConnection(Node node) {
        sourceNode = node;
    }

    public void finishConnection(Node targetNode) {
        if (sourceNode != null && sourceNode != targetNode) {
            sourceNode.connectTo(targetNode);
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
            cameraY -= cameraSpeed;
        }
        if (isDown) {
            this.setLocation(this.getX(), this.getY() - cameraSpeed);
            cameraY += cameraSpeed;
        }
        if (isLeft) {
            this.setLocation(this.getX() + cameraSpeed, this.getY());
            cameraX -= cameraSpeed;
        }
        if (isRight) {
            this.setLocation(this.getX() - cameraSpeed, this.getY());
            cameraX += cameraSpeed;
        }

    }

    // Add this to your ProgramPanel class
    public void clearAll() {
        // Create a copy of the nodes list to avoid concurrent modification
        ArrayList<Node> nodesToRemove = new ArrayList<>(nodes);

        // Remove all nodes
        for (Node node : nodesToRemove) {
            removeNode(node);
        }

        // Clear all variables
        integers.clear();
        strings.clear();
        booleans.clear();
        floats.clear();

        // Repaint the panel
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(2.0f));

        for (Node node : nodes) {
            node.drawConnection(g2d);
            node.drawXConnection(g2d);
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

    public void showNodePopupMenu(Node node, MouseEvent e) {
        nodePopupMenu.show(node, e.getX() + 10, e.getY() + 10);
        selectedNode = node;
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