package a3.npcs.enemybehavior;

import javax.swing.event.SwingPropertyChangeSupport;

import org.joml.Vector3f;

import a3.MyGame;
import a3.npcs.Enemy;
import a3.quadtree.QuadTree;
import a3.quadtree.QuadTreeNode;
import a3.quadtree.QuadTreePoint;
import tage.GameObject;
import tage.ai.behaviortrees.BTCondition;

public class SeekTarget extends BTCondition {
    QuadTree pqt;
    Vector3f pos;
    GameObject target, hunter;

    public SeekTarget(QuadTree playerQuadTree, GameObject hunter, Vector3f pos) {
        super(false);
        pqt = playerQuadTree;
        this.hunter = hunter;
        this.pos = pos;
    }

    @Override
    protected boolean check() {
        QuadTreePoint currPos = new QuadTreePoint(pos.z(),
                pos.x());
        QuadTreeNode prey;
        prey = pqt.findNearby(currPos, -1, null);

        if (prey != null) {
            target = (GameObject) prey.getData();
            ((Enemy) hunter).setTarget(target);
            return true;
        }
        // System.out.println("no prey found...");
        return false;
    }

}
