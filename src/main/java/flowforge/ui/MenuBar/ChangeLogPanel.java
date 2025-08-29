package flowforge.ui.MenuBar;

import flowforge.FlowForge;
import javax.swing.*;
import java.awt.*;

public class ChangeLogPanel {
    private JPanel rootPanel;
    private JScrollPane contentScrollPane;
    private JPanel contentPanel;
    private JButton backButton;
    private JTextArea textPane;
    private FlowForge flowForge;

    public ChangeLogPanel(FlowForge flowForge) {
        this.flowForge = flowForge;

        // Initialize the GUI components
        initializeComponents();
        setupEventHandlers();

        contentScrollPane.getVerticalScrollBar().setUnitIncrement(14);
    }

    private void initializeComponents() {
        // Create root panel with BorderLayout
        rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("ChangeLog");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        titlePanel.add(titleLabel);

        // Remove the content panel since we're adding text area directly to scroll pane

        // Create text area with changelog content
        textPane = new JTextArea();
        textPane.setEditable(false);
        textPane.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        textPane.setLineWrap(true);
        textPane.setWrapStyleWord(true);
        textPane.setText("\nWhat's new in v1.7\n\nNew Features\n• UI for nodes has been changed, nodes are easier to read.\n• Variables can be created on the spot with Dynamic connections\n• Nodes can now be Minimized and Maximized\n• Nodes can now be highlighted, commented\n\nImprovements and Bug Fixes\n• Fixed Float node bugs\n• Fixed bug where dynamic connection did not work with branch nodes\n• IO Buttons now have improved coloring\n• Optimized node rendering\n\n-----------------------------------------------------------\n\nChangelog for v1.6\n\nNew Features\n• Added EvalNode : Evaluates mathematical operations\n  To include variables in EvalNode, use '{VARIABLE_NAME}' \n• Added Auto-update.\n\nImprovements and Bug Fixes\n• Start node cannot be deleted\n• Variables can be renamed and deleted\n• Removed redundant dynamic connection for X-connections\n• Fixed bug where camera didn't move upon entering a new project\n\n-----------------------------------------------------------\n\nChangelog for v1.5\n\nNew Features\n• Step-Execution : Execute nodes in steps and view execution real time\n• Recursion node and Route node\n\nImprovements and Bug Fixes\n• Fixed Bug where upon exiting dynamic connection mode, the popup menu would not disappear\n• Improved performance for Loop nodes\n• Minor UI Improvements\n\n------------------------------------------------------------\n\nChangelog for v1.4\n\nNew Features\n• Added properties section. Users can view connections and other details of selected node\n• Added a exit option to exit out of the program during full screen.\n\nImprovements and Bug Fixes\n• Fixed critical bug where files were not being saved\n\n-------------------------------------------------------------\n\nChangelog for v1.3\n\nNew Features\n• Added Dynamic connections \n• Search popup menu, to add nodes on the fly\n• Added Changelog section in the main menu\n\nImprovements and Bug Fixes\n• Improved Connection UI, Buttons are only in selected state while making connections\n• About section is displayed within the application itself, rather than creating a new Window\n• Fixed bug where buttons selection state was incorrect\n\n-------------------------------------------------------------\n\nChangelog for v1.2\n\nNew Features\n• Added more Project Actions (Save as, open) within new instances\n• Added fullscreen and presentation mode\n• User can now modify and toggle the visibility of UI components\n\nImprovements and Bug Fixes\n• Minor UI changes\n\n-------------------------------------------------------------\n\nChangelog for v1.1\n\n• Fixed critical bug where the user could sometimes not save the program");
        textPane.setCaretPosition(0);
        textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create scroll pane directly with text area
        contentScrollPane = new JScrollPane(textPane);
        contentScrollPane.setBorder(null);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Create back button
        backButton = new JButton("Back");
        backButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

        // Create button panel with spacers for centering
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel buttonCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonCenterPanel.add(backButton);
        buttonPanel.add(buttonCenterPanel, BorderLayout.CENTER);

        // Add components to root panel
        rootPanel.add(titlePanel, BorderLayout.NORTH);
        rootPanel.add(contentScrollPane, BorderLayout.CENTER);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        backButton.setBackground(flowForge.theme);
        backButton.addActionListener(e -> {
            flowForge.remove(this.getRootPanel());
            flowForge.add(flowForge.startPanel, BorderLayout.CENTER);
            flowForge.menuBar.changelogPanelVisible = false;
            flowForge.revalidate();
            flowForge.repaint();
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}