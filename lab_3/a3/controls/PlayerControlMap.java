package a3.controls;

import a3.controls.controlactions.GameWorldAssetAction;
import a3.controls.controlactions.LeftStickPressAction;
import a3.controls.controlactions.LeftStickYAction;
import a3.controls.controlactions.MouseAction;
import a3.controls.controlactions.OverheadAction;
import a3.controls.controlactions.RightStickXAction;
import a3.controls.controlactions.RightStickYAction;
import tage.input.*;
import tage.input.IInputManager.INPUT_ACTION_TYPE;
import net.java.games.input.*;

public class PlayerControlMap {
	private LeftStickYAction leftStickYAction;
	private RightStickXAction rightStickXAction;
	private RightStickYAction rightStickYAction;
	private OverheadAction overheadAction;
	private LeftStickPressAction leftStickPressAction;
	private GameWorldAssetAction gameWorldAssetAction;
	private MouseAction mouseAction;
	private InputManager inputManager;

	public PlayerControlMap(InputManager im) {
		inputManager = im;
		leftStickYAction = new LeftStickYAction();
		rightStickXAction = new RightStickXAction();
		rightStickYAction = new RightStickYAction();
		overheadAction = new OverheadAction();
		gameWorldAssetAction = new GameWorldAssetAction();
		leftStickPressAction = new LeftStickPressAction();
		mouseAction = new MouseAction();

		// gamepad controls
		initializeGamepadControls();
		// basic movement controls
		initializeKeyboardMovementControls();
		// basic camera controls
		initializeKeyboardCameraControls();
		// gameworld asset controls
		initializeOverWorldControls();
		// mouse controls
		initializeMouseControls();
	}

	private void initializeMouseControls() {
		inputManager.associateActionWithAllMice(
				Component.Identifier.Button.LEFT, mouseAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		inputManager.associateActionWithAllMice(
				Component.Identifier.Button.MIDDLE, mouseAction, 
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		inputManager.associateActionWithAllMice(
				Component.Identifier.Button.RIGHT, mouseAction, 
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		inputManager.associateActionWithAllMice(
				Component.Identifier.Axis.X, mouseAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}

	private void initializeOverWorldControls() {
		inputManager.associateActionWithAllKeyboards(
			Component.Identifier.Key.R,
			gameWorldAssetAction,
			InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}

	private void initializeKeyboardCameraControls() {
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.UP,
				rightStickYAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.DOWN,
				rightStickYAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// overhead camera controls
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.O,
				leftStickPressAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}

	private void initializeKeyboardMovementControls() {
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.W, leftStickYAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.A, rightStickXAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.S, leftStickYAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.D, rightStickXAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}

	private void initializeGamepadControls() {
		// forward and backward y axis on left stick
		inputManager.associateActionWithAllGamepads(Component.Identifier.Axis.Y, leftStickYAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// left and right x axis on right stick
		inputManager.associateActionWithAllGamepads(Component.Identifier.Axis.Z, rightStickXAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// up and down y axis on right stick
		inputManager.associateActionWithAllGamepads(Component.Identifier.Axis.RZ,
				rightStickYAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllGamepads(Component.Identifier.Button._1,
				gameWorldAssetAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// d-pad on PS4 controller
		inputManager.associateActionWithAllGamepads(Component.Identifier.Axis.POV,
				overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		// left trigger on PS4 controller
		inputManager.associateActionWithAllGamepads(Component.Identifier.Button._6,
				overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// right trigger on PS4 controller
		inputManager.associateActionWithAllGamepads(Component.Identifier.Button._7,
				overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

	}

}
