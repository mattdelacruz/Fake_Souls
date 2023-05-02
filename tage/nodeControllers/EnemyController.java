package tage.nodeControllers;
import a3.npcs.Enemy;
import tage.*;

public class EnemyController extends NodeController {

    public EnemyController() {
        super();
    }

    @Override
    public void apply(GameObject go) {
        if (go instanceof Enemy) {
            ((Enemy) go).updateBehavior();
        }
    }
}
