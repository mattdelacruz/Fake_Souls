package tage;

import a3.npcs.Enemy;
import a3.player.Player;

public class TargetCamera extends CameraOrbit3D {

    private static final int MAX_RADIUS = 10;
    private Player origin;
    private Enemy enemy;

    public TargetCamera(Player o) {
        super(o);
        origin = o;
    }

    public void targetTo() {
        if (enemy.getLocalLocation().distance(origin.getLocalLocation()) < MAX_RADIUS) {
            origin.lookAt(enemy);
        } else {
            origin.setLock(false);
        }
    }

    public void setTarget(Enemy e) {
        enemy = e;
    }

}
