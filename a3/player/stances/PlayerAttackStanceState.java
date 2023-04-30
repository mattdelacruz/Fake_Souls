package a3.player.stances;

public class PlayerAttackStanceState implements PlayerStanceState {

    @Override
    public float getMoveValue() {
        return 1f;
    }

    @Override
    public float getGuardValue() {
        return 1f;
    }

    @Override
    public String getAnimation() {
        return "ATTACK1";
    }

    @Override
    public boolean isAttacking() {
        return true;
    }

    @Override
    public boolean isGuarding() {
        return false;
    }

    @Override
    public boolean isNormal() {
        return false;
    }

}
