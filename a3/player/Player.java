package a3.player;

import java.io.File;

import javax.script.ScriptEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import a3.MyGame;
import tage.GameObject;
import tage.ObjShape;
import tage.TargetCamera;
import tage.TextureImage;

public class Player extends GameObject {
    private TargetCamera camera;
    private boolean isLocked = false;
    private ScriptEngine jsEngine;
    private File scriptFile;
    private PlayerStanceState stanceState;
    private PlayerMovementState movementState;
    private PlayerSprintMovementState sprint = new PlayerSprintMovementState();
    private PlayerRunMovementState run = new PlayerRunMovementState();
    private PlayerGuardStanceState guardStance = new PlayerGuardStanceState();
    private PlayerNormalStanceState normalStance = new PlayerNormalStanceState();

    //player states --- if in the same state, then it is mutually exclusive : 
    /* stance states:
     *      guard - player goes into guard mode 
     *      normal - player is in a normal state, movement, damage taken, speed 
     *              is normal
     *      
     * movement states:
     *      run - player is normally running, doesn't burn stamina
     *      sprint - player moves 2x the normal run, burns stamina more
     * 
     * 
     *      
     */
    public Player(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);
        jsEngine = MyGame.getGameInstance().getScriptEngine();
        scriptFile = new File("assets/scripts/LoadInitValues.js");
        MyGame.getGameInstance().executeScript(jsEngine, scriptFile);
        setLocalScale(new Matrix4f().scaling(.2f));
        setLocalLocation(new Vector3f((int)jsEngine.get("xpos"), getLocalScale().get(0, 0), (int)jsEngine.get("zpos")));
        setStanceState(normalStance);
        setMovementState(run);
        getRenderStates().setRenderHiddenFaces(true);
    }

    @Override
    public void move(Vector3f vec, float frameTime) {
        super.move(vec, (frameTime * getStanceState().getMoveValue() * getMovementState().getSpeed()));
        if (MyGame.getGameInstance().getProtocolClient() != null) {
            MyGame.getGameInstance().getProtocolClient().sendMoveMessage(getWorldLocation());
        }
    }

    public void attack() {
        //swing weapon
        //calculate damage
    }

    public void guard() {
        //play guard animation
        this.setStanceState(guardStance);
    }

    public void unGuard() {
        this.setStanceState(normalStance);
    }

    public void sprint() {
        this.setMovementState(sprint);
    }

    public void run() {
        this.setMovementState(run);
    }

    public TargetCamera getCamera() {
        return camera;
    }

    public void setCamera(TargetCamera c) {
        camera = c;
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

    
}
