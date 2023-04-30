package a3.controls;

import tage.input.*;
//import a3.controls.controlactions.GameWorldAssetAction;
import a3.controls.controlactions.KeyboardActions;
import a3.controls.controlactions.MoveVerticalAction;
import a3.controls.controlactions.MouseAction;
//import a3.controls.controlactions.OverheadAction;
import a3.controls.controlactions.MoveHorizontalAction;
//import a3.controls.controlactions.RightStickYAction;
import net.java.games.input.*;

public class PlayerControlMap {
	private MoveVerticalAction moveVerticalAction;
	private MoveHorizontalAction moveHorizontalAction;
	// private RightStickYAction rightStickYAction;
	// private OverheadAction overheadAction;
	private KeyboardActions keyboardAction;
	// private GameWorldAssetAction gameWorldAssetAction;
	private MouseAction mouseAction;
	private InputManager inputManager;

	public PlayerControlMap(InputManager im) {
		inputManager = im;
		moveVerticalAction = new MoveVerticalAction();
		moveHorizontalAction = new MoveHorizontalAction();
		keyboardAction = new KeyboardActions();
		mouseAction = new MouseAction();

		// gamepad controls
		initializeGamepadControls();
		// keyboard button controls
		intializeKeyboardButtonControls();
		// basic movement controls
		initializeKeyboardMovementControls();
		// mouse controls
		initializeMouseControls();
	}

	private void intializeKeyboardButtonControls() {
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.G, keyboardAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.O, keyboardAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.LSHIFT, keyboardAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
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
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllMice(
				Component.Identifier.Axis.X, mouseAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}

	private void initializeKeyboardMovementControls() {
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.W, moveVerticalAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.A, moveHorizontalAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.S, moveVerticalAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
		inputManager.associateActionWithAllKeyboards(
				Component.Identifier.Key.D, moveHorizontalAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
	}

	private void initializeGamepadControls() {
		// forward and backward y axis on left stick
		inputManager.associateActionWithAllGamepads(
				Component.Identifier.Axis.Y, moveVerticalAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// left and right x axis on right stick
		inputManager.associateActionWithAllGamepads(
				Component.Identifier.Axis.Z, moveHorizontalAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// // up and down y axis on right stick
		// inputManager.associateActionWithAllGamepads(
		// Component.Identifier.Axis.RZ, rightStickYAction,
		// InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// inputManager.associateActionWithAllGamepads(
		// Component.Identifier.Button._1, gameWorldAssetAction,
		// InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// // d-pad on PS4 controller
		// inputManager.associateActionWithAllGamepads(
		// Component.Identifier.Axis.POV, overheadAction,
		// InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		// // left trigger on PS4 controller
		// inputManager.associateActionWithAllGamepads(
		// Component.Identifier.Button._6, overheadAction,
		// InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// // right trigger on PS4 controller
		// inputManager.associateActionWithAllGamepads(
		// Component.Identifier.Button._7, overheadAction,
		// InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// }
	}
}
