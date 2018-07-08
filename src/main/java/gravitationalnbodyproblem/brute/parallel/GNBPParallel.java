package gravitationalnbodyproblem.brute.parallel;

import gravitationalnbodyproblem.GalaxyJPanel;
import gravitationalnbodyproblem.brute.AbstractGNBP;
import gravitationalnbodyproblem.brute.Point;

import java.util.concurrent.CyclicBarrier;

import static gravitationalnbodyproblem.Constants.*;

public class GNBPParallel extends AbstractGNBP {

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
                double distance = Math.sqrt(Math.pow(bodies[i].px - bodies[j].px, 2) + Math.pow(bodies[i].py - bodies[j].py, 2));
                double magnitude = (G * bodies[i].m * bodies[j].m) / Math.pow(distance, 2);
                Point direction = new Point(bodies[j].px - bodies[i].px, bodies[j].py - bodies[i].py);
                bodies[i].fx = bodies[i].fx + magnitude * direction.x / distance;
                bodies[j].fx = bodies[j].fx - magnitude * direction.x / distance;
                bodies[i].fy = bodies[i].fy + magnitude * direction.y / distance;
                bodies[j].fy = bodies[j].fy - magnitude * direction.y / distance;
            }
        }
    }

    @Override
    public void moveBodies(int threadNum) {
        Point force = new Point(0, 0);

        for (int i = threadNum; i < numBodies; i += NUM_THREADS) {
            for (int k = 0; k < NUM_THREADS; k++) {
                force.x += bodies[i].fx;
                bodies[i].fx = 0.0;
                force.y += bodies[i].fy;
                bodies[i].fy = 0.0;
            }
            Point dv = new Point(
                    force.x / bodies[i].m * DT,
                    force.y / bodies[i].m * DT
            );
            Point dp = new Point(
                    (bodies[i].vx + dv.x / 2) * DT,
                    (bodies[i].vy + dv.y / 2) * DT
            );

            bodies[i].vx = bodies[i].vx + dv.x;
            bodies[i].vy = bodies[i].vy + dv.y;
            bodies[i].px = bodies[i].px + dp.x;
            bodies[i].py = bodies[i].py + dp.y;
            force.x = 0;
            force.y = 0;
        }
    }
}
