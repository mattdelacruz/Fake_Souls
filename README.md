# CSC 165 Final Project---Fake Souls
 _This was a half-semester long project that demonstrates an understanding of game architecture by designing a game independently. The game mainly uses TAGE, a game engine written and designed by California State University-Sacramento's alumni and professors._
## Key features
- Application of various handmade animated models created in Blender.
- The use of game engine features such as lighting, height maps, terrain maneveurability, and networking.
- The use of JBullet, a physics engine, to handle collisions between different entities in the game.
- Application of a quadtree to determine the 2D locations of entities that are inside the game.

## Installing the game
- Requires the installation and configuration of various .jar libraries to run the game, may fix later to not require this.
- Installation of the game assumes Java 11 JDK and JOGL 2.4. 
- The following items needs to be added to the CLASSPATH environment variable
	- jogl-all.jar
	- gluegen-rt.jar
	- joal.jar
	- joml-....jar
	- jinput.jar
	- jbullet.jar
	- vecmath.jar
-  The following item needs to be added to the PATH environment variable
	- ....\jinput\lib

## Setting up networking/Starting the game
- For playing the game on the network, in command line,
	- `.\compileServer.bat`
	- `.\runServer.bat,` leave this window running.
	- Go to a different computer or a new terminal then
	- `.\compile.bat`
	- Edit `.\run.bat` to `java -Dsun.java2d.d3d=false -Dsun.java2d.uiScale=1 a3.MyGame [Host's IP address] 6010 UDP`
	- `.\run.bat` the same instructions for each client.
	- For playing off the network, skip the `.\compileServer.bat` and `.\runServer.bat` instructions.

## Controls
### Keyboard controls:
- W 一 move forward
- A 一 turn camera left
- S 一 move backwards
- D 一 turn camera right
- O 一 target lock an enemy
### Mouse controls:
- Right mouse button 一 guard
- Left mouse button 一 attack
- Mouse movement 一 character yaw
### Gamepad controls:
- Right trigger 一 attack
- Left trigger 一 guard
- Button 2 一 target lock an enemy
- Right control stick 一 character yaw
- Left control stick forward 一 move forward
- Left control stick back 一 move backwards
- D-pad left 一 turn camera left
- D-pad right 一 turn camera right

## How to play
- The player controls an avatar
- The player must defeat the enemies and not have their health reduced to 0
- The player must survive until the end
- Watch out for invaders

## Current issues
- [ ] The game has a problem with the lighting depending on the graphics card the client is using.
	- [ ] TAGE has a problem with running on AMD cards, not sure what to do here
	- Problems with this include:
		- [ ] The game not running at all due to a `NullPointerException` at `updateSSBO`
		- [ ] The game running, but the lighting not being turned on leading to the game just showing black for the textures
		- [ ] The game running, but the enemy entity models not being loaded or being incorrectly loaded in
		- I encountered these issues by running this game on 4 different computers with different setups, not sure how to fix this.

- [ ] Installation of the game should not require the installation of the .jar files mentioned above, they should already be provided when downloading this game
