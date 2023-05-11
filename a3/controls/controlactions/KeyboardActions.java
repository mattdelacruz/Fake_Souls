package a3.controls.controlactions;

import a3.MyGame;
import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class KeyboardActions extends AbstractInputAction {
    @Override
    public void performAction(float time, Event evt) {
        switch (evt.getComponent().getIdentifier().getName().toUpperCase()) {
            case "O":
                MyGame.getGameInstance().getControls().target();
                return;
            case "G":
                if (evt.getValue() == 1.0f) {
                    MyGame.getGameInstance().getPlayer().guard();
                    return;
                } else {
                    MyGame.getGameInstance().getPlayer().unGuard();
                    return;
                }
        }
    }
}
