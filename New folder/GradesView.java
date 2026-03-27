import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.*;
import java.nio.file.*;
import java.util.*;


public class GradesView extends BaseView {


   private JTable table;
   private DefaultTableModel model;
   private JLabel gpaTitleLabel;
   private JLabel gpaValueLabel;
   private JButton calcButton;
   private JButton saveButton;
   private JButton undoButton;


   private boolean hasChanges = false;
   private String jsonFile = "grades.jsn";


   private static final String[] GRADES = {
           "", "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"
   };


   @Override
   public JPanel show() {
       JPanel panel = new JPanel(new BorderLayout());
       panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


       model = new DefaultTableModel(new Object[]{"Course Title", "Mark/Grade", "Credits"}, 0) {
           @Override
           // This will return false for fi
           public boolean isCellEditable(int row, int col) {
               return col != 0; // Course Title not editable
           }
       };


       loadJsonData();


       table = new JTable(model);
       table.setRowHeight(28);


       // Grade column uses JComboBox
       JComboBox<String> gradeCombo = new JComboBox<>(GRADES);
       table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(gradeCombo));


       // Credits column: whole numbers only
       JTextField creditField = new JTextField();
       creditField.setDocument(new NumericDocument());
       table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(creditField));


       // Detect changes
       model.addTableModelListener(e -> {
           hasChanges = true;
           calcButton.setEnabled(true);
           saveButton.setEnabled(true);
           undoButton.setEnabled(true);
       });


       JScrollPane scroll = new JScrollPane(table);


       // GPA panel
       JPanel gpaPanel = new JPanel();
       gpaPanel.setLayout(new BoxLayout(gpaPanel, BoxLayout.Y_AXIS));
       gpaPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));


       gpaTitleLabel = new JLabel("Unweighted GPA");
       gpaTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
       gpaTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


       gpaValueLabel = new JLabel(String.format("%.2f", calculateGPA()));
       gpaValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 45));
       gpaValueLabel.setForeground(Color.GRAY);
       gpaValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


       calcButton = new JButton("Calculate GPA");
       calcButton.setAlignmentX(Component.CENTER_ALIGNMENT);
       calcButton.setEnabled(false);
       calcButton.addActionListener(e -> updateGPA());


       //gpaPanel.add(gpaLabel);
       gpaPanel.add(gpaTitleLabel);
       gpaPanel.add(Box.createVerticalStrut(5));
       gpaPanel.add(gpaValueLabel);
       gpaPanel.add(Box.createVerticalStrut(20));
       gpaPanel.add(calcButton);


       // Bottom buttons
       JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
       saveButton = new JButton("Save Changes");
       undoButton = new JButton("Undo Changes");


       saveButton.setEnabled(false);
       undoButton.setEnabled(false);


       saveButton.addActionListener(e -> saveJsonData());
       undoButton.addActionListener(e -> reloadJsonData());


       bottomPanel.add(saveButton);
       bottomPanel.add(Box.createHorizontalStrut(20));
       bottomPanel.add(undoButton);


       // Layout
       JPanel centerPanel = new JPanel(new BorderLayout());
       centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
       centerPanel.add(scroll, BorderLayout.CENTER);
       centerPanel.add(gpaPanel, BorderLayout.EAST);


       panel.add(centerPanel, BorderLayout.CENTER);
       panel.add(bottomPanel, BorderLayout.SOUTH);


       return panel;
   }


   // --- Grades LOADING from a json file ---
   // Custom JSON reader/writer so there is no external dependencies
   // to JSON library. Only supports only a tiny subset of JSON syntax.
   // It expects:
   //   An outer array [ ... ]
   //   Each row ending with ],
   //   No nested arrays
   //   No objects
   //   No extra whitespace inside rows
   //   No trailing comma on the last row
   private void loadJsonData() {
       try {
           System.out.println("Working directory: " + new File(".").getAbsolutePath());
           List<String> lines = Files.readAllLines(Paths.get(jsonFile));
           StringBuilder sb = new StringBuilder();
           for (String line : lines) sb.append(line.trim());


           String json = sb.toString();


           // Remove outer brackets
           json = json.substring(1, json.length() - 1).trim();


           // Split into rows
           String[] rows = json.split("],");


           for (String row : rows) {
               row = row.replace("[", "").replace("]", "").trim();
               String[] parts = row.split(",");


               String course = parts[0].replace("\"", "").trim();
               String grade = parts[1].replace("\"", "").trim();
               double credits = Double.parseDouble(parts[2].trim());


               model.addRow(new Object[]{course, grade, credits});
           }


       } catch (Exception ex) {
           ex.printStackTrace();
           JOptionPane.showMessageDialog(null, "Error loading JSON file.");
       }
   }


   private void reloadJsonData() {
       model.setRowCount(0);
       loadJsonData();
       hasChanges = false;
       calcButton.setEnabled(false);
       saveButton.setEnabled(false);
       undoButton.setEnabled(false);
   }


   // --- Grades SAVING to a json file
   // custom JSON writer so there is no external dependencies
   // to JSON library
   private void saveJsonData() {
       try {
           StringBuilder sb = new StringBuilder();
           sb.append("[\n");


           for (int i = 0; i < model.getRowCount(); i++) {
               String course = model.getValueAt(i, 0).toString();
               String grade = model.getValueAt(i, 1).toString();
               double credits = Double.parseDouble(model.getValueAt(i, 2).toString());


               sb.append("  [\"")
                       .append(course).append("\", \"")
                       .append(grade).append("\", ")
                       .append(credits).append("]");


               if (i < model.getRowCount() - 1) sb.append(",");
               sb.append("\n");
           }


           sb.append("]");


           Files.writeString(Paths.get(jsonFile), sb.toString());


           hasChanges = false;
           saveButton.setEnabled(false);
           undoButton.setEnabled(false);


           JOptionPane.showMessageDialog(null, "Changes saved.");


       } catch (Exception ex) {
           ex.printStackTrace();
           JOptionPane.showMessageDialog(null, "Error saving JSON file.");
       }
   }


   // --- GPA CALCULATION ---
   private void updateGPA() {
       gpaValueLabel.setText(String.format("%.2f", calculateGPA()));
       calcButton.setEnabled(false);
   }


   // Course potential credit (gradePoints)is MULTIPLED by its earned credit for every grade.
   // Then each product is added for a sum. That sum is multiplied by totalCredit for the GPA.
   public double calculateGPA() {
       double totalPoints = 0;
       double totalCredits = 0; //Sum of all potential credits added together


       for (int i = 0; i < model.getRowCount(); i++) {
           String gradeString = model.getValueAt(i, 1).toString();
           double numCredit = Double.parseDouble(model.getValueAt(i, 2).toString());


           if (gradeString.isEmpty() || numCredit == 0) continue;


           totalPoints += letterGradeToVal(gradeString) * numCredit;
           totalCredits += numCredit;
       }


       return totalCredits == 0 ? 0 : totalPoints / totalCredits;
   }


   private double letterGradeToVal(String grade) {
       switch (grade) {
           case "A+": return 4.0;
           case "A": return 4.0;
           case "A-": return 3.7;
           case "B+": return 3.3;
           case "B": return 3.0;
           case "B-": return 2.7;
           case "C+": return 2.3;
           case "C": return 2.0;
           case "C-": return 1.7;
           case "D": return 1.0;
           case "F": return 0.0;
       };
       return 0.0;
   }


   // --- NUMERIC DOCUMENT ---
   // JTextField don’t validate input by default. PlainDocument is the
   // default text model used by JTextField. The Credits column should only
   // allow digits + optional decimal.
   // By extending PlainDocument, we can intercept text insertions.
   class NumericDocument extends PlainDocument {
       @Override
       public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
           if (str.matches("\\d*(\\.\\d*)?")) {  // allow digits + optional decimal
               super.insertString(offs, str, a);
           }
       }
   }


}
