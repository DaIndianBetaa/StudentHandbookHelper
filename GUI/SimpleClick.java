import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SimpleClick extends MouseAdapter {

    private Runnable action;

    public SimpleClick(Runnable action) {
        this.action = action;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        action.run();
    }
}