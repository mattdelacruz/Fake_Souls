package a3.player;

import javax.swing.Action;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import a3.MyGame;
import a3.managers.ScriptManager;
import a3.managers.SoundManager;
import a3.player.movement.PlayerMovementState;
import a3.player.movement.PlayerRunMovementState;
import a3.player.movement.PlayerSprintMovementState;
import a3.player.stances.PlayerAttackStanceState;
import a3.player.stances.PlayerDeadStance;
import a3.player.stances.PlayerGuardStanceState;
import a3.player.stances.PlayerNormalStanceState;
import a3.player.stances.PlayerStanceState;
import tage.ActiveEntityObject;
import tage.AnimatedGameObject;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;
import tage.audio.SoundType;
import tage.shapes.AnimatedShape;

public class Player extends ActiveEntityObject {
    private PlayerWeapon weapon;
    public int currFrame = 0;
    private boolean isLocked = false;
    private boolean step1isPlayed = false;
    private boolean step2isPlayed = false;
    private boolean canMove = true;
    private PlayerStanceState stanceState;
    private PlayerMovementState movementState;
    private PlayerAttackStanceState attackStance = new PlayerAttackStanceState();
    private PlayerGuardStanceState guardStance = new PlayerGuardStanceState();
    private PlayerNormalStanceState normalStance = new PlayerNormalStanceState();
    private PlayerRunMovementState runMovement = new PlayerRunMovementState();
    private PlayerSprintMovementState sprintMovement = new PlayerSprintMovementState();

    private Vector3f validLocation;
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
        super(p, s, t, 100);
        setLocalScale(new Matrix4f().scaling(.2f));
        setLocalLocation(
                new Vector3f((int) getScriptManager().getValue("xPlayerPos"),
                        (int) getScriptManager().getValue("yPlayerPos"),
                        (int) getScriptManager().getValue("zPlayerPos")));
        setStanceState(normalStance);
        setMovementState(runMovement);
        // initializeSounds();
    }

    private void initializeSounds() {
        int backgroundMusicRange = (int) getScriptManager().getValue("PLAY_AREA_SIZE");

        getSoundManager().addSound(
                "STEP1", (String) getScriptManager().getValue("STEP1"), 5, false, (float) backgroundMusicRange, 0, 0,
                getLocalLocation(), SoundType.SOUND_EFFECT);
        getSoundManager().addSound(
                "STEP2", (String) getScriptManager().getValue("STEP1"), 5, false, (float) backgroundMusicRange, 0, 0,
                getLocalLocation(), SoundType.SOUND_EFFECT);

    }

    @Override
    public void move(Vector3f vec, float frameTime) {
        if (stanceState == normalStance) {
            canMove = true;
        }
        if (canMove) {
            super.move(vec, (frameTime * getStanceState().getMoveValue() * getMovementState().getSpeed()));
            // if (!step1isPlayed &&
            // !MyGame.getGameInstance().getSoundManager().isPlaying("STEP2")) {
            // MyGame.getGameInstance().getSoundManager().playSound("STEP1");
            // step2isPlayed = false;
            // step1isPlayed = true;
            // } else if (!step2isPlayed &&
            // !MyGame.getGameInstance().getSoundManager().isPlaying("STEP1")) {
            // MyGame.getGameInstance().getSoundManager().playSound("STEP2");
            // step1isPlayed = false;
            // step2isPlayed = true;
            // }
            handleAnimationSwitch(getMovementState().getAnimation(), 1f);
            if (MyGame.getGameInstance().getProtocolClient() != null) {
                MyGame.getGameInstance().getProtocolClient().sendMoveMessage(getWorldLocation());
            }
        }
    }

    /* to be called by keyboard/gamepad events only */
    public void attack() {
        setLastValidLocation(getLocalLocation());
        if (getMovementState().isSprinting()) {
            setMovementState(runMovement);
        }

        if (isMoving()) {
            canMove = false;
        }

        if (!getStanceState().isAttacking()) {
            setStanceState(attackStance);
        }
        handleAnimationSwitch(getStanceState().getAnimation(), 1f);

        if (!getAnimationShape().isAnimPlaying()) {
            setStanceState(normalStance);
        }
    }

    /* to be called by keyboard/gamepad events only */
    public void guard() {

        // play guard animation, play some sound
        if (getMovementState().isSprinting()) {
            setMovementState(runMovement);
        }
        setStanceState(guardStance);
        // setMovementState(guardMovement);
        handleAnimationSwitch(getStanceState().getAnimation(), 1f);
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
        super.handleAnimationSwitch(animation, speed);
        if (weapon.getAnimationShape().getAnimation(animation) != null) {
            weapon.handleAnimationSwitch(animation, speed);
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

    public void addWeapon(PlayerWeapon weapon) {
        this.weapon = weapon;
    }

    public PlayerWeapon getWeapon() {
        return this.weapon;
    }

    public void setLastValidLocation(Vector3f newLocation) {
        validLocation = newLocation;
    }

    public Vector3f getLastValidLocation() {
        return validLocation;
    }

    public void checkIfDead() {
        if (getHealth() <= 0) {
            setStanceState(new PlayerDeadStance());
        }
    }

}
