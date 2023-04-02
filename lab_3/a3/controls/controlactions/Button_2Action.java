package a3.controls.controlactions;

import a3.controls.CameraControls;
import a3.controls.PlayerControls;
import a3.world.MyGame;
import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class Button_2Action extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        switch (evt.getComponent().getIdentifier().getName()) {
            case " ":
            case "2":
                if (MyGame.getGameInstance().getState().isControlPlayer()) {
                    MyGame.getGameInstance().setState(new CameraControls());
                } else {
                    MyGame.getGameInstance().setState(new PlayerControls());
                }
                return;
        }
    }
}
