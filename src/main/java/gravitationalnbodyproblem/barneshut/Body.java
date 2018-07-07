package gravitationalnbodyproblem.barneshut;

import static gravitationalnbodyproblem.Constants.G;

/**
 * Object for representing a body with position, motion and mass.
 */
public class Body {

    public double px, py, m;
    private double vy, vx, fx, fy;

    public Body(double px, double py, double vx, double vy, double mass) {
        this.px = px;
        this.py = py;
        this.vx = vx;
        this.vy = vy;
        this.m = mass;
    }

    /*
     * Updates the velocity and position, based on force, mass and DT.
     */
    public void update(double dt) {
        vx += dt * fx / m;
        vy += dt * fy / m;
        px += dt * vx;
        py += dt * vy;
    }

    /*
     * Returns the distance between this body, and the given body.
     */
    public double distanceTo(Body b) {
        double dx = px - b.px;
        double dy = py - b.py;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /*
     * Resets the force of the body.
     */
    public void resetForce() {
        fx = 0.0;
        fy = 0.0;
    }

    /*
     * Computes the force acting between this body and the other one and
     * adds it to this body, to be used during the move phase.
     */
    public void addForce(Body b) {
        double dx = b.px - px;
        double dy = b.py - py;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double F = (G * m * b.m) / (dist * dist);
        fx += F * dx / dist;
        fy += F * dy / dist;
    }

    /*
     * A static method for adding two bodies by combining their position and mass,
     * and returning the resulting body to represent center of gravity for a quadrant.
     */
    public static Body combine(Body b1, Body b2) {
        double d1 = (b1.px * b1.m + b2.px * b2.m) / (b1.m + b2.m);
        double d2 = (b1.py * b1.m + b2.py * b2.m) / (b1.m + b2.m);
        double d3 = b1.m + b2.m;
        return new Body(d1, d2, 0.0D, 0.0D, d3);
    }

    /*
     * Checks if the body is in the quadrant.
     */
    public boolean in(Quadrant q) {
        return q.contains(px, py);
    }
}