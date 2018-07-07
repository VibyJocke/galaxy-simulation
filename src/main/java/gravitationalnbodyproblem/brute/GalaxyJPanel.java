package gravitationalnbodyproblem.brute;

import gravitationalnbodyproblem.brute.Point;

import javax.swing.*;
import java.awt.*;

/**
 * The graphical part of the project, responsible for displaying the generated
 * body positions in space.
 */
public class GalaxyJPanel extends JPanel {
    private Point points[];
    private double[] mass;
    private double quadSize;
    private Dimension screenSize;

    public GalaxyJPanel(Point[] points, double[] mass, double quadSize) {
        this.points = points;
        this.mass = mass;
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

        for (int n = 0; n < points.length; n++) {
            Double px = points[n].x / (quadSize / 1000) + (screenSize.width / 2);
            Double py = points[n].y / (quadSize / 1000) + (screenSize.height / 2);
            Double m = Math.sqrt(Math.sqrt(mass[n] / 1E22)) + 2;
            g.fillOval(
                    px.intValue() - (m.intValue() / 2),
                    py.intValue() - (m.intValue() / 2),
                    m.intValue(), m.intValue()
            );
        }
    }
}
