package a3.npcs;

import tage.AnimatedGameObject;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

public class EnemyWeapon extends AnimatedGameObject {
    private Enemy owner;
    private int damage = 10;

    public EnemyWeapon(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);
    }

    public void setOwner(Enemy owner) {
        this.owner = owner;
    }

    public Enemy getOwner() {
        return this.owner;
    }

    public int getDamage() {
        return damage;
    }
}
