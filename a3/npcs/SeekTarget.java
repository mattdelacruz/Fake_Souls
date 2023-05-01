package a3.npcs;

import org.joml.Vector3f;

import a3.quadtree.QuadTree;
import a3.quadtree.QuadTreeNode;
import a3.quadtree.QuadTreePoint;
import tage.GameObject;
import tage.ai.behaviortrees.BTCondition;

public class SeekTarget extends BTCondition {
    QuadTree pqt;
    Vector3f curPos;
    GameObject target;

    public SeekTarget(QuadTree playerQuadTree, Vector3f position, GameObject target) {
        super(false);
        pqt = playerQuadTree;
        curPos = position;
        this.target = target;
    }

    @Override
    protected boolean check() {
        QuadTreePoint currPos = new QuadTreePoint(curPos.z(),
                curPos.x());
        QuadTreeNode prey;
        prey = pqt.findNearby(currPos, -1, null);

        if (prey != null) {
            target = (GameObject) prey.getData();
            return true;
        }
        return false;

    }

}
