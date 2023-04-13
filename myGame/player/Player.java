package myGame.player;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import tage.GameObject;
import tage.ObjShape;
import tage.TargetCamera;
import tage.TextureImage;

public class Player extends GameObject {
    private TargetCamera camera;
    private boolean isLocked = false;

    public Player(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);
        setLocalScale(new Matrix4f().scaling(0.2f));
        setLocalLocation(new Vector3f(0, getLocalScale().get(0, 0), 0));
        getRenderStates().setRenderHiddenFaces(true);
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
