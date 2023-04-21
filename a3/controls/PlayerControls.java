package a3.controls;

import a3.MyGame;
import a3.network.ProtocolClient;
import a3.npcs.Enemy;
import a3.player.Player;
import tage.GameObject;
import tage.TargetCamera;

public class PlayerControls implements PlayerControlFunctions {
	private Player player;
	private TargetCamera cam;
	private MyGame game;
	private ProtocolClient protocolClient;

	public PlayerControls() {
		game = MyGame.getGameInstance();
		player = game.getPlayer();
		cam = game.getTargetCamera();
	}

	@Override
	public void turnLeft(float frameTime) {
		player.move(player.getLocalRightVector(), -frameTime);
		cam.updateCameraLocation(frameTime);
		cam.lookAt(player);
	}

	@Override
	public void turnRight(float frameTime) {
		player.move(player.getLocalRightVector(), frameTime);
		cam.updateCameraLocation(frameTime);
		cam.lookAt(player);
	}

	@Override
	public void moveForward(float frameTime) {
		player.move(player.getLocalForwardVector(), frameTime);
		cam.updateCameraLocation(frameTime);
		cam.lookAt(player);
	}

	@Override
	public void moveBackward(float frameTime) {
		player.move(player.getLocalForwardVector(), -frameTime);
		cam.updateCameraLocation(frameTime);
		cam.lookAt(player);
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
			game.getTargetCamera().setTarget((Enemy) target);
			player.setLock(true);
		}
		return;
	}

	@Override
	public boolean isControlPlayer() {
		return true;
	}
}
