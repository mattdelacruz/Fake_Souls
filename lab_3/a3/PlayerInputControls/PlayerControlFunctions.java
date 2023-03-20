package a3.PlayerInputControls;

public interface PlayerControlFunctions {
	void turnLeft(float frameTime);

	void turnRight(float frameTime);

	void moveForward(float frameTime);

	void moveBackward(float frameTime);

	void rotateUp(float frameTime);

	void rotateDown(float frameTime);

	void target();

	boolean isControlPlayer();

}