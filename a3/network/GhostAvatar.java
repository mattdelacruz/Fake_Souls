package a3.network;

import java.util.UUID;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import tage.ActiveEntityObject;
import tage.GameObject;
import tage.TextureImage;
import tage.shapes.AnimatedShape;

public class GhostAvatar extends ActiveEntityObject {
    private UUID id;
    private GhostWeapon weapon;

    public GhostAvatar(UUID id, AnimatedShape s, TextureImage t, Vector3f p) {
        super(GameObject.root(), s, t, 10000000);
        this.id = id;
        setPosition(p);
        setLocalScale(new Matrix4f().scaling(.2f));
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

    public void addWeapon(GhostWeapon katana) {
        weapon = katana;
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
        weapon.idle();
    }

}
