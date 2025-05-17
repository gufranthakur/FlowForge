package flowforge.core.ui.panels;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import flowforge.FlowForge;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class StartPanel extends JPanel implements ComponentListener {

    private final FlowForge flowForge;

    private final JLabel titleLabel;
    private final JLabel mottoLabel;
    public JButton openProjectButton;
    public JButton createProjectButton;

    GradientPaint gradient = new GradientPaint(0, 0, new Color(22, 22, 22),
            1000, 1000, new Color(25, 25, 25));

    int titleWidth = 400, titleHeight = 200, mottoWidth = 400, mottoHeight = 100;

    public StartPanel(FlowForge flowForge) {
        this.flowForge = flowForge;
        this.addComponentListener(this);
        this.setBounds(0, 0, flowForge.getWidth(), flowForge.getHeight());
        this.setLayout(null);
        this.setOpaque(false);

        titleLabel = new JLabel("FlowForge");
        titleLabel.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mottoLabel = new JLabel("Forge Ideas into Visual Code");
        mottoLabel.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.ITALIC, 24));
        mottoLabel.setForeground(Color.WHITE);
        mottoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        createProjectButton = new JButton("New Project");
        createProjectButton.setForeground(Color.WHITE);
        createProjectButton.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.BOLD, 18));

        createProjectButton.setBackground(new Color(34, 169, 6));

        openProjectButton = new JButton("Open Project");
        openProjectButton.setForeground(Color.WHITE);
        openProjectButton.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.BOLD, 18));
        openProjectButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("FlowForge Programs", "flow");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                flowForge.launch();
                flowForge.dataManager.loadProgram(filePath);
                flowForge.projectFilePath = filePath;
            }
        });

        openProjectButton.setBackground(flowForge.theme);

        createProjectButton.addActionListener(e -> {
            flowForge.launch();
        });

        this.add(titleLabel);
        this.add(mottoLabel);
        this.add(openProjectButton);
        this.add(createProjectButton);
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        gradient = new GradientPaint(0, 0, new Color(18, 18, 18), getWidth(), getHeight(), new Color(30, 30, 30));

        g2D.setPaint(gradient);
        g2D.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (titleLabel != null && mottoLabel!=null && openProjectButton!= null) {
            titleLabel.setBounds(getWidth() / 2 - titleWidth / 2,
                    (getHeight() / 2 - titleHeight / 2) - 50,
                    titleWidth, titleHeight);
            mottoLabel.setBounds(getWidth() / 2 - mottoWidth / 2 - 20,
                    titleLabel.getY() + titleHeight / 2,
                    mottoWidth + 40, mottoHeight);
            openProjectButton.setBounds(getWidth() / 2 - titleWidth / 4,
                    mottoLabel.getY() + mottoHeight,
                    titleWidth / 2 - 20, mottoHeight / 2);
            createProjectButton.setBounds(getWidth() / 2 - titleWidth / 4,
                    openProjectButton.getY() + openProjectButton.getHeight() + 20,
                    titleWidth / 2 - 20, mottoHeight / 2);

        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

}