import javax.swing.*;
import java.awt.*;

// A stylized  JLabael used for navigation items in the sidepanel of main winodow (left).
// Provides consistent font and a hand cursor.

public class NavLabel extends JLabel {

    public NavLabel(String text) {
        super(text);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        
        setForeground(new Color(248,245,252));
        
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // makes cursor a hand when label is hovered over
    }
}