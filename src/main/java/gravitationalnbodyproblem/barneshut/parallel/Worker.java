package gravitationalnbodyproblem.barneshut.parallel;

import gravitationalnbodyproblem.barneshut.BHTree;

import java.util.concurrent.CyclicBarrier;

import static gravitationalnbodyproblem.Constants.DT;
import static gravitationalnbodyproblem.Constants.NUM_THREADS;

public class Worker extends Thread {

    private static final CyclicBarrier BARRIER = new CyclicBarrier(NUM_THREADS);

    private final GNBPBarnesHutParallel ref;
    private final int id;

    Worker(GNBPBarnesHutParallel ref, int id) {
        this.ref = ref;
        this.id = id;
    }

    /*
     * Thread 0 is responsible for building the tree and for
     * drawing the visuals.
     * Barriers keep different phases of the work separated. The first phase updates the
     * forces on the bodies, while the second moves the bodies based on the previous information.
     */
    @Override
    public void run() {
        while (true) {
            try {
                if (id == 0) {
                    makeTree();
                }

                BARRIER.await();

                for (int i = id; i < ref.bodies.length; i += NUM_THREADS) {
                    ref.bodies[i].resetForce();
                    if (ref.bodies[i].in(ref.quadrant)) {
                        ref.bhtTree.updateForce(ref.bodies[i]);
                    }
                }

                BARRIER.await();

                for (int i = id; i < ref.bodies.length; i += NUM_THREADS) {
                    if (ref.bodies[i].in(ref.quadrant)) {
                        ref.bodies[i].update(DT);
                    }
                }

                if (id == 0) {
                    ref.galaxyJPanel.redraw();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /*
     * Builds the tree by inserting bodies at the root.
     */
    private void makeTree() {
        ref.bhtTree = new BHTree(ref.quadrant);
        for (int i = 0; i < ref.bodies.length; i++) {
            if (ref.bodies[i].in(ref.quadrant)) {
                ref.bhtTree.insert(ref.bodies[i]);
            }
        }
    }
}
