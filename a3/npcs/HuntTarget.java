package a3.npcs;

import a3.MyGame;
import tage.GameObject;
import tage.ai.behaviortrees.BTCondition;

public class HuntTarget extends BTCondition {
    GameObject prey;
    Enemy hunter;

    public HuntTarget(Enemy hunter) {
        super(false);
        this.hunter = hunter;
    }

    @Override
    protected boolean check() {
        if (hunter.getTarget() != null) {
            hunter.lookAt(hunter.getTarget());
            hunter.move(hunter.getLocalForwardVector(), MyGame.getGameInstance().getFrameTime());
            return true;
        }
        return false;
    }

}
