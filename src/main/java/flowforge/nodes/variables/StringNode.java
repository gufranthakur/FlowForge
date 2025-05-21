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
        this.setSize(260, 110);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        textField = new JTextField();
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Set value...");
        textField.setPreferredSize(new Dimension(150, 30));

        inputButton.setVisible(false);
        outputButton.setVisible(false);

        inputXButton.setText("Set");
        outputXButton.setText("Get");

        wrapperPanel.add(resetConnectionsButton);
        wrapperPanel.add(textField);
        contentPanel.add(wrapperPanel, BorderLayout.NORTH);

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
                    node.setBorder(new EmptyBorder(3, 3, 3, 3));
                }
                this.setBorder(new LineBorder(new Color(255, 126, 23), 3));
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
