package a3.controls.controlactions;

import net.java.games.input.*;
import tage.input.action.AbstractInputAction;

public class MouseAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        System.out.println("pressing mouse button...");
        System.out.println("component: " + evt.getComponent().getName());
    }

}
