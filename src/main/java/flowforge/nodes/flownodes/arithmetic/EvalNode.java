package flowforge.nodes.flownodes.arithmetic;

import com.formdev.flatlaf.FlatClientProperties;
import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.flownodes.PrintNode;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvalNode extends Node {

    private ProgramPanel programPanel;
    public JTextField expressionField;

    private Integer result;

    public EvalNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;
        this.setSize(210, 150);

        expressionField = new JTextField();
        expressionField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter expression");

        topPanel.add(expressionField);

        inputXButton.setVisible(false);
        outputXButton.setText("Result");
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
        //-----------

        String expressionStr = expressionField.getText().trim();
        if (!expressionStr.isEmpty()) {
            expressionStr = replaceVariables(expressionStr);

            Expression expression = new ExpressionBuilder(expressionStr).build();
            float value = (float) expression.evaluate();

            result = (int) value;
        }

        //-----------

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute(isStepExecution);
        }
        for (Node node : outputNodes) {
            if (node != null) node.execute(isStepExecution);
        }
    }



    private String replaceVariables(String expression) {
        // Pattern to match variables wrapped in curly braces: {varName}
        Pattern pattern = Pattern.compile("\\{([^{}]+)\\}");
        Matcher matcher = pattern.matcher(expression);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String varName = matcher.group(1);
            String replacement = getVariableValue(varName);
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private String getVariableValue(String varName) {
        // Check all variable maps and return the value if found
        if (programPanel.integers.containsKey(varName)) {
            return programPanel.integers.get(varName).toString();
        } else if (programPanel.floats.containsKey(varName)) {
            return programPanel.floats.get(varName).toString();
        } else if (programPanel.strings.containsKey(varName)) {
            return programPanel.strings.get(varName);
        } else {
            throw new RuntimeException("Variable not found: " + varName);
        }
    }


    public int getResult() {
        return result;
    }
}