package a3.quadtree;

public class Point {

    // about an zx-plane
    public float z, x;

    public Point(float _z, float _x) {
        z = _z;
        x = _x;
    }

    public Point() {
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
