package flowforge.ui.MenuBar;

import flowforge.FlowForge;
import javax.swing.*;
import java.awt.*;

public class AboutPanel {
    private JPanel rootPanel;
    private JPanel mainPanel;
    private JTextPane aboutTextPane;
    private JTextPane flowForgeIsOpenSourceTextPane;
    private JScrollPane scrollPane;
    private JPanel aboutPanel;
    private JPanel lisencingPanel;
    private JButton confirmButton;
    private FlowForge flowForge;

    public AboutPanel(FlowForge flowForge) {
        this.flowForge = flowForge;

        // Initialize the GUI components
        initializeComponents();
        setupLayout();
        setupEventHandlers();

        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
    }

    private void initializeComponents() {
        // Create root panel
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create main title label
        JLabel titleLabel = new JLabel("About FlowForge");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create main panel for content
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Create scroll pane
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Create copyright label
        JLabel copyrightLabel = new JLabel("© 2025 [Gufran Thakur]. All rights reserved.  | MIT License ");
        copyrightLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        copyrightLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create about panel
        aboutPanel = new JPanel();
        aboutPanel.setLayout(new GridBagLayout());
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Create about text pane
        aboutTextPane = new JTextPane();
        aboutTextPane.setEditable(false);
        aboutTextPane.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        aboutTextPane.setText("FlowForge is a fast, user-friendly, general-purpose visual programming IDE designed to simplify coding through flowchart-based logic. It empowers developers, makers, and engineers to visually create, edit, and manage program logic without writing traditional code.\n");
        aboutTextPane.setPreferredSize(new Dimension(150, 50));

        // Create licensing panel
        lisencingPanel = new JPanel();
        lisencingPanel.setLayout(new GridBagLayout());
        lisencingPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Create licensing details label
        JLabel licensingLabel = new JLabel("Licensing Details");
        licensingLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        licensingLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create licensing text pane
        flowForgeIsOpenSourceTextPane = new JTextPane();
        flowForgeIsOpenSourceTextPane.setEditable(false);
        flowForgeIsOpenSourceTextPane.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        flowForgeIsOpenSourceTextPane.setText("FlowForge is open source, and distributed under the MIT License, allowing users to freely use, modify, and distribute the software with minimal restrictions.\nFor more details, refer to the LICENSE file on github\n\nCommercial and personal use Allowed\nModification and distribution permitted\nMust include original copyright\n");
        flowForgeIsOpenSourceTextPane.setPreferredSize(new Dimension(150, 50));

        // Create technical details label
        JLabel technicalDetailsLabel = new JLabel("Techincal Details");
        technicalDetailsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        technicalDetailsLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create technical detail labels
        JLabel programmingLanguageLabel = new JLabel("Programming language : Java ");
        programmingLanguageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        JLabel jdkVersionLabel = new JLabel("Required JDK Version : v21+");
        jdkVersionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        JLabel guiFrameworkLabel = new JLabel("GUI Framework : Swing");
        guiFrameworkLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        JLabel lookAndFeelLabel = new JLabel("UI Look and Feel : FlatLaf (by JFormDesigner)");
        lookAndFeelLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        JLabel versionLabel = new JLabel("Current FlowForge version : v1.7");
        versionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        // Create confirm button
        confirmButton = new JButton("Confirm");
        confirmButton.setBackground(new Color(-12693268)); // Original background color
        confirmButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

        // Add components to about panel
        GridBagConstraints aboutGbc = new GridBagConstraints();
        aboutGbc.gridx = 0;
        aboutGbc.gridy = 0;
        aboutGbc.weightx = 1.0;
        aboutGbc.weighty = 1.0;
        aboutGbc.fill = GridBagConstraints.BOTH;
        aboutPanel.add(aboutTextPane, aboutGbc);

        // Add components to licensing panel
        GridBagConstraints licensingGbc = new GridBagConstraints();
        licensingGbc.gridx = 0;
        licensingGbc.gridy = 0;
        licensingGbc.anchor = GridBagConstraints.WEST;
        licensingGbc.insets = new Insets(0, 0, 10, 0);
        lisencingPanel.add(licensingLabel, licensingGbc);

        licensingGbc.gridy = 1;
        licensingGbc.weightx = 1.0;
        licensingGbc.weighty = 1.0;
        licensingGbc.fill = GridBagConstraints.BOTH;
        licensingGbc.insets = new Insets(0, 0, 0, 0);
        lisencingPanel.add(flowForgeIsOpenSourceTextPane, licensingGbc);

        // Add components to main panel
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.gridwidth = 2;
        mainGbc.anchor = GridBagConstraints.WEST;
        mainGbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(copyrightLabel, mainGbc);

        mainGbc.gridy = 1;
        mainGbc.weightx = 1.0;
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(aboutPanel, mainGbc);

        mainGbc.gridy = 2;
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(lisencingPanel, mainGbc);

        mainGbc.gridy = 3;
        mainGbc.gridwidth = 1;
        mainGbc.weightx = 0.0;
        mainGbc.weighty = 0.0;
        mainGbc.fill = GridBagConstraints.NONE;
        mainGbc.anchor = GridBagConstraints.WEST;
        mainGbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(technicalDetailsLabel, mainGbc);

        mainGbc.gridy = 4;
        mainGbc.insets = new Insets(0, 20, 5, 0);
        mainPanel.add(programmingLanguageLabel, mainGbc);

        mainGbc.gridy = 5;
        mainPanel.add(jdkVersionLabel, mainGbc);

        mainGbc.gridy = 6;
        mainPanel.add(guiFrameworkLabel, mainGbc);

        mainGbc.gridy = 7;
        mainPanel.add(lookAndFeelLabel, mainGbc);

        mainGbc.gridy = 8;
        mainGbc.insets = new Insets(0, 20, 20, 0);
        mainPanel.add(versionLabel, mainGbc);

        // Add vertical spacer
        mainGbc.gridy = 9;
        mainGbc.gridx = 1;
        mainGbc.weighty = 1.0;
        mainGbc.fill = GridBagConstraints.VERTICAL;
        mainGbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Box.createVerticalGlue(), mainGbc);

        // Add horizontal spacer
        mainGbc.gridy = 10;
        mainGbc.gridx = 0;
        mainGbc.weightx = 1.0;
        mainGbc.weighty = 0.0;
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(Box.createHorizontalGlue(), mainGbc);
    }

    private void setupLayout() {
        GridBagConstraints rootGbc = new GridBagConstraints();

        // Add title label to root panel
        rootGbc.gridx = 0;
        rootGbc.gridy = 0;
        rootGbc.gridwidth = 3;
        rootGbc.anchor = GridBagConstraints.CENTER;
        rootGbc.insets = new Insets(0, 0, 10, 0);
        rootPanel.add(new JLabel("About FlowForge") {{
            setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
            setHorizontalAlignment(SwingConstants.CENTER);
        }}, rootGbc);

        // Add scroll pane to root panel
        rootGbc.gridy = 1;
        rootGbc.weightx = 1.0;
        rootGbc.weighty = 1.0;
        rootGbc.fill = GridBagConstraints.BOTH;
        rootGbc.insets = new Insets(0, 0, 10, 0);
        rootPanel.add(scrollPane, rootGbc);

        // Add horizontal spacer before button
        rootGbc.gridy = 2;
        rootGbc.gridx = 0;
        rootGbc.gridwidth = 1;
        rootGbc.weightx = 1.0;
        rootGbc.weighty = 0.0;
        rootGbc.fill = GridBagConstraints.HORIZONTAL;
        rootGbc.insets = new Insets(0, 0, 0, 0);
        rootPanel.add(Box.createHorizontalGlue(), rootGbc);

        // Add confirm button to root panel
        rootGbc.gridx = 1;
        rootGbc.weightx = 0.0;
        rootGbc.fill = GridBagConstraints.HORIZONTAL;
        rootGbc.anchor = GridBagConstraints.CENTER;
        rootPanel.add(confirmButton, rootGbc);

        // Add horizontal spacer after button
        rootGbc.gridx = 2;
        rootGbc.weightx = 1.0;
        rootGbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(Box.createHorizontalGlue(), rootGbc);
    }

    private void setupEventHandlers() {
        confirmButton.addActionListener(e -> {
            flowForge.remove(this.getRootPanel());
            flowForge.add(flowForge.startPanel, BorderLayout.CENTER);
            flowForge.menuBar.aboutPanelVisible = false;
            flowForge.revalidate();
            flowForge.repaint();
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}