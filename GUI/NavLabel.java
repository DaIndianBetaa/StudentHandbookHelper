import javax.swing.*;
import java.awt.*;

// A stylized  JLabael used for navigation items in the sidepanel of main winodow (left).
// Provides consistent font and a hand cursor.

public class NavLabel extends JLabel {

    public NavLabel(String text) {
        super(text);
        setFont(new Font("Arial", Font.PLAIN, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // makes cursor a hand when label is hovered over
    }
}