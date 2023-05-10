package a3.network;

import java.util.UUID;

import org.joml.Vector3f;

import tage.AnimatedGameObject;
import tage.TextureImage;
import tage.shapes.AnimatedShape;

public class GhostWeapon extends AnimatedGameObject {
    private UUID id;

    public GhostWeapon(UUID id, AnimatedShape ks, TextureImage kt, GhostAvatar newAvatar) {
        super(newAvatar, ks, kt);
        this.id = id;
    }

    public UUID getID() {
        return this.id;
    }

}
