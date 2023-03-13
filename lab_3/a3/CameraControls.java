package a3;

import tage.CameraOrbit3D;

public class CameraControls implements PlayerControlFunctions {
    private CameraOrbit3D cam;

    CameraControls() {
        cam = MyGame.getGameInstance().getOrbitCamera();
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
    public boolean isControlDolphin() {
        return false;
    }

}
