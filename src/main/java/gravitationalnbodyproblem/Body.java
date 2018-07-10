package gravitationalnbodyproblem;

/**
 * Object for representing a body with position, motion and mass.
 */
public class Body {

    public double px, py, vy, vx, fx, fy, m;

    public Body(double px, double py, double vx, double vy, double mass) {
        this.px = px;
        this.py = py;
        this.vx = vx;
        this.vy = vy;
        this.m = mass;
    }
}