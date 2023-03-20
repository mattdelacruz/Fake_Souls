package a3.PlayerInputControls;

import a3.MyGame;
import tage.GameObject;

public class PlayerControls implements PlayerControlFunctions {
	private GameObject player;

	public PlayerControls() {
		player = MyGame.getGameInstance().getPlayer();
	}

	@Override
	public void turnLeft(float frameTime) {
		player.yaw(frameTime);
	}

	@Override
	public void turnRight(float frameTime) {
		player.yaw(-frameTime);
	}

	@Override
	public void moveForward(float frameTime) {
		player.move(frameTime);
	}

	@Override
	public void moveBackward(float frameTime) {
		player.move(-frameTime);
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
		System.out.println("hello");
		return;
	}

	@Override
	public boolean isControlPlayer() {
		return true;
	}

}
