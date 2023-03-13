package a3;

import tage.*;
import tage.input.InputManager;
import tage.nodeControllers.FloatController;
import tage.nodeControllers.OrbitController;
import tage.nodeControllers.RotationController;
import tage.shapes.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import org.joml.*;

public class MyGame extends VariableFrameRateGame {
	private static final int WINDOW_WIDTH = 1900;
	private static final int WINDOW_HEIGHT = 1000;
	private static final int AXIS_LENGTH = 10000;
	private static final int PRIZE_AMOUNT = 4;
	private static final int BURGER_AMOUNT = 10;
	private static final int HUNGER_AMT = 100;
	private static final long BURGER_RESPAWN_PERIOD = 10000;
	private static final long BURGER_RESPAWN_DELAY = 10000;
	private static final double HUNGER_DECAY = 0.5;
	private static final double HUNGER_REPLENISH = 5;
	private static final float BURGER_SCALE = .5f;
	private static final float INITIAL_DOLPHIN_SCALE = 3.0f;
	private static final float PLAY_AREA_SIZE = 555f;
	private static final Vector3f INITIAL_DOLPHIN_POS = new Vector3f(0f, 1f, 0f);
	private static final Vector3f INITIAL_ORBIT_CAMERA_POS = new Vector3f(0f, 0f, 5f);
	private static final Vector3f INITIAL_OVERHEAD_CAMERA_POS = new Vector3f(0f, 10.0f, 0f);
	private static final String SKYBOX_NAME = "redNebula";

	private static Engine engine;
	private static MyGame game;

	private InputManager inputManager;
	private CameraOrbit3D orbitCamera;
	private OverheadCamera overheadCamera;
	private PlayerControlMap playerControlMaps;
	private PlayerControlFunctions state;
	private RotationController rotateController;
	private FloatController floatController;
	private OrbitController orbitController;
	private Timer timer;

	private double lastFrameTime, currFrameTime, elapsTime;
	private int score = 0;
	private double hunger = 0;
	private float frameTime = 0;
	private boolean hasMoved = false;
	private float dolphinSize = INITIAL_DOLPHIN_SCALE;
	private Viewport overheadViewport;
	private int overheadViewportX, overheadViewportY;

	private GameObject dol, planeMap, collisionObject, x, y, z, nX, nY, nZ;
	private ObjShape dolS, prizeS;
	private ManualObject burgerM;
	private TextureImage doltx, prizeTx, prizeMyTx, burgerTx, moonTx;
	private Light light1;
	private Line worldXAxis, worldYAxis, worldZAxis, worldNXAxis, worldNYAxis,
			worldNZAxis;
	private Plane moonCrater;
	private ArrayList<GameObject> prizeList = new ArrayList<GameObject>();
	private ArrayList<GameObject> burgerList = new ArrayList<GameObject>();
	private ArrayList<GameObject> eatenBurgerList = new ArrayList<GameObject>();
	private ArrayList<GameObject> worldAxisList = new ArrayList<GameObject>();

	public MyGame() {
		super();
	}

	public static void main(String[] args) {
		engine = new Engine(getGameInstance());
		getGameInstance().initializeSystem();
		getGameInstance().game_loop();
	}

	@Override
	public void loadShapes() {
		dolS = new ImportedModel("dolphinHighPoly.obj");
		prizeS = new Sphere();
		burgerM = new ManualBurger();
		moonCrater = new Plane();
		loadWorldAxes();
	}

	@Override
	public void loadTextures() {
		doltx = new TextureImage("Dolphin_HighPolyUV.png");
		prizeTx = new TextureImage("mars.png");
		prizeMyTx = new TextureImage("my_texture.png");
		burgerTx = new TextureImage("burger.png");
		moonTx = new TextureImage("moon-craters.jpg");
	}

	@Override
	public void loadSkyBoxes() {
		engine.getSceneGraph().setSkyBoxEnabled(true);
		engine.getSceneGraph().setActiveSkyBoxTexture(engine.getSceneGraph().loadCubeMap(SKYBOX_NAME));
	}

	@Override
	public void buildObjects() {
		buildDolphin();
		buildPrizes();
		buildBurgers();
		buildWorldAxes();
		buildPlaneMap();
		buildControllers();
	}

	@Override
	public void initializeLights() {
		Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
		light1 = new Light();
		light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
		(engine.getSceneGraph()).addLight(light1);
	}

	@Override
	public void initializeGame() {
		lastFrameTime = System.currentTimeMillis();
		currFrameTime = System.currentTimeMillis();
		elapsTime = 0.0;

		(engine.getRenderSystem()).setWindowDimensions(WINDOW_WIDTH, WINDOW_HEIGHT);

		initializeControls();
		initializeCameras();
		initializeBurgerRespawn();

		state = new DolphinControls();
		hunger = HUNGER_AMT;
	}

	private void initializeCameras() {
		buildOrbitCamera();
		buildOverheadCamera();
	}

	private void initializeControls() {
		inputManager = engine.getInputManager();
		playerControlMaps = new PlayerControlMap(inputManager);
	}

	@Override
	public void update() {
		updateHUD();
		orbitCamera.update();

		if (hasMoved) {
			if (!floatController.isEnabled()) {
				floatController.enable();
			}
			collisionObject = getCollidedObject(prizeList.iterator());
			if (collisionObject != null) {
				score++;
				orbitController.addTarget(new GameObject(GameObject.root(), prizeS, collisionObject.getTextureImage()));
			}
			collisionObject = getCollidedObject(burgerList.iterator());
			if (collisionObject != null) {
				eatenBurgerList.add(collisionObject);
				floatController.removeTarget(collisionObject);
				rotateController.removeTarget(collisionObject);
			}
			handleOutOfBounds();
			handleHungerDecay();
			handleGoal();
		} else {
			hasMoved();
		}

		inputManager.update((float) elapsTime);
	}

	private void initializeBurgerRespawn() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			float xPos, zPos;
			GameObject respawn;
			Iterator<GameObject> it;

			@Override
			public void run() {
				it = eatenBurgerList.iterator();
				if (it.hasNext()) {
					xPos = (float) ThreadLocalRandom.current().nextDouble(-PLAY_AREA_SIZE, PLAY_AREA_SIZE);
					zPos = (float) ThreadLocalRandom.current().nextDouble(-PLAY_AREA_SIZE, PLAY_AREA_SIZE);
					respawn = it.next();
					it.remove();
					respawn.setLocalTranslation(new Matrix4f().translation(xPos, BURGER_SCALE * 3, zPos));
					rotateController.addTarget(respawn);
					floatController.addTarget(respawn);
					burgerList.add(respawn);
				}
			}
		}, BURGER_RESPAWN_DELAY, BURGER_RESPAWN_PERIOD);
	}

	private void handleHungerDecay() {
		hunger -= HUNGER_DECAY * frameTime;
	}

	private boolean hasMoved() {
		if (dol.getWorldLocation().x > 0 || dol.getWorldLocation().z > 0 || dol.getWorldLocation().x < 0
				|| dol.getWorldLocation().z < 0) {
			hasMoved = true;
		}
		return hasMoved;
	}

	private void handleGoal() {
		if (hunger <= 0) {
			handleReset();
		}

		if (score == PRIZE_AMOUNT) {
			handleReset();
		}
	}

	private void handleReset() {
		hunger = HUNGER_AMT;
		score = 0;
		dolphinSize = INITIAL_DOLPHIN_SCALE;
		dol.setLocalScale(new Matrix4f().scale(dolphinSize));
		dol.setLocalLocation(INITIAL_DOLPHIN_POS);
		rotateController.clearTargets();
		floatController.clearTargets();
		orbitController.clearTargets();
		clearList(prizeList.iterator());
		clearList(burgerList.iterator());
		buildPrizes();
		buildBurgers();
		buildControllers();
	}

	private void clearList(Iterator<?> iterator) {
		while (iterator.hasNext()) {
			GameObject go = (GameObject) iterator.next();
			go.getRenderStates().disableRendering();
			iterator.remove();
		}
	}

	private void handleOutOfBounds() {
		if (dol.getWorldLocation().x >= PLAY_AREA_SIZE) {
			dol.setLocalLocation(new Vector3f(PLAY_AREA_SIZE, dol.getWorldLocation().y, dol.getWorldLocation().z));
		}
		if (dol.getWorldLocation().x <= -PLAY_AREA_SIZE) {
			dol.setLocalLocation(new Vector3f(-PLAY_AREA_SIZE, dol.getWorldLocation().y, dol.getWorldLocation().z));
		}
		if (dol.getWorldLocation().z >= PLAY_AREA_SIZE) {
			dol.setLocalLocation(new Vector3f(dol.getWorldLocation().x, dol.getWorldLocation().y, PLAY_AREA_SIZE));
		}
		if (dol.getWorldLocation().z <= -PLAY_AREA_SIZE) {
			dol.setLocalLocation(new Vector3f(dol.getWorldLocation().x, dol.getWorldLocation().y, -PLAY_AREA_SIZE));
		}
	}

	private void updateHUD() {
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		frameTime = (float) (currFrameTime - lastFrameTime) / 1000.0f;
		elapsTime += frameTime;

		String dispStr1 = "Score = " + score;
		String dispStr2 = "Hunger = " + (int) hunger;
		String dispStr3 = "Position = " + String.format(
				"x: %.2f, y: %.2f, z: %.2f",
				dol.getWorldLocation().x, dol.getWorldLocation().y, dol.getWorldLocation().z);

		overheadViewportX = (int) ((overheadViewport.getRelativeLeft()
				* getEngineInstance().getRenderSystem().getWidth()) + overheadViewport.getBorderWidth() * 1.3);
		overheadViewportY = (int) ((overheadViewport.getRelativeBottom()
				* getEngineInstance().getRenderSystem().getHeight()) + overheadViewport.getBorderWidth() * 2);
		Vector3f hud1Color = new Vector3f(1, 0, 0);
		Vector3f hud2Color = new Vector3f(0, 1, 0);
		Vector3f hud3Color = new Vector3f(255, 223, 0);
		(engine.getHUDmanager()).setHUD1(dispStr1, hud1Color,
				(int) (getEngineInstance().getRenderSystem().getWidth() * .01f),
				(int) (getEngineInstance().getRenderSystem().getHeight() * .01f));
		(engine.getHUDmanager()).setHUD2(dispStr2, hud2Color,
				(int) (getEngineInstance().getRenderSystem().getWidth() * .2f),
				(int) (getEngineInstance().getRenderSystem().getHeight() * .01f));
		(engine.getHUDmanager()).setHUD3(dispStr3, hud3Color, overheadViewportX, overheadViewportY);
	}

	private void loadWorldAxes() {
		worldXAxis = new Line(new Vector3f(0, 0, 0), new Vector3f(AXIS_LENGTH, 0, 0));
		worldYAxis = new Line(new Vector3f(0, 0, 0), new Vector3f(0, AXIS_LENGTH, 0));
		worldZAxis = new Line(new Vector3f(0, 0, 0), new Vector3f(0, 0, AXIS_LENGTH));
		worldNXAxis = new Line(new Vector3f(0, 0, 0), new Vector3f(-AXIS_LENGTH, 0, 0));
		worldNYAxis = new Line(new Vector3f(0, 0, 0), new Vector3f(0, -AXIS_LENGTH, 0));
		worldNZAxis = new Line(new Vector3f(0, 0, 0), new Vector3f(0, 0, -AXIS_LENGTH));
	}

	private void buildPlaneMap() {
		planeMap = new GameObject(GameObject.root(), moonCrater, moonTx);
		planeMap.setLocalLocation(new Vector3f(0, 0, 0));
		planeMap.setLocalScale((new Matrix4f().scaling(PLAY_AREA_SIZE)));
	}

	private void buildControllers() {
		buildRotationController(burgerList);
		buildFloatController(burgerList);
		buildOrbitController();
	}

	private void buildRotationController(ArrayList<GameObject> list) {
		rotateController = new RotationController(new Vector3f(0, 1f, 0), 0.001f);
		for (GameObject o : list) {
			rotateController.addTarget(o);
		}
		getEngineInstance().getSceneGraph().addNodeController(rotateController);
		rotateController.enable();
	}

	private void buildFloatController(ArrayList<GameObject> list) {
		floatController = new FloatController(0.001f);
		for (GameObject o : list) {
			floatController.addTarget(o);
		}
		getEngineInstance().getSceneGraph().addNodeController(floatController);
	}

	private void buildOrbitController() {
		orbitController = new OrbitController(dol);
		getEngineInstance().getSceneGraph().addNodeController(orbitController);
		orbitController.enable();
	}

	private void buildDolphin() {
		Matrix4f initialTranslation, initialScale;

		dol = new GameObject(GameObject.root(), dolS, doltx);
		initialTranslation = (new Matrix4f()).translation(INITIAL_DOLPHIN_POS);
		initialScale = (new Matrix4f()).scaling(INITIAL_DOLPHIN_SCALE);
		dol.setLocalTranslation(initialTranslation);
		dol.setLocalScale(initialScale);
	}

	private void buildOrbitCamera() {
		orbitCamera = new CameraOrbit3D(dol);
		orbitCamera.setLocation(INITIAL_ORBIT_CAMERA_POS);
		engine.getRenderSystem().getViewport("MAIN").setCamera(orbitCamera);
	}

	private void buildOverheadCamera() {
		overheadCamera = new OverheadCamera(getEngineInstance());
		overheadCamera.setLocation(INITIAL_OVERHEAD_CAMERA_POS);
		overheadViewport = getEngineInstance().getRenderSystem().getViewport("OVERHEAD");
		overheadViewport.setBorderColor(255, 255, 255);

	}

	private void buildPrizes() {
		float xPos = (float) ThreadLocalRandom.current().nextDouble(0, PLAY_AREA_SIZE);
		float zPos = (float) ThreadLocalRandom.current().nextDouble(0, PLAY_AREA_SIZE);
		float scale = (float) ThreadLocalRandom.current().nextDouble(0.3, 2);
		GameObject obj = new GameObject(GameObject.root(), prizeS, prizeMyTx);

		prizeList.add(obj);

		for (int i = 0; i < PRIZE_AMOUNT - 1; i++) {
			xPos = (float) ThreadLocalRandom.current().nextDouble(-PLAY_AREA_SIZE, PLAY_AREA_SIZE);
			zPos = (float) ThreadLocalRandom.current().nextDouble(-PLAY_AREA_SIZE, PLAY_AREA_SIZE);
			scale = (float) ThreadLocalRandom.current().nextDouble(0.3, 5);

			obj = new GameObject(GameObject.root(), prizeS, prizeTx);
			obj.setLocalScale(new Matrix4f().scaling(scale));
			obj.setLocalTranslation(
					new Matrix4f().translation(xPos, scale, zPos));

			prizeList.add(obj);
		}
	}

	private void buildBurgers() {
		float xPos, zPos;
		GameObject obj;
		for (int i = 0; i < BURGER_AMOUNT; i++) {
			obj = new GameObject(GameObject.root(), burgerM, burgerTx);
			xPos = (float) ThreadLocalRandom.current().nextDouble(-PLAY_AREA_SIZE, PLAY_AREA_SIZE);
			zPos = (float) ThreadLocalRandom.current().nextDouble(-PLAY_AREA_SIZE, PLAY_AREA_SIZE);

			obj.setLocalScale(new Matrix4f().scaling(BURGER_SCALE));
			obj.setLocalTranslation(new Matrix4f().translation(xPos, BURGER_SCALE * 3, zPos));
			burgerList.add(obj);
		}
	}

	private void buildWorldAxes() {
		x = new GameObject(GameObject.root(), worldXAxis);
		y = new GameObject(GameObject.root(), worldYAxis);
		z = new GameObject(GameObject.root(), worldZAxis);
		nX = new GameObject(GameObject.root(), worldNXAxis);
		nY = new GameObject(GameObject.root(), worldNYAxis);
		nZ = new GameObject(GameObject.root(), worldNZAxis);

		x.getRenderStates().setColor(new Vector3f(1f, 0f, 0f));
		y.getRenderStates().setColor(new Vector3f(0f, 1f, 0f));
		z.getRenderStates().setColor(new Vector3f(0f, 0f, 1f));
		nX.getRenderStates().setColor(new Vector3f(1f, 0f, 0f));
		nY.getRenderStates().setColor(new Vector3f(0f, 1f, 0f));
		nZ.getRenderStates().setColor(new Vector3f(0f, 0f, 1f));

		worldAxisList.add(x);
		worldAxisList.add(y);
		worldAxisList.add(z);
		worldAxisList.add(nX);
		worldAxisList.add(nY);
		worldAxisList.add(nZ);
	}

	public static MyGame getGameInstance() {
		if (game == null)
			game = new MyGame();
		return game;
	}

	public static Engine getEngineInstance() {
		if (engine == null)
			engine = new Engine(getGameInstance());
		return engine;
	}

	private GameObject getCollidedObject(Iterator<GameObject> list) {
		while (list.hasNext()) {
			GameObject p = list.next();
			if (Math.floor(dol.getLocalLocation()
					.distance(p.getWorldLocation())) <= p.getLocalScale().get(0, 0)) {
				p.getRenderStates().disableRendering();
				list.remove();
				System.gc();
				return p;
			}
		}
		return null;
	}

	public void renderGameAxis() {
		if (worldAxisList.get(0).getRenderStates().renderingEnabled()) {
			for (GameObject go : worldAxisList) {
				go.getRenderStates().disableRendering();
			}
		} else {
			for (GameObject go : worldAxisList) {
				go.getRenderStates().enableRendering();
			}
		}
	}

	public float getFrameTime() {
		return frameTime;
	}

	public PlayerControlFunctions getState() {
		return state;
	}

	public CameraOrbit3D getOrbitCamera() {
		return orbitCamera;
	}

	public Camera getOverheadCamera() {
		return overheadCamera;
	}

	public GameObject getDolphin() {
		return dol;
	}

	public void setState(PlayerControlFunctions s) {
		state = s;
	}
}
