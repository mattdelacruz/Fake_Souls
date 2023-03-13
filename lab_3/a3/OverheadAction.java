package a3;

import org.joml.Vector3f;

import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class OverheadAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        float cur_x = MyGame.getGameInstance().getOverheadCamera().getLocation().x;
        float cur_y = MyGame.getGameInstance().getOverheadCamera().getLocation().y;
        float cur_z = MyGame.getGameInstance().getOverheadCamera().getLocation().z;

        switch (evt.getComponent().getIdentifier().getName()) {
            case "I":
                MyGame.getGameInstance().getOverheadCamera()
                        .setLocation(
                                new Vector3f(cur_x, cur_y, cur_z - (10 * MyGame.getGameInstance().getFrameTime())));
                return;
            case "J":
                MyGame.getGameInstance().getOverheadCamera()
                        .setLocation(
                                new Vector3f(cur_x - (10 * MyGame.getGameInstance().getFrameTime()), cur_y, cur_z));
                return;
            case "K":
                MyGame.getGameInstance().getOverheadCamera()
                        .setLocation(
                                new Vector3f(cur_x, cur_y, cur_z + (10 * MyGame.getGameInstance().getFrameTime())));
                return;

            case "L":
                MyGame.getGameInstance().getOverheadCamera()
                        .setLocation(
                                new Vector3f(cur_x + (10 * MyGame.getGameInstance().getFrameTime()), cur_y, cur_z));
                return;
            case "7":
            case "N":
                MyGame.getGameInstance().getOverheadCamera()
                        .setLocation(new Vector3f(cur_x, cur_y - (5 * MyGame.getGameInstance().getFrameTime()), cur_z));
                return;
            case "6":
            case "M":
                MyGame.getGameInstance().getOverheadCamera()
                        .setLocation(new Vector3f(cur_x, cur_y + (5 * MyGame.getGameInstance().getFrameTime()), cur_z));
                return;
        }

        if (evt.getValue() == net.java.games.input.Component.POV.UP) {
            MyGame.getGameInstance().getOverheadCamera()
                    .setLocation(
                            new Vector3f(cur_x, cur_y, cur_z - (10 * MyGame.getGameInstance().getFrameTime())));
            return;
        } else if (evt.getValue() == net.java.games.input.Component.POV.DOWN) {
            MyGame.getGameInstance().getOverheadCamera()
                    .setLocation(
                            new Vector3f(cur_x, cur_y, cur_z + (10 * MyGame.getGameInstance().getFrameTime())));

            return;
        } else if (evt.getValue() == net.java.games.input.Component.POV.LEFT) {
            MyGame.getGameInstance().getOverheadCamera()
                    .setLocation(
                            new Vector3f(cur_x - (10 * MyGame.getGameInstance().getFrameTime()), cur_y, cur_z));
            return;

        } else if (evt.getValue() == net.java.games.input.Component.POV.RIGHT) {
            MyGame.getGameInstance().getOverheadCamera()
                    .setLocation(
                            new Vector3f(cur_x + (10 * MyGame.getGameInstance().getFrameTime()), cur_y, cur_z));
            return;
        }

    }

}
