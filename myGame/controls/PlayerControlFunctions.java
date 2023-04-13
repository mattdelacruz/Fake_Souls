package myGame.controls;

public interface PlayerControlFunctions {
	void turnLeft(float frameTime);

	void turnCameraLeft(float frameTime);

	void turnRight(float frameTime);

	void turnCameraRight(float frameTime);

	void moveForward(float frameTime);

	void moveBackward(float frameTime);

	void rotateUp(float frameTime);

	void rotateDown(float frameTime);

	void target();

	boolean isControlPlayer();

}