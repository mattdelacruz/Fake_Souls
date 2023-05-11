package a3.npcs.stance;

import tage.ActiveEntityStanceState;

public interface EnemyStanceState extends ActiveEntityStanceState {
    public boolean isHunting();

    public boolean isFlinched();
}