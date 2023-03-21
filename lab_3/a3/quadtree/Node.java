package a3.quadtree;

import a3.Enemy;

public class Node {
    public Point pos;
    public Enemy data;

    Node(Point _pos, Enemy _data) {
        pos = _pos;
        data = _data;
    }

    Node() {
        data = null;
    }

    public Point getPosition() {
        return pos;
    }

    public Enemy getEnemy() {
        return data;
    }
}
