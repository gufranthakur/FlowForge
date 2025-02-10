package flowforge.nodes;

import flowforge.core.FlowPanel;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Node extends JInternalFrame {
    private FlowPanel flowPanel;
    public ArrayList<Node> inputNodes = new ArrayList<>();
    public ArrayList<Node> outputNodes = new ArrayList<>();

    public JRadioButton inputButton;
    public JRadioButton outputButton;
    public JPanel contentPanel;

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

        JPanel outputsPanel = new JPanel();
        JPanel inputsPanel = new JPanel();

        inputButton = new JRadioButton("Input");
        outputButton = new JRadioButton("Output");

        inputsPanel.setLayout(new BoxLayout(inputsPanel, BoxLayout.Y_AXIS));
        inputsPanel.add(Box.createVerticalGlue());
        inputsPanel.add(inputButton);
        inputsPanel.add(Box.createVerticalGlue());

        outputsPanel.setLayout(new BoxLayout(outputsPanel, BoxLayout.Y_AXIS));
        outputsPanel.add(Box.createVerticalGlue());
        outputsPanel.add(outputButton);
        outputsPanel.add(Box.createVerticalGlue());

        contentPanel.add(inputsPanel, BorderLayout.WEST);
        contentPanel.add(outputsPanel, BorderLayout.EAST);

        setContentPane(contentPanel);
        setVisible(true);
    }

    private void loadActionListeners() {
        inputButton.addActionListener(e -> {
            if (inputButton.isSelected()) {
                flowPanel.finishConnection(Node.this);
            }
        });

        outputButton.addActionListener(e -> {
            if (outputButton.isSelected()) {
                flowPanel.startConnection(this);
            }
        });

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                flowPanel.removeNode(Node.this);
            }
        });
    }

    public void connectTo(Node target) {
        this.outputNodes.add(target);
        target.inputNodes.add(this);
        flowPanel.repaint();
    }


    public void drawConnection(Graphics2D g) {
        for (Node output : outputNodes) {
            Point start = getOutputPoint();
            Point end = output.getInputPoint();
            drawGradientLine(g, start, end, new Color(253, 46, 46), new Color(37, 114, 205));
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

        inputNodes.clear();
        outputNodes.clear();

    }

    public Point getInputPoint() {
        return new Point(getX(), getY() + getHeight()/2);
    }

    public Point getOutputPoint() {
        return new Point(getX() + getWidth(), getY() + getHeight()/2);
    }

    public Point getExternalInputPoint() {
        return new Point(getX(), getY() + getHeight()/4);
    }

    public Point getExternalOutputPoint() {
        return new Point(getX() + getWidth(), getY() + getHeight()/4);
    }

    public abstract void execute();
}