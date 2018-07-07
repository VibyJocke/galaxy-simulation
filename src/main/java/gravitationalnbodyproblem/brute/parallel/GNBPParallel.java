package gravitationalnbodyproblem.brute.parallel;

import gravitationalnbodyproblem.brute.GalaxyJPanel;
import gravitationalnbodyproblem.brute.AbstractGNBP;
import gravitationalnbodyproblem.brute.Point;

import java.util.concurrent.CyclicBarrier;

import static gravitationalnbodyproblem.Constants.DT;
import static gravitationalnbodyproblem.Constants.G;

public class GNBPParallel extends AbstractGNBP {

    private static final int NUM_THREADS = 4;

    public CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS);

    public GNBPParallel() throws Exception {
        super();
    }

    @Override
    public void work(GalaxyJPanel panel) throws Exception {
        Worker workers[] = new Worker[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            workers[i] = new Worker(this, i);
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            workers[i].start();
        }
    }

    @Override
    public void calculateForces(int threadNum) {
        for (int i = threadNum; i < numBodies; i += NUM_THREADS) {
            for (int j = i + 1; j < numBodies; ++j) {
                double distance = Math.sqrt(Math.pow(position[i].x - position[j].x, 2) + Math.pow(position[i].y - position[j].y, 2));
                double magnitude = (G * mass[i] * mass[j]) / Math.pow(distance, 2);
                Point direction = new Point(position[j].x - position[i].x, position[j].y - position[i].y);
                force[i].x = force[i].x + magnitude * direction.x / distance;
                force[j].x = force[j].x - magnitude * direction.x / distance;
                force[i].y = force[i].y + magnitude * direction.y / distance;
                force[j].y = force[j].y - magnitude * direction.y / distance;
            }
        }
    }

    @Override
    public void moveBodies(int threadNum) {
        Point force = new Point(0, 0);

        for (int i = threadNum; i < numBodies; i += NUM_THREADS) {
            for (int k = 0; k < NUM_THREADS; k++) {
                force.x += this.force[i].x;
                this.force[i].x = 0.0;
                force.y += this.force[i].y;
                this.force[i].y = 0.0;
            }
            Point dv = new Point(
                    force.x / mass[i] * DT,
                    force.y / mass[i] * DT
            );
            Point dp = new Point(
                    (velocity[i].x + dv.x / 2) * DT,
                    (velocity[i].y + dv.y / 2) * DT
            );

            velocity[i].x = velocity[i].x + dv.x;
            velocity[i].y = velocity[i].y + dv.y;
            position[i].x = position[i].x + dp.x;
            position[i].y = position[i].y + dp.y;
            force.x = 0;
            force.y = 0;
        }
    }
}
