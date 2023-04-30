package a3.player.stances;

public interface PlayerStanceState {
    public float getMoveValue();

    public float getGuardValue();

    public String getAnimation();

    public boolean isAttacking();

    public boolean isGuarding();

    public boolean isNormal();
}
