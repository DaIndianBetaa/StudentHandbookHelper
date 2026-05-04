import javax.swing.*;
import java.awt.*;

class ProjectsListCellRenderer extends JTextArea implements ListCellRenderer<Object> {

    public ProjectsListCellRenderer(ProjectsView projectsView) {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        if(value instanceof VolunteerProject) {
            setText(value.toString());
        }
        setSize(list.getWidth(), getPreferredSize().height); // Necessary for height calculation

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
