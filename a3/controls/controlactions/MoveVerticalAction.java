package a3.controls.controlactions;

import a3.MyGame;
import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class MoveVerticalAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        String key = evt.getComponent().getIdentifier().getName();

        boolean isPressed = evt.getValue() == 1.0;

        if (isPressed) {
            MyGame.getGameInstance().addKeyToActiveKeys(key);
        } else {
            MyGame.getGameInstance().removeKeyFromActiveKeys(key);
        }

        switch (key) {
            case "W":
            case "S":
                return;
        }

        // gamepad controls
        if (evt.getValue() == -1.0) {
            MyGame.getGameInstance().getControls().moveNorth(MyGame.getGameInstance().getFrameTime());
            return;
        }
        if (evt.getValue() == 1.0) {
            // MyGame.getGameInstance().getControls().moveSouth(MyGame.getGameInstance().getFrameTime());
            // return;
        }
    }
}
