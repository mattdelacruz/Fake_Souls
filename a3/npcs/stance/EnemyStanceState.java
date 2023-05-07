package a3.npcs.stance;

public interface EnemyStanceState {
    public float getMoveValue();

    public String getAnimation();

    public boolean isAttacking();

    public boolean isNormal();

    public boolean isHunting();

}