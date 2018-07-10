package gravitationalnbodyproblem;

import javax.swing.*;
import java.awt.*;

/**
 * The graphical part of the project, responsible for displaying the generated
 * body positions in space.
 */
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setColor(Color.white);

        for (Body body : bodies) {
            Double px = body.px / (quadSize / 1000) + (screenSize.width / 2);
            Double py = body.py / (quadSize / 1000) + (screenSize.height / 2);
            Double size = Math.sqrt(Math.sqrt(body.m / 1E22)) + 2;
            g.fillOval(
                    px.intValue() - (size.intValue() / 2),
                    py.intValue() - (size.intValue() / 2),
                    size.intValue(), size.intValue()
            );
        }
    }
}
