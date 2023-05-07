package a3.player.stances;

public class PlayerNormalStanceState implements PlayerStanceState {

    @Override
    public float getMoveValue() {
        return 1;
    }

    @Override
    public float getGuardValue() {
        return 1;
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
    public boolean isGuarding() {
        return false;
    }

    @Override
    public boolean isNormal() {
        return true;
    }

    @Override
    public boolean isDead() {
        return false;
    }

}
