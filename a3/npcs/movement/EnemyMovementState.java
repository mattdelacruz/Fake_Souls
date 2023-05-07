package a3.npcs.movement;

import tage.ActiveEntityMovementState;

public interface EnemyMovementState extends ActiveEntityMovementState {
    public float getSpeed();

    public boolean isRunning();

    public String getAnimation();

}
