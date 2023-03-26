package a3.quadtree;

import tage.GameObject;

public class QuadTreeNode {
    private QuadTreePoint pos;
    private GameObject data;

    public QuadTreeNode(QuadTreePoint _pos, GameObject _data) {
        pos = _pos;
        data = _data;
    }

    public QuadTreeNode() {
        data = null;
    }

    public QuadTreePoint getPosition() {
        return pos;
    }

    public GameObject getData() {
        return data;
    }

    @Override
    public String toString() {
        return pos.toString();
    }

}
