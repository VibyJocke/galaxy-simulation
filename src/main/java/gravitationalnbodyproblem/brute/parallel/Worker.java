package gravitationalnbodyproblem.brute.parallel;

import java.util.concurrent.CyclicBarrier;

import static gravitationalnbodyproblem.Constants.NUM_THREADS;

/**
 * A thread that performs simulation work.
 */
public class Worker extends Thread {

    private static final CyclicBarrier BARRIER = new CyclicBarrier(NUM_THREADS);

    private final GNBPParallel ref;
    private final int tid;

    public Worker(GNBPParallel ref, int id) {
        this.ref = ref;
        tid = id;
    }

    /**
     * Calculate forces for assigned bodies, then wait for other threads to finish too,
     * then move assigned bodies and await the others again. Thread 0 is responsible
     * for drawing the result on the screen. Disable (comment out) for performance.
     */
    @Override
    public void run() {
        while (true) {
            try {
                ref.calculateForces(tid, NUM_THREADS);

                BARRIER.await();

                ref.moveBodies(tid, NUM_THREADS);

                BARRIER.await();

                if (tid == 0) {
                    ref.galaxyJPanel.redraw();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
