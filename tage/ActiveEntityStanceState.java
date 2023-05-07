package tage;

public interface ActiveEntityStanceState {
    public float getMoveValue();

    public boolean isNormal();

    public boolean isAttacking();

    public String getAnimation();

    public boolean isDead();
}
