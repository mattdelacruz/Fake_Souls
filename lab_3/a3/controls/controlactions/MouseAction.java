package a3.controls.controlactions;

import a3.world.MyGame;
import net.java.games.input.*;
import tage.input.action.AbstractInputAction;

public class MouseAction extends AbstractInputAction {
    @Override
    public void performAction(float time, Event evt) {
        switch(evt.getComponent().getName()) {
            case "Left":
                System.out.println("pressing left");
                break;
            case "Right":
                System.out.println("pressing right");
                break;
            case "Middle":
                System.out.println("pressing middle");
                break;
            case "x":
            case "X":
                if (evt.getValue() > 0) {
                    MyGame.getGameInstance().getState().turnCameraLeft(MyGame.getGameInstance().getFrameTime());
                    // MyGame.getGameInstance().getState().turnLeft(MyGame.getGameInstance().getFrameTime());
                }
                else {
                    MyGame.getGameInstance().getState().turnCameraRight(MyGame.getGameInstance().getFrameTime());
                    // MyGame.getGameInstance().getState().turnRight(MyGame.getGameInstance().getFrameTime());
                }
                break;
        }
    }
}
