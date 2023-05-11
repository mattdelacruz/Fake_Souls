var JavaPackages = new JavaImporter(
    Packages.tage.Light,
    Packages.org.joml.Vector3f
);

with (JavaPackages) {

	//----------------------------------------------------------------
    var WINDOW_WIDTH = 600;
	var WINDOW_HEIGHT = 600;
	var ENEMY_AMOUNT = 1;
	var PLAY_AREA_SIZE = 300;
	var PLAYER_HEIGHT_SPEED = 0.5;
	var INITIAL_CAMERA_POS = new Vector3f(0, 0, 5);
	var SKYBOX_NAME = "fluffyClouds";
	var PLAYER_TEXTURE = "player-texture.png";
	var GHOST_TEXTURE = "neptune.jpg";
	var ENEMY_TEXTURE = "knight-texture.png";
	var TERRAIN_MAP = "terrain-map.png";
	var TERRAIN_TEXTURE = "moon-craters.jpg";
	var KATANA_TEXTURE = "katana-texture.png";
	var SPEAR_TEXTURE = "longinus-texture.png";
	//----------------------------------------------------------------
	// Player animation file paths
	//----------------------------------------------------------------
	var PLAYER_RKM = "player-animations/player.rkm";
	var PLAYER_RKS = "player-animations/player.rks";
	var PLAYER_RUN_RKA = "player-animations/player-run.rka";
	var PLAYER_BACKWARDS_RUN_RKA = "player-animations/player-backwards-run.rka";
	var PLAYER_IDLE_RKA = "player-animations/player-idle.rka";
	var PLAYER_ATTACK_1_RKA = "player-animations/player-attack-1.rka";
	var PLAYER_GUARD_RKA = "player-animations/player-guard.rka";
	var PLAYER_GUARD_WALK_RKA = "player-animations/player-guard-walk.rka";
	var PLAYER_STRAFE_RKA = "player-animations/player-strafe.rka";
	var PLAYER_GUARD_STRAFE_RKA = "player-animations/player-guard-strafe.rka";
	//----------------------------------------------------------------
	// Katana animation file paths
	//----------------------------------------------------------------
	var KATANA_RKM = "player-animations/weapon-animations/katana.rkm";
	var KATANA_RKS = "player-animations/weapon-animations/katana.rks";
	var KATANA_RUN_RKA = "player-animations/weapon-animations/katana-run.rka";
	var KATANA_BACKWARDS_RUN_RKA = "player-animations/weapon-animations/katana-backwards-run.rka";
	var KATANA_IDLE_RKA = "player-animations/weapon-animations/katana-idle.rka";
	var KATANA_ATTACK_1_RKA = "player-animations/weapon-animations/katana-attack-1.rka";
	var KATANA_GUARD_RKA = "player-animations/weapon-animations/katana-guard.rka";
	var KATANA_GUARD_WALK_RKA = "player-animations/weapon-animations/katana-guard-walk.rka";
	var KATANA_STRAFE_RKA = "player-animations/weapon-animations/katana-strafe.rka";
	//----------------------------------------------------------------	
	// Enemy animation file paths
	//----------------------------------------------------------------
	var ENEMY_RKM = "enemy-animations/knight-enemy.rkm";
	var ENEMY_RKS = "enemy-animations/knight-enemy.rks";
	var ENEMY_RUN_RKA = "enemy-animations/knight-enemy-run.rka";
	var ENEMY_IDLE_RKA = "enemy-animations/knight-enemy-idle.rka";
	var ENEMY_ATTACK_RKA = "enemy-animations/knight-enemy-attack.rka";
	var ENEMY_DEATH_RKA = "enemy-animations/knight-enemy-death.rka";
	//----------------------------------------------------------------
	// Spear animation file paths
	//----------------------------------------------------------------
	var SPEAR_RKM = "enemy-animations/weapon-animations/longinus.rkm";
	var SPEAR_RKS = "enemy-animations/weapon-animations/longinus.rks";
	var SPEAR_IDLE_RKA = "enemy-animations/weapon-animations/longinus-idle.rka";
	var SPEAR_RUN_RKA = "enemy-animations/weapon-animations/longinus-run.rka";
	var SPEAR_ATTACK_RKA = "enemy-animations/weapon-animations/longinus-attack.rka";
	var SPEAR_DEATH_RKA = "enemy-animations/weapon-animations/longinus-death.rka";
	//----------------------------------------------------------------
	// Music file paths
	//----------------------------------------------------------------
	var BACKGROUND_MUSIC = "assets/sounds/background-music.wav";
	var KATANA_WHIFF_SOUND = "assets/sounds/katana-slash-whiff.wav";
	var KATANA_HIT_SOUND = "assets/sounds/katana-slash-hit.wav";
	var BOSS_MUSIC = "assets/sounds/"

	var STEP1 = "assets/sounds/step1.wav";
	var STEP2 = "assets/sounds/step2.wav";
}

var xPlayerPos = 70;
var yPlayerPos = 0;
var zPlayerPos = 124;
var pushBackDistance = 0.1;
