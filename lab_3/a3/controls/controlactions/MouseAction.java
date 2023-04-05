package a3.controls.controlactions;

import a3.MyGame;
import net.java.games.input.*;
import tage.input.action.AbstractInputAction;

public class MouseAction extends AbstractInputAction {
    @Override
    public void performAction(float time, Event evt) {
        switch (evt.getComponent().getName().toUpperCase()) {
            case "LEFT":
                System.out.println("pressing lefttttt");
                return;
            case "RIGHT":
                System.out.println("pressing rightttt");
                return;
            case "MIDDLE":
                System.out.println("pressing midddddle");
                return;
            case "X":
                if (evt.getValue() > 0) {
                    System.out.println("testing");
                    MyGame.getGameInstance().getState().turnCameraLeft(MyGame.getGameInstance().getFrameTime());

                } else {
                    MyGame.getGameInstance().getState().turnCameraRight(MyGame.getGameInstance().getFrameTime());
                }
                return;
        }
    }
}
