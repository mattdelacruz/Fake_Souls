package a3;

import a3.quadtree.*;
import tage.*;
import tage.input.InputManager;
import tage.shapes.*;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MyGame extends VariableFrameRateGame {
	private static final int WINDOW_WIDTH = 1900;
	private static final int WINDOW_HEIGHT = 1000;
	private static final int AXIS_LENGTH = 10000;
	private static final int ENEMY_AMOUNT = 10;
	private static final float PLAY_AREA_SIZE = 555f;

	private static final Vector3f INITIAL_ORBIT_CAMERA_POS = new Vector3f(0f, 0f, 5f);

	private static final String SKYBOX_NAME = "fluffyClouds";

	private static Engine engine;
	private static MyGame game;

	private PlayerControlMap playerControlMaps; // do not delete!!!
	private InputManager inputManager;
	private TargetCamera targetCamera;
	private OverheadCamera overheadCamera;
	private PlayerControlFunctions state;

	private double lastFrameTime, currFrameTime, elapsTime;

	private float frameTime = 0;

	private GameObject planeMap, x, y, z, nX, nY, nZ;

	private Enemy enemy;
	private Player player;

	private ObjShape playerS, enemyS;
	private TextureImage playerTx, enemyTx, moonTx;
	private Light light1;
	private Line worldXAxis, worldYAxis, worldZAxis, worldNXAxis, worldNYAxis,
			worldNZAxis;
	private Plane moonCrater;
	private ArrayList<GameObject> worldAxisList = new ArrayList<GameObject>();
	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();

	private QuadTree quadTree = new QuadTree(new QuadTreePoint(-PLAY_AREA_SIZE, PLAY_AREA_SIZE),
			new QuadTreePoint(PLAY_AREA_SIZE, -PLAY_AREA_SIZE));

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
		playerS = new ImportedModel("dolphinHighPoly.obj");
		enemyS = new Cube();
		moonCrater = new Plane();
		loadWorldAxes();
	}

	@Override
	public void loadTextures() {
		playerTx = new TextureImage("Dolphin_HighPolyUV.png");
		enemyTx = new TextureImage("mars.png");
		moonTx = new TextureImage("moon-craters.jpg");
	}

	@Override
	public void loadSkyBoxes() {
		engine.getSceneGraph().setSkyBoxEnabled(true);
		engine.getSceneGraph().setActiveSkyBoxTexture(engine.getSceneGraph().loadCubeMap(SKYBOX_NAME));
	}

	@Override
	public void buildObjects() {
		buildPlayer();
		buildEnemy();
		buildWorldAxes();
		buildPlaneMap();
		buildEnemyQuadTree();
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

		state = new PlayerControls();
	}

	private void initializeCameras() {
		buildTargetCamera();
	}

	private void initializeControls() {
		inputManager = engine.getInputManager();
		playerControlMaps = new PlayerControlMap(inputManager);
	}

	@Override
	public void update() {
		updateFrameTime();
		targetCamera.update();
		if (player.isLocked) {
			targetCamera.targetTo();
		}

		inputManager.update((float) elapsTime);
	}

	private void updateFrameTime() {
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		frameTime = (float) (currFrameTime - lastFrameTime) / 1000.0f;
		elapsTime += frameTime;
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

	private void buildPlayer() {
		player = new Player(GameObject.root(), playerS, playerTx);
	}

	private void buildEnemy() {
		for (int i = 0; i < ENEMY_AMOUNT; i++) {
			enemy = new Enemy(GameObject.root(), enemyS, enemyTx);
			enemyList.add(enemy);
		}
	}

	private void buildEnemyQuadTree() {
		for (Enemy e : enemyList) {
			quadTree.insert(new QuadTreeNode(new QuadTreePoint(e.getLocalLocation().z, e.getLocalLocation().x), e));
		}
	}

	private void buildTargetCamera() {
		targetCamera = new TargetCamera(getPlayer());
		getPlayer().setCamera(targetCamera);
		targetCamera.setLocation(INITIAL_ORBIT_CAMERA_POS);
		engine.getRenderSystem().getViewport("MAIN").setCamera(targetCamera);
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

	public GameObject findTarget() {
		QuadTreePoint playerPos = new QuadTreePoint(player.getLocalLocation().z,
				player.getLocalLocation().x());
		QuadTreeNode target;
		target = quadTree.findNearby(playerPos, -1, null);

		if (target != null) {
			return target.getData();
		}
		return null;
	}

	public float getFrameTime() {
		return frameTime;
	}

	public PlayerControlFunctions getState() {
		return state;
	}

	public TargetCamera getTargetCamera() {
		return targetCamera;
	}

	public Camera getOverheadCamera() {
		return overheadCamera;
	}

	public void setState(PlayerControlFunctions s) {
		state = s;
	}

	public Player getPlayer() {
		return player;
	}

	public Enemy getEnemy() {
		return enemy;
	}

}
