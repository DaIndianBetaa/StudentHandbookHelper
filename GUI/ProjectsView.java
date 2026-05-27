import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ProjectsView extends BaseView {

    private DefaultListModel<VolunteerProject> projectsListModel = new DefaultListModel<>();
    private JList<VolunteerProject> list1;
    private JLabel totalHoursValue;
    private JPanel mainPanel;
    private VolunteerProjectManager manager = new VolunteerProjectManager();

    // Dark mode state — set by StudentHelperMain before calling show()
    private boolean darkMode = false;

    public void setDarkMode(boolean dark) {
        this.darkMode = dark;
    }

    // Colors derived from darkMode flag
    private Color bg()         { return darkMode ? new Color(25, 25, 35)   : Color.WHITE; }
    private Color cardBg()     { return darkMode ? new Color(35, 35, 50)   : new Color(248, 248, 252); }
    private Color borderCol()  { return darkMode ? new Color(60, 60, 80)   : new Color(210, 205, 225); }
    private Color fg()         { return darkMode ? new Color(220, 220, 230): new Color(40, 30, 60); }
    private Color subFg()      { return darkMode ? new Color(160, 155, 180): new Color(80, 70, 100); }
    private Color listBg()     { return darkMode ? new Color(30, 30, 45)   : new Color(248, 248, 252); }
    private Color fieldBg()    { return darkMode ? new Color(40, 40, 58)   : Color.WHITE; }
    private Color fieldFg()    { return darkMode ? new Color(220, 220, 230): Color.BLACK; }

    @Override
    public JPanel show() {
        mainPanel = new JPanel(new BorderLayout(16, 0));
        mainPanel.setBackground(bg());

        // ── Left: project list + total hours ─────────────────────────────
        JPanel leftPane = new JPanel(new BorderLayout(0, 8));
        leftPane.setBackground(bg());

        JLabel listHeader = new JLabel("Volunteer Projects");
        listHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        listHeader.setForeground(fg());
        leftPane.add(listHeader, BorderLayout.NORTH);

        list1 = new JList<>(projectsListModel);
        list1.setCellRenderer(new ProjectsListCellRenderer(this));
        list1.setBackground(listBg());
        list1.setForeground(fg());
        // FIX: selection stays light regardless of dark mode so text is always readable
        list1.setSelectionBackground(new Color(200, 210, 240));
        list1.setSelectionForeground(new Color(20, 20, 40));
        list1.setFixedCellHeight(-1);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete Project");
        deleteItem.addActionListener(e -> {
            VolunteerProject selected = list1.getSelectedValue();
            if (selected != null) deleteProject(selected);
        });
        popupMenu.add(deleteItem);
        list1.setComponentPopupMenu(popupMenu);
        list1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                int idx = list1.locationToIndex(e.getPoint());
                if (idx >= 0) list1.setSelectedIndex(idx);
            }
        });

        JScrollPane listScroll = new JScrollPane(list1);
        listScroll.setBorder(BorderFactory.createLineBorder(borderCol(), 1));
        listScroll.getViewport().setBackground(listBg());
        leftPane.add(listScroll, BorderLayout.CENTER);

        // Total hours row
        JPanel hoursPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        hoursPanel.setBackground(bg());
        hoursPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        JLabel hoursLbl = new JLabel("Total Hours:");
        hoursLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hoursLbl.setForeground(fg());
        totalHoursValue = new JLabel("0.0");
        totalHoursValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalHoursValue.setForeground(darkMode ? new Color(100, 160, 255) : new Color(60, 100, 170));
        hoursPanel.add(hoursLbl);
        hoursPanel.add(totalHoursValue);
        leftPane.add(hoursPanel, BorderLayout.SOUTH);

        // ── Right: Add Project form ───────────────────────────────────────
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(cardBg());
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderCol(), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel formHeader = new JLabel("Add New Project");
        formHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        formHeader.setForeground(fg());
        formHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formHeader);
        formCard.add(Box.createVerticalStrut(16));

        JTextField titleField      = addFormField(formCard, "Project Title");
        JTextField dateField       = addFormField(formCard, "Date (YYYY-MM-DD)");
        JTextField hoursField      = addFormField(formCard, "Hours");

        JTextArea descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionArea.setBackground(fieldBg());
        descriptionArea.setForeground(fieldFg());
        descriptionArea.setCaretColor(fieldFg());
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderCol(), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        JLabel descLbl = new JLabel("Description");
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLbl.setForeground(subFg());
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(descLbl);
        formCard.add(Box.createVerticalStrut(4));
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        descScroll.getViewport().setBackground(fieldBg());
        descScroll.setBorder(BorderFactory.createLineBorder(borderCol(), 1));
        formCard.add(descScroll);
        formCard.add(Box.createVerticalStrut(16));

        JButton addProjectButton = new JButton("+ Add Project");
        addProjectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addProjectButton.setBackground(new Color(60, 130, 80));
        addProjectButton.setForeground(Color.WHITE);
        addProjectButton.setFocusPainted(false);
        addProjectButton.setBorderPainted(false);
        addProjectButton.setOpaque(true);
        addProjectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addProjectButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        addProjectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addProjectButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        addProjectButton.addActionListener(e -> {
            String title   = titleField.getText().trim();
            String dateStr = dateField.getText().trim();
            String hoursStr = hoursField.getText().trim();
            String desc    = descriptionArea.getText().trim();

            if (title.isEmpty()) {
                showDialog("Title cannot be empty.");
                return;
            }
            LocalDate date;
            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException ex) {
                showDialog("Invalid date. Use YYYY-MM-DD format.");
                return;
            }
            float hours;
            try {
                hours = Float.parseFloat(hoursStr);
                if (hours < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showDialog("Hours must be a positive number.");
                return;
            }

            manager.AddProject(title, desc, date, hours);
            manager.saveJsonData();
            titleField.setText("");
            dateField.setText("");
            hoursField.setText("");
            descriptionArea.setText("");
            refreshList();
        });

        formCard.add(addProjectButton);

        mainPanel.add(leftPane, BorderLayout.CENTER);
        mainPanel.add(formCard, BorderLayout.EAST);

        loadData();
        return mainPanel;
    }

    /**
     * Re-applies dark/light colors to the already-built panel.
     * Call this after SwingUtilities.updateComponentTreeUI() to undo UIManager overrides.
     * Safe to call if show() hasn't been called yet (mainPanel will be null).
     */
    public void applyTheme() {
        if (mainPanel == null) return;
        forceColors(mainPanel, bg(), fg(), fieldBg(), fieldFg(), borderCol(), cardBg());
        // Also refresh list selection colors since updateComponentTreeUI resets them
        if (list1 != null) {
            list1.setBackground(listBg());
            list1.setForeground(fg());
            list1.setSelectionBackground(new Color(200, 210, 240));
            list1.setSelectionForeground(new Color(20, 20, 40));
        }
        if (totalHoursValue != null) {
            totalHoursValue.setForeground(darkMode ? new Color(100, 160, 255) : new Color(60, 100, 170));
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Recursively applies correct colors to every component in the tree.
     * Skips the green Add button and the list (handled by cell renderer).
     */
    private void forceColors(Container root, Color panelBg, Color panelFg,
                              Color inputBg, Color inputFg, Color border, Color cardBg) {
        for (Component c : root.getComponents()) {
            // Keep the green Add button
            if (c instanceof JButton
                    && ((JButton) c).getBackground() != null
                    && ((JButton) c).getBackground().getGreen() > 100
                    && ((JButton) c).getBackground().getRed() < 120) {
                continue;
            }
            // Let the cell renderer handle list row colors
            if (c instanceof JList) continue;

            if (c instanceof JTextField || c instanceof JTextArea) {
                c.setBackground(inputBg);
                c.setForeground(inputFg);
                if (c instanceof JTextField) ((JTextField) c).setCaretColor(inputFg);
                if (c instanceof JTextArea)  ((JTextArea)  c).setCaretColor(inputFg);
            } else if (c instanceof JScrollPane) {
                c.setBackground(inputBg);
                ((JScrollPane) c).getViewport().setBackground(inputBg);
            } else if (c instanceof JLabel) {
                if (c == totalHoursValue) continue; // keep blue accent
                c.setForeground(panelFg);
                c.setBackground(panelBg);
            } else {
                c.setBackground(panelBg);
                c.setForeground(panelFg);
            }

            if (c instanceof Container) {
                forceColors((Container) c, panelBg, panelFg, inputBg, inputFg, border, cardBg);
            }
        }
    }

    // Always shows dialogs with a light background so they are readable regardless of dark mode
    private void showDialog(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE);
        JDialog dialog = pane.createDialog(mainPanel, "Notice");
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setVisible(true);
    }

    private JTextField addFormField(JPanel parent, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(subFg());
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(fieldBg());
        field.setForeground(fieldFg());
        field.setCaretColor(fieldFg());
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderCol(), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        parent.add(lbl);
        parent.add(Box.createVerticalStrut(4));
        parent.add(field);
        parent.add(Box.createVerticalStrut(12));
        return field;
    }

    private void loadData() {
        manager.loadJsonData();
        refreshList();
    }

    private void refreshList() {
        projectsListModel.clear();
        for (VolunteerProject p : manager.getVolunteerProjects()) {
            projectsListModel.addElement(p);
        }
        totalHoursValue.setText(String.format("%.1f", manager.getTotalHours()));
    }

    public void deleteProject(VolunteerProject project) {
        // Confirm dialog always in light mode for readability
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        int confirm = JOptionPane.showConfirmDialog(
            mainPanel,
            "Delete \"" + project.getTitle() + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        manager.getVolunteerProjects().remove(project);
        manager.saveJsonData();
        refreshList();
    }

    // Expose darkMode so ProjectsListCellRenderer can use it
    public boolean isDarkMode() { return darkMode; }
}