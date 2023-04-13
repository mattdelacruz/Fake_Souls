package tage.nodeControllers;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import tage.GameObject;
import tage.NodeController;

/**
 * An OrbitController is a node controller that, when enabled, causes any
 * object
 * it is attached to orbit around a specified origin. The radius of the orbit is
 * determined by the object's index in the target ArrayList defined in
 * NodeController.
 * 
 * @author Matthew Dela Cruz
 */

public class OrbitController extends NodeController {
    private static final float RADIUS = 5.0f;
    private static final float SPEED = 0.2f;

    private GameObject origin; // where the target object will orbit around

    public OrbitController(GameObject o) {
        super();
        origin = o;
    }

    @Override
    public void apply(GameObject go) {
        if (Math.toRadians(go.getTime() * SPEED) >= Math.PI * 2) {
            go.setTime(0);
        }

        if (go.getControllerPosition() == 0) {
            go.setControllerPosition(getTargetCount() * 5.0f);
        }

        go.addTime(super.getElapsedTime());
        go.setParent(origin);
        go.propagateTranslation(true);
        go.propagateRotation(false);
        go.setLocalScale(new Matrix4f().scaling(0.3f));
        Matrix4f currentTranslation = go.getLocalTranslation();
        currentTranslation.translation(
                (float) (Math.sin(Math.toRadians(go.getTime() * SPEED)) * (RADIUS + go.getControllerPosition())),
                0.0f, (float) (Math.cos(Math.toRadians(go.getTime() * SPEED)) * (RADIUS + go.getControllerPosition())));
        go.setLocalTranslation(currentTranslation);
    }
}
