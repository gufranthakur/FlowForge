package flowforge.nodes.variables;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.InputNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StringNode extends Node {

    private ProgramPanel programPanel;
    public JTextField textField;
    private String value;

    public StringNode(String title, ProgramPanel programPanel, String stringValue) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(210, 90);

        this.nodeTheme = programPanel.flowForge.variableNodeTheme;

        textField = new JTextField();
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Set value...");
        textField.setPreferredSize(new Dimension(120, 30));

        inputButton.setVisible(false);
        outputButton.setVisible(false);

        inputXButton.setText("Set");
        outputXButton.setText("Get");

        topPanel.add(textField);

    }
    @Override
    public Point getInputXPoint() {
        if (isMinimized) return new Point(getX(), getY() + 15);
        return new Point(getX(), getY() + getHeight()/2 + 20);
    }
    @Override
    public Point getOutputXPoint() {
        if (isMinimized) return new Point(getX() + getWidth(), getY() + 15);
        return new Point(getX() + getWidth(), getY() + getHeight() / 2 + 20);
    }

    @Override
    public void execute(boolean isStepExecution) {
        if (isStepExecution) {
            synchronized (programPanel.stepExecutorLock) {
                try {
                    programPanel.stepExecutorLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            SwingUtilities.invokeLater(() -> {
                for (Node node : programPanel.nodes) {
                    node.restoreBorder();
                }
                this.setStepExecutedBorder();
            });
        }
        for (Node node : inputXNodes) {

            if (node != null) {
                if (node instanceof InputNode) setStringValue(((InputNode) node).inputValue);
                else setStringValue(textField.getText());
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute(isStepExecution);
        }
        
    }

    public String getValue() {
        return programPanel.strings.get(title);
    }

    public void setStringValue(String stringValue) {
        programPanel.strings.put(title, stringValue);
    }

}
