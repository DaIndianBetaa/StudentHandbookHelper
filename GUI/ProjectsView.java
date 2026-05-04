import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ProjectsView extends BaseView {

    private JButton addProjectButton;
    private JTextField titleField;
    private JTextField dateField;
    private JTextField hoursField;
    private JTextArea descriptionArea;
    private JScrollPane descriptionScroll;
    private JList list1;
    private JTextField totalHoursField;
    private JLabel totalHoursLabel;
    private JLabel titleLabel;
    private JLabel dateLabel;
    private JLabel hoursLabel;
    private JLabel descriptionLabel;
    private DefaultListModel projectsListModel = new DefaultListModel();
    private JPanel mainPanel;

    public void createUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        final JScrollPane scrollPane1 = new JScrollPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane1, gbc);
        list1 = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        list1.setModel(defaultListModel1);
        scrollPane1.setViewportView(list1);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panel1, gbc);
        totalHoursLabel = new JLabel();
        totalHoursLabel.setText("Total Hours:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 0, 0);
        panel1.add(totalHoursLabel, gbc);
        addProjectButton = new JButton();
        addProjectButton.setText("Add Project");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.ipady = 1;
        gbc.insets = new Insets(0, 5, 0, 0);
        panel1.add(addProjectButton, gbc);
        titleLabel = new JLabel();
        titleLabel.setText("Title:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 0, 0);
        panel1.add(titleLabel, gbc);
        titleField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(titleField, gbc);
        dateLabel = new JLabel();
        dateLabel.setText("Date (YYYY-MM-DD):");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 0, 0);
        panel1.add(dateLabel, gbc);
        dateField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(dateField, gbc);
        hoursLabel = new JLabel();
        hoursLabel.setText("Hours:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 0, 0);
        panel1.add(hoursLabel, gbc);
        hoursField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(hoursField, gbc);
        descriptionLabel = new JLabel();
        descriptionLabel.setText("Description:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 0, 0);
        panel1.add(descriptionLabel, gbc);
        totalHoursField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(totalHoursField, gbc);
        descriptionScroll = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(descriptionScroll, gbc);
        descriptionArea = new JTextArea();
        descriptionScroll.setViewportView(descriptionArea);
    }

    VolunteerProjectManager manager = new VolunteerProjectManager();

    @Override
    public JPanel show() {
        createUI();
        setModel();
        loadData();
        addProjectButton.addActionListener(e -> addProject());

        return mainPanel;
    }

    private void addProject() {
        manager.AddProject(titleField.getText(), descriptionArea.getText(), LocalDate.parse(dateField.getText()), Float.parseFloat(hoursField.getText()));
        manager.saveJsonData();
        setModel();
        loadData();
    }

    private void setModel() {
        manager.getVolunteerProjects().clear();
        projectsListModel.clear();
        list1.setModel(projectsListModel);
        list1.setCellRenderer(new ProjectsListCellRenderer(this));
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Delete Project");
        addItem.addActionListener(e ->  {
            System.out.println(list1.getSelectedValue());
            deleteProject((VolunteerProject) list1.getSelectedValue());
        });
        popupMenu.add(addItem);
        list1.setComponentPopupMenu(popupMenu);
    }

    private void loadData() {
        manager.loadJsonData();
        manager.getVolunteerProjects().forEach(project -> {
            projectsListModel.addElement(project);
        });
        totalHoursField.setText(manager.getTotalHours() + "");
        totalHoursField.setEditable(false);
    }

    private void saveData() {
        manager.saveJsonData();
    }

    public void deleteProject(VolunteerProject project) {
        manager.getVolunteerProjects().remove(project);
        System.out.println("Project Deleted....");
        manager.saveJsonData();
        setModel();
        loadData();
    }
}
