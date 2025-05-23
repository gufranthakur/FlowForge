package flowforge.nodes;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.ui.panels.ProgramPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public abstract class Node extends JPanel {
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

    TitledBorder titledBorder;
    public JPanel contentPanel;
    public JPanel topPanel;
    public JPanel outputsPanel;
    public JPanel inputsPanel;

    private int nodeX;
    private int nodeY;
    private int nodeWidth;
    private int nodeHeight;

    // Dragging variables
    private Point dragStart;
    private boolean isDragging = false;

    public boolean isBeingConnected = false;
    public boolean isMinimized = false;

    public Color connectionColor = Color.WHITE;
    public Color connectionColor2 = Color.WHITE;
    public Color connectionXColor = new Color(253, 108, 46);
    public Color connectionXColor2 = new Color(205, 183, 37);

    public String nodeType;
    public String title;

    public Node(String title, ProgramPanel programPanel) {
        this.title = title;
        this.programPanel = programPanel;

        // Mouse listeners for selection, right-click menu, and dragging
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

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragStart = e.getPoint();
                    isDragging = true;
                    programPanel.selectedNode = Node.this;
                    programPanel.flowForge.controlPanel.updatePropertiesPanel(Node.this);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                dragStart = null;
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging && dragStart != null) {
                    Point current = e.getPoint();
                    Point parentLocation = getLocation();

                    int newX = parentLocation.x + current.x - dragStart.x;
                    int newY = parentLocation.y + current.y - dragStart.y;

                    setLocation(newX, newY);
                    updateNodeDimensions();
                    programPanel.repaint(); // Repaint to update connections
                }
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


        setFocusable(true);

        loadUI();
        loadActionListeners();
    }

    private void loadUI() {
        setLocation(300, 300);
        setSize(200, 150);
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));
        putClientProperty(FlatClientProperties.STYLE, "arc : 20");

        // Create titled border with the title
        titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleColor(Color.WHITE);

        restoreBorder();

        // Set background color
        //setBorder(new EmptyBorder(3, 3, 3, 3));

        contentPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel();
        outputsPanel = new JPanel();
        inputsPanel = new JPanel();

        inputButton = new JRadioButton("Input");
        outputButton = new JRadioButton("Output");
        inputXButton = new JRadioButton("InputX");
        outputXButton = new JRadioButton("OutputX");

        // Style radio buttons
        styleRadioButton(inputButton, false);
        styleRadioButton(outputButton, false);
        styleRadioButton(inputXButton, true);
        styleRadioButton(outputXButton, true);

        resetConnectionsButton = new JButton("↺");
        resetConnectionsButton.setPreferredSize(new Dimension(30, 25));

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(resetConnectionsButton);
        topPanel.setOpaque(false);

        inputsPanel.setLayout(new BoxLayout(inputsPanel, BoxLayout.Y_AXIS));
        inputsPanel.add(Box.createVerticalGlue());
        inputsPanel.add(inputButton);
        inputsPanel.add(inputXButton);
        inputsPanel.add(Box.createVerticalGlue());
        inputsPanel.setOpaque(false);

        outputsPanel.setLayout(new BoxLayout(outputsPanel, BoxLayout.Y_AXIS));
        outputsPanel.add(Box.createVerticalGlue());
        outputsPanel.add(outputButton);
        outputsPanel.add(outputXButton);
        outputsPanel.add(Box.createVerticalGlue());
        outputsPanel.setOpaque(false);

        contentPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(inputsPanel, BorderLayout.WEST);
        contentPanel.add(outputsPanel, BorderLayout.EAST);
        contentPanel.setOpaque(false);

        add(contentPanel, BorderLayout.CENTER);

        updateNodeDimensions();
    }

    private void styleRadioButton(JRadioButton button, boolean isXConnection) {
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setFont(button.getFont().deriveFont(12f));

        if (isXConnection) {
            button.putClientProperty(FlatClientProperties.STYLE,
                    "icon.selectedBackground: rgb(229,117,42);");
        }

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

        resetConnectionsButton.addActionListener(e -> {
            disconnectAll();
            programPanel.repaint();
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
        inputXNodes.clear();
        outputXNodes.clear();

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

    public void restoreBorder() {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                titledBorder
        ));
    }

    public void setStepExecutedBorder() {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 117, 42), 2),
                titledBorder
        ));
    }

    // Getters
    public String getTitle() { return title; }
    public int getNodeX() { return nodeX; }
    public int getNodeY() { return nodeY; }
    public int getNodeWidth() { return nodeWidth; }
    public int getNodeHeight() { return nodeHeight; }

    public Point getInputPoint() {
        if (isMinimized) return new Point(getX(), getY() + 10);
        return new Point(getX(), getY() + getHeight()/2 + 20);
    }

    public Point getOutputPoint() {
        if (isMinimized) return new Point(getX() + getWidth(), getY() + 10);
        return new Point(getX() + getWidth(), getY() + getHeight()/2 + 20);
    }

    public Point getInputXPoint() {
        return new Point(getX(), getY() + getHeight()/2 + 20);
    }

    public Point getOutputXPoint() {
        return new Point(getX() + getWidth(), getY() + getHeight()/2 + 20);
    }

    public abstract void execute(boolean isStepExecution);

    public void setClosable(boolean b) {

    }

    public void setResizable(boolean b) {

    }
    public void pack() {

    }

    public void setTitle(String s) {
        this.title = s;
    }
}