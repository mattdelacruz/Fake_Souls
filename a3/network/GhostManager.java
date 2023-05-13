package a3.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import a3.MyGame;
import a3.player.Player;
import tage.ObjShape;
import tage.TextureImage;
import tage.VariableFrameRateGame;
import tage.physics.PhysicsObject;
import tage.shapes.AnimatedShape;

public class GhostManager {
    private MyGame game;
    private Vector<GhostAvatar> ghostAvatars = new Vector<GhostAvatar>();
    private Map<Integer, GhostAvatar> ghostMap = new HashMap<Integer, GhostAvatar>();
    private Map<Integer, GhostWeapon> ghostWeaponMap = new HashMap<Integer, GhostWeapon>();
    private float[] vals = new float[16];

    public GhostManager(VariableFrameRateGame vfrg) {
        this.game = (MyGame) vfrg;
    }

    public void removeGhostAvatar(UUID ghostID) {
        GhostAvatar ghostAvatar = findAvatar(ghostID);
        if (ghostAvatar != null) {
            ghostAvatar.getRenderStates().disableRendering();
            MyGame.getEngine().getSceneGraph().removeGameObject(ghostAvatar);
            ghostAvatars.remove(ghostAvatar);
        } else {
            System.out.println("tried to remove, but unable to find ghost in list");
        }
    }

    public void createGhostAvatar(UUID id, Vector3f pos) throws IOException {
        System.out.println("adding ghost with ID --> " + id);
        float mass = 1f;
        double[] tempTransform;
        float[] size;
        float[] vals = new float[16];
        Matrix4f translation;
        PhysicsObject ghostBody, ghostWeaponBody;

        AnimatedShape s = game.getGhostShape();
        TextureImage t = game.getGhostTexture();
        GhostAvatar newAvatar = new GhostAvatar(id, s, t, pos);
        AnimatedShape ks = game.getGhostKatanaShape();
        TextureImage kt = game.getGhostKatanaTexture();
        GhostWeapon newKatana = new GhostWeapon(id, ks, kt, newAvatar);
        newAvatar.addWeapon(newKatana);
        newKatana.setOwner(newAvatar);
        newKatana.propagateRotation(true);

        translation = newAvatar.getLocalTranslation();
        tempTransform = game.toDoubleArray(translation.get(vals));

        ghostBody = game.getPhysicsManager().addCapsuleObject(mass, tempTransform, 1f, 5f);
        newAvatar.setPhysicsObject(ghostBody);

        size = new float[] { 3f, 3f, 3f };

        ghostWeaponBody = game.getPhysicsManager().addBoxObject(mass, tempTransform, size);
        newKatana.setPhysicsObject(ghostWeaponBody);

        game.getAnimationController().addTarget(newAvatar);
        game.addToPlayerQuadTree(newAvatar);
        addToGhostMap(newAvatar);
        addToGhostWeaponMap(newKatana);
        ghostAvatars.add(newAvatar);
    }

    private void addToGhostMap(GhostAvatar ghost) {
        ghostMap.put(ghost.getPhysicsObject().getUID(), ghost);
    }

    public void addToGhostWeaponMap(GhostWeapon ghostWeapon) {
        ghostWeaponMap.put(ghostWeapon.getPhysicsObject().getUID(), ghostWeapon);
    }

    public GhostAvatar getGhostFromMap(int uid) {
        return ghostMap.get(uid);
    }

    public GhostWeapon getGhostWeaponFromMap(int uid) {
        return ghostWeaponMap.get(uid);
    }

    private GhostAvatar findAvatar(UUID ghostID) {
        GhostAvatar ghostAvatar;
        Iterator<GhostAvatar> it = ghostAvatars.iterator();

        while (it.hasNext()) {
            ghostAvatar = it.next();
            if (ghostAvatar.getID().equals(ghostID)) {
                return ghostAvatar;
            }
        }
        return null;
    }

    public void updateGhostAvatar(UUID ghostID, Vector3f pos) {
        GhostAvatar ghostAvatar = findAvatar(ghostID);
        if (ghostAvatar != null) {
            ghostAvatar.setPosition(pos);
            updatePhysicsObjectLocation(ghostAvatar.getPhysicsObject(), ghostAvatar.getLocalTranslation());
        } else {
            System.out.println("Can't find ghost!!");

        }
    }

    private PhysicsObject updatePhysicsObjectLocation(PhysicsObject po, Matrix4f localTranslation) {
        Matrix4f translation = new Matrix4f();
        double[] tempTransform;
        translation = new Matrix4f(localTranslation);
        tempTransform = game.toDoubleArray(translation.get(vals));
        po.setTransform(tempTransform);
        return po;
    }

    public void updateGhostAvatarAnimation(UUID ghostID, String animation) {
        GhostAvatar ghostAvatar = findAvatar(ghostID);
        if (ghostAvatar != null) {
            if (animation.equals("IDLE")) {
                ghostAvatar.idle();
            } else {
                ghostAvatar.handleAnimationSwitch(animation, 1f);
            }
        } else {
            System.out.println("Can't find ghost!!");
        }
    }

    public void updateGhostAvatarYaw(UUID ghostID, float rotation) {
        GhostAvatar ghostAvatar = findAvatar(ghostID);
        if (ghostAvatar != null) {
            ghostAvatar.yaw(game.getFrameTime(), rotation);
        } else {
            System.out.println("Can't find ghost!!");
        }
    }

    public void updateGhostAvatarAttack(UUID ghostID, boolean isAttack) {
        GhostAvatar ghostAvatar = findAvatar(ghostID);
        if (ghostAvatar != null) {
            ghostAvatar.setAttack(isAttack);
        } else {
            System.out.println("Can't find ghost!!");
        }
    }

    public void updateGhostAvatarDamage(UUID ghostID, int damage) {
        GhostAvatar ghostAvatar = findAvatar(ghostID);
        if (ghostAvatar != null) {
            ghostAvatar.getOwner().reduceHealth(damage);
        } else {
            System.out.println("Can't find ghost!!");
        }
    }

    public void updateGhostAvatarHealth(UUID ghostID, int health) {
        GhostAvatar ghostAvatar = findAvatar(ghostID);
        if (ghostAvatar != null) {
            ghostAvatar.setHealth(health);
        } else {
            System.out.println("Can't find ghost!!");
        }
    }

    public void setOwner(UUID ghostID, Player player) {
        GhostAvatar ghostAvatar = findAvatar(ghostID);
        if (ghostAvatar != null) {
            ghostAvatar.setOwner(player);
        } else {
            System.out.println("Can't find ghost avatar to set owner to!!");
        }
    }

}
