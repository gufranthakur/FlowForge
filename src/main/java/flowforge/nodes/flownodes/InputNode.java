package flowforge.nodes.flownodes;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class InputNode extends Node {

    private ProgramPanel programPanel;
    public JTextField inputField;
    public String inputValue;

    public InputNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(210, 150);
        inputXButton.setVisible(false);
        outputXButton.setText("Input Value");

        inputField = new JTextField();
        inputField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Input");

        resetConnectionsButton.addActionListener(e -> {
            inputField.setText("");
        });

        topPanel.add(inputField);
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
        inputValue = JOptionPane.showInputDialog(inputField.getText());

        for (Node node : outputXNodes) {
            node.execute(isStepExecution);
        }

        for (Node node : outputNodes) {
            node.execute(isStepExecution);
        }
    }


}
