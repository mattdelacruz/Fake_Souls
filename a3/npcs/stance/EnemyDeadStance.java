package a3.npcs.stance;

public class EnemyDeadStance implements EnemyStanceState {

    @Override
    public float getMoveValue() {
        return 0f;
    }

    @Override
    public boolean isNormal() {
        return false;
    }

    @Override
    public boolean isAttacking() {
        return false;
    }

    @Override
    public String getAnimation() {
        return "DEATH";
    }

    @Override
    public boolean isDead() {
        return true;
    }

    @Override
    public boolean isHunting() {
        return false;
    }

}
