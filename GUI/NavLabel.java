import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// A stylized  JLabael used for navigation items in the sidepanel of main winodow (left).
// Provides consistent font and a hand cursor; makes it easy to change. Also,
// looks too cluttered in main otherwise

public class NavLabel extends JLabel {
	Font customFont;
		
	    public NavLabel(String text) {
	        super(text);
	        
	        try {
	            // 1. Load the font file
	            File fontFile = new File("Poppins-SemiBold.ttf");
	            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
	
	            // 2. Register the font with the Graphics Environment
	            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	            ge.registerFont(customFont);
	
	            // 3. Use the font (set the size, as createFont defaults to 1pt)
	            customFont = customFont.deriveFont(24f); 
	
	        } catch (IOException | FontFormatException e) {
	            e.printStackTrace();
	            // Fallback to a logical font if loading fails
	        }

       // setFont(new Font("customFont", Font.PLAIN, 16));
	    setFont(customFont.deriveFont(16f));
	    setForeground(new Color(248,245,252));
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // makes cursor a hand when label is hovered over
    }
}
