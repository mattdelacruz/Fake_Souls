package a3.npcs.enemybehavior;

import a3.npcs.Enemy;
import a3.npcs.stance.EnemyAttackStance;
import tage.ai.behaviortrees.BTCondition;

public class KillTarget extends BTCondition {
    private Enemy hunter;

    public KillTarget(Enemy hunter) {
        super(false);
        this.hunter = hunter;
    }

    @Override
    protected boolean check() {
        float distanceToPrey = hunter.getTarget().getLocalLocation().distance(hunter.getLocalLocation());
        if (distanceToPrey <= hunter.getAttackRange()) {
            if (!hunter.getStanceState().isAttacking())
                hunter.setStanceState(new EnemyAttackStance());
            return true;
        }
        return false;
    }
}
