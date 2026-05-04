import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

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
            public boolean isCellEditable(int row, int column) {

                return column == 5;
            }

            @Override
            public Class<?> getColumnClass(int column) {

                if (column == 5) {
                    return Boolean.class;
                }
                return String.class;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        for (int i = 1; i <= 4; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new TodoRowRenderer());
        }

        tableModel.addTableModelListener(e -> {
            int row    = e.getFirstRow();
            int column = e.getColumn();
            if (column == 5) {

                boolean isDone = (Boolean) tableModel.getValueAt(row, 5);
                int id         = (int) tableModel.getValueAt(row, 0);
                if (isDone) {
                    todoManager.markComplete(id);
                }
                todoManager.saveToFile();

                table.repaint();
            }
        });

        populateTable();

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buildBottomPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        for (TodoItem item : todoManager.getTodos()) {
            double days = item.daysTillDue();

            String dueDisplay;
            if (days < 0) {

                dueDisplay = "OVERDUE (" + (int) -days + "d)";
            } else {
                dueDisplay = item.getDueDate().toString();
            }

            tableModel.addRow(new Object[]{
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                dueDisplay,
                item.getPriority(),
                item.isCompleted()   

            });
        }
    }

    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JButton btnAdd = new JButton("+ Add");
        JButton btnRemove = new JButton("X Remove");

        btnAdd.setBackground(new Color(60, 130, 80));
        btnAdd.setForeground(Color.WHITE);

        btnRemove.setBackground(new Color(180, 60, 60));
        btnRemove.setForeground(Color.WHITE);

        styleButton(btnAdd);
        styleButton(btnRemove);

        btnAdd.addActionListener(e -> onAdd());
        btnRemove.addActionListener(e -> onRemove());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionPanel.add(btnAdd);
        actionPanel.add(btnRemove);
        bottom.add(actionPanel, BorderLayout.WEST);

        JLabel sortLabel = new JLabel("Sort:");
        sortLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JComboBox<String> sortCombo = new JComboBox<>(new String[]{
            "Due Date ↑", "Due Date ↓", "Priority ↑", "Priority ↓", "Title A–Z", "Title Z–A"
        });
        sortCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sortCombo.addActionListener(e -> onSort(sortCombo.getSelectedIndex()));

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
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
    }

    private void onAdd() {
        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField priorityField = new JTextField();
        priorityField.setToolTipText("Leave blank to auto-calculate");

        JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(
                LocalDate.now().getYear(), 2000, 2100, 1));
        JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(
                LocalDate.now().getMonthValue(), 1, 12, 1));
        JSpinner daySpinner = new JSpinner(new SpinnerNumberModel(
                LocalDate.now().getDayOfMonth(), 1, 31, 1));

        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        datePanel.add(new JLabel("Year:"));
        datePanel.add(yearSpinner);
        datePanel.add(new JLabel("Month:"));
        datePanel.add(monthSpinner);
        datePanel.add(new JLabel("Day:"));
        datePanel.add(daySpinner);

        int result = JOptionPane.showConfirmDialog(table, new Object[]{
            "Title:", titleField,
            "Description:", descField,
            "Due Date:", datePanel,
            "Priority (optional):", priorityField
        }, "Add To-Do", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(table, "Title cannot be empty.");
            return;
        }

        int year = (int) yearSpinner.getValue();
        int month = (int) monthSpinner.getValue();
        int day = (int) daySpinner.getValue();

        LocalDate dueDate;
        try {
            dueDate = LocalDate.of(year, month, day);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(table, "Invalid date — that day doesn't exist in that month.");
            return;
        }

        String priText = priorityField.getText().trim();
        TodoItem item;
        try {
            if (priText.isEmpty()) {

                item = new TodoItem(0, title, descField.getText().trim(), dueDate);
            } else {
                item = new TodoItem(0, title, descField.getText().trim(), Integer.parseInt(priText), dueDate);
            }
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
        if (index == 0) {
        	todoManager.sortByDueDate();
        }
        else if (index == 1) {
        	todoManager.sortRevByDueDate();
        }
        else if (index == 2) {
        	todoManager.sortByPriority();
        }
        else if (index == 3) {
        	todoManager.sortRevByPriority();
        }
        else if (index == 4) {
        	todoManager.sortAlphabetically();
        }
        else if (index == 5) {
        	todoManager.sortRevAlphabetically();
        }
        populateTable();
    }

    private static class TodoRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
            	return cell;
            }

            boolean done = (boolean) table.getValueAt(row, 5);
            String dueStr = table.getValueAt(row, 3).toString();

            if (done) {
                cell.setBackground(new Color(220, 245, 220)); 

            } else if (dueStr.startsWith("OVERDUE")) {
                cell.setBackground(new Color(255, 220, 220)); 

            } else {
                try {
                    long days = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(dueStr));
                    if (days <= 1) {
                        cell.setBackground(new Color(255, 255, 200)); 

                    } else {
                        cell.setBackground(Color.WHITE);
                    }
                } catch (Exception e) {
                    cell.setBackground(Color.WHITE);
                }
            }

            return cell;
        }
    }
}

