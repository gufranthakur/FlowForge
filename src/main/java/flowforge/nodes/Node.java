package flowforge.nodes;

import flowforge.core.ui.panels.ProgramPanel;
import flowforge.core.ui.popupMenus.NodePopupMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public abstract class Node extends JInternalFrame {
    private ProgramPanel programPanel;

    public ArrayList<Node> inputNodes = new ArrayList<>();
    public ArrayList<Node> outputNodes = new ArrayList<>();
    public ArrayList<Node> inputXNodes = new ArrayList<>();
    public ArrayList<Node> outputXNodes = new ArrayList<>();

    public JRadioButton inputButton;
    public JRadioButton outputButton;
    public JRadioButton inputXButton;
    public JRadioButton outputXButton;
    public JButton resetConnectionsButton;

    public JPanel contentPanel;
    public JPanel topPanel;
    public JPanel outputsPanel;
    public JPanel inputsPanel;

    private int nodeX;
    private int nodeY;
    private int nodeWidth;
    private int nodeHeight;

    public boolean isBeingConnected = false;
    public boolean isBeingXConnected = false;

    public Color connectionColor = Color.WHITE;
    public Color connectionColor2 = Color.WHITE;
//    public Color connectionColor = new Color(255, 255, 255);
//    public Color connectionColor2 = new Color(24, 130, 220);
    public Color connectionXColor = new Color(253, 108, 46);
    public Color connectionXColor2 = new Color(205, 183, 37);

    public String nodeType;

    public Node(String title, ProgramPanel programPanel) {
        super(title, true, false, false, false);
        this.programPanel = programPanel;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                    programPanel.selectedNode = Node.this;
                    programPanel.flowForge.controlPanel.updatePropertiesPanel(Node.this);
                    if (SwingUtilities.isRightMouseButton(e)) {
                        programPanel.nodePopupMenu.show(Node.this, e.getX() + 10, e.getY() + 10);
                    }
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                updateNodeDimensions();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                updateNodeDimensions();
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    programPanel.selectedNode = null;
                }
            }
        });

        loadUI();
        loadActionListeners();
    }

    private void loadUI() {
        setLocation(300, 300);

        contentPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel();
        outputsPanel = new JPanel();
        inputsPanel = new JPanel();

        inputButton = new JRadioButton("Input");
        outputButton = new JRadioButton("Output");
        inputXButton = new JRadioButton("InputX");
        outputXButton = new JRadioButton("OutputX");

        resetConnectionsButton = new JButton("↺");

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(resetConnectionsButton);

        inputsPanel.setLayout(new BoxLayout(inputsPanel, BoxLayout.Y_AXIS));
        inputsPanel.add(Box.createVerticalGlue());
        inputsPanel.add(inputButton);
        inputsPanel.add(inputXButton);
        inputsPanel.add(Box.createVerticalGlue());

        outputsPanel.setLayout(new BoxLayout(outputsPanel, BoxLayout.Y_AXIS));
        outputsPanel.add(Box.createVerticalGlue());
        outputsPanel.add(outputButton);
        outputsPanel.add(outputXButton);
        outputsPanel.add(Box.createVerticalGlue());

        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(inputsPanel, BorderLayout.WEST);
        contentPanel.add(outputsPanel, BorderLayout.EAST);

        setContentPane(contentPanel);
        setVisible(true);
    }

    private void loadActionListeners() {
        inputButton.addActionListener(e -> {
            programPanel.selectedNode = Node.this;
            programPanel.flowForge.controlPanel.updatePropertiesPanel(Node.this);
            for (Node node : programPanel.nodes) {
                node.inputXButton.setEnabled(true);
                node.outputXButton.setEnabled(true);
            }
            if (inputButton.isSelected()) {
                programPanel.finishConnection(Node.this);
            }
        });

        outputButton.addActionListener(e -> {
            this.isBeingConnected = true;
            this.isBeingXConnected = false;

            programPanel.selectedNode = Node.this;
            programPanel.flowForge.controlPanel.updatePropertiesPanel(Node.this);
            for (Node node : programPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }
            if (outputButton.isSelected()) {
                programPanel.startConnection(this);
            }
        });

        inputXButton.addActionListener(e -> {
            programPanel.selectedNode = Node.this;
            programPanel.flowForge.controlPanel.updatePropertiesPanel(Node.this);
            for (Node node : programPanel.nodes) {
                node.inputButton.setEnabled(true);
                node.outputButton.setEnabled(true);
            }
            if (inputXButton.isSelected()) {
                programPanel.finishXConnection(Node.this);
            }
        });

        outputXButton.addActionListener(e -> {
            this.isBeingXConnected = true;
            this.isBeingConnected = false;

            programPanel.selectedNode = Node.this;
            programPanel.flowForge.controlPanel.updatePropertiesPanel(Node.this);
            if (outputButton.isSelected()) this.isBeingConnected = true;
            for (Node node : programPanel.nodes) {
                node.inputButton.setEnabled(false);
                node.outputButton.setEnabled(false);
            }
            if(outputXButton.isSelected()) {
                programPanel.startXConnection(this);
            }
        });
    }

    public void connectTo(Node target) {
        this.outputNodes.add(target);
        target.inputNodes.add(this);
        programPanel.repaint();
    }

    public void connectToX(Node target) {
        this.outputXNodes.add(target);
        target.inputXNodes.add(this);
        programPanel.repaint();
    }

    public void drawConnection(Graphics2D g) {
        for (Node output : outputNodes) {
            Point start = getOutputPoint();
            Point end = output.getInputPoint();
            drawCurvedGradientLine(g, start, end, connectionColor, connectionColor2);
        }
    }

    public void drawXConnection(Graphics2D g) {
        for (Node output : outputXNodes) {
            Point start = getOutputXPoint();
            Point end = output.getInputXPoint();
            drawCurvedGradientLine(g, start, end, connectionXColor, connectionXColor2);
        }
    }

    public void drawCurvedGradientLine(Graphics2D g, Point start, Point end, Color startColor, Color endColor) {
        int dx = end.x - start.x;

        int ctrlX1, ctrlY1, ctrlX2, ctrlY2;

        boolean isBackward = end.x < start.x;

        int offsetX = isBackward ? Math.abs(dx) / 2 + 100 : Math.abs(dx) / 3;

        ctrlX1 = start.x + offsetX;
        ctrlY1 = start.y;
        ctrlX2 = end.x - offsetX;
        ctrlY2 = end.y;

        Path2D path = new Path2D.Float();
        path.moveTo(start.x, start.y);
        path.curveTo(ctrlX1, ctrlY1, ctrlX2, ctrlY2, end.x, end.y);

        GradientPaint gp = new GradientPaint(start.x, start.y, startColor, end.x, end.y, endColor);
        g.setPaint(gp);

        Stroke originalStroke = g.getStroke();

        g.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g.draw(path);

        g.setStroke(originalStroke);
    }

    public void disconnectAll() {
        for (Node input : inputNodes) {
            input.outputNodes.remove(this);
        }
        for (Node output : outputNodes) {
            output.inputNodes.remove(this);
        }

        for (Node input : inputXNodes) {
            input.outputXNodes.remove(this);
        }

        for (Node output : outputXNodes) {
            output.inputXNodes.remove(this);
        }

        inputNodes.clear();
        outputNodes.clear();

        inputButton.setSelected(false);
        outputButton.setSelected(false);
        inputXButton.setSelected(false);
        outputXButton.setSelected(false);

    }

    private void updateNodeDimensions() {
        nodeX = getX();
        nodeY = getY();
        nodeWidth = getWidth();
        nodeHeight = getHeight();
    }

    public void restoreDimensions(int x, int y, int width, int height) {
        setLocation(x, y);
        setSize(width, height);
        updateNodeDimensions();
    }

    private Node getNode() {
        return this;
    }

    public int getNodeX() { return nodeX; }
    public int getNodeY() { return nodeY; }
    public int getNodeWidth() { return nodeWidth; }
    public int getNodeHeight() { return nodeHeight; }

    public Point getInputPoint() {
        return new Point(getX(), getY() + getHeight()/2 - 20);
    }

    public Point getOutputPoint() {
        return new Point(getX() + getWidth(), getY() + getHeight()/2 - 20);
    }

    public Point getInputXPoint() {
        return new Point(getX(), getY() + getHeight()/2 + 20);
    }

    public Point getOutputXPoint() {
        return new Point(getX() + getWidth(), getY() + getHeight()/2 + 20);
    }

    public abstract void execute();

}