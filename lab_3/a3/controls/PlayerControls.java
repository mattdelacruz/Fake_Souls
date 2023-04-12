package a3.controls;

import a3.MyGame;
import a3.npcs.Enemy;
import a3.player.Player;
import tage.GameObject;
import tage.TargetCamera;

public class PlayerControls implements PlayerControlFunctions {
	private Player player;
	private TargetCamera cam;

	public PlayerControls() {
		player = MyGame.getGameInstance().getPlayer();
		cam = MyGame.getGameInstance().getTargetCamera();
	}

	@Override
	public void turnLeft(float frameTime) {
		player.move(player.getLocalRightVector(), -frameTime);
		cam.updateCameraLocation();
	}

	@Override
	public void turnRight(float frameTime) {
		player.move(player.getLocalRightVector(), frameTime);
		cam.updateCameraLocation();
	}

	@Override
	public void moveForward(float frameTime) {
		player.move(player.getLocalForwardVector(), frameTime);
		cam.updateCameraLocation();
	}

	@Override
	public void moveBackward(float frameTime) {
		player.move(player.getLocalForwardVector(), -frameTime);
		cam.updateCameraLocation();
	}

	@Override
	public void turnCameraLeft(float frameTime) {
		cam.move(-frameTime, cam.getU());
		cam.updateCameraAngles(frameTime);
	}

	@Override
	public void turnCameraRight(float frameTime) {
		cam.move(frameTime, cam.getU());
		cam.updateCameraAngles(frameTime);
	}

	@Override
	public void rotateUp(float frameTime) {
		return;
	}

	@Override
	public void rotateDown(float frameTime) {
		return;
	}

	@Override
	public void target() {
		GameObject target = MyGame.getGameInstance().findTarget();
		if (target != null && target instanceof Enemy) {
			player.getCamera().setTarget((Enemy) target);
			player.setLock(true);
		}
		return;
	}

	@Override
	public boolean isControlPlayer() {
		return true;
	}
}
