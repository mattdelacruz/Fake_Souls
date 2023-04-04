package a3.network;

import java.util.UUID;

import org.joml.Vector3f;

import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

public class GhostAvatar extends GameObject {
    private UUID id;
    private Vector3f position;

    public GhostAvatar(UUID id, ObjShape s, TextureImage t, Vector3f p) {
        super(GameObject.root(), s, t);
        this.id = id;
        this.position = p;
    }

    public UUID getID() {
        return this.id;
    }

    public void setID(UUID id) {
        this.id = id;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public void setPosition(Vector3f pos) {
        this.position = pos;
    }

}
