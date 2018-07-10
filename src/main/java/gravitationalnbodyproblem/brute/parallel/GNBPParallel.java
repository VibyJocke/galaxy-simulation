package gravitationalnbodyproblem.brute.parallel;

import gravitationalnbodyproblem.GalaxyJPanel;
import gravitationalnbodyproblem.brute.AbstractGNBP;

import static gravitationalnbodyproblem.Constants.NUM_THREADS;

public class GNBPParallel extends AbstractGNBP {

    public GNBPParallel() throws Exception {
        super();
    }

    @Override
    public void work(GalaxyJPanel panel) throws Exception {
        Worker workers[] = new Worker[NUM_THREADS];

        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker(this, i);
        }

        for (Worker worker : workers) {
            worker.start();
        }
    }
}
