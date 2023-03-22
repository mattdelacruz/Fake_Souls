package a3;

import java.util.concurrent.ThreadLocalRandom;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

public class Enemy extends GameObject {
    Matrix4f initialTranslation, initialScale;

    public Enemy(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);

        initialScale = (new Matrix4f()).scaling(1);

        setLocalLocation(new Vector3f(ThreadLocalRandom.current().nextInt(-50, 50), getLocalScale().get(0, 0),
                ThreadLocalRandom.current().nextInt(-50, 50)));
        setLocalScale(initialScale);
    }

}
