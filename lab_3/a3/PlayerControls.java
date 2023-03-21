package a3;

public class PlayerControls implements PlayerControlFunctions {
	private Player player;

	public PlayerControls() {
		player = MyGame.getGameInstance().getPlayer();
	}

	@Override
	public void turnLeft(float frameTime) {
		player.move(player.getLocalRightVector(), -frameTime);
	}

	@Override
	public void turnRight(float frameTime) {
		player.move(player.getLocalRightVector(), frameTime);

	}

	@Override
	public void moveForward(float frameTime) {
		player.move(player.getLocalForwardVector(), frameTime);
	}

	@Override
	public void moveBackward(float frameTime) {
		player.move(player.getLocalForwardVector(), -frameTime);
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
		Enemy target = MyGame.getGameInstance().findTarget();
		if (target != null) {
			player.getCamera().setTarget(target);
			player.setLock(true);
		} else {
			System.out.println("no target found");
		}
		return;
	}

	@Override
	public boolean isControlPlayer() {
		return true;
	}

}
