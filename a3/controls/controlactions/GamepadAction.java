package a3.controls.controlactions;

import a3.MyGame;
import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class GamepadAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        System.out.println("event name: " + evt.getComponent().getIdentifier().getName().toUpperCase());
        switch (evt.getComponent().getIdentifier().getName().toUpperCase()) {
            // O button on PS4 controller
            case "2":
                MyGame.getGameInstance().findTarget();
                return;

            // left trigger
            case "6":
                if (evt.getValue() > 0) {
                    MyGame.getGameInstance().getPlayer().guard();

                } else if (evt.getValue() == 0) {
                    MyGame.getGameInstance().getPlayer().unGuard();
                }
                return;
            // right trigger
            case "7":
                if (evt.getValue() > 0) {
                    MyGame.getGameInstance().getPlayer().attack();
                }
                return;
            case "POV":
                if (evt.getValue() == 1.0) {
                    MyGame.getGameInstance().getControls().turnCameraLeft(MyGame.getGameInstance().getFrameTime());
                    return;
                } else if (evt.getValue() == 0.5) {
                    MyGame.getGameInstance().getControls().turnCameraRight(MyGame.getGameInstance().getFrameTime());
                    return;
                }

        }

    }

}
