package flowforge;

import javax.swing.*;

public class AboutWindow extends JFrame{
    private JPanel rootPanel;
    private JPanel mainPanel;
    private JTextPane aboutTextPane;
    private JTextPane flowForgeIsOpenSourceTextPane;
    private JScrollPane scrollPane;
    private JPanel aboutPanel;
    private JPanel technicalDetailsPanel;
    private JPanel lisencingPanel;
    private JButton confirmButton;

    private FlowForge flowForge;

    public AboutWindow(FlowForge flowForge) {
        this.flowForge = flowForge;
        this.setSize(600, 700);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(rootPanel);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        confirmButton.addActionListener(e -> {
            this.dispose();
        });

        this.setVisible(true);
    }

}
