import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TodoListView extends BaseView {

    private TodoList todoManager;
    private static final String FILE_PATH = "todos.csv";
    private DefaultTableModel tableModel;
    private JTable table;

    @Override
    public JPanel show() {
        todoManager = new TodoList(FILE_PATH);
        todoManager.loadFromFile();

        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("My To-Do List");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 0));
        panel.add(header, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Description", "Due Date", "Priority", "Done"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 5; }
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 5 ? Boolean.class : String.class;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        for (int i = 1; i <= 4; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(new TodoRowRenderer());

        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 5 && row >= 0) {
                int id = (int) tableModel.getValueAt(row, 0);
                todoManager.markComplete(id);
                todoManager.saveToFile();
                table.repaint();
            }
        });

        populateTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buildBottomPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        for (TodoItem item : todoManager.getTodos()) {
            double days = item.daysTillDue();
            String dueDisplay = days < 0
                ? "OVERDUE (" + (int) -days + "d)"
                : item.getDueDate().toString();

            tableModel.addRow(new Object[]{
                item.getId(), item.getTitle(), item.getDescription(),
                dueDisplay, item.getPriority(), item.isCompleted()
            });
        }
    }

    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        bottom.setOpaque(false);

        JButton btnAdd = new JButton("+ Add");
        JButton btnRemove = new JButton("✕ Remove");

        btnAdd.setBackground(new Color(60, 130, 80));
        btnAdd.setForeground(Color.WHITE);
        btnRemove.setBackground(new Color(180, 60, 60));
        btnRemove.setForeground(Color.WHITE);

        styleButton(btnAdd);
        styleButton(btnRemove);

        btnAdd.addActionListener(e -> onAdd());
        btnRemove.addActionListener(e -> onRemove());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(btnAdd);
        actionPanel.add(btnRemove);
        bottom.add(actionPanel, BorderLayout.WEST);

        JLabel sortLabel = new JLabel("Sort:");
        sortLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JComboBox<String> sortCombo = new JComboBox<>(new String[]{
            "Due Date ↑", "Due Date ↓", "Priority ↑", "Priority ↓", "Title A–Z", "Title Z–A"
        });
        sortCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sortCombo.setBackground(Color.WHITE);
        sortCombo.addActionListener(e -> onSort(sortCombo.getSelectedIndex()));

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        sortPanel.setOpaque(false);
        sortPanel.add(sortLabel);
        sortPanel.add(sortCombo);
        bottom.add(sortPanel, BorderLayout.EAST);

        return bottom;
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void onAdd() {
        Color lightBg = Color.WHITE;
        Color lightFg = new Color(30, 30, 30);

        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField priorityField = new JTextField();
        priorityField.setToolTipText("Leave blank to auto-calculate");

        for (JTextField f : new JTextField[]{titleField, descField, priorityField}) {
            f.setBackground(lightBg);
            f.setForeground(lightFg);
            f.setCaretColor(lightFg);
        }

        JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 2100, 1));
        JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(LocalDate.now().getMonthValue(), 1, 12, 1));
        JSpinner daySpinner = new JSpinner(new SpinnerNumberModel(LocalDate.now().getDayOfMonth(), 1, 31, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        for (JSpinner sp : new JSpinner[]{yearSpinner, monthSpinner, daySpinner}) {
            sp.setBackground(lightBg);
            sp.setForeground(lightFg);
            JComponent editor = sp.getEditor();
            editor.setBackground(lightBg);
            editor.setForeground(lightFg);
            if (editor instanceof JSpinner.DefaultEditor) {
                JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
                tf.setBackground(lightBg);
                tf.setForeground(lightFg);
                tf.setCaretColor(lightFg);
            }
        }

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        datePanel.setBackground(lightBg);
        JLabel yearLbl = new JLabel("Year:");
        JLabel monthLbl = new JLabel("Month:");
        JLabel dayLbl = new JLabel("Day:");
        yearLbl.setForeground(lightFg);
        monthLbl.setForeground(lightFg);
        dayLbl.setForeground(lightFg);
        datePanel.add(yearLbl);
        datePanel.add(yearSpinner);
        datePanel.add(Box.createHorizontalStrut(4));
        datePanel.add(monthLbl);
        datePanel.add(monthSpinner);
        datePanel.add(Box.createHorizontalStrut(4));
        datePanel.add(dayLbl);
        datePanel.add(daySpinner);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(lightBg);
        form.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);

        gbc.gridy = 0;
        gbc.insets = new Insets(8, 16, 2, 16);
        JLabel lTitle = new JLabel("Title:");
        lTitle.setFont(labelFont);
        lTitle.setForeground(lightFg);
        form.add(lTitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 16, 8, 16);
        form.add(titleField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(4, 16, 2, 16);
        JLabel lDesc = new JLabel("Description:");
        lDesc.setFont(labelFont);
        lDesc.setForeground(lightFg);
        form.add(lDesc, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 16, 8, 16);
        form.add(descField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(4, 16, 2, 16);
        JLabel lDate = new JLabel("Due Date:");
        lDate.setFont(labelFont);
        lDate.setForeground(lightFg);
        form.add(lDate, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 16, 8, 16);
        form.add(datePanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(4, 16, 2, 16);
        JLabel lPri = new JLabel("Priority (optional):");
        lPri.setFont(labelFont);
        lPri.setForeground(lightFg);
        form.add(lPri, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 16, 8, 16);
        form.add(priorityField, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        form.add(Box.createVerticalGlue(), gbc);

        JDialog addDialog = new JDialog(
            (java.awt.Frame) SwingUtilities.getWindowAncestor(table), "Add To-Do", true);
        addDialog.getContentPane().setBackground(lightBg);
        addDialog.setLayout(new BorderLayout());
        addDialog.add(form, BorderLayout.CENTER);

        boolean[] confirmed = {false};
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        btnRow.setBackground(new Color(238, 238, 242));
        btnRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 218)));

        JButton okBtn = new JButton("Add");
        JButton cancelBtn = new JButton("Cancel");
        okBtn.setBackground(new Color(60, 130, 80));
        okBtn.setForeground(Color.WHITE);
        okBtn.setFocusPainted(false);
        okBtn.setBorderPainted(false);
        okBtn.setOpaque(true);
        cancelBtn.setBackground(new Color(230, 230, 235));
        cancelBtn.setForeground(lightFg);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setOpaque(true);

        okBtn.addActionListener(e -> { confirmed[0] = true; addDialog.dispose(); });
        cancelBtn.addActionListener(e -> { confirmed[0] = false; addDialog.dispose(); });
        btnRow.add(cancelBtn);
        btnRow.add(okBtn);
        addDialog.add(btnRow, BorderLayout.SOUTH);
        addDialog.pack();
        addDialog.setMinimumSize(new Dimension(340, addDialog.getHeight()));
        addDialog.setLocationRelativeTo(table);
        addDialog.setVisible(true);

        if (!confirmed[0]) return;

        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(table, "Title cannot be empty.");
            return;
        }

        LocalDate dueDate;
        try {
            dueDate = LocalDate.of(
                (int) yearSpinner.getValue(),
                (int) monthSpinner.getValue(),
                (int) daySpinner.getValue());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(table, "Invalid date — that day doesn't exist in that month.");
            return;
        }

        String priText = priorityField.getText().trim();
        TodoItem item;
        try {
            item = priText.isEmpty()
                ? new TodoItem(0, title, descField.getText().trim(), dueDate)
                : new TodoItem(0, title, descField.getText().trim(), Integer.parseInt(priText), dueDate);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(table, "Priority must be a whole number.");
            return;
        }

        todoManager.addTodo(item);
        todoManager.saveToFile();
        populateTable();
    }

    private void onRemove() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(table, "Select a to-do to remove.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String title = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(
            table, "Remove \"" + title + "\"?", "Confirm Remove", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            todoManager.removeTodo(id);
            todoManager.saveToFile();
            populateTable();
        }
    }

    private void onSort(int index) {
        switch (index) {
            case 0: todoManager.sortByDueDate(); break;
            case 1: todoManager.sortRevByDueDate(); break;
            case 2: todoManager.sortByPriority(); break;
            case 3: todoManager.sortRevByPriority(); break;
            case 4: todoManager.sortAlphabetically(); break;
            case 5: todoManager.sortRevAlphabetically(); break;
        }
        populateTable();
    }

    private static class TodoRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component cell = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

            if (isSelected) return cell;

            boolean done = (boolean) table.getValueAt(row, 5);
            String dueStr = table.getValueAt(row, 3).toString();

            if (done) {
                cell.setBackground(new Color(220, 245, 220));
                cell.setForeground(new Color(100, 130, 100));
            } else if (dueStr.startsWith("OVERDUE")) {
                cell.setBackground(new Color(255, 220, 220));
                cell.setForeground(new Color(150, 40, 40));
            } else {
                try {
                    long days = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(dueStr));
                    if (days <= 1) {
                        cell.setBackground(new Color(255, 255, 200));
                        cell.setForeground(Color.BLACK);
                    } else {
                        cell.setBackground(Color.WHITE);
                        cell.setForeground(Color.BLACK);
                    }
                } catch (Exception e) {
                    cell.setBackground(Color.WHITE);
                    cell.setForeground(Color.BLACK);
                }
            }
            return cell;
        }
    }
}

