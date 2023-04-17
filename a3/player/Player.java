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

    public Player(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);
        jsEngine = MyGame.getGameInstance().getScriptEngine();
        scriptFile = new File("assets/scripts/LoadInitValues.js");
        MyGame.getGameInstance().executeScript(jsEngine, scriptFile);
        setLocalScale(new Matrix4f().scaling(.2f));
        setLocalLocation(new Vector3f((int)jsEngine.get("xpos"), getLocalScale().get(0, 0), (int)jsEngine.get("zpos")));
        getRenderStates().setRenderHiddenFaces(true);
    }

    @Override
    public void move(Vector3f vec, float frameTime) {
        super.move(vec, frameTime);
        if (MyGame.getGameInstance().getProtocolClient() != null) {
            MyGame.getGameInstance().getProtocolClient().sendMoveMessage(getWorldLocation());
        }
    }

    public void attack() {
        //swing weapon
        //calculate damage

    }

    public void guard() {
        //set the player into a guard state
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
}
