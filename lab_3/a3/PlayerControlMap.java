package a3;

import tage.input.*;
import tage.input.IInputManager.INPUT_ACTION_TYPE;

public class PlayerControlMap {

	public PlayerControlMap(InputManager inputManager) {
		LeftStickYAction leftStickYAction = new LeftStickYAction();
		RightStickXAction rightStickXAction = new RightStickXAction();
		RightStickYAction rightStickYAction = new RightStickYAction();
		Button_2Action button_2Action = new Button_2Action();
		OverheadAction overheadAction = new OverheadAction();
		GameWorldAssetAction gameWorldAssetAction = new GameWorldAssetAction();

		// gamepad controls
		// forward and backward y axis on left stick
		inputManager.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Y, leftStickYAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// left and right x axis on right stick
		inputManager.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Z, rightStickXAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// up and down y axis on right stick
		inputManager.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.RZ,
				rightStickYAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// button 2 == O button on PS4 controller
		inputManager.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._2, button_2Action,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		inputManager.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._1, gameWorldAssetAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// d-pad on PS4 controller
		inputManager.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.POV,
				overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// left trigger on PS4 controller
		inputManager.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._6,
				overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// right trigger on PS4 controller
		inputManager.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._7,
				overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		

		// keyboard controls
		// basic movement controls
		inputManager.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.W, leftStickYAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.A, rightStickXAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.S, leftStickYAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.D, rightStickXAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.UP,
				rightStickYAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.DOWN,
				rightStickYAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// switch between orbit camera and avatar
		inputManager.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.SPACE,
				button_2Action, INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// overhead camera controls
		inputManager.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.I, overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		inputManager.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.J, overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		inputManager.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.K, overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.L, overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.N, overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		inputManager.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.M, overheadAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// gameworld asset controls
		inputManager.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.R,
				gameWorldAssetAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}

}
