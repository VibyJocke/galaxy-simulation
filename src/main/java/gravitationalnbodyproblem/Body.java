package gravitationalnbodyproblem;

/**
 * Object for representing a body with position, motion and mass.
 */
public class Body {

    // Position
    public double px;
    public double py;
    // Velocity
    public double vy;
    public double vx;
    // Force
    public double fx;
    public double fy;
    // Mass
    public double m;

    public Body(double px, double py, double vx, double vy, double mass) {
        this.px = px;
        this.py = py;
        this.vx = vx;
        this.vy = vy;
        this.m = mass;
    }
}