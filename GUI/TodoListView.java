import javax.swing.*;
import java.awt.*;

public class ProjectsView extends BaseView {

    @Override
    public JPanel show() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Projects"));
        return panel;
    }
}