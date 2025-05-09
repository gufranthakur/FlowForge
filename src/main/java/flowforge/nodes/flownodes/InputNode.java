package flowforge.nodes.flownodes;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.core.panels.ProgramPanel;
import flowforge.nodes.Node;

import javax.swing.*;

public class InputNode extends Node {

    private ProgramPanel programPanel;
    private JTextField inputField;
    public String inputValue;

    public InputNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(210, 150);
        inputXButton.setVisible(false);

        inputField = new JTextField();
        inputField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Input");

        topPanel.add(inputField);
    }


    @Override
    public void execute() {
        inputValue = JOptionPane.showInputDialog(inputField.getText());

        for (Node node : outputXNodes) {
            node.execute();
        }

        for (Node node : outputNodes) {
            node.execute();
        }
    }


}
