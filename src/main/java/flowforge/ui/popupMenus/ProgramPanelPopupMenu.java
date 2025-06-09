package flowforge.ui.popupMenus;

import flowforge.ui.panels.ProgramPanel;

import javax.swing.*;

public class ProgramPanelPopupMenu extends JPopupMenu {

    private ProgramPanel programPanel;

    private JMenuItem showGrid, snapToGrid,
            addNode, createInteger, createString, createBoolean, createFloat;

    public ProgramPanelPopupMenu(ProgramPanel programPanel) {
        this.programPanel = programPanel;

        initUI();
        initListeners();
        addComponents();
    }

    private void initUI() {
        showGrid = new JMenuItem("Show Grid");
        snapToGrid = new JMenuItem("Snap to Grid");

        addNode = new JMenuItem("Add node");
        createInteger = new JMenuItem("Create Integer");
        createString = new JMenuItem("Create String");
        createBoolean = new JMenuItem("Create Boolean");
        createFloat = new JMenuItem("Create Float");
    }

    private void initListeners() {
        showGrid.addActionListener(e -> {
            programPanel.showGrid = !programPanel.showGrid;
        });

        snapToGrid.addActionListener(e -> {
            programPanel.snapToGrid = !programPanel.snapToGrid;
        });
    }

    private void addComponents() {
        this.add(showGrid);
        this.add(snapToGrid);
        this.addSeparator();
        this.add(addNode);
        this.add(createInteger);
        this.add(createString);
        this.add(createBoolean);
        this.add(createFloat);
    }

    public void displayMenu(int x, int y, boolean showGrid, boolean snapToGrid) {
        this.show(programPanel, x, y);

        if (showGrid) {
            this.showGrid.setText("Hide Grid");
        } else {
            this.showGrid.setText("Show Grid");
        }

        if (snapToGrid) {
            this.snapToGrid.setText("Snap to Grid ✓");
        } else {
            this.snapToGrid.setText("Snap to Grid");
        }
    }

}
