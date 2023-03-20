package a3;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

public class Player extends GameObject {
    Matrix4f initialTranslation, initialScale;

    public Player(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);

        initialScale = (new Matrix4f()).scaling(1);
        initialTranslation = (new Matrix4f()).translation(new Vector3f(0, getLocalScale().get(0, 0), 0));

        setLocalTranslation(initialTranslation);
        setLocalScale(initialScale);
    }

}
