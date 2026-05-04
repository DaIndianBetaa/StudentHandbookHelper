import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// credit : google, my mom, & copilot for helping create the new JFrame uses / features like JPanel use. overall code troubleshooting.
@SuppressWarnings("serial")
public class StudentHelperMain extends JFrame {

    private static final Color LEFT_NAV_PANEL_BGCOLOR = new Color(17, 3, 33);

    private JPanel leftPanel;
    private JPanel iconPanel;
    private JPanel navPanel;
    private JPanel navWrapper;
    private JPanel bottomPanel;
    private JPanel rightContainer;
    private JPanel rightPanel;
    private JPanel footer;

    private JLabel welcomeLabel;
    private JLabel aclLabel;
    private JLabel csProjLabel;
    private NavLabel lblTodoList;
    private NavLabel lblGrades;
    private NavLabel lblAssignments;
    private NavLabel lblSettings;

    private String studentName = "Student";
    private static final String preferenceFilePath = "preferences.txt";

    //setting mods
    private boolean darkMode = false;
    private int fontSize = 14;
    private int accentIndex = 0;

    private BaseView projectsView = new TodoListView();
    private BaseView gradesView = new GradesView();
    private BaseView assignmentsView = new AssignmentsView();

    public StudentHelperMain() {
    	loadPreferences();
    	
        setTitle("Student Helper Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createLeftPanel(), BorderLayout.WEST);
        add(createRightContainer(), BorderLayout.CENTER);

        applySettings();
        
        setVisible(true);
        
        savePreferences();

        SwingUtilities.invokeLater(() -> {
            StartupReminderDialog.show(this);
        });
    }

    //ai helped with preferences file key matching
    private void loadPreferences() {
        File file = new File(preferenceFilePath);
        if (!file.exists()) {
        	return;
        }


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    String key = parts[0].trim();
                    String value = parts[1].trim();


                    if (key.equals("name")) studentName = value;
                    else if (key.equals("darkMode")) darkMode = Boolean.parseBoolean(value);
                    else if (key.equals("fontSize")) fontSize = Integer.parseInt(value);
                    else if (key.equals("accentIndex")) accentIndex = Integer.parseInt(value);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading preferences. Using defaults.");
        }
    }


    private void savePreferences() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(preferenceFilePath))) {
            writer.write("name:" + studentName);
            writer.newLine();
            writer.write("darkMode:" + darkMode);
            writer.newLine();
            writer.write("fontSize:" + fontSize);
            writer.newLine();
            writer.write("accentIndex:" + accentIndex);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing preferences: " + e.getMessage());
        }
    }


    
    private JPanel createLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(200, 600));
        leftPanel.setBackground(LEFT_NAV_PANEL_BGCOLOR);
        leftPanel.setLayout(new BorderLayout());

        JLabel icon = new JLabel(); 
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        
        java.net.URL iconUrl = getClass().getResource("icon-person.png");
        if (iconUrl != null) {
            ImageIcon rawIcon = new ImageIcon(iconUrl);
            Image scaled = rawIcon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            icon.setIcon(new ImageIcon(scaled));
        } else {
            icon.setText("No Icon");
            icon.setForeground(Color.WHITE);
        }

        iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(LEFT_NAV_PANEL_BGCOLOR);
        iconPanel.add(icon, BorderLayout.CENTER);

        lblTodoList = new NavLabel("Todo List"); 
        lblGrades = new NavLabel("Grades");      
        lblAssignments = new NavLabel("Volunteer Logger");
        lblTodoList.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        lblGrades.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        lblAssignments.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(3, 1, 0, 0));
        navPanel.setBackground(LEFT_NAV_PANEL_BGCOLOR);
        navPanel.add(lblTodoList);
        navPanel.add(lblGrades);
        navPanel.add(lblAssignments);

        navWrapper = new JPanel(new BorderLayout());
        navWrapper.setBackground(LEFT_NAV_PANEL_BGCOLOR);
        navWrapper.setBorder(BorderFactory.createEmptyBorder(85, 30, 0, 0));
        navWrapper.add(navPanel, BorderLayout.NORTH);                        

        lblSettings = new NavLabel("Settings");
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(LEFT_NAV_PANEL_BGCOLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 40, 0));
        bottomPanel.add(lblSettings, BorderLayout.NORTH);
        
        lblTodoList.setForeground(new Color(248,245,252));
        lblGrades.setForeground(new Color(248,245,252));
        lblAssignments.setForeground(new Color(248,245,252));
        lblSettings.setForeground(new Color(248,245,252));

        leftPanel.add(iconPanel, BorderLayout.NORTH);
        leftPanel.add(navWrapper, BorderLayout.CENTER);
        leftPanel.add(bottomPanel, BorderLayout.SOUTH);

        lblTodoList.addMouseListener(new SimpleClick(() -> showView(projectsView))); 
        lblGrades.addMouseListener(new SimpleClick(() -> showView(gradesView)));     
        lblAssignments.addMouseListener(new SimpleClick(() -> showView(assignmentsView)));
        lblSettings.addMouseListener(new SimpleClick(this::openSettings));

        return leftPanel;
    }

    private JPanel createRightContainer() {
        rightContainer = new JPanel(new BorderLayout());

        welcomeLabel = new JLabel("Welcome back, " + studentName + "!");
        welcomeLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, Math.max(10, fontSize - 2)));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 0));

        rightPanel = new JPanel(new BorderLayout());

        footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        aclLabel = new JLabel("Academy of Loudoun");
        aclLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        aclLabel.setForeground(new Color(100, 100, 100));

        csProjLabel = new JLabel("Computer Science Project");
        csProjLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        csProjLabel.setForeground(new Color(120, 120, 120));

        aclLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        csProjLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(aclLabel);
        footer.add(csProjLabel);

        rightContainer.add(welcomeLabel, BorderLayout.NORTH);
        rightContainer.add(rightPanel, BorderLayout.CENTER);
        rightContainer.add(footer, BorderLayout.SOUTH);

        return rightContainer;
    }

    private void showView(BaseView view) {
        rightPanel.removeAll();

        JPanel viewContent = view.show();
        RoundedPanel wrapper = new RoundedPanel(15);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        wrapper.add(viewContent, BorderLayout.CENTER);

        rightPanel.add(wrapper, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }


    private void openSettings() {
        SettingsDialog dialog = new SettingsDialog(this, studentName, darkMode, fontSize, accentIndex);
        dialog.setVisible(true);

        if (dialog.getUpdatedName() != null) {
            studentName = dialog.getUpdatedName();
            welcomeLabel.setText("Welcome back, " + studentName + "!");
        }

        darkMode = dialog.isDarkMode();
        fontSize = dialog.getFontSize();
        accentIndex = dialog.getAccentIndex();
        
        savePreferences();
        applySettings();
    }

    private static final Color[] ACCENT_COLORS = {
    	new Color(17, 3, 33),
        new Color(99, 102, 241),
        new Color(20, 184, 166),
        new Color(244, 63, 94),
        new Color(245, 158, 11)
    };

    private void applySettings() {
        Color navBg, rightBg, rightFg, footerFg;
        Color accent = ACCENT_COLORS[accentIndex];

        if (darkMode) {
            // Sidebar follows selected accent color (darkened for contrast)
            navBg = accent.darker().darker();
            rightBg = new Color(25, 25, 35);
            rightFg = new Color(240, 240, 240);
            footerFg = new Color(160, 160, 160);
        } else {
            // Sidebar follows selected accent color
            navBg = accent;
            rightBg = Color.WHITE;
            rightFg = new Color(40, 30, 60);
            footerFg = new Color(100, 100, 100);
        }

        UIManager.put("Panel.background", rightBg);
        UIManager.put("Label.foreground", rightFg);
        UIManager.put("ScrollPane.background", rightBg);
        UIManager.put("Viewport.background", rightBg);

        Font baseFont = new Font("SansSerif", Font.BOLD, fontSize);
        UIManager.put("Label.font", baseFont);
        UIManager.put("Button.font", baseFont);
        UIManager.put("TextField.font", baseFont);
        UIManager.put("Table.font", baseFont);
        UIManager.put("TableHeader.font", new Font("SansSerif", Font.BOLD, fontSize));
        UIManager.put("List.font", baseFont);

        SwingUtilities.updateComponentTreeUI(this);
        if (projectsView != null) SwingUtilities.updateComponentTreeUI(projectsView.show());
        if (gradesView != null) SwingUtilities.updateComponentTreeUI(gradesView.show());
        if (assignmentsView != null) SwingUtilities.updateComponentTreeUI(assignmentsView.show());

        leftPanel.setBackground(navBg);
        iconPanel.setBackground(navBg);
        navPanel.setBackground(navBg);
        navWrapper.setBackground(navBg);
        bottomPanel.setBackground(navBg);

        rightContainer.setBackground(rightBg);
        rightPanel.setBackground(rightBg);
        footer.setBackground(rightBg);

        welcomeLabel.setForeground(rightFg);
        aclLabel.setForeground(footerFg);
        csProjLabel.setForeground(footerFg);

        Color navFg = new Color(240, 240, 240); 
        lblTodoList.setForeground(navFg);
        lblGrades.setForeground(navFg);
        lblAssignments.setForeground(navFg);
        lblSettings.setForeground(navFg);

        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, fontSize + 4));
        aclLabel.setFont(new Font("SansSerif", Font.BOLD, Math.max(10, fontSize - 2)));
        csProjLabel.setFont(new Font("SansSerif", Font.PLAIN, Math.max(10, fontSize - 2)));

        Font navFont = new Font("SansSerif", Font.BOLD, fontSize);
        lblTodoList.setFont(navFont);
        lblGrades.setFont(navFont);
        lblAssignments.setFont(navFont);
        lblSettings.setFont(navFont);

        revalidate();
        repaint();
    }

    private Font customFont(String font, float size) {
    	Font newFont = null;
    	try {
            // 1. Load the font file
            File fontFile = new File(font);
            newFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            // 2. Register the font with the Graphics Environment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(newFont);

            // 3. Use the font (set the size, as createFont defaults to 1pt)
            newFont = newFont.deriveFont(size); 

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Fallback to a logical font if loading fails
            return new Font("SansSerif", Font.PLAIN, (int)size);
        }
    	return newFont;
    }


    public static void main(String[] args) {
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Menu.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("List.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));

        SwingUtilities.invokeLater(() -> { //ai method to prevent crashing
            new StudentHelperMain();
        });
    }
}

