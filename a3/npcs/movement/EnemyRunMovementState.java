package a3.npcs.movement;

public class EnemyRunMovementState implements EnemyMovementState {

    @Override
    public float getSpeed() {
        return 0.5f;
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public String getAnimation() {
        return "RUN";
    }

}
