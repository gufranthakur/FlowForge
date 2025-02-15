package flowforge.core;

import flowforge.FlowForge;
import flowforge.nodes.Node;
import flowforge.nodes.StartNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlowPanel extends JDesktopPane {
    public FlowForge flowForge;
    public StartNode startNode;
    private Node sourceNode;

    public List<Node> nodes = new ArrayList<>();

    public HashMap<String, Integer> integers = new HashMap<>(20);
    public HashMap<String, String> strings = new HashMap<>(20);
    public HashMap<String, Boolean> booleans = new HashMap<>(20);
    public HashMap<String, Float> floats = new HashMap<>(20);

    public FlowPanel(FlowForge flowForge) {
        this.flowForge = flowForge;
        setDoubleBuffered(true);

        startNode = new StartNode("Start", this);
        addNode(startNode);
    }

    public void addNode(Node node) {
        nodes.add(node);
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
            return;
        }
    }

    public void removeNode(Node node) {
        nodes.remove(node);
        this.remove(node);
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

}