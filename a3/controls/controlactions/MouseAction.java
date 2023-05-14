package a3.controls.controlactions;

import a3.MyGame;
import net.java.games.input.*;
import tage.input.action.AbstractInputAction;

public class MouseAction extends AbstractInputAction {
    @Override
    public void performAction(float time, Event evt) {

        switch (evt.getComponent().getName().toUpperCase()) {

            case "X":
                if (evt.getValue() > 0) {
                    MyGame.getGameInstance().getPlayer().addCurrentRotation(-1);
                    MyGame.getGameInstance().getPlayer().yaw(MyGame.getGameInstance().getFrameTime(),
                            MyGame.getGameInstance().getPlayer().getCurrentRotation());

                } else {
                    MyGame.getGameInstance().getPlayer().addCurrentRotation(1);
                    MyGame.getGameInstance().getPlayer().yaw(MyGame.getGameInstance().getFrameTime(),
                            MyGame.getGameInstance().getPlayer().getCurrentRotation());

                }
                return;
            // guard
            case "RIGHT":
                if (evt.getValue() > 0) {
                    MyGame.getGameInstance().getPlayer().guard();

                } else if (evt.getValue() == 0) {
                    MyGame.getGameInstance().getPlayer().unGuard();
                }
                return;
            // attack
            case "LEFT":
                if (evt.getValue() > 0) {
                    MyGame.getGameInstance().getPlayer().attack();
                }

        }
    }
}
