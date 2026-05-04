import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class SettingsDialog extends JDialog {

    private String updatedName;
    private boolean darkMode;
    private int fontSize;
    private String accentColor;

    private static final String[] ACCENT_NAMES = {"Default","Indigo", "Teal", "Rose", "Amber"};
    private static final Color[] ACCENT_COLORS = {
    	new Color(17, 3, 33), //Default
        new Color(99, 102, 241),   //Indigo
        new Color(20, 184, 166),   // Teal
        new Color(244, 63, 94),    // Rose
        new Color(245, 158, 11)    // Amber
    };

    public SettingsDialog(JFrame parent, String currentName, boolean currentDarkMode, int currentFontSize, int currentAccentIndex) {
        super(parent, "Settings", true);
        setSize(420, 380);
        setResizable(false);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        // ── Header ───
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(17, 3, 33));
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        JLabel lbl = new JLabel("Settings");
        lbl.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        lbl.setForeground(Color.WHITE);
        header.add(lbl, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ── Body ───
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));
        body.setBackground(Color.WHITE);

        // -- Student Name --
        body.add(sectionLabel("Profile"));
        body.add(Box.createVerticalStrut(6));
        JPanel nameRow = row("Student Name");
        JTextField txtName = new JTextField(currentName);
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 195, 215), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        txtName.setPreferredSize(new Dimension(160, 30));
        nameRow.add(txtName);
        body.add(nameRow);
        body.add(Box.createVerticalStrut(18));

        // -- Appearance --
        body.add(sectionLabel("Appearance"));
        body.add(Box.createVerticalStrut(6));

        // Dark mode toggle (ai helped with this portion)
        
        JPanel darkRow = row("Dark Mode");
        JToggleButton btnDark = new JToggleButton(currentDarkMode ? "ON" : "OFF", currentDarkMode);
        btnDark.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDark.setPreferredSize(new Dimension(60, 28));
        btnDark.setFocusPainted(false);
        btnDark.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDark.setBackground(currentDarkMode ? new Color(99, 102, 241) : new Color(200, 200, 210));
        btnDark.setForeground(Color.WHITE);
        btnDark.setBorderPainted(false);
        btnDark.setOpaque(true);
        btnDark.addItemListener(e -> {
            if (btnDark.isSelected()) {
                btnDark.setText("ON");
                btnDark.setBackground(new Color(99, 102, 241));
            } else {
                btnDark.setText("OFF");
                btnDark.setBackground(new Color(200, 200, 210));
            }
        });
        darkRow.add(btnDark);
        body.add(darkRow);
        body.add(Box.createVerticalStrut(10));

        // Font size
        JPanel fontRow = row("Font Size");
        JComboBox<String> cmbFontSize = new JComboBox<>(new String[]{"12", "14", "16", "18"});
        cmbFontSize.setSelectedItem(String.valueOf(currentFontSize));
        cmbFontSize.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbFontSize.setPreferredSize(new Dimension(160, 30));
        cmbFontSize.setFocusable(false);
        fontRow.add(cmbFontSize);
        body.add(fontRow);
        body.add(Box.createVerticalStrut(10));

        // Accent color
        JPanel accentRow = row("Accent Color");
        JComboBox<String> cmbAccent = new JComboBox<>(ACCENT_NAMES);
        cmbAccent.setSelectedIndex(currentAccentIndex);
        cmbAccent.setRenderer(new AccentRenderer());
        cmbAccent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbAccent.setPreferredSize(new Dimension(160, 30));
        cmbAccent.setFocusable(false);
        accentRow.add(cmbAccent);
        body.add(accentRow);

        JScrollPane scrollPane = new JScrollPane(body);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        add(scrollPane, BorderLayout.CENTER);

        // ── Footer ─────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 14));
        footer.setBackground(new Color(245, 245, 250));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 230)));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = new JButton("Save");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setBackground(new Color(17, 3, 33));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setOpaque(true);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> {
            updatedName = txtName.getText().trim();
            darkMode = btnDark.isSelected();
            fontSize = Integer.parseInt((String) cmbFontSize.getSelectedItem());
            accentColor = (String) cmbAccent.getSelectedItem();
            dispose();
        });

        footer.add(btnCancel);
        footer.add(btnSave);
        add(footer, BorderLayout.SOUTH);
    }

    public String getUpdatedName() {
        return updatedName;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public int getAccentIndex() {
        for (int i = 0; i < ACCENT_NAMES.length; i++) {
            if (ACCENT_NAMES[i].equals(accentColor)) {
                return i;
            }
        }
        return 0;
    }

    // ── Helpers ─────
    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(140, 130, 160));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel row(String labelText) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(new Color(40, 30, 60));
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    // Renders a small color swatch next to each accent name in the dropdown
    private static class AccentRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
            if (index >= 0 && index < ACCENT_COLORS.length) {
                setIcon(new SwatchIcon(ACCENT_COLORS[index]));
            } else {
                for (int i = 0; i < ACCENT_NAMES.length; i++) {
                    if (ACCENT_NAMES[i].equals(value)) {
                        setIcon(new SwatchIcon(ACCENT_COLORS[i]));
                        break;
                    }
                }
            }
            return this;
        }
    }

    private static class SwatchIcon implements javax.swing.Icon {
        private final Color color;
        SwatchIcon(Color c) { this.color = c; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRoundRect(x, y + 1, 14, 14, 4, 4);
        }
        public int getIconWidth()  { return 18; }
        public int getIconHeight() { return 16; }
    }
}

