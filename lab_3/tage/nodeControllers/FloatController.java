package tage.nodeControllers;

import org.joml.Vector3f;

import a3.world.MyGame;
import tage.*;

/**
 * A FloatController is a node controller that, when enabled, causes any
 * object
 * it is attached to to float in place between a specified range in the
 * y-direction that is calculated using the object's scale.
 * 
 * @author Matthew Dela Cruz
 */

public class FloatController extends NodeController {
    private float floatSpeed = 0.1f;
    private boolean isFloating = false;

    public FloatController() {
        super();
    }

    public FloatController(float s) {
        floatSpeed = s;
    }

    public void setSpeed(float s) {
        floatSpeed = s;
    }

    @Override
    public void apply(GameObject go) {
        float elapsedTime = super.getElapsedTime();
        float floatAmt = floatSpeed * elapsedTime;
        float maxHeight = go.getLocalScale().get(0, 0) * 5;

        if (go.getLocalLocation().y > maxHeight) {
            isFloating = false; // fall
        }
        if (go.getLocalLocation().y <= go.getLocalScale().get(0, 0) * 3) {
            isFloating = true; // float
        }

        if (isFloating) {
            go.setLocalLocation(go.getLocalLocation().add(new Vector3f(0, floatAmt, 0)));
            return;
        } else if (!isFloating) {
            go.setLocalLocation(go.getLocalLocation().add(new Vector3f(0, -floatAmt, 0)));
            return;
        }
    }
}
