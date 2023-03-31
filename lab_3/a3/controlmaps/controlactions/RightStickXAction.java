package a3.controlmaps.controlactions;

import a3.MyGame;
import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class RightStickXAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        switch (evt.getComponent().getIdentifier().getName()) {
            case "A":
                MyGame.getGameInstance().getState().turnLeft(MyGame.getGameInstance().getFrameTime());
                return;
            case "D":
                MyGame.getGameInstance().getState().turnRight(MyGame.getGameInstance().getFrameTime());
                return;
        }

        if (evt.getValue() == 1.0) {
            MyGame.getGameInstance().getState().turnRight(MyGame.getGameInstance().getFrameTime());
            return;
        }

        if (evt.getValue() == -1.0) {
            MyGame.getGameInstance().getState().turnLeft(MyGame.getGameInstance().getFrameTime());
            return;
        }
    }

}
