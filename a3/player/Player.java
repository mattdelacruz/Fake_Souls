package a3.player;

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

    public Player(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);
        setLocalScale(new Matrix4f().scaling(0.2f));
        setLocalLocation(new Vector3f(50, getLocalScale().get(0, 0), 50));
        getRenderStates().setRenderHiddenFaces(true);
    }

    @Override
    public void move(Vector3f vec, float frameTime) {
        super.move(vec, frameTime);
        if (MyGame.getGameInstance().getProtocolClient() != null) {
            MyGame.getGameInstance().getProtocolClient().sendMoveMessage(getWorldLocation());
        }

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
