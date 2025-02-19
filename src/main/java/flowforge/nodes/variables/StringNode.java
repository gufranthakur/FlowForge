package flowforge.nodes.variables;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.core.FlowPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import java.awt.*;

public class StringNode extends Node {

    private FlowPanel flowPanel;
    public JTextField textField;

    public StringNode(String title, FlowPanel flowPanel, String stringValue) {
        super(title, flowPanel);
        this.flowPanel = flowPanel;
        this.setSize(260, 100);

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
    public void execute() {

        for (Node node : inputXNodes) {
            if (node != null) {
                setStringValue(textField.getText());
            }
        }

        for (Node nodes : outputNodes) {
            if (nodes != null) nodes.execute();
        }
    }

    public String getStringValue() {
        return flowPanel.strings.get(title);
    }

    public void setStringValue(String stringValue) {
        flowPanel.strings.put(title, stringValue);
        System.out.println(flowPanel.strings);
    }

}
