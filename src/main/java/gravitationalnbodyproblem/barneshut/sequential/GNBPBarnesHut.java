package gravitationalnbodyproblem.barneshut.sequential;

import gravitationalnbodyproblem.GalaxyJPanel;
import gravitationalnbodyproblem.barneshut.BHBody;
import gravitationalnbodyproblem.barneshut.BHTree;
import gravitationalnbodyproblem.barneshut.Quadrant;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.Scanner;

import static gravitationalnbodyproblem.Constants.DT;
import static gravitationalnbodyproblem.Constants.FILE_NAME;

public class GNBPBarnesHut extends JFrame {

    private final BHBody bodies[];
    private final Quadrant quadrant;

    public GNBPBarnesHut() throws Exception {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("N-Body Simulator using Barnes Hut   -   By Joakim Lahtinen TIDAB3 2012");

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
        GalaxyJPanel galaxyJPanel = new GalaxyJPanel(bodies, quadrantSize);
        add(galaxyJPanel, "Center");
        repaint();
        validate();

        while (true) {
            doMagic();
            galaxyJPanel.redraw();
        }
    }

    /*
     * Iterates through the array with bodies, populating the tree, calculating forces
     * and moving the bodies. Bodies only simulated as long as they are inside the quadrant.
     */
    private void doMagic() {
        BHTree tree = new BHTree(quadrant);
        for (int i = 0; i < bodies.length; i++) {
            if (bodies[i].in(quadrant)) {
                tree.insert(bodies[i]);
            }
        }
        for (int i = 0; i < bodies.length; i++) {
            bodies[i].resetForce();
            if (bodies[i].in(quadrant)) {
                tree.updateForce(bodies[i]);
                bodies[i].update(DT);
            }
        }
    }
}
