package a3.controls.controlactions;

import a3.MyGame;
import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class GameWorldAssetAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        switch (evt.getComponent().getIdentifier().getName()) {
            case "1":
            case "R":
                MyGame.getGameInstance().renderGameAxis();
                break;
        }
    }
}
