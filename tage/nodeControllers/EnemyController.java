package tage.nodeControllers;

import javax.swing.event.SwingPropertyChangeSupport;

import org.joml.Vector3f;

import a3.MyGame;
import a3.npcs.Enemy;
import tage.*;

/**
 * A FloatController is a node controller that, when enabled, causes any
 * object
 * it is attached to to float in place between a specified range in the
 * y-direction that is calculated using the object's scale.
 * 
 * @author Matthew Dela Cruz
 */

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
            System.out.println("updating enemy...");
            ((Enemy) go).update();
        }

    }
}
