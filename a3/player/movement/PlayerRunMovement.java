package a3.player.movement;

public class PlayerRunMovement implements PlayerMovement {

    @Override
    public float getSpeed() {
        return 1f;
    }

    @Override
    public String getAnimation() {
        return "RUN";
    }

}
