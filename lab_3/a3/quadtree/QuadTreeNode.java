package a3.quadtree;

import a3.Enemy;

public class QuadTreeNode {
    private QuadTreePoint pos;
    private Enemy data;

    public QuadTreeNode(QuadTreePoint _pos, Enemy _data) {
        pos = _pos;
        data = _data;
    }

    public QuadTreeNode() {
        data = null;
    }

    public QuadTreePoint getPosition() {
        return pos;
    }

    public Enemy getEnemy() {
        return data;
    }
}
