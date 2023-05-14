package a3.controls.controlactions;

import a3.MyGame;
import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class MoveHorizontalAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        String key = evt.getComponent().getIdentifier().getName();
        boolean isPressed = evt.getValue() == 1.0;

        switch (key) {
            case "A":
                MyGame.getGameInstance().getControls().turnCameraRight(MyGame.getGameInstance().getFrameTime());
                return;
            case "D":

                MyGame.getGameInstance().getControls().turnCameraLeft(MyGame.getGameInstance().getFrameTime());
                return;
        }

        // gamepad controls
        if (evt.getValue() == 1.0) {
            MyGame.getGameInstance().getPlayer().addCurrentRotation(-5);
            MyGame.getGameInstance().getPlayer().yaw(MyGame.getGameInstance().getFrameTime(),
                    MyGame.getGameInstance().getPlayer().getCurrentRotation());
            return;
        }

        if (evt.getValue() == -1.0) {
            MyGame.getGameInstance().getPlayer().addCurrentRotation(5);
            MyGame.getGameInstance().getPlayer().yaw(MyGame.getGameInstance().getFrameTime(),
                    MyGame.getGameInstance().getPlayer().getCurrentRotation());
            return;
        }
    }

}
