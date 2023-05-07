package a3.npcs.stance;

public class EnemyHuntStance implements EnemyStanceState {

    @Override
    public float getMoveValue() {
        return 1f;
    }

    @Override
    public String getAnimation() {
        return "RUN";
    }

    @Override
    public boolean isAttacking() {
        return false;
    }

    @Override
    public boolean isNormal() {
        return false;
    }

    @Override
    public boolean isHunting() {
        return true;
    }

}
