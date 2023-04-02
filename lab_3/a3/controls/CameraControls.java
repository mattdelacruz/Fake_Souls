package a3.controls;

import a3.world.MyGame;
import tage.TargetCamera;

public class CameraControls implements PlayerControlFunctions {
    private TargetCamera cam;

    public CameraControls() {
        cam = MyGame.getGameInstance().getTargetCamera();
    }

    @Override
    public void turnLeft(float frameTime) {
        cam.move(-frameTime, cam.getU());
    }

    @Override
    public void turnRight(float frameTime) {
        cam.move(frameTime, cam.getU());
    }

    @Override
    public void moveForward(float frameTime) {
        cam.move(frameTime, cam.getN());
    }

    @Override
    public void moveBackward(float frameTime) {
        cam.move(-frameTime, cam.getN());
    }

    @Override
    public void rotateUp(float frameTime) {
        cam.changeAltitude(-frameTime);
    }

    @Override
    public void rotateDown(float frameTime) {
        cam.changeAltitude(frameTime);
    }

    @Override
    public void target() {
        return;
    }

    @Override
    public boolean isControlPlayer() {
        return false;
    }
}
