package a3.PlayerInputControls.PlayerActions;

import a3.MyGame;
import a3.PlayerInputControls.CameraControls;
import a3.PlayerInputControls.PlayerControls;
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
