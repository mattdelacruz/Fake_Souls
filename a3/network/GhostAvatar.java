package a3.network;

import java.util.UUID;

import org.joml.Vector3f;

import tage.AnimatedGameObject;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

public class GhostAvatar extends AnimatedGameObject {
    private UUID id;

    public GhostAvatar(UUID id, ObjShape s, TextureImage t, Vector3f p) {
        super(GameObject.root(), s, t);
        this.id = id;
        setPosition(p);
    }

    public UUID getID() {
        return this.id;
    }

    public Vector3f getPosition() {
        return getWorldLocation();
    }

    public void setPosition(Vector3f pos) {
        setLocalLocation(pos);
    }

}
