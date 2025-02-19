package flowforge.nodes.variables;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.core.MainPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import java.awt.*;

public class StringNode extends Node {

    private MainPanel mainPanel;
    public JTextField textField;

    public StringNode(String title, MainPanel mainPanel, String stringValue) {
        super(title, mainPanel);
        this.mainPanel = mainPanel;
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
        return mainPanel.strings.get(title);
    }

    public void setStringValue(String stringValue) {
        mainPanel.strings.put(title, stringValue);
        System.out.println(mainPanel.strings);
    }

}
