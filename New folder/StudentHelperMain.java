import javax.swing.*;
import java.awt.*;

public class StudentHelperMain extends JFrame {

    private static Color LEFT_NAV_PANEL_BGCOLOR = new Color(220, 220, 220);
    private JPanel rightPanel;
    private JLabel welcomeLabel;
    private String studentName = "Student";

    private BaseView projectsView = new ProjectsView();
    private BaseView gradesView = new GradesView();
    private BaseView assignmentsView = new AssignmentsView();

    public StudentHelperMain() {
        setTitle("Student Helper Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createLeftPanel(), BorderLayout.WEST);
        add(createRightContainer(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(170, 600));
        left.setBackground(LEFT_NAV_PANEL_BGCOLOR);
        left.setLayout(new BorderLayout());

        // --- STUDENT ICON AT TOP ---
        JLabel icon = new JLabel();
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0)); // spacing

        // Load and scale icon
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("icon-person.png"));
        Image scaled = rawIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        icon.setIcon(new ImageIcon(scaled));

        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(LEFT_NAV_PANEL_BGCOLOR);
        iconPanel.add(icon, BorderLayout.CENTER);

        // --- NAVIGATION LABELS ---
        NavLabel lblProjects = new NavLabel("Projects");
        NavLabel lblGrades = new NavLabel("Grades");
        NavLabel lblAssignments = new NavLabel("Assignments");
        lblProjects.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        lblGrades.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        lblAssignments.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(3, 1, 0, 0));
        navPanel.setBackground(LEFT_NAV_PANEL_BGCOLOR);
        navPanel.add(lblProjects);
        navPanel.add(lblGrades);
        navPanel.add(lblAssignments);

        JPanel navWrapper = new JPanel(new BorderLayout());
        navWrapper.setBackground(LEFT_NAV_PANEL_BGCOLOR);
        navWrapper.setBorder(BorderFactory.createEmptyBorder(85, 30, 0, 0)); // top padding
        navWrapper.add(navPanel, BorderLayout.NORTH);

        // --- SETTINGS LABEL AT BOTTOM ---
        NavLabel lblSettings = new NavLabel("Settings");
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(LEFT_NAV_PANEL_BGCOLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 40, 0));
        bottomPanel.add(lblSettings, BorderLayout.NORTH);

        // --- ASSEMBLE LEFT PANEL ---
        left.add(iconPanel, BorderLayout.NORTH);
        left.add(navWrapper, BorderLayout.CENTER);
        left.add(bottomPanel, BorderLayout.SOUTH);

        // Mouse click handlers
        lblProjects.addMouseListener(new SimpleClick(() -> showView(projectsView)));
        lblGrades.addMouseListener(new SimpleClick(() -> showView(gradesView)));
        lblAssignments.addMouseListener(new SimpleClick(() -> showView(assignmentsView)));
        lblSettings.addMouseListener(new SimpleClick(this::openSettings));

        return left;
    }

    private JPanel createRightContainer() {
        JPanel container = new JPanel(new BorderLayout());

        // Top welcome label
        welcomeLabel = new JLabel("Welcome back, " + studentName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 0));

        // Center panel where Projects, Grades or Assignments view appears
        rightPanel = new JPanel(new BorderLayout());

        // FOOTER PANEL (bottom)
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0)); // spacing from bottom

        JLabel aclLabel = new JLabel("Academy of Loudoun");
        aclLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        aclLabel.setForeground(new Color(100, 100, 100));

        JLabel csProjLabel = new JLabel("Computer Science Project");
        csProjLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        csProjLabel.setForeground(new Color(120, 120, 120));

        aclLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        csProjLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(aclLabel);
        footer.add(csProjLabel);

        // Add everything to container
        container.add(welcomeLabel, BorderLayout.NORTH);
        container.add(rightPanel, BorderLayout.CENTER);
        container.add(footer, BorderLayout.SOUTH);

        return container;
    }

    private void showView(BaseView view) {
        rightPanel.removeAll();

        /* --- old version
        // No rounded-corner surrounding the projects/grades/assignment panels
        rightPanel.add(view.show(), BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
         */

        // Wrap the ProjectsView/GradesView/AssignmentsView panel with a rounded-corner
        // panel so that it appears inside a rounded rectangle
        JPanel viewContent = view.show();  // the ProjectsView / GradesView / AssignmentsView panel
        RoundedPanel wrapper = new RoundedPanel(15);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // padding inside the rounded box
        wrapper.add(viewContent, BorderLayout.CENTER);

        rightPanel.add(wrapper, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private void openSettings() {
        SettingsDialog dialog = new SettingsDialog(this, studentName);
        dialog.setVisible(true);

        if (dialog.getUpdatedName() != null) {
            studentName = dialog.getUpdatedName();
            welcomeLabel.setText("Welcome back, " + studentName + "!");
        }
    }

    public static void main(String[] args) {
        // Set the default font for the following UI components so as not to keep
        //  setting for every instance
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Menu.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("List.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));

        new StudentHelperMain();
    }
}