package a3.player.stances;

public class PlayerGuardStanceState implements PlayerStanceState {

    @Override
    public float getMoveValue() {
        return 0.5f; // reduce speed by half
    }

    @Override
    public float getGuardValue() {
        return 0.5f; // reduce damage by half
    }

    @Override
    public String getAnimation() {
        return "GUARD";
    }

    @Override
    public boolean isAttacking() {
        return false;
    }

    @Override
    public boolean isGuarding() {
        return true;
    }

    @Override
    public boolean isNormal() {
        return false;
    }

    @Override
    public boolean isDead() {
        return false;
    }

}
