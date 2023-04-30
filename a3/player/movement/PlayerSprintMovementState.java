package a3.player.movement;

public class PlayerSprintMovementState implements PlayerMovementState {

    @Override
    public float getSpeed() {
        return 1.5f;
    }

    @Override
    public boolean isSprinting() {
        return true;
    }

    @Override
    public String getAnimation() {
        return "SPRINT";
    }

}
