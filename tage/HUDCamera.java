package tage;

public class HUDCamera extends Camera {
    private static final float VIEWPORT_BOTTOM = 0f;
    private static final float VIEWPORT_LEFT = 0.95f;
    private static final float VIEWPORT_HEIGHT = .05f;
    private static final String VIEWPORT_NAME = "HUD";

    public HUDCamera(Engine e) {
        System.out.println(e.getRenderSystem().getWidth());
        e.getRenderSystem().addViewport(VIEWPORT_NAME, VIEWPORT_BOTTOM, VIEWPORT_LEFT,
                e.getRenderSystem().getWidth(), VIEWPORT_HEIGHT);
        addToViewPort(e.getRenderSystem().getViewport(VIEWPORT_NAME));
    }

    private void addToViewPort(Viewport view) {
        view.setCamera(this);
        view.setHasBorder(false);
    }
}
