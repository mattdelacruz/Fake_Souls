package a3.npcs.enemybehavior;

import a3.MyGame;
import a3.npcs.Enemy;
import tage.GameObject;
import tage.ai.behaviortrees.BTCondition;

public class HuntTarget extends BTCondition {
    private static final float HUNTING_DISTANCE = 20f;
    private GameObject prey;
    private Enemy hunter;

    public HuntTarget(Enemy hunter) {
        super(false);
        this.hunter = hunter;
    }

    @Override
    protected boolean check() {
        if (hunter.getTarget() != null) {
            float distanceToPrey = hunter.getTarget().getLocalLocation().distance(hunter.getLocalLocation());
            if (distanceToPrey <= HUNTING_DISTANCE && distanceToPrey >= hunter.getAttackRange()) {
                hunter.lookAt(hunter.getTarget());
                hunter.move(hunter.getLocalForwardVector(), MyGame.getGameInstance().getFrameTime());
                return true;
            }
        }
        return false;
    }

}
