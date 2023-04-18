package a3.player;

public class PlayerNormalStanceState implements PlayerStanceState{

    @Override
    public float getMoveValue() {
        return 1;
    }

    @Override
    public float getGuardValue() {
        return 1;
    }
    
}
