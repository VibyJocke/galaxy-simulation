package gravitationalnbodyproblem.barneshut.parallel;

import gravitationalnbodyproblem.barneshut.BHTree;
import gravitationalnbodyproblem.barneshut.Body;
import gravitationalnbodyproblem.barneshut.Quadrant;
import gravitationalnbodyproblem.barneshut.GalaxyJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

import static gravitationalnbodyproblem.Constants.FILE_NAME;

public class GNBPBarnesHutParallel extends JFrame {

    public static final int NUM_THREADS = 8;

    private double quadSize;
    public int numBodies;

    public BHTree bhtTree;
    public CyclicBarrier barrier;
    public GalaxyJPanel galaxyJPanel;
    public Body bodies[];
    public Quadrant quadrant;

    public GNBPBarnesHutParallel() throws Exception {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("N-Body Simulator using Barnes Hut with " + NUM_THREADS + " threads   -   By Joakim Lahtinen TIDAB3 2012");

        bodies = new Body[numBodies];
        quadrant = new Quadrant(0, 0, 2E06);

        Scanner scanner = new Scanner(new File(FILE_NAME));
        scanner.useLocale(Locale.US);

        numBodies = scanner.nextInt();
        bodies = new Body[numBodies];
        System.out.println("The total number of bodies: " + numBodies);
        quadSize = scanner.nextDouble();
        quadrant = new Quadrant(0, 0, quadSize * 2);
        System.out.println("The quadrant size is: " + quadrant.length());

        double px, py, vx, vy, mass;
        int n = 0;
        while (scanner.hasNextDouble()) {
            px = scanner.nextDouble();
            py = scanner.nextDouble();
            vx = scanner.nextDouble();
            vy = scanner.nextDouble();
            mass = scanner.nextDouble();
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

        //Barrier used to synchronize all threads between work.
        barrier = new CyclicBarrier(NUM_THREADS);
        Worker workers[] = new Worker[NUM_THREADS];

        //Creating the threads.
        for (int i = 0; i < NUM_THREADS; i++)
            workers[i] = new Worker(this, i);

        //Starting the threads.
        for (int i = 0; i < NUM_THREADS; i++)
            workers[i].start();
    }

    /*
     * Builds the tree by inserting bodies at the root.
     */
    public void makeTree() {
        bhtTree = new BHTree(quadrant);
        for (int i = 0; i < numBodies; i++) {
            if (bodies[i].in(quadrant)) {
                bhtTree.insert(bodies[i]);
            }
        }
    }
}
