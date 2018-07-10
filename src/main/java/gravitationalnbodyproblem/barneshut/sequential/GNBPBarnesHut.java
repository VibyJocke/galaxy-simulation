package gravitationalnbodyproblem.barneshut.sequential;

import gravitationalnbodyproblem.barneshut.AbstractBH;
import gravitationalnbodyproblem.barneshut.BHBody;
import gravitationalnbodyproblem.barneshut.BHTree;

import static gravitationalnbodyproblem.Constants.DT;

public class GNBPBarnesHut extends AbstractBH {

    public GNBPBarnesHut() throws Exception {
        super();
    }

    /*
     * Iterates through the array with bodies, populating the tree, calculating forces
     * and moving the bodies. Bodies only simulated as long as they are inside the quadrant.
     */
    @Override
    public void work() {
        while (true) {
            bhtTree = new BHTree(quadrant);
            for (BHBody body : bodies) {
                if (body.in(quadrant)) {
                    bhtTree.insert(body);
                }
            }
            for (BHBody body : bodies) {
                body.resetForce();
                if (body.in(quadrant)) {
                    bhtTree.updateForce(body);
                    body.update(DT);
                }
            }
            galaxyJPanel.redraw();
        }
    }
}
