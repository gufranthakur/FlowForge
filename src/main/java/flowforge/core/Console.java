package flowforge.core;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import flowforge.FlowForge;
import flowforge.nodes.Node;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Console {
    private JPanel rootPanel;
    private JPanel toolbar;
    private JButton closeButton;
    private JScrollPane scrollPane;
    private JTextPane consoleTextPane;
    private JButton moreButton;

    private FlowForge flowForge;

    private JPopupMenu popupMenu;
    private JMenuItem maximise, dockDown, dockRight, newWindow, increaseFontSize, decreaseFontSize;

    private boolean isMaximised = false;
    private int fontSize = 14;

    private boolean instanceCreated = false;
    private String currentDockPosition = "SOUTH";
    private int preferredHeight = 250;
    private int preferredWidth = 400;

    public Console(FlowForge flowForge) {
        this.flowForge = flowForge;

        toolbar.setBackground(flowForge.getBackground().darker());
        closeButton.setBackground(toolbar.getBackground());
        moreButton.setBackground(toolbar.getBackground());
        consoleTextPane.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, fontSize));

        initPopupMenu();
        initListeners();
    }

    private void initListeners() {
        closeButton.addActionListener(e -> rootPanel.setVisible(false));
        moreButton.addActionListener(e -> showPopupMenu());

        maximise.addActionListener(e -> {
            if (!isMaximised) {
                repositionConsole("CENTER");
                maximise.setText("Minimise");
            } else {
                repositionConsole(currentDockPosition == "EAST" ? "EAST" : "SOUTH");
                maximise.setText("Maximise");
            }
            isMaximised = !isMaximised;
        });

        dockDown.addActionListener(e -> {
            repositionConsole("SOUTH");
            isMaximised = false;
            maximise.setText("Maximise");
        });

        dockRight.addActionListener(e -> {
            repositionConsole("EAST");
            isMaximised = false;
            maximise.setText("Maximise");
        });

        newWindow.addActionListener(e -> {

            if (instanceCreated) {
                JOptionPane.showMessageDialog(null, "New instance cannot be opened in another existing instance",
                        "Console Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFrame frame = new JFrame("Console");
            frame.setSize(900, 600);
            frame.add(rootPanel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

            flowForge.revalidate();
            flowForge.repaint();

            instanceCreated = true;

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    flowForge.consoleOverlayPanel.add(rootPanel);
                    flowForge.repaint();
                    flowForge.revalidate();
                    repositionConsole(currentDockPosition);

                    instanceCreated = false;
                }
            });
        });

        increaseFontSize.addActionListener(e -> {
            fontSize += 2;
            changeFontSize(consoleTextPane, fontSize);
        });

        decreaseFontSize.addActionListener(e -> {
            fontSize -= 2;
            changeFontSize(consoleTextPane, fontSize);
        });
    }

    private void initPopupMenu() {
        popupMenu = new JPopupMenu();

        maximise = new JMenuItem("Maximize");
        dockDown = new JMenuItem("Dock Down");
        dockRight = new JMenuItem("Dock Right");
        newWindow = new JMenuItem("Open in new Window");

        increaseFontSize = new JMenuItem("Increase Font Size");
        decreaseFontSize = new JMenuItem("Decrease Font Size");

        popupMenu.add(maximise);
        popupMenu.add(dockDown);
        popupMenu.add(dockRight);
        popupMenu.add(newWindow);
        popupMenu.addSeparator();
        popupMenu.add(increaseFontSize);
        popupMenu.add(decreaseFontSize);
    }

    private void repositionConsole(String position) {
        currentDockPosition = position;

        switch (position) {
            case "SOUTH":
                rootPanel.setBounds(0, flowForge.getHeight() - preferredHeight,
                        flowForge.getWidth(), preferredHeight);
                break;
            case "EAST":
                rootPanel.setBounds(flowForge.getWidth() - preferredWidth, 0,
                        preferredWidth, flowForge.getHeight());
                break;
            case "CENTER":
                rootPanel.setBounds(0, 0, flowForge.getWidth(), flowForge.getHeight());
                break;
        }

        flowForge.consoleOverlayPanel.revalidate();
        flowForge.consoleOverlayPanel.repaint();
    }

    private void showPopupMenu() {
        popupMenu.show(moreButton, 0, moreButton.getHeight());
    }

    public void print(String value) {
        consoleTextPane.setText(consoleTextPane.getText() + value + "\n");
        consoleTextPane.setCaretPosition(consoleTextPane.getDocument().getLength());
    }

    public void clear() {
        consoleTextPane.setText("");
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void throwError(String error, Node node) {
        consoleTextPane.setForeground(Color.RED);
        print("Error at Node : " + node.getTitle() + " : " + error);
    }

    public void printSaveStatement() {
        clear();
        print("Project saved succesfully at location : \n" + flowForge.projectFilePath);
    }

    private void changeFontSize(JTextPane textPane, int fontSize) {
        StyledDocument doc = textPane.getStyledDocument();
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontSize(attr, fontSize);
        doc.setCharacterAttributes(0, doc.getLength(), attr, false);
    }

    public String getCurrentDockPosition() {
        return currentDockPosition;
    }

    public int getPreferredHeight() {
        return preferredHeight;
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }
}