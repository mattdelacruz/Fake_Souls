package tage;

import a3.Enemy;

public class TargetCamera extends CameraOrbit3D {

    GameObject origin;

    public TargetCamera(GameObject o) {
        super(o);
        origin = o;
    }

    public void targetTo(Enemy enemy) {
        if (enemy.getLocalLocation().distance(origin.getLocalLocation()) < 4) {
            origin.lookAt(enemy);
        }
    }

}
