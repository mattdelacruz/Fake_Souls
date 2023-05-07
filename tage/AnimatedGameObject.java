package tage;

import org.joml.Vector3f;

import a3.MyGame;
import a3.managers.ScriptManager;
import a3.managers.SoundManager;
import tage.shapes.AnimatedShape;

public class AnimatedGameObject extends GameObject {
	private AnimatedShape animatedShape;
	private boolean isMoving = false;
	private Vector3f lastLocation = new Vector3f();
	private ScriptManager scriptManager = MyGame.getGameInstance().getScriptManager();
	private SoundManager soundManager = MyGame.getGameInstance().getSoundManager();
	private int frameCount = 0;

	public AnimatedGameObject(GameObject p, ObjShape s, TextureImage t) {
		super(p, s, t);
		animatedShape = (AnimatedShape) s;
	}

	/**
	 * Sets the last location of the object
	 */
	public void setLastLocation(Vector3f pos) {
		lastLocation = pos;
	}

	/**
	 * Returns the last location of the object
	 */
	public Vector3f getLastLocation() {
		return lastLocation;
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
	private void playAnimation(String animation) {
		if (getAnimationShape().getAnimation(animation) == null) {
			System.out.println("Animation not found...");
			return;
		}

		getAnimationShape().playAnimation(animation, 1f, AnimatedShape.EndType.PAUSE, 0);
	}

	/**
	 * plays the specified animation at the indicated speed
	 */
	private void playAnimation(String animation, float speed) {
		if (getAnimationShape().getAnimation(animation) == null) {
			System.out.println("Animation not found...");
			return;
		}

		getAnimationShape().playAnimation(animation, speed, AnimatedShape.EndType.PAUSE, 0);
	}

	/*
	 * handles the switch to a different animation, cancels the current playing
	 * animation and then plays the new specified animation. Calls the playAnimation
	 * method.
	 */
	public void handleAnimationSwitch(String animation) {
		if (!getAnimationShape().isAnimPlaying()) {
			playAnimation(animation);
		} else if (!getAnimationShape().getAnimation(animation).equals(getAnimationShape().getCurrentAnimation())) {
			getAnimationShape().pauseAnimation();
			playAnimation(animation);
		}
	}

	/*
	 * handles the switch to a different animation, cancels the current playing
	 * animation and then plays the new specified animation. Calls the playAnimation
	 * method. The speed of the animation is specified.
	 */
	public void handleAnimationSwitch(String animation, float speed) {
		if (!getAnimationShape().isAnimPlaying()) {
			playAnimation(animation, speed);
		} else if (!getAnimationShape().getAnimation(animation).equals(getAnimationShape().getCurrentAnimation())) {
			getAnimationShape().pauseAnimation();
			playAnimation(animation, speed);
		}
	}

	public void idle() {
		handleAnimationSwitch("IDLE");
	}

	public void updateAnimation() {
		getAnimationShape().updateAnimation();
	}

	public SoundManager getSoundManager() {
		if (soundManager == null) {
			soundManager = MyGame.getGameInstance().getSoundManager();
		}
		return soundManager;
	}

	public ScriptManager getScriptManager() {
		if (scriptManager == null) {
			scriptManager = MyGame.getGameInstance().getScriptManager();
		}
		return scriptManager;
	}

	public void addFrameCount() {
		frameCount++;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public void resetFrameCount() {
		frameCount = 0;
	}
}
