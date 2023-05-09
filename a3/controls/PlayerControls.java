package a3.controls;

import a3.MyGame;
import a3.network.ProtocolClient;
import a3.npcs.Enemy;
import a3.player.Player;
import tage.GameObject;
import tage.TargetCamera;

public class PlayerControls {
	private Player player;
	private TargetCamera cam;
	private MyGame game;
	private ProtocolClient protocolClient;

	public PlayerControls() {
		game = MyGame.getGameInstance();
		player = game.getPlayer();
		cam = game.getTargetCamera();
	}

	public void moveNorth(float frameTime) {
		player.moveNorth(player.getWorldForwardVector(), frameTime);
		cam.updateCameraLocation(frameTime);
		cam.lookAt(player);
	}

	public void moveEast(float frameTime) {
		player.moveEast(player.getWorldRightVector(), frameTime);
		cam.updateCameraLocation(frameTime);
		cam.lookAt(player);
	}

	public void turnCameraLeft(float frameTime) {
		cam.move(-frameTime, cam.getU());
		cam.updateCameraAngles(frameTime);
	}

	public void turnCameraRight(float frameTime) {
		cam.move(frameTime, cam.getU());
		cam.updateCameraAngles(frameTime);
	}

	public void rotateUp(float frameTime) {
		return;
	}

	public void rotateDown(float frameTime) {
		return;
	}

	public void target() {
		GameObject target = MyGame.getGameInstance().findTarget();
		if (target != null && target instanceof Enemy) {
			game.getTargetCamera().setTarget((Enemy) target);
			player.setLock(true);
		}
		return;
	}
}
