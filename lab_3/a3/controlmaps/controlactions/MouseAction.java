package a3.controlmaps.controlactions;

import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class MouseAction extends AbstractInputAction {

    @Override
    public void performAction(float time, Event evt) {
        System.out.println("pressing mouse button...");
    }

}
