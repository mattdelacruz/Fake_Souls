package a3.controls.controlactions;

import net.java.games.input.*;
import tage.input.action.AbstractInputAction;

public class MouseAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        if (evt.getComponent() == Component.Identifier.Button.LEFT)
            System.out.println("pressing mouse button...");
        else {
            System.out.println("no!");
        }
    }

}
