package gravitationalnbodyproblem.barneshut;

/**
 * Object for holding quadrants in a tree structure, to be manipulated and traversed.
 * Each BHTree represents a quadrant and a center of mass, in the form of a body.
 */
public class BHTree {

    private static final double FAR_LIMIT = 1;

    private Body body;              //The body of the current node.
    private Quadrant quadrant;          //The quadrant of the current node.
    private BHTree NW, NE, SW, SE;  //Tree representing a quadrant.

    /*
     * Creates and initializes BH-tree. All nodes start as null values to
     * be filled by recursion.
     */
    public BHTree(Quadrant q) {
        this.quadrant = q;
    }

    /*
     * Checks if given BHTree is a leaf node (i.e. all quadrant-references are null).
     */
    public Boolean isExternal(BHTree t) {
        return t.NW == null && t.NE == null && t.SW == null && t.SE == null;
    }

    /*
     * Recursively inserts bodies into the tree by starting with the current one.
     */
    public void insert(Body b) {
        if (body == null) {
            body = b;
        } else if (!this.isExternal(this)) {
            body = Body.combine(body, b);
            Quadrant nw = quadrant.NW();
            if (b.in(nw)) {
                if (NW == null) {
                    NW = new BHTree(nw);
                }
                NW.insert(b);
            } else {
                Quadrant ne = quadrant.NE();
                if (b.in(ne)) {
                    if (NE == null) {
                        NE = new BHTree(ne);
                    }
                    NE.insert(b);
                } else {
                    Quadrant se = quadrant.SE();
                    if (b.in(se)) {
                        if (SE == null) {
                            SE = new BHTree(se);
                        }
                        SE.insert(b);
                    } else {
                        Quadrant sw = quadrant.SW();
                        if (SW == null) {
                            SW = new BHTree(sw);
                        }
                        SW.insert(b);
                    }
                }
            }
        } else if (this.isExternal(this)) {
            Quadrant nw = quadrant.NW();
            if (body.in(nw)) {
                if (NW == null) {
                    NW = new BHTree(nw);
                }
                NW.insert(body);
            } else {
                Quadrant ne = quadrant.NE();
                if (body.in(ne)) {
                    if (NE == null) {
                        NE = new BHTree(ne);
                    }
                    NE.insert(body);
                } else {
                    Quadrant se = quadrant.SE();
                    if (body.in(se)) {
                        if (SE == null) {
                            SE = new BHTree(se);
                        }
                        SE.insert(body);
                    } else {
                        Quadrant sw = quadrant.SW();
                        if (SW == null) {
                            SW = new BHTree(sw);
                        }
                        SW.insert(body);
                    }
                }
            }
            this.insert(b);
        }
    }

    /*
     * Recursively go through each branch until an external node, or a node that
     * is far enough to be approximated is encountered, and do force calculations
     * for that body.
     */
    public void updateForce(Body b) {
        if (isExternal(this)) {
            if (body != b) {
                b.addForce(body);
            }
        } else if (quadrant.length() / (body.distanceTo(b)) < FAR_LIMIT) {
            b.addForce(body);
        } else {
            if (NW != null) {
                NW.updateForce(b);
            }
            if (SW != null) {
                SW.updateForce(b);
            }
            if (SE != null) {
                SE.updateForce(b);
            }
            if (NE != null) {
                NE.updateForce(b);
            }
        }
    }
}