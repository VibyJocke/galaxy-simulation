package gravitationalnbodyproblem.barneshut.parallel;

import gravitationalnbodyproblem.GalaxyJPanel;
import gravitationalnbodyproblem.barneshut.BHBody;
import gravitationalnbodyproblem.barneshut.BHTree;
import gravitationalnbodyproblem.barneshut.Quadrant;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.Scanner;

import static gravitationalnbodyproblem.Constants.FILE_NAME;
import static gravitationalnbodyproblem.Constants.NUM_THREADS;

public class GNBPBarnesHutParallel extends JFrame {

    public final Quadrant quadrant;
    public final GalaxyJPanel galaxyJPanel;

    public volatile BHTree bhtTree;
    public volatile BHBody bodies[];

    public GNBPBarnesHutParallel() throws Exception {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("N-Body Simulator using Barnes Hut with " + NUM_THREADS + " threads   -   By Joakim Lahtinen TIDAB3 2012");

        Scanner scanner = new Scanner(new File(FILE_NAME));
        scanner.useLocale(Locale.US);

        int numBodies = scanner.nextInt();
        bodies = new BHBody[numBodies];
        double quadrantSize = scanner.nextDouble();
        quadrant = new Quadrant(0, 0, quadrantSize * 2);

        System.out.println("The total number of bodies: " + numBodies);
        System.out.println("The quadrant size is: " + quadrant.length());

        int n = 0;
        while (scanner.hasNextDouble()) {
            double px = scanner.nextDouble();
            double py = scanner.nextDouble();
            double vx = scanner.nextDouble();
            double vy = scanner.nextDouble();
            double mass = scanner.nextDouble();
            new Color(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()); // TODO: use color
            bodies[n] = new BHBody(px, py, vx, vy, mass);
            n++;
        }

        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        galaxyJPanel = new GalaxyJPanel(bodies, quadrantSize);
        add(galaxyJPanel, "Center");
        repaint();
        validate();

        Worker workers[] = new Worker[NUM_THREADS];

        //Creating the threads.
        for (int i = 0; i < NUM_THREADS; i++)
            workers[i] = new Worker(this, i);

        //Starting the threads.
        for (int i = 0; i < NUM_THREADS; i++)
            workers[i].start();
    }
}
