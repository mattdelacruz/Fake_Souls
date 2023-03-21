package tage;

import org.joml.Vector3f;

import a3.Enemy;
import a3.Player;

public class TargetCamera extends CameraOrbit3D {

    private Player origin;
    private Enemy enemy;

    public TargetCamera(Player o) {
        super(o);
        origin = o;
    }

    public void targetTo() {

        if (enemy.getLocalLocation().distance(origin.getLocalLocation()) < 10) {
            origin.lookAt(enemy);
        } else {
            origin.setLock(false);
        }
    }

    public void setTarget(Enemy e) {
        enemy = e;
    }

}
