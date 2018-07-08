package gravitationalnbodyproblem.brute;

import gravitationalnbodyproblem.Body;
import gravitationalnbodyproblem.GalaxyJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.Scanner;

import static gravitationalnbodyproblem.Constants.FILE_NAME;

public abstract class AbstractGNBP extends JFrame {

    public int numBodies;
    public GalaxyJPanel galaxyJPanel;
    public Body bodies[];

    public AbstractGNBP() throws Exception {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Universe-sim");

        Scanner scanner = new Scanner(new File(FILE_NAME));
        scanner.useLocale(Locale.US);

        numBodies = scanner.nextInt();
        bodies = new Body[numBodies];
        double quadSize = scanner.nextDouble();

        System.out.println("The total number of bodies: " + numBodies);
        System.out.println("The quadrant size is: " + quadSize);

        int n = 0;
        while (scanner.hasNextDouble()) {
            double px = scanner.nextDouble();
            double py = scanner.nextDouble();
            double vx = scanner.nextDouble();
            double vy = scanner.nextDouble();
            double mass = scanner.nextDouble();
            new Color(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()); // TODO: use color
            bodies[n] = new Body(px, py, vx, vy, mass);
            n++;
        }

        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        galaxyJPanel = new GalaxyJPanel(bodies, quadSize);
        add(galaxyJPanel, "Center");
        repaint();
        validate();

        work(galaxyJPanel);
    }

    public abstract void work(GalaxyJPanel panel) throws Exception;

    /**
     * Calculates forces acting between one body (outer loop) and another bodies (inner loop).
     */
    public abstract void calculateForces(int threadNum);

    /**
     * Moves bodies based on current force, speed and mass.
     */
    public abstract void moveBodies(int threadNum);
}
