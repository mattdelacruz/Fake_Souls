package a3;

import tage.*;
import tage.input.InputManager;
import tage.networking.IGameConnection.ProtocolType;
import tage.physics.PhysicsEngine;
import tage.physics.PhysicsEngineFactory;
import tage.physics.PhysicsObject;
import tage.shapes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import a3.controls.PlayerControlFunctions;
import a3.controls.PlayerControlMap;
import a3.controls.PlayerControls;
import a3.network.GhostManager;
import a3.network.ProtocolClient;
import a3.npcs.Enemy;
import a3.player.Player;
import a3.quadtree.*;

public class MyGame extends VariableFrameRateGame {
	private static final int WINDOW_WIDTH = 1900;
	private static final int WINDOW_HEIGHT = 1000;
	private static final int AXIS_LENGTH = 10000;
	private static final int ENEMY_AMOUNT = 10;
	private static final float PLAY_AREA_SIZE = 300f;

	private static final Vector3f INITIAL_CAMERA_POS = new Vector3f(0f, 0f, 5f);

	private static final String SKYBOX_NAME = "fluffyClouds";

	private static Engine engine;
	private static MyGame game;
	private static PlayerControlMap playerControlMaps; // do not delete!!!

	private InputManager inputManager;
	private TargetCamera targetCamera;
	private OverheadCamera overheadCamera;
	private PlayerControlFunctions state;

	private int serverPort;
	private double lastFrameTime, currFrameTime, elapsTime;
	private float lastHeightLoc, currHeightLoc;

	private String serverAddress;

	private float frameTime = 0;
	private float[] vals = new float[16];

	private GameObject terrain, x, y, z, nX, nY, nZ;

	private static ScriptEngine jsEngine;
	private PhysicsEngine physicsEngine;
	private GhostManager ghostManager;
	private File scriptFile;

	private Enemy enemy;
	private Player player;
	private static ScriptEngineManager factory;
	private ProtocolType serverProtocol;
	private ProtocolClient protocolClient;
	private boolean isClientConnected = false;

	private ObjShape playerS, enemyS, ghostS, terrS;
	private TextureImage playerTx, enemyTx, terrMap, ghostTx, terrTx;
	private Line worldXAxis, worldYAxis, worldZAxis, worldNXAxis, worldNYAxis,
			worldNZAxis;

	private ArrayList<GameObject> worldAxisList = new ArrayList<GameObject>();
	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	private ArrayList<PhysicsObject> enemyPhysicsList = new ArrayList<PhysicsObject>();
	private QuadTree quadTree = new QuadTree(new QuadTreePoint(-PLAY_AREA_SIZE, PLAY_AREA_SIZE),
			new QuadTreePoint(PLAY_AREA_SIZE, -PLAY_AREA_SIZE));

	public MyGame(String serverAddress, int serverPort, String protocol) {
		super();
		if (serverAddress != null && serverPort != 0 && protocol != null) {
			ghostManager = new GhostManager(this);
			this.serverAddress = serverAddress;
			this.serverPort = serverPort;
			if (protocol.toUpperCase().compareTo("UDP") == 0) {
				this.serverProtocol = ProtocolType.UDP;
			} else {
				System.err.println("PROTOCOL NOT SUPPORTED, EXITING...");
				System.exit(-1);
			}
		}
	}

	public MyGame() {
		super();
	}
	public static void main(String[] args) {
		if (args.length != 0) {
			System.out.println("setting up network...");
			game = new MyGame(args[0], Integer.parseInt(args[1]), args[2]);
		} else {
			game = new MyGame();
		}
		engine = new Engine(getGameInstance());
		factory = new ScriptEngineManager();
		jsEngine = factory.getEngineByName("js");
		getGameInstance().initializeSystem();
		getGameInstance().game_loop();
	}

	private void setupNetworking() {
		isClientConnected = false;
		try {
			protocolClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (protocolClient == null) {
			System.out.println("missing protocol host");
		} else {
			System.out.println("sending join message...");
			protocolClient.sendJoinMessage();
		}
	}

	protected void processNetworking(float elapsTime) {
		if (protocolClient != null) {
			protocolClient.processPackets();
		}
	}

	@Override
	public void loadShapes() {
		playerS = new ImportedModel("player.obj");
		ghostS = new Sphere();
		enemyS = new ImportedModel("enemy.obj");
		terrS = new TerrainPlane(100);
		loadWorldAxes();
	}

	@Override
	public void loadTextures() {
		playerTx = new TextureImage("player-texture.png");
		ghostTx = new TextureImage("neptune.jpg");
		enemyTx = new TextureImage("enemy-texture.png");
		terrMap = new TextureImage("terrain-map.png");
		terrTx = new TextureImage("moon-craters.jpg");
	}

	@Override
	public void loadSkyBoxes() {
		engine.getSceneGraph().setSkyBoxEnabled(true);
		engine.getSceneGraph().setActiveSkyBoxTexture(engine.getSceneGraph().loadCubeMap(SKYBOX_NAME));
	}

	@Override
	public void buildObjects() {
		buildPhysicsEngine();
		buildWorldAxes();
		buildTerrainMap();
		buildPlayer();
		buildEnemy();
		buildEnemyQuadTree();
	}

	@Override
	public void initializeLights() {
		scriptFile = new File("assets/scripts/LoadInitValues.js");
		executeScript(jsEngine, scriptFile);

		(engine.getSceneGraph()).addLight((Light) jsEngine.get("light"));
	}

	@Override
	public void initializeGame() {
		lastFrameTime = System.currentTimeMillis();
		currFrameTime = System.currentTimeMillis();
		lastHeightLoc = 0;
		elapsTime = 0.0;

		(engine.getRenderSystem()).setWindowDimensions(WINDOW_WIDTH, WINDOW_HEIGHT);

		initializeControls();
		updateFrameTime();
		initializeCameras();
		setupNetworking();
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
		if (player.isLocked()) {
			targetCamera.targetTo();
		}
		targetCamera.setLookAtTarget(player.getLocalLocation());
		updatePlayerTerrainLocation();
		inputManager.update((float) elapsTime);
		processNetworking((float) elapsTime);
	}

	private void updatePlayerTerrainLocation() {
		Vector3f currLoc = player.getLocalLocation();
		currHeightLoc = terrain.getHeight(currLoc.x, currLoc.z);
		if (Math.abs(currHeightLoc - lastHeightLoc) > 2f) {
			player.setLocalLocation(new Vector3f(currLoc.x, currHeightLoc + player.getLocalScale().m00(), currLoc.z));
			lastHeightLoc = currHeightLoc;
			targetCamera.updateCameraLocation(frameTime);
		} else {
			player.setLocalLocation(new Vector3f(currLoc.x, lastHeightLoc + player.getLocalScale().m00(), currLoc.z));
		}
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

	private void buildPhysicsEngine() {
		String engine = "tage.physics.JBullet.JBulletPhysicsEngine";
		float[] gravity = { 0f, -5f, 0f };
		physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
		physicsEngine.initSystem();
		physicsEngine.setGravity(gravity);
	}

	private void buildTerrainMap() {
		terrain = new GameObject(GameObject.root(), terrS, terrTx);
		terrain.setLocalLocation(new Vector3f(0, 0, 0));
		terrain.setLocalScale((new Matrix4f().scaling(PLAY_AREA_SIZE)));
		terrain.setHeightMap(terrMap);
		terrain.getRenderStates().setTiling(1);
	}

	private void buildPlayer() {
		player = new Player(GameObject.root(), playerS, playerTx);
	}

	private void buildEnemy() {
		float mass = 0.1f;
		double[] tempTransform;
		float[] size = { 0.75f, 0.75f, 0.75f };
		Matrix4f translation;
		for (int i = 0; i < ENEMY_AMOUNT; i++) {
			enemy = new Enemy(GameObject.root(), enemyS, enemyTx);
			enemyList.add(enemy);
			translation = new Matrix4f(enemy.getLocalTranslation());
			tempTransform = toDoubleArray(translation.get(vals));
			enemyPhysicsList.add(physicsEngine.addBoxObject(physicsEngine.nextUID(), mass, tempTransform, size));
		}
	}

	private double[] toDoubleArray(float[] arr) {
		if (arr == null) {
			return null;
		}
		int n = arr.length;
		double[] ret = new double[n];
		for (int i = 0; i < n; i++) {
			ret[i] = (double) arr[i];
		}
		return ret;
	}

	private void buildEnemyQuadTree() {
		for (Enemy e : enemyList) {
			quadTree.insert(new QuadTreeNode(new QuadTreePoint(e.getLocalLocation().z, e.getLocalLocation().x), e));
		}
	}

	private void buildTargetCamera() {
		targetCamera = new TargetCamera(getPlayer());
		getPlayer().setCamera(targetCamera);
		targetCamera.setLocation(INITIAL_CAMERA_POS);
		engine.getRenderSystem().getViewport("MAIN").setCamera(targetCamera);
		targetCamera.setLookAtTarget(player.getLocalLocation());
		targetCamera.setLocation(targetCamera.getLocation().mul(new Vector3f(1, 1, -1)));
		targetCamera.updateCameraAngles(frameTime);
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

	public void executeScript(ScriptEngine engine, File file) {
		try {
			FileReader fileReader = new FileReader(file);
			engine.eval(fileReader);
			fileReader.close();
		} catch (FileNotFoundException e1) {
			System.out.println(file + " not found " + e1);
		} catch (IOException e2) {
			System.out.println("IO problem with " + file + e2);
		} catch (ScriptException e3) {
			System.out.println("ScriptException in " + file + e3);
		} catch (NullPointerException e4) {
			System.out.println("Null pointer exception in " + file + e4);
		}
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

	public void setIsConnected(boolean b) {
		isClientConnected = b;
	}

	public boolean isConnected() {
		return isClientConnected;
	}

	public GhostManager getGhostManager() {
		return ghostManager;
	}

	public ObjShape getGhostShape() {
		return ghostS;
	}

	public TextureImage getGhostTexture() {
		return ghostTx;
	}

	public ProtocolClient getProtocolClient() {
		return protocolClient;
	}

	public ScriptEngineManager getScriptEngineManager() {
		return factory;
	}

	public ScriptEngine getScriptEngine() {
		return jsEngine;
	}
}
