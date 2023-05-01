package a3.npcs;

import org.joml.Matrix4f;

import a3.player.Player;
import a3.quadtree.QuadTree;
import tage.AnimatedGameObject;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;
import tage.ai.behaviortrees.BTCompositeType;
import tage.ai.behaviortrees.BTSequence;
import tage.ai.behaviortrees.BehaviorTree;

public class Enemy extends AnimatedGameObject {
    private BehaviorTree ebt = new BehaviorTree(BTCompositeType.SEQUENCE);
    private QuadTree pqt;
    private GameObject target;
    private long thinkStartTime, tickStartTime;
    private long lastThinkUpdateTime, lastTickUpdateTime;

    public Enemy(GameObject p, ObjShape s, TextureImage t, QuadTree playerQuadTree, int id) {
        super(p, s, t);
        float posX = (float) 50f;
        float posZ = (float) 124f + (5 * id);
        pqt = playerQuadTree;
        setLocalScale((new Matrix4f()).scaling(0.5f));
        setLocalTranslation(new Matrix4f().translation(posX, 0.5f, posZ));
        setupBehaviorTree();
        thinkStartTime = System.nanoTime();
        tickStartTime = System.nanoTime();
        lastThinkUpdateTime = thinkStartTime;
        lastTickUpdateTime = tickStartTime;
    }

    @Override
    public void idle() {
        super.idle();
    }

    public void updateBehavior() {
        long currentTime = System.nanoTime();
        float elapsedThinkMilliSecs = (currentTime - lastThinkUpdateTime) / (1000000.0f);
        float elapsedTickMilliSecs = (currentTime - lastTickUpdateTime) / (1000000.0f);

        lastTickUpdateTime = currentTime;

        lastThinkUpdateTime = currentTime;
        ebt.update(elapsedThinkMilliSecs);

    }

    private void setupBehaviorTree() {
        ebt.insertAtRoot(new BTSequence(10));
        ebt.insert(10, new SeekTarget(pqt, this, this.getLocalLocation()));
        ebt.insert(10, new HuntTarget(this));
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    public GameObject getTarget() {
        return this.target;
    }
}
