package taxi;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MyScrollBarUI {

    public static ScrollBarUI myScrollBarUI() {
        ScrollBarUI UI = new BasicScrollBarUI() {
            Color barColor = Color.decode("#cccccc");

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(Color.white);
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBackground(Color.white);
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = barColor;
                this.trackColor = Color.white;
            }
        };

        return UI;
    }

}
