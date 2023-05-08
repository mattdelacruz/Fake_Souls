package a3.player;

import tage.AnimatedGameObject;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

public class PlayerWeapon extends AnimatedGameObject {
    private static final int DAMAGE = 10;

    public PlayerWeapon(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);
    }

    public int getDamage() {
        return DAMAGE;
    }
}
