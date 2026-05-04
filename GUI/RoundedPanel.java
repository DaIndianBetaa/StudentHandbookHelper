import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

// A wrapper panel class so that ProjectsView/GradesView/AssignmentsView
// panel appears inside a rounded rectangle
public class RoundedPanel extends JPanel {

    private int radius;

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {

    	
    	/*

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background fill
        g2.setColor(new Color(200, 200, 200));  // thin gray outline
        g2.setStroke(new BasicStroke(1.5f));         // 1‑pixel border
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2.dispose();
         */

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int w = getWidth() - insets.left - insets.right - 1;
        int h = getHeight() - insets.top - insets.bottom - 1;

        // Thin border
        g2.setColor(new Color(180, 180, 180));   // thin gray outline
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, w, h, radius, radius);

        g2.dispose();
    }
}