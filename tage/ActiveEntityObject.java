package tage;

public class ActiveEntityObject extends AnimatedGameObject {
    private int health;
    private ActiveEntityStanceState stanceState;
    private ActiveEntityMovementState movementState;

    public ActiveEntityObject(GameObject p, ObjShape s, TextureImage t, int h) {
        super(p, s, t);
        health = h;
    }

    public void setStanceState(ActiveEntityStanceState state) {
        stanceState = state;
    }

    public ActiveEntityStanceState getStanceState() {
        return stanceState;
    }

    public void setMovementState(ActiveEntityMovementState state) {
        movementState = state;
    }

    public ActiveEntityMovementState getMovementState() {
        return movementState;
    }

    public void addHealth(int h) {
        health += h;
    }

    public void reduceHealth(int h) {
        health -= h;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int h) {
        health = h;
    }

}
