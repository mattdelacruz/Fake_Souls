package a3.npcs;

import org.joml.Matrix4f;

import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

public class Enemy extends GameObject {
    Matrix4f initialTranslation, initialScale;

    public Enemy(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);
        float posX = (float) (-50 + (Math.random() * ((50 + 50) + 1)));
        float posZ = (float) (-50 + (Math.random() * ((50 + 50) + 1)));

        setLocalScale((new Matrix4f()).scaling(0.5f));
        setLocalTranslation(new Matrix4f().translation(posX, 0.5f, posZ));
        getRenderStates().setRenderHiddenFaces(true);
    }
}
