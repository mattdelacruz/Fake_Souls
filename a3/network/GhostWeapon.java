package a3.network;

import java.util.UUID;

import org.joml.Vector3f;

import tage.AnimatedGameObject;
import tage.TextureImage;
import tage.shapes.AnimatedShape;

public class GhostWeapon extends AnimatedGameObject {
    private UUID id;
    private GhostAvatar owner;
    private int damage = 10;

    public GhostWeapon(UUID id, AnimatedShape ks, TextureImage kt, GhostAvatar newAvatar) {
        super(newAvatar, ks, kt);
        this.id = id;
    }

    public UUID getID() {
        return this.id;
    }

    public void setOwner(GhostAvatar owner) {
        this.owner = owner;
    }

    public GhostAvatar getOwner() {
        return this.owner;
    }

    public int getDamage() {
        return damage;
    }
}
