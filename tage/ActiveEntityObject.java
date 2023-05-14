package tage;

import a3.MyGame;
import a3.managers.SoundManager;

public class ActiveEntityObject extends AnimatedGameObject {
    private int health;
    private ActiveEntityStanceState stanceState;
    private ActiveEntityMovementState movementState;
    private SoundManager soundManager;

    public ActiveEntityObject(GameObject p, ObjShape s, TextureImage t, int h) {
        super(p, s, t);
        health = h;
        soundManager = MyGame.getGameInstance().getSoundManager();
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

    public SoundManager getSoundManager() {
        return soundManager;
    }

}
