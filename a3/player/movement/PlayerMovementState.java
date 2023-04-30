package a3.player.movement;

public interface PlayerMovementState {
    public float getSpeed();

    public boolean isSprinting();

    public String getAnimation();
}
