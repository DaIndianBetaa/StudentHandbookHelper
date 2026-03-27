import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {

    private String updatedName;

    public SettingsDialog(JFrame parent, String currentName) {
        super(parent, "Settings", true);
        setSize(300, 180);
        setLayout(new GridLayout(3, 1, 10, 10));
        setLocationRelativeTo(parent);

        JLabel lbl = new JLabel("Student Name:");
        JTextField txtName = new JTextField(currentName);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
            updatedName = txtName.getText().trim();
            dispose();
        });

        add(lbl);
        add(txtName);
        add(btnSave);
    }

    public String getUpdatedName() {
        return updatedName;
    }
}