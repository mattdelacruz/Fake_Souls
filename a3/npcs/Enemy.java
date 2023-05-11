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
import a3.npcs.stance.EnemyDeadStance;
import a3.npcs.stance.EnemyFlinchStance;
import a3.npcs.stance.EnemyHuntStance;
import a3.npcs.stance.EnemyNormalStance;
import a3.npcs.stance.EnemyStanceState;
import a3.quadtree.QuadTree;
import tage.ActiveEntityObject;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;
import tage.ai.behaviortrees.BTCompositeType;
import tage.ai.behaviortrees.BTSequence;
import tage.ai.behaviortrees.BehaviorTree;
import tage.audio.SoundType;

public class Enemy extends ActiveEntityObject {
    private static final float ATTACK_RANGE = 5f;
    private static final float ATTACK_CONE = (float) Math.toRadians(45f);

    private BehaviorTree ebt = new BehaviorTree(BTCompositeType.SEQUENCE);
    private QuadTree pqt;
    private GameObject target;
    private EnemyWeapon weapon;
    private long thinkStartTime, tickStartTime;
    private long lastThinkUpdateTime, lastTickUpdateTime;
    private EnemyMovementState movementState;
    private EnemyRunMovementState runMovement = new EnemyRunMovementState();
    private EnemyStanceState stanceState;
    private EnemyAttackStance attackStance = new EnemyAttackStance();
    private EnemyNormalStance normalStance = new EnemyNormalStance();
    private EnemyFlinchStance flinchStance = new EnemyFlinchStance();
    private boolean step1isPlayed = false;
    private boolean step2isPlayed = false;
    private boolean isAttacking = false;
    private float elapsedThinkMilliSecs;

    public Enemy(GameObject p, ObjShape s, TextureImage t, QuadTree playerQuadTree, int id) {
        super(p, s, t, 100);
        float posX = (float) 50f;
        float posZ = (float) 124f + (5 * id);
        pqt = playerQuadTree;

        setLocalScale((new Matrix4f()).scaling(0.5f));
        setLocalTranslation(new Matrix4f().translation(posX, 0.5f, posZ));
        setupBehaviorTree();
        initializeSounds();
        movementState = runMovement;
        stanceState = normalStance;
        tickStartTime = System.nanoTime();
        lastThinkUpdateTime = thinkStartTime;
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
    public void handleAnimationSwitch(String animation, float speed) {
        super.handleAnimationSwitch(animation, speed);
        if (weapon.getAnimationShape().getAnimation(animation) != null) {
            weapon.handleAnimationSwitch(animation, speed);
        }
    }

    @Override
    public void idle() {
        super.idle();
        weapon.idle();
    }

    public void attack() {
        setStanceState(attackStance);
        handleAnimationSwitch(getStanceState().getAnimation(), .5f);
        elapsedThinkMilliSecs = 0f;
    }

    public void flinch() {
        if (getAnimationShape().getAnimation("ATTACK").equals(getAnimationShape().getCurrentAnimation())
                && getAnimationShape().isAnimPlaying()) {
            getAnimationShape().stopAnimation();
        }

        setStanceState(flinchStance);
        handleAnimationSwitch(getStanceState().getAnimation(), 2f);
    }

    @Override
    public void move(Vector3f vec, float frameTime) {
        super.move(vec, (frameTime * getMovementState().getSpeed()));
        handleAnimationSwitch(getMovementState().getAnimation(), 1f);
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
        float elapsedThinkMilliSecs = (currentTime - lastThinkUpdateTime) /
                (1000000.0f);
        float elapsedTickMilliSecs = (currentTime - lastTickUpdateTime) / (1000000.0f);
        if (!getStanceState().isDead()) {

            isAttacking = checkIfAttacking();

            if (getStanceState().isHunting()) {
                move(getLocalForwardVector(), MyGame.getGameInstance().getFrameTime());
            }

            if (elapsedThinkMilliSecs >= 1500f) {
                if (getStanceState().isFlinched()) {
                    lastTickUpdateTime = currentTime;
                    setStanceState(normalStance);
                }

                if (getStanceState().isAttacking()) {
                    attack();
                }
                lastThinkUpdateTime = currentTime;
                ebt.update(elapsedThinkMilliSecs);
            }
        }
    }

    private boolean checkIfAttacking() {
        return (getAnimationShape().isAnimPlaying() &&
                getAnimationShape().getCurrentAnimation().equals(getAnimationShape().getAnimation("ATTACK")));
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

    public void addWeapon(EnemyWeapon weapon) {
        this.weapon = weapon;
    }

    public EnemyWeapon getWeapon() {
        return this.weapon;
    }

    public float getAttackCone() {
        return ATTACK_CONE;
    }

    public boolean checkIfDead() {
        if (getHealth() <= 0) {
            setStanceState(new EnemyDeadStance());
            handleAnimationSwitch(getStanceState().getAnimation(), 1f);
            MyGame.getGameInstance().getEnemyController().removeTarget(this);
            return true;
        }
        return false;
    }

    public void damageSound() {

    }

    public boolean isAttacking() {
        return isAttacking;
    }
}
