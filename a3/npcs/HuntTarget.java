package a3.npcs;

import tage.GameObject;
import tage.ai.behaviortrees.BTCondition;

public class HuntTarget extends BTCondition {
    GameObject prey;
    Enemy hunter;

    public HuntTarget(GameObject target, Enemy hunter) {
        super(false);
        prey = target;
        this.hunter = hunter;
    }

    @Override
    protected boolean check() {
        if (prey != null) {
            hunter.lookAt(prey);
            return true;
        }
        return false;
    }

}
