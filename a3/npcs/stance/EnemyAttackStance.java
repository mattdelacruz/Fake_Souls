package a3.npcs.stance;

public class EnemyAttackStance implements EnemyStanceState {

    @Override
    public String getAnimation() {
        return "ATTACK";
    }

    @Override
    public float getMoveValue() {
        return 0f;
    }

    @Override
    public boolean isAttacking() {
        return true;
    }

    @Override
    public boolean isNormal() {
        return false;
    }

    @Override
    public boolean isHunting() {
        return false;
    }

    @Override
    public boolean isDead() {
        return false;
    }

}
