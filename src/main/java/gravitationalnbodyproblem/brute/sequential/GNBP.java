package gravitationalnbodyproblem.brute.sequential;

import gravitationalnbodyproblem.brute.GalaxyJPanel;
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
        for (int i = 0; i < numBodies; i++) {
            Point dv = new Point(force[i].x / mass[i] * DT,
                    force[i].y / mass[i] * DT);
            Point dp = new Point((velocity[i].x + dv.x / 2) * DT,
                    (velocity[i].y + dv.y / 2) * DT);

            velocity[i].x = velocity[i].x + dv.x;
            velocity[i].y = velocity[i].y + dv.y;
            position[i].x = position[i].x + dp.x;
            position[i].y = position[i].y + dp.y;
            force[i].x = 0.0;
            force[i].y = 0.0;
        }
    }
}
