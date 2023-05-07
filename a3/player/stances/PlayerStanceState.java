package a3.player.stances;

import tage.ActiveEntityStanceState;

public interface PlayerStanceState extends ActiveEntityStanceState {

    public float getGuardValue();

    public boolean isGuarding();

}