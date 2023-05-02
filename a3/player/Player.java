package a3.player;

import javax.swing.Action;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import a3.MyGame;
import a3.ScriptManager;
import a3.player.movement.PlayerMovementState;
import a3.player.movement.PlayerRunMovementState;
import a3.player.movement.PlayerSprintMovementState;
import a3.player.stances.PlayerAttackStanceState;
import a3.player.stances.PlayerGuardStanceState;
import a3.player.stances.PlayerNormalStanceState;
import a3.player.stances.PlayerStanceState;
import tage.AnimatedGameObject;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;
import tage.shapes.AnimatedShape;

public class Player extends AnimatedGameObject {
    private AnimatedShape playerShape;
    private AnimatedGameObject weapon;
    public int currFrame = 0;
    private boolean isLocked = false;
    private ScriptManager scriptManager;
    private PlayerStanceState stanceState;
    private PlayerMovementState movementState;
    private PlayerAttackStanceState attackStance = new PlayerAttackStanceState();
    private PlayerGuardStanceState guardStance = new PlayerGuardStanceState();
    private PlayerNormalStanceState normalStance = new PlayerNormalStanceState();
    private PlayerRunMovementState runMovement = new PlayerRunMovementState();
    private PlayerSprintMovementState sprintMovement = new PlayerSprintMovementState();
    /*
     * player states --- if in the same state category, then it is mutually
     * exclusive :
     *
     * stance states:
     * guard - player goes into guard mode, cannot attack, speed is halved,
     * damage taken is halved
     * 
     * normal - player is in a normal state, movement, damage taken, speed
     * is normal
     * 
     * movement states:
     * run - player is normally running, doesn't burn stamina
     * 
     * sprint - player moves 2x the normal run, burns stamina
     * 
     */

    public Player(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);
        scriptManager = MyGame.getGameInstance().getScriptManager();
        scriptManager.loadScript("assets/scripts/LoadInitValues.js");
        setLocalScale(new Matrix4f().scaling(.2f));
        System.out.println(getLocalScale().get(1, 1));
        setLocalLocation(
                new Vector3f((int) scriptManager.getValue("xPlayerPos"), (int) scriptManager.getValue("yPlayerPos"),
                        (int) scriptManager.getValue("zPlayerPos")));
        setStanceState(normalStance);
        setMovementState(runMovement);
        getRenderStates().setRenderHiddenFaces(true);
    }

    @Override
    public void move(Vector3f vec, float frameTime) {
        super.move(vec, (frameTime * getStanceState().getMoveValue() * getMovementState().getSpeed()));
        this.handleAnimationSwitch(getMovementState().getAnimation());
        if (MyGame.getGameInstance().getProtocolClient() != null) {
            MyGame.getGameInstance().getProtocolClient().sendMoveMessage(getWorldLocation());
        }
    }

    /* to be called by keyboard/gamepad events only */
    public void attack() {
        System.out.println("attacking..");
        if (getMovementState().isSprinting()) {
            setMovementState(runMovement);
        }
        setStanceState(attackStance);
        handleAnimationSwitch(getStanceState().getAnimation(), 0.25f);
    }

    /* to be called by keyboard/gamepad events only */
    public void guard() {
        // play guard animation, play some sound
        if (getMovementState().isSprinting()) {
            setMovementState(runMovement);
        }
        setStanceState(guardStance);
        this.handleAnimationSwitch(getStanceState().getAnimation());
    }

    /* to be called by keyboard/gamepad events only */
    public void unGuard() {
        setStanceState(normalStance);
    }

    /* to be called by keyboard/gamepad events only */
    public void sprint() {
        setMovementState(sprintMovement);
    }

    /* to be called by keyboard/gamepad events only */
    public void run() {
        setMovementState(runMovement);
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        weapon.updateAnimation();
    }

    @Override
    public void handleAnimationSwitch(String animation, float speed) {
        super.handleAnimationSwitch(animation);
        if (weapon.getAnimationShape().getAnimation(animation) != null) {
            weapon.handleAnimationSwitch(animation);
        }
    }

    @Override
    public void idle() {
        super.idle();
        setStanceState(normalStance);
        weapon.idle();
    }

    public void setLock(boolean lock) {
        isLocked = lock;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setStanceState(PlayerStanceState stanceState) {
        this.stanceState = stanceState;
    }

    public void setMovementState(PlayerMovementState moveState) {
        this.movementState = moveState;
    }

    public PlayerMovementState getMovementState() {
        return this.movementState;
    }

    public PlayerStanceState getStanceState() {
        return this.stanceState;
    }

    public void addWeapon(AnimatedGameObject weapon) {
        this.weapon = weapon;
    }
}
