package a3.controls.controlactions;

import a3.MyGame;
import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class MoveVerticalAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        switch (evt.getComponent().getIdentifier().getName()) {
            case "W":
                MyGame.getGameInstance().getControls().moveForward(MyGame.getGameInstance().getFrameTime());

                return;

            case "S":
                MyGame.getGameInstance().getControls().moveBackward(MyGame.getGameInstance().getFrameTime());
                return;
        }
        if (evt.getValue() == -1.0) {
            MyGame.getGameInstance().getControls().moveForward(MyGame.getGameInstance().getFrameTime());
            return;
        }
        if (evt.getValue() == 1.0) {
            MyGame.getGameInstance().getControls().moveBackward(MyGame.getGameInstance().getFrameTime());
            return;
        }
    }
}
