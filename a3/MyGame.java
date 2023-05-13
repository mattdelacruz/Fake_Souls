package a3;

import tage.*;
import tage.audio.AudioResourceType;
import tage.audio.SoundType;
import tage.input.InputManager;
import tage.networking.IGameConnection.ProtocolType;
import tage.nodeControllers.AnimationController;
import tage.nodeControllers.EnemyController;
import tage.physics.PhysicsObject;
import tage.physics.JBullet.*;
import tage.shapes.*;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import a3.controls.PlayerControlMap;
import a3.controls.PlayerControls;
import a3.managers.PhysicsManager;
import a3.managers.ScriptManager;
import a3.managers.SoundManager;
import a3.network.GhostAvatar;
import a3.network.GhostManager;
import a3.network.GhostWeapon;
import a3.network.ProtocolClient;
import a3.npcs.Enemy;
import a3.npcs.EnemyWeapon;
import a3.player.Player;
import a3.player.PlayerWeapon;
import a3.quadtree.*;

public class MyGame extends VariableFrameRateGame {
	private static final float HUD_VIEWPORT_BOTTOM = 0f;
	private static final float HUD_VIEWPORT_LEFT = 0.95f;
	private static final float HUD_VIEWPORT_HEIGHT = .05f;
	private static final String HUD_VIEWPORT_NAME = "HUD";
	private static final String SCRIPT_INIT_PATH = "assets/scripts/LoadInitValues.js";
	private static Engine engine;
	private static MyGame game;
	private static PlayerControlMap playerControlMaps; // do not delete!!!
	private static ScriptManager scriptManager;
	private static PhysicsManager physicsManager;
	private static GhostManager ghostManager;
	private static SoundManager soundManager;

	private InputManager inputManager;
	private TargetCamera targetCamera;
	private HUDCamera HUDCamera;
	private PlayerControls controls;
	private QuadTree enemyQuadTree, playerQuadTree;

	private int serverPort;
	private int HUDViewportX, HUDViewportY;
	private double lastFrameTime, currFrameTime, elapsTime;
	private float lastHeightLoc, currHeightLoc;

	private String serverAddress;

	private float frameTime = 0;
	private float currentRotation = 0;
	private float[] vals = new float[16];
	int count = 0;
	int hudTimer = 0;

	private GameObject terrain;
	private Player player;
	private GhostAvatar ghost;
	private PlayerWeapon katana;
	private ProtocolType serverProtocol;
	private ProtocolClient protocolClient;
	private boolean isClientConnected = false;
	private boolean hasRotated = false;
	private boolean hasMovedNorthWest = false;
	private boolean hasMovedNorthEast = false;
	private boolean hasMovedSouthWest = false;
	private boolean hasMovedSouthEast = false;
	private boolean hasMovedWest = false;
	private boolean hasMovedEast = false;
	private boolean hasMovedSouth = false;
	private boolean isEnemyHit = false;

	private ObjShape terrS;
	private TextureImage playerTx, enemyTx, terrMap, ghostTx, terrTx, katanaTx, spearTx;
	private AnimatedShape playerShape, ghostShape, katanaShape, ghostKatanaShape;
	private EnemyController enemyController;
	private AnimationController animationController;
	private Viewport HUDViewport;
	private ActiveEntityObject damagedEnemy;

	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private Map<Integer, Enemy> enemyMap = new HashMap<Integer, Enemy>();
	private Map<Integer, EnemyWeapon> enemyWeaponMap = new HashMap<Integer, EnemyWeapon>();
	private Set<String> activeKeys = new HashSet<>();
	private com.bulletphysics.dynamics.RigidBody object1, object2;
	com.bulletphysics.dynamics.DynamicsWorld dynamicsWorld;
	Iterator<Enemy> enemyIterator;

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
		}
		game = new MyGame(args[0], Integer.parseInt(args[1]), args[2]);

		engine = new Engine(getGameInstance());
		scriptManager = new ScriptManager();
		scriptManager.loadScript(SCRIPT_INIT_PATH);
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
		terrS = new TerrainPlane(100);
		initializePlayerAnimations();
		initializeGhostAnimations();
		// initializeEnemyAnimations();
	}

	@Override
	public void loadTextures() {
		playerTx = new TextureImage((String) scriptManager.getValue("PLAYER_TEXTURE"));
		ghostTx = new TextureImage((String) scriptManager.getValue("GHOST_TEXTURE"));
		enemyTx = new TextureImage((String) scriptManager.getValue("ENEMY_TEXTURE"));
		terrMap = new TextureImage((String) scriptManager.getValue("TERRAIN_MAP"));
		terrTx = new TextureImage((String) scriptManager.getValue("TERRAIN_TEXTURE"));
		katanaTx = new TextureImage((String) scriptManager.getValue("KATANA_TEXTURE"));
		spearTx = new TextureImage((String) scriptManager.getValue("SPEAR_TEXTURE"));
	}

	@Override
	public void loadSkyBoxes() {
		getEngineInstance().getSceneGraph().setSkyBoxEnabled(true);
		getEngineInstance().getSceneGraph().setActiveSkyBoxTexture(
				getEngineInstance().getSceneGraph().loadCubeMap((String) scriptManager.getValue("SKYBOX_NAME")));
	}

	@Override
	public void buildObjects() {
		int playAreaSize = (int) scriptManager.getValue("PLAY_AREA_SIZE");
		enemyQuadTree = new QuadTree(
				new QuadTreePoint((float) -playAreaSize, (float) playAreaSize),
				new QuadTreePoint((float) playAreaSize, (float) -playAreaSize));
		playerQuadTree = new QuadTree(
				new QuadTreePoint((float) -playAreaSize, (float) playAreaSize),
				new QuadTreePoint((float) playAreaSize, (float) -playAreaSize));
		initializeAudio();
		buildTerrainMap();
		buildControllers();
		buildPlayer();
		buildPlayerQuadTree();
		buildEnemy();
		buildEnemyQuadTree();
	}

	@Override
	public void createViewports() {
		super.createViewports();
		getEngine().getRenderSystem().addViewport(HUD_VIEWPORT_NAME, HUD_VIEWPORT_BOTTOM, HUD_VIEWPORT_LEFT,
				getEngine().getRenderSystem().getWidth(), HUD_VIEWPORT_HEIGHT);
	}

	@Override
	public void initializeLights() {
		Light light = new Light();
		Light.setGlobalAmbient(.25f, .25f, .25f);
		light.setLocation(new Vector3f(5.0f, 0.0f, 2.0f));
		(getEngineInstance().getSceneGraph()).addLight(light);
	}

	@Override
	public void initializeGame() {
		lastFrameTime = System.currentTimeMillis();
		currFrameTime = System.currentTimeMillis();
		lastHeightLoc = 0;
		elapsTime = 0.0;

		(getEngineInstance().getRenderSystem()).setWindowDimensions(
				(int) scriptManager.getValue("WINDOW_WIDTH"),
				(int) scriptManager.getValue("WINDOW_HEIGHT"));

		initializeControls();
		updateFrameTime();
		initializeCameras();
		setupNetworking();
		controls = new PlayerControls();
	}

	public void initializeAudio() {
		soundManager = new SoundManager();
		/*
		 * String soundName, String soundPath, int volume, boolean toLoop, float
		 * maxDistance, float minDistance, float rollOff, Vector3f soundLocation
		 */
		int backgroundMusicRange = (int) scriptManager.getValue("PLAY_AREA_SIZE");
		soundManager.addSound(
				"BACKGROUND_MUSIC", (String) scriptManager.getValue("BACKGROUND_MUSIC"), 50, true,
				(float) backgroundMusicRange, 0, 0, new Vector3f(0, 0, 0), SoundType.SOUND_MUSIC,
				AudioResourceType.AUDIO_STREAM);

		soundManager.playSound("BACKGROUND_MUSIC");
	}

	private void initializePlayerAnimations() {
		playerShape = new AnimatedShape(
				(String) scriptManager.getValue("PLAYER_RKM"),
				(String) scriptManager.getValue("PLAYER_RKS"));
		playerShape.loadAnimation(
				"RUN", (String) scriptManager.getValue("PLAYER_RUN_RKA"));
		playerShape.loadAnimation(
				"BACKWARDS_RUN", (String) scriptManager.getValue("PLAYER_BACKWARDS_RUN_RKA"));
		playerShape.loadAnimation(
				"STRAFE", (String) scriptManager.getValue("PLAYER_STRAFE_RKA"));
		playerShape.loadAnimation(
				"IDLE", (String) scriptManager.getValue("PLAYER_IDLE_RKA"));
		playerShape.loadAnimation(
				"ATTACK1", (String) scriptManager.getValue("PLAYER_ATTACK_1_RKA"));
		playerShape.loadAnimation(
				"GUARD", (String) scriptManager.getValue("PLAYER_GUARD_RKA"));
		playerShape.loadAnimation(
				"GUARD_WALK", (String) scriptManager.getValue("PLAYER_GUARD_WALK_RKA"));
		playerShape.loadAnimation(
				"GUARD_STRAFE", (String) scriptManager.getValue("PLAYER_GUARD_STRAFE_RKA"));

		katanaShape = new AnimatedShape(
				(String) scriptManager.getValue("KATANA_RKM"),
				(String) scriptManager.getValue("KATANA_RKS"));
		katanaShape.loadAnimation(
				"RUN", (String) scriptManager.getValue("KATANA_RUN_RKA"));
		katanaShape.loadAnimation(
				"BACKWARDS_RUN", (String) scriptManager.getValue("KATANA_BACKWARDS_RUN_RKA"));
		katanaShape.loadAnimation(
				"STRAFE", (String) scriptManager.getValue("KATANA_STRAFE_RKA"));
		katanaShape.loadAnimation(
				"IDLE",
				(String) scriptManager.getValue("KATANA_IDLE_RKA"));
		katanaShape.loadAnimation(
				"ATTACK1",
				(String) scriptManager.getValue("KATANA_ATTACK_1_RKA"));
		katanaShape.loadAnimation(
				"GUARD",
				(String) scriptManager.getValue("KATANA_GUARD_RKA"));
		katanaShape.loadAnimation(
				"GUARD_WALK", (String) scriptManager.getValue("KATANA_GUARD_WALK_RKA"));
		katanaShape.loadAnimation(
				"GUARD_STRAFE", (String) scriptManager.getValue("KATANA_GUARD_WALK_RKA"));

	}

	private void initializeGhostAnimations() {
		ghostShape = new AnimatedShape(
				(String) scriptManager.getValue("PLAYER_RKM"),
				(String) scriptManager.getValue("PLAYER_RKS"));
		ghostShape.loadAnimation(
				"RUN", (String) scriptManager.getValue("PLAYER_RUN_RKA"));
		ghostShape.loadAnimation(
				"BACKWARDS_RUN", (String) scriptManager.getValue("PLAYER_BACKWARDS_RUN_RKA"));
		ghostShape.loadAnimation(
				"STRAFE", (String) scriptManager.getValue("PLAYER_STRAFE_RKA"));
		ghostShape.loadAnimation(
				"IDLE", (String) scriptManager.getValue("PLAYER_IDLE_RKA"));
		ghostShape.loadAnimation(
				"ATTACK1", (String) scriptManager.getValue("PLAYER_ATTACK_1_RKA"));
		ghostShape.loadAnimation(
				"GUARD", (String) scriptManager.getValue("PLAYER_GUARD_RKA"));
		ghostShape.loadAnimation(
				"GUARD_WALK", (String) scriptManager.getValue("PLAYER_GUARD_WALK_RKA"));
		ghostShape.loadAnimation(
				"GUARD_STRAFE", (String) scriptManager.getValue("PLAYER_GUARD_STRAFE_RKA"));

		ghostKatanaShape = new AnimatedShape(
				(String) scriptManager.getValue("KATANA_RKM"),
				(String) scriptManager.getValue("KATANA_RKS"));
		ghostKatanaShape.loadAnimation(
				"RUN", (String) scriptManager.getValue("KATANA_RUN_RKA"));
		ghostKatanaShape.loadAnimation(
				"BACKWARDS_RUN", (String) scriptManager.getValue("KATANA_BACKWARDS_RUN_RKA"));
		ghostKatanaShape.loadAnimation(
				"STRAFE", (String) scriptManager.getValue("KATANA_STRAFE_RKA"));
		ghostKatanaShape.loadAnimation(
				"IDLE",
				(String) scriptManager.getValue("KATANA_IDLE_RKA"));
		ghostKatanaShape.loadAnimation(
				"ATTACK1",
				(String) scriptManager.getValue("KATANA_ATTACK_1_RKA"));
		ghostKatanaShape.loadAnimation(
				"GUARD",
				(String) scriptManager.getValue("KATANA_GUARD_RKA"));
		ghostKatanaShape.loadAnimation(
				"GUARD_WALK", (String) scriptManager.getValue("KATANA_GUARD_WALK_RKA"));
		ghostKatanaShape.loadAnimation(
				"GUARD_STRAFE", (String) scriptManager.getValue("KATANA_GUARD_WALK_RKA"));

	}

	private AnimatedShape initializeEnemyAnimations() {
		AnimatedShape enemyShape = new AnimatedShape(
				(String) scriptManager.getValue("ENEMY_RKM"),
				(String) scriptManager.getValue("ENEMY_RKS"));
		enemyShape.loadAnimation(
				"RUN",
				(String) scriptManager.getValue("ENEMY_RUN_RKA"));
		enemyShape.loadAnimation(
				"IDLE",
				(String) scriptManager.getValue("ENEMY_IDLE_RKA"));
		enemyShape.loadAnimation("ATTACK",
				(String) scriptManager.getValue("ENEMY_ATTACK_RKA"));
		enemyShape.loadAnimation("FLINCH",
				(String) scriptManager.getValue("ENEMY_FLINCH_RKA"));
		enemyShape.loadAnimation("DEATH",
				(String) scriptManager.getValue("ENEMY_DEATH_RKA"));
		return enemyShape;
	}

	private AnimatedShape initializeEnemyWeaponAnimations() {
		AnimatedShape spearShape = new AnimatedShape(
				(String) scriptManager.getValue("SPEAR_RKM"),
				(String) scriptManager.getValue("SPEAR_RKS"));
		spearShape.loadAnimation("IDLE",
				(String) scriptManager.getValue("SPEAR_IDLE_RKA"));
		spearShape.loadAnimation("RUN",
				(String) scriptManager.getValue("SPEAR_RUN_RKA"));
		spearShape.loadAnimation("ATTACK",
				(String) scriptManager.getValue("SPEAR_ATTACK_RKA"));
		spearShape.loadAnimation("FLINCH",
				(String) scriptManager.getValue("SPEAR_FLINCH_RKA"));
		spearShape.loadAnimation("DEATH",
				(String) scriptManager.getValue("SPEAR_DEATH_RKA"));
		return spearShape;
	}

	private void initializeCameras() {
		buildTargetCamera();
		buildHUDCamera();
	}

	private void initializeControls() {
		inputManager = getEngineInstance().getInputManager();
		playerControlMaps = new PlayerControlMap(inputManager);
	}

	@Override
	public void update() {
		if (isClientConnected()) {
			protocolClient.sendHealthMessage(player.getHealth());
			switchToInvasionArena();
		}
		// System.out.printf("x: %.2f, y: %.2f, z: %.2f\n",
		// player.getLocalLocation().x(), player.getLocalLocation().y(),
		// player.getLocalLocation().z());
		updateHUD();
		updateSoundManager();
		updateFrameTime();
		updateMovementControls();
		updatePlayer();
		updateKatana();
		updateEnemies();
		checkForCollisions();
		updateTargeting();

		inputManager.update((float) elapsTime);
		physicsManager.update((float) elapsTime);
		processNetworking((float) elapsTime);
	}

	private void switchToInvasionArena() {
		// if host, pos 1
		// if invader, pos 2

		// player.setLocalLocation(null);

	}

	private void updateSoundManager() {
		soundManager.setEarParameters(getTargetCamera(), player.getWorldLocation(), getTargetCamera().getN(),
				new Vector3f(0, 1f, 0));
	}

	private void updatePlayer() {
		checkObjectMovement(player);
		if (player.isMoving()) {
			// updatePlayerTerrainLocation();
			// targetCamera.updateCameraLocation(frameTime);
		}
		updatePhysicsObjectLocation(
				player.getPhysicsObject(), player.getWorldTranslation());
	}

	private void updateKatana() {
		updatePhysicsObjectLocation(
				katana.getPhysicsObject(), katana.getWorldTranslation());
	}

	private void updateEnemies() {
		enemyIterator = enemyList.iterator();
		while (enemyIterator.hasNext()) {
			Enemy enemy = enemyIterator.next();
			enemy.getRenderStates().enableRendering();
			enemy.getWeapon().getRenderStates().enableRendering();
			// System.out.printf(
			// "enemy " + enemy.getID() + "x: %.2f y: %.2f z: %.2f\n weapon x: %.2f, y:
			// %.2f, z: %.2f\n", enemy.getLocalLocation().x(), enemy.getLocalLocation().y(),
			// enemy.getLocalLocation().z(), enemy.getWeapon().getWorldLocation().x(),
			// enemy.getWeapon().getWorldLocation().y(),
			// enemy.getWeapon().getWorldLocation().z()
			// );

			checkObjectMovement(enemy);
			enemy.setPhysicsObject(updatePhysicsObjectLocation(enemy.getPhysicsObject(),
					enemy.getLocalTranslation()));
			enemy.getWeapon().setPhysicsObject(updatePhysicsObjectLocation(enemy.getWeapon().getPhysicsObject(),
					enemy.getWeapon().getWorldTranslation()));
			if (enemy.checkIfDead()) {
				enemyQuadTree.remove(new QuadTreePoint(enemy.getLocalLocation().z(), enemy.getLocalLocation().x()));
				enemyIterator.remove();
			}
		}
	}

	private void updateTargeting() {
		if (player.isLocked()) {
			targetCamera.targetTo();
		}
		targetCamera.setLookAtTarget(player.getLocalLocation());
	}

	private PhysicsObject updatePhysicsObjectLocation(PhysicsObject po, Matrix4f localTranslation) {
		Matrix4f translation = new Matrix4f();
		double[] tempTransform;
		translation = new Matrix4f(localTranslation);
		tempTransform = toDoubleArray(translation.get(vals));
		po.setTransform(tempTransform);
		return po;
	}

	private void updateGameObjectwithPhysicsObject(GameObject go, PhysicsObject po) {
		Matrix4f translation = new Matrix4f().set(toFloatArray(po.getTransform()));
		go.setLocalTranslation(translation);
	}

	private void checkObjectMovement(ActiveEntityObject obj) {
		updateIsMoving(obj);
		if (!obj.isMoving()) {
			handleObjectNotMoving(obj);
		} else {
			handleMoving(obj);
		}
		updateLastLocation(obj);
	}

	private void updateIsMoving(ActiveEntityObject obj) {
		obj.setIsMoving(obj.getLocalLocation().x() != obj.getLastLocation().x()
				|| obj.getLocalLocation().z() != obj.getLastLocation().z());
	}

	private void handleObjectNotMoving(ActiveEntityObject obj) {
		if (obj.getStanceState().isNormal()) {
			obj.idle();
		} else if (obj.getStanceState().isAttacking() && !obj.getAnimationShape().isAnimPlaying()) {
			obj.idle();
		}
	}

	private void handleMoving(ActiveEntityObject obj) {
		if (obj instanceof Player) {
			updatePlayerQuadTree((Player) obj);
		} else if (obj instanceof Enemy) {
			updateEnemyQuadTree((Enemy) obj);
		}
	}

	private void updatePlayerQuadTree(Player player) {
		playerQuadTree.update(
				new QuadTreePoint(player.getLastLocation().z(), player.getLastLocation().x()),
				new QuadTreePoint(player.getLocalLocation().z(), player.getLocalLocation().x()),
				player);
	}

	private void updateEnemyQuadTree(Enemy enemy) {
		enemyQuadTree.update(
				new QuadTreePoint(enemy.getLastLocation().z(), enemy.getLastLocation().x()),
				new QuadTreePoint(enemy.getLocalLocation().z(), enemy.getLocalLocation().x()),
				enemy);
	}

	private void updateLastLocation(ActiveEntityObject obj) {
		obj.setLastLocation(
				new Vector3f(obj.getLocalLocation().x(), obj.getLocalLocation().y(), obj.getLocalLocation().z()));
	}

	private void updatePlayerTerrainLocation() {
		Vector3f currLoc = player.getLocalLocation();
		currHeightLoc = terrain.getHeight(currLoc.x, currLoc.z);
		if (currHeightLoc == 300) {
			// on the arena platform
			return;
		}

		if (currHeightLoc > 1.2f) {
			player.setLocalLocation(player.getLastValidLocation());
			return;
		}

		double playerHeightSpeed = (double) scriptManager.getValue("PLAYER_HEIGHT_SPEED");
		float alpha = frameTime * (float) playerHeightSpeed;
		float newHeight = lerp(lastHeightLoc, currHeightLoc, alpha);

		Vector3f newLocation = new Vector3f(currLoc.x(), newHeight, currLoc.z());
		player.setLocalLocation(newLocation);
		player.setLastValidLocation(newLocation);
		lastHeightLoc = newHeight;
	}

	private void updateFrameTime() {
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		frameTime = (float) (currFrameTime - lastFrameTime) / 1000.0f;
		elapsTime += frameTime;
	}

	private void buildControllers() {
		buildEnemyController();
		buildAnimationController();
	}

	private void buildEnemyController() {
		enemyController = new EnemyController();
		getEngineInstance().getSceneGraph().addNodeController(enemyController);
		enemyController.enable();
	}

	private void buildAnimationController() {
		animationController = new AnimationController();
		getEngineInstance().getSceneGraph().addNodeController(animationController);
		animationController.enable();
	}

	private void buildTerrainMap() {
		float up[] = { 0, 1, 0 };
		double[] tempTransform;
		PhysicsObject planeP;
		Matrix4f translation;
		int playAreaSize = (int) scriptManager.getValue("PLAY_AREA_SIZE");

		terrain = new GameObject(GameObject.root(), terrS, terrTx);
		terrain.setLocalLocation(new Vector3f(0, 0, 0));
		terrain.setLocalScale((new Matrix4f().scaling((float) playAreaSize)));
		terrain.setHeightMap(terrMap);
		terrain.getRenderStates().setTiling(1);

		translation = new Matrix4f(terrain.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		planeP = physicsManager.addStaticPlaneObject(tempTransform, up, 0.0f);
		terrain.setPhysicsObject(planeP);
	}

	private void buildPlayer() {
		float mass = 1f;
		double[] tempTransform;
		float[] size;
		Matrix4f translation;
		PhysicsObject playerBody, katanaBody;

		player = new Player(GameObject.root(), playerShape, playerTx);
		katana = new PlayerWeapon(player, katanaShape, katanaTx);
		player.addWeapon(katana);
		katana.propagateTranslation(true);

		translation = player.getLocalTranslation();
		tempTransform = toDoubleArray(translation.get(vals));

		playerBody = physicsManager.addCapsuleObject(mass, tempTransform, 1f, 5f);
		player.setPhysicsObject(playerBody);

		size = new float[] { 3f, 3f, 3f };

		katanaBody = physicsManager.addBoxObject(mass, tempTransform, size);
		katana.setPhysicsObject(katanaBody);

		playerList.add(player);
		animationController.addTarget(player);
	}

	private void updateHUD() {
		hudTimer++;
		handlePlayerHUD();
		handleEnemyHUD();
	}

	private void handlePlayerHUD() {
		updatePlayerHealthHUD();
	}

	private void updatePlayerHealthHUD() {
		String dispStr1 = "Health: " + player.getHealth();

		HUDViewportX = (int) ((HUDViewport.getRelativeLeft() * getEngineInstance().getRenderSystem().getWidth()));
		HUDViewportY = (int) (((HUDViewport.getRelativeBottom()
				* getEngineInstance().getRenderSystem().getHeight()))
				- (HUDViewport.getActualHeight()) / 2);
		Vector3f hpColor = new Vector3f(1, 0, 0);

		getEngineInstance().getHUDmanager().setHUD1(dispStr1, hpColor, HUDViewportX, HUDViewportY);
	}

	private void handleEnemyHUD() {
		if (isEnemyHit && damagedEnemy != null) {
			updateHealthHUD((String) scriptManager.getValue("ENEMY_HEALTH_TEXT"), damagedEnemy.getHealth());
		}

		if (hudTimer >= 2500) {
			removeEnemyHealthHUD();
			damagedEnemy = null;
			isEnemyHit = false;
		}
	}

	private void updateHealthHUD(String text, int health) {
		String dispStr2 = new String(text + health);
		System.out.println("health: " + health);
		HUDViewportX = (int) ((HUDViewport.getRelativeLeft() * getEngineInstance().getRenderSystem().getWidth())) + 150;
		HUDViewportY = (int) ((HUDViewport.getRelativeBottom()
				* getEngineInstance().getRenderSystem().getHeight()) - (HUDViewport.getActualHeight()) / 2);
		Vector3f hpColor = new Vector3f(1, 0, 0);
		getEngineInstance().getHUDmanager().setHUD2(dispStr2, hpColor, HUDViewportX, HUDViewportY);
	}

	private void removeEnemyHealthHUD() {
		String dispStr2 = "";

		Vector3f hpColor = new Vector3f(1, 0, 0);
		getEngineInstance().getHUDmanager().setHUD2(dispStr2, hpColor, HUDViewportX, HUDViewportY);
	}

	private void buildEnemy() {
		int enemyCount = (int) scriptManager.getValue("ENEMY_AMOUNT");
		for (int i = 1; i <= enemyCount; i++) {
			float mass = 20f;
			float[] size;
			double[] tempTransform;
			Matrix4f translation;
			PhysicsObject enemyBody, spearBody;
			String enemyPosName = new String("ENEMY_POS_" + i);
			Vector3f enemyPos = (Vector3f) scriptManager.getValue(enemyPosName);
			AnimatedShape enemyShape = initializeEnemyAnimations();
			AnimatedShape spearShape = initializeEnemyWeaponAnimations();

			Enemy enemy = new Enemy(GameObject.root(), enemyShape, enemyTx, playerQuadTree, i);
			enemy.setLocalTranslation(new Matrix4f().setTranslation(enemyPos));
			EnemyWeapon longinus = new EnemyWeapon(enemy, spearShape, spearTx);
			enemy.addWeapon(longinus);
			longinus.propagateTranslation(true);

			translation = enemy.getLocalTranslation();
			tempTransform = toDoubleArray(translation.get(vals));

			enemyBody = physicsManager.addCapsuleObject(mass, tempTransform, 1f, 5);
			enemy.setPhysicsObject(enemyBody);

			size = new float[] { 5f, 1f, 5f };
			translation = longinus.getWorldTranslation();
			tempTransform = toDoubleArray(translation.get(vals));
			spearBody = physicsManager.addBoxObject(mass, tempTransform, size);
			longinus.setPhysicsObject(spearBody);
			longinus.setOwner(enemy);

			enemyWeaponMap.put(spearBody.getUID(), longinus);
			enemyMap.put(enemyBody.getUID(), enemy);
			enemyList.add(enemy);
			enemyController.addTarget(enemy);
			enemy.getRenderStates().enableRendering();
			longinus.getRenderStates().enableRendering();
		}
	}

	private void checkForCollisions() {
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

			for (int j = 0; j < manifold.getNumContacts(); j++) {
				contactPoint = manifold.getContactPoint(j);
				if (contactPoint.getDistance() < 0f) {
					if (enemyMap.get(obj2.getUID()) != null || ghostManager.getGhostFromMap(obj2.getUID()) != null) {
						handlePlayerHit(obj1, obj2);
						handlePlayerCollision(obj1, obj2);
					}

					if (enemyWeaponMap.get(obj2.getUID()) != null ||
							ghostManager.getGhostWeaponFromMap(obj2.getUID()) != null) {
						handleEnemyHit(obj1, obj2);
					}
				}
			}
		}
	}

	private void handlePlayerHit(JBulletPhysicsObject obj1, JBulletPhysicsObject obj2) {
		// obj1 is player weapon, obj2 is enemy
		if (isEnemyHitByPlayerWeapon(obj1, obj2) &&
				isAttackAnimationMidpoint(player.getWeapon()) &&
				enemyMap.get(obj2.getUID()) != null) {
			updateEnemyDamage(enemyMap.get(obj2.getUID()), enemyMap.get(obj2.getUID()).getHealth());
		} else if (isGhostHitByPlayerWeapon(obj1, obj2) &&
				isAttackAnimationMidpoint(player.getWeapon()) &&
				ghostManager.getGhostFromMap(obj2.getUID()) != null) {
			System.out.println("ghost is hit!");
			updateEnemyDamage(ghostManager.getGhostFromMap(obj2.getUID()),
					ghostManager.getGhostFromMap(obj2.getUID()).getOwner().getHealth());
			if (protocolClient != null) {
				protocolClient.sendDamageMessage(player.getWeapon().getDamage());

			}
		}
		player.getWeapon().addFrameCount();
	}

	private void updateEnemyDamage(ActiveEntityObject obj, int health) {
		updateHealthHUD((String) getScriptManager().getValue("ENEMY_HEALTH_TEXT"), health);
		System.out.println("health: " + health);
		isEnemyHit = true;
		soundManager.stopSound("KATANA_WHIFF");
		soundManager.playSound("KATANA_HIT");
		damagedEnemy = obj;
		hudTimer = 0;
		player.getWeapon().resetFrameCount();
	}

	private boolean isGhostHitByPlayerWeapon(JBulletPhysicsObject obj1, JBulletPhysicsObject obj2) {
		if (ghostManager.getGhostFromMap(obj2.getUID()) == null) {
			return false;
		}
		return obj2 == ghostManager.getGhostFromMap(obj2.getUID()).getPhysicsObject() &&
				obj1.getUID() == player.getWeapon().getPhysicsObject().getUID()
				&& player.getStanceState().isAttacking();
	}

	private boolean isEnemyHitByPlayerWeapon(JBulletPhysicsObject obj1, JBulletPhysicsObject obj2) {
		if (enemyMap.get(obj2.getUID()) == null) {
			return false;
		}
		return obj2 == enemyMap.get(obj2.getUID()).getPhysicsObject() &&
				obj1.getUID() == player.getWeapon().getPhysicsObject().getUID()
				&& player.getStanceState().isAttacking();
	}

	private void handlePlayerCollision(JBulletPhysicsObject obj1, JBulletPhysicsObject obj2) {
		float pushBackDistance = 0.1f;
		if (isPlayerCollidedWithEnemy(obj1, obj2) &&
				enemyMap.get(obj2.getUID()) != null) {
			Vector3f enemyToPlayer = player.getLocalLocation().sub(enemyMap.get(obj2.getUID()).getLocalLocation());
			pushBackPlayer(enemyToPlayer, pushBackDistance);

			if (getProtocolClient() != null) {
				getProtocolClient().sendMoveMessage(player.getWorldLocation());
			}
		} else if (isPlayerCollidedWithGhost(obj1, obj2) && ghostManager.getGhostFromMap(obj2.getUID()) != null) {
			Vector3f ghostToPlayer = player.getLocalLocation()
					.sub(ghostManager.getGhostFromMap(obj2.getUID()).getLocalLocation());
			// pushBackPlayer(ghostToPlayer, pushBackDistance);
			if (getProtocolClient() != null) {
				// System.out.println(
				// "collided with ghost UID: " +
				// ghostManager.getGhostFromMap(obj2.getUID()).getID() + " my pos: "
				// + player.getLocalLocation().x() + " "
				// + player.getLocalLocation().y() + " " + player.getLocalLocation().z());
				getProtocolClient().sendMoveMessage(player.getWorldLocation());
			}
		}
	}

	private void pushBackPlayer(Vector3f enemyToPlayer, float pushBackDistance) {
		Vector3f pushBackVector = enemyToPlayer.normalize().mul((float) pushBackDistance);
		player.setLocalLocation(player.getWorldLocation().add(pushBackVector));
		targetCamera.updateCameraLocation(getFrameTime());
	}

	private boolean isPlayerCollidedWithEnemy(JBulletPhysicsObject obj1, JBulletPhysicsObject obj2) {
		if (enemyMap.get(obj2.getUID()) == null) {
			return false;
		}
		return obj2 == enemyMap.get(obj2.getUID()).getPhysicsObject() &&
				obj1.getUID() == player.getPhysicsObject().getUID();
	}

	private boolean isPlayerCollidedWithGhost(JBulletPhysicsObject obj1, JBulletPhysicsObject obj2) {
		if (ghostManager.getGhostFromMap(obj2.getUID()) == null) {
			return false;
		}
		return obj2 == ghostManager.getGhostFromMap(obj2.getUID()).getPhysicsObject() &&
				obj1.getUID() == player.getPhysicsObject().getUID();
	}

	private void handleEnemyHit(JBulletPhysicsObject obj1, JBulletPhysicsObject obj2) {
		// obj1 == player, obj2 == enemyWeapon
		if (isPlayerHitByEnemyWeapon(obj1, obj2) &&
				enemyMap.get(obj2.getUID()) != null) {
			Enemy enemy = enemyWeaponMap.get(obj2.getUID()).getOwner();
			Vector3f enemyToPlayer = player.getLocalLocation().sub(enemy.getLocalLocation()).normalize();
			float angle = enemy.getLocalForwardVector().angle(enemyToPlayer);

			if (angle <= enemy.getAttackCone()) {
				if (isAttackAnimationMidpoint(enemy)) {
					updateGameObjectwithPhysicsObject(player, player.getPhysicsObject());
					if (player.getStanceState().isGuarding()) {
						player.reduceHealth(enemy.getWeapon().getDamage() / 2);
						enemy.flinch();
					} else {
						player.reduceHealth(enemy.getWeapon().getDamage());
					}
					enemy.resetFrameCount();
				}
				enemy.addFrameCount();
			}
		} else if (isPlayerHitByGhostWeapon(obj1, obj2) && ghostManager.getGhostFromMap(obj2.getUID()) != null) {
			GhostAvatar ghost = ghostManager.getGhostWeaponFromMap(obj2.getUID()).getOwner();
			if (isAttackAnimationMidpoint(ghost)) {
				updateGameObjectwithPhysicsObject(player, player.getPhysicsObject());

				if (player.getStanceState().isGuarding()) {
					player.reduceHealth(ghost.getWeapon().getDamage() / 2);
				} else {
					player.reduceHealth(ghost.getWeapon().getDamage());
				}
				ghost.resetFrameCount();
			}
			ghost.addFrameCount();
		}
	}

	private boolean isPlayerHitByGhostWeapon(JBulletPhysicsObject obj1, JBulletPhysicsObject obj2) {
		if (enemyMap.get(obj2.getUID()) == null) {
			return false;
		}
		return obj1.getUID() == player.getPhysicsObject().getUID()
				&& obj2 == ghostManager.getGhostWeaponFromMap(obj2.getUID()).getPhysicsObject()
				&& ghostManager.getGhostWeaponFromMap(obj2.getUID()).getOwner().isAttacking();
	}

	private boolean isPlayerHitByEnemyWeapon(JBulletPhysicsObject obj1, JBulletPhysicsObject obj2) {
		if (enemyMap.get(obj2.getUID()) == null) {
			return false;
		}
		return obj1.getUID() == player.getPhysicsObject().getUID()
				&& obj2 == enemyWeaponMap.get(obj2.getUID()).getPhysicsObject()
				&& enemyWeaponMap.get(obj2.getUID()).getOwner().isAttacking();
	}

	private boolean isAttackAnimationMidpoint(AnimatedGameObject obj) {
		return obj.getFrameCount()
				% (obj.getAnimationShape().getCurrentAnimation().getFrameCount() * 2) == 0;
	}

	private void updateMovementControls() {
		boolean movingNorth = isKeyPressed("W");
		boolean movingWest = isKeyPressed("A");
		boolean movingSouth = isKeyPressed("S");
		boolean movingEast = isKeyPressed("D");

		if (movingNorth == false && movingWest == false && movingEast == false && movingSouth == false) {
			hasMovedNorthWest = false;
			hasMovedSouthEast = false;
			hasMovedNorthEast = false;
			hasMovedSouthWest = false;
			hasMovedEast = false;
			hasMovedWest = false;
			hasMovedSouth = false;
		}

		if (movingNorth && movingWest) {
			if (!hasMovedNorthWest) {
				if (!hasRotated) {
					currentRotation += 45f;
					player.yaw(getFrameTime(), currentRotation);
					if (getProtocolClient() != null) {
						getProtocolClient().sendYawMessage(currentRotation);
					}
					hasRotated = true;
				}
				hasMovedNorthWest = true;
				hasMovedSouthEast = false;
				hasMovedNorthEast = false;
				hasMovedSouthWest = false;
				hasMovedEast = false;
				hasMovedWest = false;
				hasMovedSouth = false;
			}
			getControls().moveNorth(getFrameTime());

		} else if (movingNorth && movingEast) {
			if (!hasMovedNorthEast) {
				if (!hasRotated) {
					currentRotation += -45f;
					player.yaw(getFrameTime(), currentRotation);
					if (getProtocolClient() != null) {
						getProtocolClient().sendYawMessage(currentRotation);
					}
					hasRotated = true;
				}
				hasMovedNorthWest = false;
				hasMovedSouthEast = false;
				hasMovedNorthEast = true;
				hasMovedSouthWest = false;
				hasMovedEast = false;
				hasMovedWest = false;
				hasMovedSouth = false;
			}
			getControls().moveNorth(getFrameTime());

		} else if (movingSouth && movingWest) {
			if (!hasMovedSouthWest) {
				if (!hasRotated) {
					currentRotation += 135f;
					player.yaw(getFrameTime(), currentRotation);
					if (getProtocolClient() != null) {
						getProtocolClient().sendYawMessage(currentRotation);
					}
					hasRotated = true;
				}
				hasMovedNorthWest = false;
				hasMovedSouthEast = false;
				hasMovedNorthEast = false;
				hasMovedSouthWest = true;
				hasMovedEast = false;
				hasMovedWest = false;
				hasMovedSouth = false;
			}
			getControls().moveNorth(getFrameTime());
		} else if (movingSouth && movingEast) {
			if (!hasMovedSouthEast) {
				if (!hasRotated) {
					currentRotation += -135f;
					player.yaw(getFrameTime(), currentRotation);
					if (getProtocolClient() != null) {
						getProtocolClient().sendYawMessage(currentRotation);
					}
					hasRotated = true;
				}
				hasMovedNorthWest = false;
				hasMovedSouthEast = true;
				hasMovedNorthEast = false;
				hasMovedSouthWest = false;
				hasMovedEast = false;
				hasMovedWest = false;
				hasMovedSouth = false;
			}
			getControls().moveNorth(getFrameTime());
		} else if (movingNorth) {
			hasMovedNorthWest = false;
			hasMovedNorthEast = false;
			getControls().moveNorth(getFrameTime());
		} else if (movingSouth) {
			if (!hasMovedSouth) {
				if (!hasRotated) {
					player.yaw(getFrameTime(), currentRotation);
					if (getProtocolClient() != null) {
						getProtocolClient().sendYawMessage(currentRotation);
					}
					hasRotated = true;
				}
				hasMovedNorthWest = false;
				hasMovedSouthEast = false;
				hasMovedNorthEast = false;
				hasMovedSouthWest = false;
				hasMovedEast = false;
				hasMovedWest = false;
				hasMovedSouth = true;
			}
			if (!player.isLocked()) {
				getControls().moveSouth(-getFrameTime());
			} else {
				getControls().moveSouth(-getFrameTime());
			}
		} else if (movingWest) {
			if (!hasMovedWest) {
				if (!hasRotated) {
					currentRotation += 90f;
					player.yaw(getFrameTime(), currentRotation);
					if (getProtocolClient() != null) {
						getProtocolClient().sendYawMessage(currentRotation);
					}
					hasRotated = true;
				}
				hasMovedNorthWest = false;
				hasMovedSouthEast = false;
				hasMovedNorthEast = false;
				hasMovedSouthWest = false;
				hasMovedEast = false;
				hasMovedWest = true;
				hasMovedSouth = false;
			}
			if (!player.isLocked()) {
				getControls().moveNorth(getFrameTime());
			} else {
				getControls().moveEast(-getFrameTime());
			}

		} else if (movingEast) {
			if (!hasMovedEast) {
				if (!hasRotated) {
					currentRotation += -90f;
					player.yaw(getFrameTime(), currentRotation);
					if (getProtocolClient() != null) {
						getProtocolClient().sendYawMessage(currentRotation);
					}
					hasRotated = true;
				}
				hasMovedNorthWest = false;
				hasMovedSouthEast = false;
				hasMovedNorthEast = false;
				hasMovedSouthWest = false;
				hasMovedEast = true;
				hasMovedWest = false;
				hasMovedSouth = false;
			}
			if (!player.isLocked()) {
				getControls().moveNorth(getFrameTime());
			} else {
				getControls().moveEast(getFrameTime());
			}

		}
		hasRotated = false;

		if (currentRotation >= 360f) {
			currentRotation -= 360f;
		} else if (currentRotation <= -360f) {
			currentRotation += 360f;
		}
	}

	private void buildEnemyQuadTree() {
		for (Enemy e : enemyList) {
			enemyQuadTree
					.insert(new QuadTreeNode(new QuadTreePoint(e.getLocalLocation().z, e.getLocalLocation().x), e));
		}
	}

	private void buildPlayerQuadTree() {
		for (Player p : playerList) {
			playerQuadTree
					.insert(new QuadTreeNode(new QuadTreePoint(p.getLocalLocation().z, p.getLocalLocation().x), p));
		}
	}

	public void addToPlayerQuadTree(ActiveEntityObject obj) {
		playerQuadTree
				.insert(new QuadTreeNode(new QuadTreePoint(obj.getLocalLocation().z, obj.getLocalLocation().x), obj));
	}

	private void buildTargetCamera() {
		targetCamera = new TargetCamera(getPlayer());
		targetCamera.setLocation((Vector3f) scriptManager.getValue("INITIAL_CAMERA_POS"));
		getEngineInstance().getRenderSystem().getViewport("MAIN").setCamera(targetCamera);
		targetCamera.setLookAtTarget(player.getLocalLocation());
		targetCamera.setLocation(targetCamera.getLocation().mul(new Vector3f(1, 1, -1)));
		targetCamera.updateCameraAngles(frameTime);
	}

	private void buildHUDCamera() {
		HUDCamera = new HUDCamera(getEngineInstance());
		HUDCamera.setLocation((Vector3f) scriptManager.getValue("INITIAL_HUD_CAMERA_POS"));
		HUDViewport = getEngineInstance().getRenderSystem().getViewport(HUDCamera.getViewportName());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				if (MyGame.getGameInstance().getProtocolClient() != null
						&& MyGame.getGameInstance().isClientConnected()) {
					getProtocolClient().sendByeMessage();
				}
				break;
		}
		super.keyPressed(e);
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
		target = enemyQuadTree.findNearby(playerPos, -1, null);

		if (target != null) {
			return (GameObject) target.getData();
		}
		return null;
	}

	public float[] toFloatArray(double[] arr) {
		if (arr == null)
			return null;
		int n = arr.length;
		float[] ret = new float[n];
		for (int i = 0; i < n; i++) {
			ret[i] = (float) arr[i];
		}
		return ret;
	}

	public double[] toDoubleArray(float[] arr) {
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

	private float lerp(float start, float end, float alpha) {
		return start + alpha * (end - start);
	}

	public float getFrameTime() {
		return frameTime;
	}

	public PlayerControls getControls() {
		return controls;
	}

	public TargetCamera getTargetCamera() {
		return targetCamera;
	}

	public Player getPlayer() {
		return player;
	}

	public void setIsConnected(boolean b) {
		isClientConnected = b;
	}

	public boolean isClientConnected() {
		return isClientConnected;
	}

	public GhostManager getGhostManager() {
		return ghostManager;
	}

	public AnimatedShape getGhostShape() {
		return ghostShape;
	}

	public TextureImage getGhostTexture() {
		return playerTx;
	}

	public TextureImage getGhostKatanaTexture() {
		return katanaTx;
	}

	public AnimatedShape getGhostKatanaShape() {
		return ghostKatanaShape;
	}

	public ProtocolClient getProtocolClient() {
		return protocolClient;
	}

	public ScriptManager getScriptManager() {
		return scriptManager;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public void addKeyToActiveKeys(String key) {
		activeKeys.add(key);
	}

	public void removeKeyFromActiveKeys(String key) {
		activeKeys.remove(key);
	}

	public boolean isKeyPressed(String key) {
		return activeKeys.contains(key);
	}

	public EnemyController getEnemyController() {
		return enemyController;
	}

	public AnimationController getAnimationController() {
		return animationController;
	}

	public PhysicsManager getPhysicsManager() {
		return physicsManager;
	}

	public ArrayList<Enemy> getEnemyList() {
		return enemyList;
	}
}
