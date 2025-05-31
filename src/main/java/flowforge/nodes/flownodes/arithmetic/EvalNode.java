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
    private float result;

    public EvalNode(String title, ProgramPanel programPanel) {
        super(title, programPanel);
        this.programPanel = programPanel;

        this.nodeTheme = new Color(85, 45, 142);

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
                    node.restoreBorder();
                }
                this.setStepExecutedBorder();
            });
        }

        String expressionStr = expressionField.getText().trim();
        if (!expressionStr.isEmpty()) {
            try {
                expressionStr = replaceVariables(expressionStr);
                Expression expression = new ExpressionBuilder(expressionStr).build();
                double doubleResult = expression.evaluate();
                result = (float) doubleResult;
            } catch (Exception e) {
                result = Float.NaN;
                System.err.println("Error evaluating expression: " + e.getMessage());
            }
        } else {
            result = 0.0f;
        }

        for (Node node : outputXNodes) {
            if (node != null && !(node instanceof PrintNode)) node.execute(isStepExecution);
        }
        for (Node node : outputNodes) {
            if (node != null) node.execute(isStepExecution);
        }
    }

    private String replaceVariables(String expression) {
        Pattern pattern = Pattern.compile("\\{([^{}]+)\\}");
        Matcher matcher = pattern.matcher(expression);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String varName = matcher.group(1);
            try {
                String replacement = getVariableValue(varName);
                matcher.appendReplacement(result, replacement);
            } catch (RuntimeException e) {
                matcher.appendReplacement(result, "0");
                System.err.println("Variable not found, using 0: " + varName);
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private String getVariableValue(String varName) {
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

    public float getResult() {
        return result;
    }
}