package tage;

import tage.shapes.AnimatedShape;

public class AnimatedGameObject extends GameObject {
    private AnimatedShape animatedShape;
    private boolean isMoving = false;
	private float[] lastLocation = new float[3];

    public AnimatedGameObject(GameObject p, ObjShape s, TextureImage t) {
        super(p, s, t);
        animatedShape = (AnimatedShape)s;
    }

	public void setLastLocation(float[] x) {
		lastLocation = x;
	}

	public float getLastLocation(int i) {
		return lastLocation[i];
	}

    /**
	 * sets the animation shape for this GameObject
	 */
	public void setAnimationShape(AnimatedShape animatedShape) {
		this.animatedShape = animatedShape;
	}

	/**
	 * gets the animation shape for this GameObject
	 */
	public AnimatedShape getAnimationShape() {
		return animatedShape;
	}

	/**
	 * sets the whether if the GameObject is moving in animation
	 */
	public void setIsMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	/**
	 * returns whether the GameObject is moving in animation
	 */
	public boolean isMoving() {
		return isMoving;
	}

	/**
	 * plays the specified animation
	 */
	public void playAnimation(String animation) {
		if (getAnimationShape().getAnimation(animation) == null) {
			System.out.println("Animation not found...");
			return;
		}

		getAnimationShape().playAnimation(animation, 1f, AnimatedShape.EndType.PAUSE, 0);
	}

	private void handleAnimationSwitch(String animation) {
		if (!getAnimationShape().isAnimPlaying()) {
			playAnimation(animation);
		} else if (!getAnimationShape().getAnimation(animation).equals(getAnimationShape().getCurrentAnimation())) {
			getAnimationShape().pauseAnimation();
			playAnimation(animation);
		}
	}

    public void idle() {
        handleAnimationSwitch("IDLE");
    }

    public void updateAnimation() {
        getAnimationShape().updateAnimation();
    }
}
