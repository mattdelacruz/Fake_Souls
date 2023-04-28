package a3.player;

import javax.swing.Action;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import a3.MyGame;
import a3.ScriptManager;
import a3.player.movement.PlayerMovementState;
import a3.player.movement.PlayerRunMovementState;
import a3.player.movement.PlayerSprintMovementState;
import a3.player.stances.PlayerGuardStanceState;
import a3.player.stances.PlayerNormalStanceState;
import a3.player.stances.PlayerStanceState;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;
import tage.shapes.AnimatedShape;

public class Player extends GameObject {
    private AnimatedShape playerShape;
    private GameObject weapon;
    public int currFrame = 0;
    private boolean isLocked = false;
    private ScriptManager scriptManager;
    private PlayerStanceState stanceState;
    private PlayerMovementState movementState;
    private PlayerSprintMovementState sprint = new PlayerSprintMovementState();
    private PlayerRunMovementState run = new PlayerRunMovementState();
    private PlayerGuardStanceState guardStance = new PlayerGuardStanceState();
    private PlayerNormalStanceState normalStance = new PlayerNormalStanceState();

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
                new Vector3f((int) scriptManager.getValue("xPlayerPos"), 0,
                        (int) scriptManager.getValue("zPlayerPos")));
        setStanceState(normalStance);
        setMovementState(run);
        getRenderStates().setRenderHiddenFaces(true);
    }

    @Override
    public void move(Vector3f vec, float frameTime) {
        super.move(vec, (frameTime * getStanceState().getMoveValue() * getMovementState().getSpeed()));
        handleAnimationSwitch("RUN");
        if (MyGame.getGameInstance().getProtocolClient() != null) {
            MyGame.getGameInstance().getProtocolClient().sendMoveMessage(getWorldLocation());
        }
    }

    public void idle() {
        handleAnimationSwitch("IDLE");
        // weapon.handleAnimationSwitch("IDLE");
    }

    public void attack() {
        // swing weapon
        // calculate damage
    }

    public void guard() {
        // play guard animation, play some sound
        if (getMovementState() == sprint) {
            setMovementState(run);
        }
        setStanceState(guardStance);
    }

    public void unGuard() {
        setStanceState(normalStance);
    }

    public void sprint() {
        setMovementState(sprint);
    }

    public void run() {
        setMovementState(run);
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
        return movementState;
    }

    public PlayerStanceState getStanceState() {
        return stanceState;
    }

    public void updateAnimation() {
        getAnimationShape().updateAnimation();
        // weapon.getAnimationShape().updateAnimation();
    }

    @Override
    public void playAnimation(String animation) {
        super.playAnimation(animation);
        // if (weapon.getAnimationShape().getAnimation(animation) != null) {
        // weapon.getAnimationShape().playAnimation(animation, 0.25f,
        // AnimatedShape.EndType.PAUSE, 0);
        // }
    }

    public void addWeapon(GameObject weapon) {
        this.weapon = weapon;
    }
}
