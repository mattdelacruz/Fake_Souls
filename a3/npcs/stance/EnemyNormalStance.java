package a3.npcs.stance;

public class EnemyNormalStance implements EnemyStanceState {

    @Override
    public float getMoveValue() {
        return 0.5f;
    }

    @Override
    public String getAnimation() {
        return "IDLE";
    }

    @Override
    public boolean isAttacking() {
        return false;
    }

    @Override
    public boolean isNormal() {
        return true;
    }

    @Override
    public boolean isHunting() {
        return false;
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isFlinched() {
        return false;
    }

}
