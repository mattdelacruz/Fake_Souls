package a3.player.movement;

public class PlayerRunMovementState implements PlayerMovementState {

    @Override
    public float getSpeed() {
        return 1f;
    }

    @Override
    public String getAnimation() {
        return "RUN";
    }

}
