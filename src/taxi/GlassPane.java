package taxi;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public class GlassPane extends JComponent {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 90));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
