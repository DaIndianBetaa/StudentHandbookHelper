import java.awt.EventQueue;
import java.awt.GridLayout;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.awt.*;

public class GUITest {

	private JFrame frame;
	private JTextField textField;
	private TodoList todoManager;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUITest window = new GUITest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws ParseException 
	 */
	public GUITest() throws ParseException {
		todoManager = new TodoList("todos.csv");
		todoManager.loadFromFile();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws ParseException 
	 */
	private void initialize() throws ParseException {
        frame = new JFrame("Add To-Do");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(6, 2, 10, 10));

        // Fields
        MaskFormatter yearMask = new MaskFormatter("####-##-##");
        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JFormattedTextField dateField = new JFormattedTextField(yearMask);
        dateField.setToolTipText("YYYY-MM-DD");
        JTextField priorityField = new JTextField("");
        priorityField.setToolTipText("Leave blank to auto-calculate");

        // Labels + fields
        frame.getContentPane().add(new JLabel("Title:"));
        frame.getContentPane().add(titleField);
        frame.getContentPane().add(new JLabel("Description:"));
        frame.getContentPane().add(descField);
        frame.getContentPane().add(new JLabel("Due Date:"));
        frame.getContentPane().add(dateField);
        frame.getContentPane().add(new JLabel("Priority (optional):"));
        frame.getContentPane().add(priorityField);

        // Buttons
        JButton addButton = new JButton("Add");
        JButton viewButton = new JButton("View All");
        JButton cancelButton = new JButton("Cancel");
        frame.getContentPane().add(addButton);
        frame.getContentPane().add(viewButton);
        frame.getContentPane().add(cancelButton);

        addButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String desc = descField.getText().trim();
                LocalDate dueDate = LocalDate.parse(dateField.getText().trim());
                String priorityText = priorityField.getText().trim();

                TodoItem item;
                if (priorityText.isEmpty() || priorityText.equals("Leave blank to auto-calculate")) {
                    item = new TodoItem(0, title, desc, dueDate);
                } else {
                    int priority = Integer.parseInt(priorityText);
                    item = new TodoItem(0, title, desc, priority, dueDate);
                }

                todoManager.addTodo(item);
                todoManager.saveToFile();
                JOptionPane.showMessageDialog(frame, "Todo added!");

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date! Use YYYY-MM-DD format.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Priority must be a number.");
            }
        });

        viewButton.addActionListener(e -> {
            ArrayList<TodoItem> todos = todoManager.getTodos();
            if (todos.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No todos yet!");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (TodoItem item : todos) {
                double days = item.daysTillDue();
                String status = item.isCompleted() ? "[DONE]" : "[PENDING]";
                String dueStr = days < 0
                    ? "OVERDUE by " + Math.abs(days) + " day(s)"
                    : "Due in " + days + " day(s)";

                sb.append(status + " " + item.getTitle() + "\n");
                sb.append("  " + item.getDescription() + "\n");
                sb.append("  " + dueStr + " | Priority: " + String.format("%.2f", (double) item.getPriority()) + "\n\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            JOptionPane.showMessageDialog(frame, scrollPane, "All To-Dos", JOptionPane.PLAIN_MESSAGE);
        });

        
        cancelButton.addActionListener(e -> frame.dispose());
	}
}
