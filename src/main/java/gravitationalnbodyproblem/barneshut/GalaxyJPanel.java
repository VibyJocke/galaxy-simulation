package gravitationalnbodyproblem.barneshut;

import java.awt.*;
import javax.swing.*;

public class GalaxyJPanel extends JPanel {

    private Body[] bodies;
    private double quadSize;
    private Dimension screenSize;

    public GalaxyJPanel(Body[] bodies, double quadSize) {
        this.bodies = bodies;
        this.quadSize = quadSize;
        setBackground(Color.black);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }

    public void redraw() {
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setColor(Color.white);

        for (int i = 0; i < bodies.length; i++) {
            Double px = bodies[i].px / (quadSize / 1000) + (screenSize.width / 2);
            Double py = bodies[i].py / (quadSize / 1000) + (screenSize.height / 2);
            Double size = Math.sqrt(Math.sqrt(bodies[i].m / 1E22)) + 2;
            g.fillOval(
                    px.intValue() - (size.intValue() / 2),
                    py.intValue() - (size.intValue() / 2),
                    size.intValue(), size.intValue()
            );
        }
    }
}
