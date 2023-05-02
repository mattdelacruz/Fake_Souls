package a3.npcs.movement;

public class EnemyRunMovementState implements EnemyMovementState {

    @Override
    public float getSpeed() {
        return 1f;
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
