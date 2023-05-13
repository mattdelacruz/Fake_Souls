package tage;

import org.joml.Vector3f;

import a3.npcs.Enemy;
import a3.player.Player;

public class TargetCamera extends CameraOrbit3D {

    private static final int MAX_RADIUS = 10;
    private Player origin;
    private Enemy enemy;

    public TargetCamera(Player o) {
        super(o);
        origin = o;
        setLocation(o.getLocalLocation().add(new Vector3f(0, 5, 0)));
    }

    public void targetTo() {
        if (enemy.getLocalLocation().distance(origin.getLocalLocation()) < MAX_RADIUS) {
            origin.lookAt(enemy.getLocalLocation().x, origin.getLocalLocation().y, enemy.getLocalLocation().z);
            setLookAtTarget(origin.getLocalLocation());

        } else {
            origin.setLock(false);
            setLookAtTarget(origin.getLocalLocation());
        }
    }

    public void setTarget(Enemy e) {
        enemy = e;
    }

}
