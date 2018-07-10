package gravitationalnbodyproblem.barneshut.parallel;

import gravitationalnbodyproblem.barneshut.AbstractBH;

import static gravitationalnbodyproblem.Constants.NUM_THREADS;

public class GNBPBarnesHutParallel extends AbstractBH {

    public GNBPBarnesHutParallel() throws Exception {
        super();
    }

    @Override
    public void work() {
        Worker workers[] = new Worker[NUM_THREADS];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker(this, i);
        }
        for (Worker worker : workers) {
            worker.start();
        }
    }
}
