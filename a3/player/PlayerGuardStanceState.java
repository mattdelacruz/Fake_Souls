package a3.player;

public class PlayerGuardStanceState implements PlayerStanceState {

    @Override
    public float getMoveValue() {
        return 0.5f; //reduce speed by half
    }

    @Override
    public float getGuardValue() {
        return 0.5f; //reduce damage by half
    }
    
}
