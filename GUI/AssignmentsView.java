import javax.swing.*;
import java.awt.*;

public class AssignmentsView extends BaseView {

    @Override
    public JPanel show() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Assignments"));
        return panel;
    }
}