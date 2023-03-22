package a3.quadtree;

public class QuadTreePoint {

    // about an zx-plane
    private float z, x;

    public QuadTreePoint(float _z, float _x) {
        z = _z;
        x = _x;
    }

    public QuadTreePoint() {
        z = 0;
        x = 0;
    }

    public float x() {
        return x;
    }

    public float z() {
        return z;
    }

}
