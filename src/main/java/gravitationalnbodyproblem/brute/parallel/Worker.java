package gravitationalnbodyproblem.brute.parallel;

/**
 * A thread that performs simulation work.
 */
public class Worker extends Thread {

    private final GNBPParallel ref;
    private final int time;

    public Worker(GNBPParallel ref, int id) {
        this.ref = ref;
        time = id;
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
                ref.calculateForces(time);
                ref.barrier.await();
                ref.moveBodies(time);
                ref.barrier.await();
                if (time == 0) {
                    ref.galaxyJPanel.redraw();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
