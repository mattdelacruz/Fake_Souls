package a3.network;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import a3.MyGame;
import tage.ObjShape;
import tage.TextureImage;
import tage.VariableFrameRateGame;

public class GhostManager {
    private MyGame game;
    private Vector<GhostAvatar> ghostAvatars = new Vector<GhostAvatar>();

    public GhostManager(VariableFrameRateGame vfrg) {
        this.game = (MyGame) vfrg;
    }

    public void removeGhostAvatar(UUID ghostID) {
        GhostAvatar ghostAvatar = findAvatar(ghostID);
        if (ghostAvatar != null) {
            VariableFrameRateGame.getEngine().getSceneGraph().removeGameObject(ghostAvatar);
            ghostAvatars.remove(ghostAvatar);
        }
    }

    public void createGhost(UUID id, Vector3f p) throws IOException {
        ObjShape s = game.getGhostShape();
        TextureImage t = game.getGhostTexture();
        GhostAvatar newAvatar = new GhostAvatar(id, s, t, p);
        Matrix4f initialScale = (new Matrix4f()).scaling(0.25f);
        newAvatar.setLocalScale(initialScale);
        ghostAvatars.add(newAvatar);
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
        } else {
            System.out.println("Can't find ghost!!");
        }
    }

}
