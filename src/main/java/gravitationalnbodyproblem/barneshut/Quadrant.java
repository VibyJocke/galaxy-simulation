package gravitationalnbodyproblem.barneshut;

/**
 * Object for representing a quadrant, which holds methods for subdividing itself into
 * smaller quadrants.
 */
public class Quadrant {

    private double x, y, length;

    /*
     * Constructs a new quadrant with center coordinates and a specific length.
     */
    public Quadrant(double xmid, double ymid, double length) {
        this.x = xmid;
        this.y = ymid;
        this.length = length;
    }

    /*
     * Returns the length of the current quadrant.
     */
    public double length() {
        return length;
    }

    /*
     * Checks if a certain coordinate belongs to the current quadrant.
     */
    public boolean contains(double xmid, double ymid) {
        return xmid <= x + length / 2.0 && xmid >= x - length / 2.0 && ymid <= y + length / 2.0 && ymid >= y - length / 2.0;
    }

    /*
     * Following are constructors which represent subdivisions of the current quadrant.
     */
    public Quadrant NW() {
        return new Quadrant(x - length / 4.0, y + length / 4.0, length / 2.0);
    }

    public Quadrant NE() {
        return new Quadrant(x + length / 4.0, y + length / 4.0, length / 2.0);
    }

    public Quadrant SW() {
        return new Quadrant(x - length / 4.0, y - length / 4.0, length / 2.0);
    }

    public Quadrant SE() {
        return new Quadrant(x + length / 4.0, y - length / 4.0, length / 2.0);
    }
}