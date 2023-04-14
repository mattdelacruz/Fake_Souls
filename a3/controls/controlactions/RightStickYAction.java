package a3.controls.controlactions;

import a3.MyGame;
import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class RightStickYAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        switch (evt.getComponent().getIdentifier().getName()) {
            case "Up":
                MyGame.getGameInstance().getState().rotateUp(MyGame.getGameInstance().getFrameTime());
                return;

            case "Down":
                MyGame.getGameInstance().getState().rotateDown(MyGame.getGameInstance().getFrameTime());
                return;
        }
        if (evt.getValue() == -1.0) {
            MyGame.getGameInstance().getState().rotateUp(MyGame.getGameInstance().getFrameTime());
            return;
        }

        if (evt.getValue() == 1.0) {
            MyGame.getGameInstance().getState().rotateDown(MyGame.getGameInstance().getFrameTime());
            return;
        }
    }

}
