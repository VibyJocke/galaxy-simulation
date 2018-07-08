package gravitationalnbodyproblem.brute.sequential;

import gravitationalnbodyproblem.GalaxyJPanel;
import gravitationalnbodyproblem.brute.AbstractGNBP;
import gravitationalnbodyproblem.brute.Point;

import static gravitationalnbodyproblem.Constants.DT;
import static gravitationalnbodyproblem.Constants.G;

public class GNBP extends AbstractGNBP {

    public GNBP() throws Exception {
        super();
    }

    @Override
    public void work(GalaxyJPanel panel) {
        while (true) {
            calculateForces(0);
            moveBodies(0);
            panel.redraw();
        }
    }

    @Override
    public void calculateForces(int threadNum) {
        for (int i = 0; i < numBodies; i++) {
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
        for (int i = 0; i < numBodies; i++) {
            Point dv = new Point(bodies[i].fx / bodies[i].m * DT,
                    bodies[i].fy / bodies[i].m * DT);
            Point dp = new Point((bodies[i].vx + dv.x / 2) * DT,
                    (bodies[i].vy + dv.y / 2) * DT);

            bodies[i].vx = bodies[i].vx + dv.x;
            bodies[i].vy = bodies[i].vy + dv.y;
            bodies[i].px = bodies[i].px + dp.x;
            bodies[i].py = bodies[i].py + dp.y;
            bodies[i].fx = 0.0;
            bodies[i].fy = 0.0;
        }
    }
}
