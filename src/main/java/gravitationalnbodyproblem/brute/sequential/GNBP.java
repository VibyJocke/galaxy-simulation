package gravitationalnbodyproblem.brute.sequential;

import gravitationalnbodyproblem.GalaxyJPanel;
import gravitationalnbodyproblem.brute.AbstractGNBP;

public class GNBP extends AbstractGNBP {

    public GNBP() throws Exception {
        super();
    }

    @Override
    public void work(GalaxyJPanel panel) {
        while (true) {
            calculateForces(0, 1);
            moveBodies(0, 1);
            panel.redraw();
        }
    }
}
