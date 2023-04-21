package a3;

import tage.*;
import tage.input.InputManager;
import tage.networking.IGameConnection.ProtocolType;
import tage.physics.PhysicsObject;
import tage.physics.JBullet.*;
import tage.shapes.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

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
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 500;
	private static final int ENEMY_AMOUNT = 4;
	private static final float PLAY_AREA_SIZE = 300f;
	private static final Vector3f INITIAL_CAMERA_POS = new Vector3f(0f, 0f, 5f);
	private static final String SKYBOX_NAME = "fluffyClouds";
	private static final String PLAYER_TEXTURE = "player-texture.png";
	private static final String GHOST_TEXTURE = "neptune.jpg";
	private static final String ENEMY_TEXTURE = "enemy-texture.png";
	private static final String TERRAIN_MAP = "terrain-map.png";
	private static final String TERRAIN_TEXTURE = "moon-craters.jpg";
	private static final String PLAYER_OBJ = "player.obj";
	private static final String ENEMY_OBJ = "enemy.obj";

	private static Engine engine;
	private static MyGame game;
	private static PlayerControlMap playerControlMaps; // do not delete!!!
	private static ScriptManager scriptManager;

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

	private GameObject terrain;

	private GhostManager ghostManager;
	private static PhysicsManager physicsManager;

	private Enemy enemy;
	private Player player;
	private ProtocolType serverProtocol;
	private ProtocolClient protocolClient;
	private boolean isClientConnected = false;

	private ObjShape playerS, enemyS, ghostS, terrS;
	private TextureImage playerTx, enemyTx, terrMap, ghostTx, terrTx;

	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	private ArrayList<PhysicsObject> enemyPhysicsList = new ArrayList<PhysicsObject>();
	private QuadTree quadTree = new QuadTree(
			new QuadTreePoint(-PLAY_AREA_SIZE, PLAY_AREA_SIZE),
			new QuadTreePoint(PLAY_AREA_SIZE, -PLAY_AREA_SIZE));
	private com.bulletphysics.dynamics.RigidBody object1, object2;

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
		scriptManager = new ScriptManager();
		scriptManager.loadScript("assets/scripts/LoadInitValues.js");
		physicsManager = new PhysicsManager();
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
		playerS = new ImportedModel(PLAYER_OBJ);
		ghostS = new Sphere();
		enemyS = new ImportedModel(ENEMY_OBJ);
		terrS = new TerrainPlane(100);
	}

	@Override
	public void loadTextures() {
		playerTx = new TextureImage(PLAYER_TEXTURE);
		ghostTx = new TextureImage(GHOST_TEXTURE);
		enemyTx = new TextureImage(ENEMY_TEXTURE);
		terrMap = new TextureImage(TERRAIN_MAP);
		terrTx = new TextureImage(TERRAIN_TEXTURE);
	}

	@Override
	public void loadSkyBoxes() {
		engine.getSceneGraph().setSkyBoxEnabled(true);
		engine.getSceneGraph().setActiveSkyBoxTexture(engine.getSceneGraph().loadCubeMap(SKYBOX_NAME));
	}

	@Override
	public void buildObjects() {
		buildTerrainMap();
		buildPlayer();
		buildEnemy();
		buildEnemyQuadTree();
	}

	@Override
	public void initializeLights() {
		(engine.getSceneGraph()).addLight((Light) scriptManager.getValue("light"));
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
		initializePhysicsObjects();
		state = new PlayerControls();
	}

	private void initializePhysicsObjects() {
		Iterator<PhysicsObject> it = enemyPhysicsList.iterator();

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
		Matrix4f mat = new Matrix4f();
		Matrix4f mat2 = new Matrix4f().identity();

		updateFrameTime();
		if (player.isLocked()) {
			targetCamera.targetTo();
		}
		checkForCollisions();
		for (GameObject go : getEngine().getSceneGraph().getGameObjects()) {
			if (go.getPhysicsObject() != null) {
				mat.set(toFloatArray(go.getPhysicsObject().getTransform()));
				mat2.set(3, 0, mat.m30());
				mat2.set(3, 1, mat.m31());
				mat2.set(3, 2, mat.m32());
				go.setLocalTranslation(mat2);
				if (go instanceof Player) {
					getTargetCamera().lookAt((Player) go);
					getTargetCamera().updateCameraLocation(frameTime);
					getTargetCamera().updateCameraAngles(frameTime);
				}
			}
		}
		targetCamera.setLookAtTarget(player.getLocalLocation());
		updatePlayerTerrainLocation();
		inputManager.update((float) elapsTime);
		physicsManager.update((float) elapsTime);
		processNetworking((float) elapsTime);
	}

	private void updatePlayerTerrainLocation() {
		Vector3f currLoc = player.getLocalLocation();
		currHeightLoc = terrain.getHeight(currLoc.x, currLoc.z);
		if (Math.abs(currHeightLoc - lastHeightLoc) > 5f) {
			player.setLocalLocation(new Vector3f(currLoc.x,
					currHeightLoc + player.getLocalScale().m00(), currLoc.z()));
			lastHeightLoc = currHeightLoc;
			targetCamera.updateCameraLocation(frameTime);
		} else {
			player.setLocalLocation(
					new Vector3f(currLoc.x(), lastHeightLoc + player.getLocalScale().m00(), currLoc.z()));
		}
	}

	private void updateFrameTime() {
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		frameTime = (float) (currFrameTime - lastFrameTime) / 1000.0f;
		elapsTime += frameTime;
	}

	private void buildTerrainMap() {
		float up[] = { 0, 1, 0 };
		double[] tempTransform;
		PhysicsObject planeP;
		Matrix4f translation;

		terrain = new GameObject(GameObject.root(), terrS, terrTx);
		terrain.setLocalLocation(new Vector3f(0, 0, 0));
		terrain.setLocalScale((new Matrix4f().scaling(PLAY_AREA_SIZE)));
		terrain.setHeightMap(terrMap);
		terrain.getRenderStates().setTiling(1);

		translation = new Matrix4f(terrain.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		planeP = physicsManager.addStaticPlaneObject(
				physicsManager.nextUID(), tempTransform, up, 0.0f);
		terrain.setPhysicsObject(planeP);
	}

	private void buildPlayer() {
		float mass = 0.1f;
		double[] tempTransform;
		float[] size = { 0.75f, 0.75f, 0.75f };
		Matrix4f translation;
		PhysicsObject playerBody;

		player = new Player(GameObject.root(), playerS, playerTx);
		translation = new Matrix4f(player.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		playerBody = physicsManager.addBoxObject(physicsManager.nextUID(), mass, tempTransform, size);
		player.setPhysicsObject(playerBody);
		JBulletPhysicsObject.getJBulletPhysicsObject(playerBody);
	}

	private void buildEnemy() {
		float mass = 0.1f;
		double[] tempTransform;
		float[] size;
		Matrix4f translation;

		for (int i = 0; i < ENEMY_AMOUNT; i++) {
			enemy = new Enemy(GameObject.root(), enemyS, enemyTx, i);
			enemyList.add(enemy);
			size = new float[] { enemy.getLocalScale().m00(),
					enemy.getLocalScale().m00(),
					enemy.getLocalScale().m00() };
			translation = new Matrix4f(enemy.getLocalTranslation());
			tempTransform = toDoubleArray(translation.get(vals));
			enemyPhysicsList.add(physicsManager.addBoxObject(physicsManager.nextUID(),
					mass, tempTransform, size));
			enemy.setPhysicsObject(enemyPhysicsList.get(i));
		}
	}

	private void checkForCollisions() {
		com.bulletphysics.dynamics.DynamicsWorld dynamicsWorld;
		com.bulletphysics.collision.broadphase.Dispatcher dispatcher;
		com.bulletphysics.collision.narrowphase.PersistentManifold manifold;
		com.bulletphysics.collision.narrowphase.ManifoldPoint contactPoint;

		dynamicsWorld = ((JBulletPhysicsEngine) physicsManager.getPhysicsEngine()).getDynamicsWorld();

		dispatcher = dynamicsWorld.getDispatcher();
		int manifoldCount = dispatcher.getNumManifolds();
		for (int i = 0; i < manifoldCount; i++) {
			manifold = dispatcher.getManifoldByIndexInternal(i);
			object1 = (com.bulletphysics.dynamics.RigidBody) manifold.getBody0();
			object2 = (com.bulletphysics.dynamics.RigidBody) manifold.getBody1();

			JBulletPhysicsObject obj1 = JBulletPhysicsObject.getJBulletPhysicsObject(object1);
			JBulletPhysicsObject obj2 = JBulletPhysicsObject.getJBulletPhysicsObject(object2);

			// for (int j = 0; j < manifold.getNumContacts(); j++) {
			// contactPoint = manifold.getContactPoint(j);
			// if (contactPoint.getDistance() < 0.001f) {

			// System.out.println("---- hit between " + obj1 + " and " + obj2);
			// System.out.printf("obj1 bounciness %.2f obj2 bounciness %.2f\n",
			// obj1.getBounciness(),
			// obj2.getBounciness());
			// break;
			// }
			// }
		}
	}

	private float[] toFloatArray(double[] arr) {
		if (arr == null)
			return null;
		int n = arr.length;
		float[] ret = new float[n];
		for (int i = 0; i < n; i++) {
			ret[i] = (float) arr[i];
		}
		return ret;
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
		targetCamera.setLocation(INITIAL_CAMERA_POS);
		engine.getRenderSystem().getViewport("MAIN").setCamera(targetCamera);
		targetCamera.setLookAtTarget(player.getLocalLocation());
		targetCamera.setLocation(targetCamera.getLocation().mul(new Vector3f(1, 1, -1)));
		targetCamera.updateCameraAngles(frameTime);
	}

	public static MyGame getGameInstance() {
		return game;
	}

	public static Engine getEngineInstance() {
		if (engine == null)
			engine = new Engine(getGameInstance());
		return engine;
	}

	public GameObject findTarget() {
		QuadTreePoint playerPos = new QuadTreePoint(player.getLocalLocation().z,
				player.getLocalLocation().x());
		QuadTreeNode target;
		target = quadTree.findNearby(playerPos, -1, null);

		if (target != null) {
			return (GameObject) target.getData();
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

	public ScriptManager getScriptManager() {
		return scriptManager;
	}
}
