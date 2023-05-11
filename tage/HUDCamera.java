package tage;

public class HUDCamera extends Camera {
    private static final String HUD_VIEWPORT_NAME = "HUD";

    public HUDCamera(Engine e) {
        addToViewPort(e.getRenderSystem().getViewport(HUD_VIEWPORT_NAME));
    }

    private void addToViewPort(Viewport view) {
        view.setCamera(this);
        view.setHasBorder(false);
    }

    public String getViewportName() {
        return HUD_VIEWPORT_NAME;
    }
}
