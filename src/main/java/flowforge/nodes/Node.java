package flowforge.nodes;

import flowforge.core.FlowPanel;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;

public abstract class Node extends JInternalFrame {

    private FlowPanel flowPanel;

    public JRadioButton inputButton;
    public JRadioButton outputButton;

    public Node inputNode, outputNode;

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

        inputButton = new JRadioButton("Input");
        outputButton = new JRadioButton("Output");

        contentPanel.add(inputButton, BorderLayout.WEST);
        contentPanel.add(outputButton, BorderLayout.EAST);

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
        this.outputNode = target;
        target.inputNode = this;
        flowPanel.repaint();
    }

    public void drawConnection(Graphics2D g) {
        if (outputNode != null) {
            Point start = getOutputPoint();
            Point end = outputNode.getInputPoint();

            GradientPaint gp = new GradientPaint(
                    start.x, start.y, new Color(253, 46, 46),
                    end.x, end.y, new Color(37, 114, 205)
            );

            g.setPaint(gp);
            g.drawLine(start.x, start.y, end.x, end.y);
        }
    }

    public void disconnectAll() {
        if (inputNode != null) {
            inputNode.outputNode = null;
            inputNode = null;
        }
        if (outputNode != null) {
            outputNode.inputNode = null;
            outputNode = null;
        }
    }

    public Point getInputPoint() {
        if (inputButton.isSelected()) {
            return new Point(getX(), getY() + getHeight()/2);
        } else {
            return new Point(getX() + getWidth(), getY() + getHeight()/2);
        }
    }

    public Point getOutputPoint() {

        return new Point(getX() + getWidth(), getY() + getHeight()/2);

    }

    public abstract void execute();
}