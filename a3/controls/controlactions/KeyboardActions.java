package a3.controls.controlactions;

import a3.MyGame;
import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class KeyboardActions extends AbstractInputAction {
    @Override
    public void performAction(float time, Event evt) {
        switch (evt.getComponent().getIdentifier().getName()) {
            case "O":
                MyGame.getGameInstance().getState().target();
                return;
            case "G":
                if (evt.getValue() == 1.0f) {
                    MyGame.getGameInstance().getPlayer().guard();
                    return;
                } else {
                    MyGame.getGameInstance().getPlayer().unGuard();
                    return;
                }
            case "LSHIFT":
            System.out.println("press lshift...");
                if (evt.getValue() == 1.0f) {
                    System.out.println("sprinting");
                    MyGame.getGameInstance().getPlayer().sprint();
                    return;
                } else {
                    System.out.println("running");

                    MyGame.getGameInstance().getPlayer().run();
                    return;
                }
        }
    }
}
