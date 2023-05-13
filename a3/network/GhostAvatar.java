package a3.network;

import java.util.UUID;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import a3.player.Player;
import tage.ActiveEntityObject;
import tage.GameObject;
import tage.TextureImage;
import tage.shapes.AnimatedShape;

public class GhostAvatar extends ActiveEntityObject {
    private UUID id;
    private GhostWeapon weapon;
    private Player owner;
    private boolean isAttack = false;
    private int enemyHealth;

    public GhostAvatar(UUID id, AnimatedShape s, TextureImage t, Vector3f p) {
        super(GameObject.root(), s, t, 100);
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

    public GhostWeapon getWeapon() {
        return weapon;
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

    public void setAttack(boolean isAttack) {
        this.isAttack = isAttack;
    }

    public boolean isAttacking() {
        return this.isAttack;
    }

    @Override
    public void idle() {
        super.idle();
        weapon.idle();
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    public Player getOwner() {
        return owner;
    }
}
