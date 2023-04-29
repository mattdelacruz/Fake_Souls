package a3.npcs;

import org.joml.Matrix4f;

import tage.AnimatedGameObject;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

public class Enemy extends AnimatedGameObject {
    Matrix4f initialTranslation, initialScale;
    int id;

    public Enemy(GameObject p, ObjShape s, TextureImage t, int id) {
        //(float) (min + (Math.random() * ((max - min) + 1)));
        super(p, s, t);
//        float posX = (float) (40 + (Math.random() * ((60 - 40) + 1)));
        float posX = (float) 50f;

        float posZ = (float) 124f + (5 * id);
        this.id = id;
        setLocalScale((new Matrix4f()).scaling(0.5f));
        setLocalTranslation(new Matrix4f().translation(posX, 0.5f, posZ));
        getRenderStates().setRenderHiddenFaces(true);
    }

    @Override
    public void idle() {
        super.idle();
        System.out.println("playing enemy idle...");
    }
}
