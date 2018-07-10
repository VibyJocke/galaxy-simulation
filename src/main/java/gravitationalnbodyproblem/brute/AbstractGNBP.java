package gravitationalnbodyproblem.brute;

import gravitationalnbodyproblem.Body;
import gravitationalnbodyproblem.GalaxyJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.Scanner;

import static gravitationalnbodyproblem.Constants.DT;
import static gravitationalnbodyproblem.Constants.FILE_NAME;
import static gravitationalnbodyproblem.Constants.G;

public abstract class AbstractGNBP extends JFrame {

    private final int numBodies;
    private final Body bodies[];

    public final GalaxyJPanel galaxyJPanel;

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
    public void calculateForces(int threadNum, int numThreads) {
        for (int i = threadNum; i < numBodies; i += numThreads) {
            for (int j = i + 1; j < numBodies; ++j) {
                double distance = Math.sqrt(Math.pow(bodies[i].px - bodies[j].px, 2) + Math.pow(bodies[i].py - bodies[j].py, 2));
                double magnitude = (G * bodies[i].m * bodies[j].m) / Math.pow(distance, 2);
                double dx = bodies[j].px - bodies[i].px;
                double dy = bodies[j].py - bodies[i].py;
                bodies[i].fx = bodies[i].fx + magnitude * dx / distance;
                bodies[j].fx = bodies[j].fx - magnitude * dx / distance;
                bodies[i].fy = bodies[i].fy + magnitude * dy / distance;
                bodies[j].fy = bodies[j].fy - magnitude * dy / distance;
            }
        }
    }

    /**
     * Moves bodies based on current force, speed and mass.
     */
    public void moveBodies(int threadNum, int numThreads) {
        for (int i = threadNum; i < numBodies; i += numThreads) {
            double dvx = bodies[i].fx / bodies[i].m * DT;
            double dvy = bodies[i].fy / bodies[i].m * DT;
            double dpx = (bodies[i].vx + dvx / 2) * DT;
            double dpy = (bodies[i].vy + dvy / 2) * DT;

            bodies[i].vx = bodies[i].vx + dvx;
            bodies[i].vy = bodies[i].vy + dvy;
            bodies[i].px = bodies[i].px + dpx;
            bodies[i].py = bodies[i].py + dpy;
            bodies[i].fx = 0.0;
            bodies[i].fy = 0.0;
        }
    }
}
