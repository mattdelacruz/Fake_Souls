package tage;

import org.joml.Vector3f;

/**
 * An OverheadCamera is a special type of Camera looks down upon the x-z plane from a certain coordinate in the y-direction
 * An OverheadCamera can be placed into a second viewport so that two Camera can be used side-by-side.
 * 
 * @author Matthew Dela Cruz
 */

public class OverheadCamera extends Camera {
    private static final float OVERHEAD_CAMERA_HEIGHT = -5f;
    private static final float OVERHEAD_VIEWPORT_BOTTOM = .75f;
    private static final float OVERHEAD_VIEWPORT_LEFT = 0;
    private static final float OVERHEAD_VIEWPORT_WIDTH = .25f;
    private static final float OVERHEAD_VIEWPORT_HEIGHT = .25f;
    private static final int OVERHEAD_VIEWPORT_BORDER_WIDTH = 4;
    private static final String OVERHEAD_NAME = "OVERHEAD";

    public OverheadCamera(Engine e) {
        setLocation(new Vector3f(0, 2, 0));
        setU(new Vector3f(1, 0, 0));
        setV(new Vector3f(0, 0, -1));
        setN(new Vector3f(0, OVERHEAD_CAMERA_HEIGHT, 0));
        e.getRenderSystem().addViewport(OVERHEAD_NAME, OVERHEAD_VIEWPORT_BOTTOM, OVERHEAD_VIEWPORT_LEFT,
                OVERHEAD_VIEWPORT_WIDTH, OVERHEAD_VIEWPORT_HEIGHT);
        addToViewPort(e.getRenderSystem().getViewport(OVERHEAD_NAME));
    }

    private void addToViewPort(Viewport view) {
        view.setCamera(this);
        view.setHasBorder(true);
        view.setBorderWidth(OVERHEAD_VIEWPORT_BORDER_WIDTH);
        view.setBorderColor(0.0f, 1.0f, 0.0f);
    }

}
