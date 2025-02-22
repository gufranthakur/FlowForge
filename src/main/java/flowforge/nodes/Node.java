package flowforge.nodes;

import flowforge.core.ProgramPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
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

    public Node(String title, ProgramPanel programPanel) {
        super(title, true, true, false, false);
        this.programPanel = programPanel;
        loadUI();
        loadActionListeners();
    }

    private void loadUI() {

        setSize(200, 200);
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
            for (Node node : programPanel.nodes) {
                node.inputXButton.setEnabled(true);
                node.outputXButton.setEnabled(true);
            }
            if (inputButton.isSelected()) {
                programPanel.finishConnection(Node.this);
            }
        });

        outputButton.addActionListener(e -> {
            for (Node node : programPanel.nodes) {
                node.inputXButton.setEnabled(false);
                node.outputXButton.setEnabled(false);
            }
            if (outputButton.isSelected()) {
                programPanel.startConnection(this);
            }
        });

        inputXButton.addActionListener(e -> {
            for (Node node : programPanel.nodes) {
                node.inputButton.setEnabled(true);
                node.outputButton.setEnabled(true);
            }
            if (inputXButton.isSelected()) {
                programPanel.finishXConnection(Node.this);
            }
        });

        outputXButton.addActionListener(e -> {
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
            for (Node node : programPanel.nodes) {
                node.inputButton.setEnabled(true);
                node.outputButton.setEnabled(true);
                node.inputXButton.setEnabled(true);
                node.outputXButton.setEnabled(true);
            }
        });

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                programPanel.removeNode(Node.this);
                disconnectAll();
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
            drawCurvedGradientLine(g, start, end, new Color(64, 193, 239), new Color(10, 97, 200));
        }
    }

    public void drawXConnection(Graphics2D g) {
        for (Node output : outputXNodes) {
            Point start = getOutputXPoint();
            Point end = output.getInputXPoint();
            drawCurvedGradientLine(g, start, end, new Color(253, 108, 46), new Color(205, 183, 37));
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