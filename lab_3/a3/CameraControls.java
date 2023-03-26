package a3;

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
    public boolean isControlPlayer() {
        return false;
    }

    @Override
    public void target() {
        Enemy target = (Enemy) MyGame.getGameInstance().findTarget();
        if (target != null) {
            cam.setTarget(target);
        } else {
        }
    }

}
