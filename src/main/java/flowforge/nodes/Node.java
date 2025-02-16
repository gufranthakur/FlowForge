package flowforge.nodes;

import flowforge.core.FlowPanel;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.util.ArrayList;

public abstract class Node extends JInternalFrame {
    private FlowPanel flowPanel;

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

    public Node(String title, FlowPanel flowPanel) {
        super(title, true, true, false, false);
        this.flowPanel = flowPanel;
        loadUI();
        loadActionListeners();
    }

    private void loadUI() {
        contentPanel = new JPanel(new BorderLayout());
        setSize(200, 200);
        setLocation(300, 300);

        topPanel = new JPanel();
        outputsPanel = new JPanel();
        JPanel inputsPanel = new JPanel();

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

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(inputsPanel, BorderLayout.WEST);
        contentPanel.add(outputsPanel, BorderLayout.EAST);

        setContentPane(contentPanel);
        setVisible(true);
    }

    private void loadActionListeners() {
        inputButton.addActionListener(e -> {
            for (Node node : flowPanel.nodes) {
                node.inputXButton.setEnabled(true);
                node.outputXButton.setEnabled(true);
            }
            if (inputButton.isSelected()) {
                flowPanel.finishConnection(Node.this);
            }
        });

        outputButton.addActionListener(e -> {
            for (Node node : flowPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }
            if (outputButton.isSelected()) {
                flowPanel.startConnection(this);
            }
        });

        inputXButton.addActionListener(e -> {
            for (Node node : flowPanel.nodes) {
                node.inputButton.setEnabled(true);
                node.outputButton.setEnabled(true);
            }
            if (inputXButton.isSelected()) {
                flowPanel.finishXConnection(Node.this);
            }
        });

        outputXButton.addActionListener(e -> {
            for (Node node : flowPanel.nodes) {
                node.inputButton.setEnabled(false);
                node.outputButton.setEnabled(false);
            }
            if(outputXButton.isSelected()) {
                flowPanel.startXConnection(this);
            }
        });

        resetConnectionsButton.addActionListener(e -> {
            disconnectAll();
            for (Node node : flowPanel.nodes) {
                node.inputButton.setEnabled(true);
                node.outputButton.setEnabled(true);
                node.inputXButton.setEnabled(true);
                node.outputXButton.setEnabled(true);
            }
        });

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                flowPanel.removeNode(Node.this);
                disconnectAll();
            }
        });
    }

    public void connectTo(Node target) {
        this.outputNodes.add(target);
        target.inputNodes.add(this);
        flowPanel.repaint();
    }

    public void connectToX(Node target) {
        this.outputXNodes.add(target);
        target.inputXNodes.add(this);
        flowPanel.repaint();
    }

    public void drawConnection(Graphics2D g) {
        for (Node output : outputNodes) {
            Point start = getOutputPoint();
            Point end = output.getInputPoint();
            drawGradientLine(g, start, end, new Color(253, 46, 46), new Color(37, 114, 205));
        }
    }
    public void drawXConnection(Graphics2D g) {
        for (Node output : outputXNodes) {
            Point start = getOutputXPoint();
            Point end = output.getInputXPoint();
            drawGradientLine(g, start, end, new Color(253, 243, 46), new Color(37, 205, 71));
        }
    }
    private void drawGradientLine(Graphics2D g, Point start, Point end, Color startColor, Color endColor) {
        GradientPaint gp = new GradientPaint(start.x, start.y, startColor, end.x, end.y, endColor);
        g.setPaint(gp);
        g.drawLine(start.x, start.y, end.x, end.y);
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

    public Point getExternalInputPoint() {
        return new Point(getX(), getY() + getHeight()/4);
    }

    public Point getExternalOutputPoint() {
        return new Point(getX() + getWidth(), getY() + getHeight()/4);
    }

    public abstract void execute();
}