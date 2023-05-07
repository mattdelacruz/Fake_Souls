package a3.player.stances;

public class PlayerDeadStance implements PlayerStanceState {

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
    public float getGuardValue() {
        return 0f;
    }

    @Override
    public boolean isGuarding() {
        return false;
    }

}
