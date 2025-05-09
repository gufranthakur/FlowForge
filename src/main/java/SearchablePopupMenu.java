import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchablePopupMenu extends JFrame {
    private JPanel mainPanel;
    private JPopupMenu popupMenu;
    private JTextField searchField;
    private JComboBox<String> resultComboBox;
    private DefaultComboBoxModel<String> comboBoxModel;

    // Sample data - replace with your actual data source
    private List<String> allItems = new ArrayList<>();

    public SearchablePopupMenu() {
        setTitle("Searchable Popup Menu Demo");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize with sample data
        initSampleData();

        // Create main panel
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createTitledBorder("Click anywhere to show search popup"));

        // Create popup components
        createPopupMenu();

        // Add click listener to the main panel
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Show the popup at the click location
                popupMenu.show(mainPanel, e.getX(), e.getY());
                // Focus on search field when popup appears
                searchField.requestFocus();
            }
        });

        add(mainPanel);
    }

    private void initSampleData() {
        // Add some sample data
        allItems.add("Apple");
        allItems.add("Banana");
        allItems.add("Orange");
        allItems.add("Strawberry");
        allItems.add("Blueberry");
        allItems.add("Pineapple");
        allItems.add("Mango");
        allItems.add("Watermelon");
        allItems.add("Grapes");
        allItems.add("Peach");
    }

    private void createPopupMenu() {
        popupMenu = new JPopupMenu() {
            // Override to prevent auto-closing when clicking inside components
            @Override
            public void setVisible(boolean b) {
                // Only allow programmatic hiding
                if (!b || isVisible()) {
                    super.setVisible(b);
                }
            }
        };

        // Create a custom panel for the popup content
        JPanel popupContent = new JPanel();
        popupContent.setLayout(new BorderLayout(5, 5));
        popupContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Create search field
        searchField = new JTextField(15);
        searchField.setToolTipText("Type to search");

        // Create combo box for results
        comboBoxModel = new DefaultComboBoxModel<>();
        updateComboBoxWithAllItems();
        resultComboBox = new JComboBox<>(comboBoxModel);
        resultComboBox.setPreferredSize(new Dimension(200, resultComboBox.getPreferredSize().height));

        // Add document listener to search field for real-time filtering
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterItems();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterItems();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterItems();
            }
        });

        // Add components to popup content
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        popupContent.add(searchPanel, BorderLayout.NORTH);
        popupContent.add(resultComboBox, BorderLayout.CENTER);

        // Add "Close" button to explicitly close the popup
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> popupMenu.setVisible(false));
        popupContent.add(closeButton, BorderLayout.SOUTH);

        // Add a selection listener to the combo box
        resultComboBox.addActionListener(e -> {
            String selectedItem = (String) resultComboBox.getSelectedItem();
            if (selectedItem != null && !e.getActionCommand().equals("comboBoxEdited")) {
                System.out.println("Selected: " + selectedItem);
                // You can add your own action here
                popupMenu.setVisible(false); // Hide popup after selection
            }
        });

        // Add keyboard listeners to handle common behaviors
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    popupMenu.setVisible(false);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    resultComboBox.requestFocus();
                    if (resultComboBox.getItemCount() > 0) {
                        resultComboBox.setSelectedIndex(0);
                    }
                }
            }
        });

        // Add click listener to close popup when clicking outside
        addPopupCloseListener();

        // Add the custom panel to the popup
        popupMenu.add(popupContent);
    }

    private void addPopupCloseListener() {
        // Add global mouse listener to close popup when clicking outside
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent && popupMenu.isVisible()) {
                MouseEvent mouseEvent = (MouseEvent) event;

                // Only process MOUSE_PRESSED events
                if (mouseEvent.getID() != MouseEvent.MOUSE_PRESSED) {
                    return;
                }

                // Check if click is outside the popup
                Point clickPoint = mouseEvent.getPoint();
                SwingUtilities.convertPointToScreen(clickPoint, mouseEvent.getComponent());

                Rectangle popupBounds = popupMenu.getBounds();
                Point popupLocation = popupMenu.getLocationOnScreen();
                popupBounds.setLocation(popupLocation);

                // If click is outside popup bounds, close the popup
                if (!popupBounds.contains(clickPoint)) {
                    popupMenu.setVisible(false);
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }

    private void filterItems() {
        String searchText = searchField.getText().toLowerCase();

        // Filter items based on search text
        List<String> filteredItems = allItems.stream()
                .filter(item -> item.toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        // Update combo box with filtered items
        updateComboBox(filteredItems);
    }

    private void updateComboBoxWithAllItems() {
        updateComboBox(allItems);
    }

    private void updateComboBox(List<String> items) {
        comboBoxModel.removeAllElements();
        for (String item : items) {
            comboBoxModel.addElement(item);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SearchablePopupMenu demo = new SearchablePopupMenu();
            demo.setVisible(true);
        });
    }
}