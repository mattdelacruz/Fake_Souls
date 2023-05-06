package a3.npcs;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import a3.MyGame;
import a3.managers.ScriptManager;
import a3.managers.SoundManager;
import a3.npcs.enemybehavior.HuntTarget;
import a3.npcs.enemybehavior.KillTarget;
import a3.npcs.enemybehavior.SeekTarget;
import a3.npcs.movement.EnemyMovementState;
import a3.npcs.movement.EnemyRunMovementState;
import a3.npcs.stance.EnemyAttackStance;
import a3.npcs.stance.EnemyStanceState;
import a3.quadtree.QuadTree;
import tage.AnimatedGameObject;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;
import tage.ai.behaviortrees.BTCompositeType;
import tage.ai.behaviortrees.BTSequence;
import tage.ai.behaviortrees.BehaviorTree;
import tage.audio.SoundType;

public class Enemy extends AnimatedGameObject {
    private static final float ATTACK_RANGE = 5f;

    private BehaviorTree ebt = new BehaviorTree(BTCompositeType.SEQUENCE);
    private QuadTree pqt;
    private GameObject target;
    private AnimatedGameObject weapon;
    private long thinkStartTime, tickStartTime;
    private long lastThinkUpdateTime, lastTickUpdateTime;
    private EnemyMovementState movementState;
    private EnemyRunMovementState runMovement = new EnemyRunMovementState();
    private EnemyStanceState stanceState;
    private EnemyAttackStance attackStance = new EnemyAttackStance();
    private boolean step1isPlayed = false;
    private boolean step2isPlayed = false;
    private float elapsedThinkMilliSecs;

    public Enemy(GameObject p, ObjShape s, TextureImage t, QuadTree playerQuadTree, int id) {
        super(p, s, t);
        float posX = (float) 50f;
        float posZ = (float) 124f + (5 * id);
        pqt = playerQuadTree;

        setLocalScale((new Matrix4f()).scaling(0.5f));
        setLocalTranslation(new Matrix4f().translation(posX, 0.5f, posZ));
        setupBehaviorTree();
        initializeSounds();
        movementState = runMovement;
        thinkStartTime = System.nanoTime();
        tickStartTime = System.nanoTime();
        lastThinkUpdateTime = thinkStartTime;
        lastTickUpdateTime = tickStartTime;
    }

    private void initializeSounds() {
        getSoundManager().addSound(
                "STEP1", (String) getScriptManager().getValue("STEP1"), 20, false, (float) 20f, 0, 5.0f,
                getLocalLocation(), SoundType.SOUND_EFFECT);
        getSoundManager().addSound(
                "STEP2", (String) getScriptManager().getValue("STEP1"), 20, false, (float) 20f, 0, 5.0f,
                getLocalLocation(), SoundType.SOUND_EFFECT);
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        weapon.updateAnimation();
    }

    @Override
    public void handleAnimationSwitch(String animation) {
        super.handleAnimationSwitch(animation);
        if (weapon.getAnimationShape().getAnimation(animation) != null) {
            weapon.handleAnimationSwitch(animation);
        }
    }

    @Override
    public void idle() {
        super.idle();
        weapon.idle();
    }

    public void attack() {
        if (elapsedThinkMilliSecs >= 1000f) {
            System.out.println("attacking...");
            setStanceState(attackStance);
            handleAnimationSwitch(getStanceState().getAnimation());
            elapsedThinkMilliSecs = 0f;
        }
    }

    @Override
    public void move(Vector3f vec, float frameTime) {
        super.move(vec, (frameTime * getMovementState().getSpeed()));
        handleAnimationSwitch(getMovementState().getAnimation());
        if (!step1isPlayed && !MyGame.getGameInstance().getSoundManager().isPlaying("STEP2")) {
            MyGame.getGameInstance().getSoundManager().playSound("STEP1");
            step2isPlayed = false;
            step1isPlayed = true;
        } else if (!step2isPlayed && !MyGame.getGameInstance().getSoundManager().isPlaying("STEP1")) {
            MyGame.getGameInstance().getSoundManager().playSound("STEP2");
            step1isPlayed = false;
            step2isPlayed = true;
        }
        if (MyGame.getGameInstance().getProtocolClient() != null) {
            MyGame.getGameInstance().getProtocolClient().sendMoveMessage(getWorldLocation());
        }
    }

    public void updateBehavior() {
        long currentTime = System.nanoTime();
        elapsedThinkMilliSecs += (currentTime - lastThinkUpdateTime) / 
        (1000000.0f);
        float elapsedTickMilliSecs = (currentTime - lastTickUpdateTime) / (1000000.0f);

        lastTickUpdateTime = currentTime;
        lastThinkUpdateTime = currentTime;
        ebt.update(elapsedThinkMilliSecs);
    }

    private void setupBehaviorTree() {
        ebt.insertAtRoot(new BTSequence(10));
        ebt.insert(10, new SeekTarget(pqt, this, this.getLocalLocation()));
        ebt.insert(10, new HuntTarget(this));
        ebt.insert(10, new KillTarget(this));
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    public GameObject getTarget() {
        return this.target;
    }

    public EnemyMovementState getMovementState() {
        return this.movementState;
    }

    public float getAttackRange() {
        return ATTACK_RANGE;
    }

    public EnemyStanceState getStanceState() {
        return stanceState;
    }

    public void setStanceState(EnemyStanceState s) {
        stanceState = s;
    }

    public void addWeapon(AnimatedGameObject weapon) {
        this.weapon = weapon;
    }
}
