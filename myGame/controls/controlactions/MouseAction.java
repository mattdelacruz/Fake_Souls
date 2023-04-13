package myGame.controls.controlactions;

import myGame.MyGame;
import net.java.games.input.*;
import tage.input.action.AbstractInputAction;

public class MouseAction extends AbstractInputAction {
    @Override
    public void performAction(float time, Event evt) {
        switch (evt.getComponent().getName().toUpperCase()) {
            case "LEFT":
                return;
            case "RIGHT":
                return;
            case "MIDDLE":
                return;
            case "X":
                if (evt.getValue() > 0) {
                    MyGame.getGameInstance().getState().turnCameraLeft(MyGame.getGameInstance().getFrameTime());

                } else {
                    MyGame.getGameInstance().getState().turnCameraRight(MyGame.getGameInstance().getFrameTime());
                }
                return;
        }
    }
}
