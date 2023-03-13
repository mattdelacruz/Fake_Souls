package a3;

import tage.GameObject;

public class DolphinControls implements PlayerControlFunctions {
	private GameObject dol;

	DolphinControls() {
		dol = MyGame.getGameInstance().getDolphin();
	}

	@Override
	public void turnLeft(float frameTime) {
		dol.yaw(frameTime);
	}

	@Override
	public void turnRight(float frameTime) {
		dol.yaw(-frameTime);
	}

	@Override
	public void moveForward(float frameTime) {
		dol.move(frameTime);
	}

	@Override
	public void moveBackward(float frameTime) {
		dol.move(-frameTime);
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
	public boolean isControlDolphin() {
		return true;
	}
}
