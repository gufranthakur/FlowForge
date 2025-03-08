package flowforge.nodes.flownodes;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.core.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.variables.StringNode;

import javax.swing.*;
import java.awt.*;

public class InputNode extends Node {

    private ProgramPanel programPanel;
    private JTextField inputField;

    public InputNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        inputXButton.setVisible(false);

        inputField = new JTextField();
        inputField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Input");

        topPanel.add(inputField);
    }

    public String getInputString() {
        return JOptionPane.showInputDialog(inputField.getText());
    }

    public void setInputString(String string) {
        inputField.setText(string);
    }

    @Override
    public void execute() {

        for (Node node : outputNodes) {
            node.execute();
        }
        System.out.println(compileToC());
    }

    @Override
    public String compileToC() {
       return "";
    }

}
