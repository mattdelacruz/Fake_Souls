package a3.npcs.enemybehavior;

import a3.npcs.Enemy;
import tage.ai.behaviortrees.BTCondition;

public class KillTarget extends BTCondition {
    private Enemy hunter;

    public KillTarget(Enemy hunter) {
        super(false);
        this.hunter = hunter;
    }

    @Override
    protected boolean check() {
        if (hunter.getTarget() != null) {
            float distanceToPrey = hunter.getTarget().getLocalLocation().distance(hunter.getLocalLocation());
            if (distanceToPrey <= hunter.getAttackRange()) {
                System.out.println("attacking... add animation later");
                return true;
            }

        }
        System.out.println("not close enough...");
        return false;
    }

}
