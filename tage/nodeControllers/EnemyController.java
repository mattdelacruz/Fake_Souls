package tage.nodeControllers;

import javax.swing.event.SwingPropertyChangeSupport;

import org.joml.Vector3f;

import a3.MyGame;
import a3.npcs.Enemy;
import tage.*;

public class EnemyController extends NodeController {
    private float floatSpeed = 0.1f;
    private boolean isFloating = false;

    public EnemyController() {
        super();
    }

    @Override
    public void apply(GameObject go) {
        float elapsedTime = super.getElapsedTime();

        if (go instanceof Enemy) {
            ((Enemy) go).updateBehavior();
        }

    }
}
