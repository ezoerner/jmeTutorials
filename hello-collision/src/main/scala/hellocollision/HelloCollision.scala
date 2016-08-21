package hellocollision

import com.jme3.app.SimpleApplication
import com.jme3.asset.plugins.ZipLocator
import com.jme3.bullet.BulletAppState
import com.jme3.bullet.collision.shapes.{CapsuleCollisionShape, CollisionShape}
import com.jme3.bullet.control.{CharacterControl, RigidBodyControl}
import com.jme3.bullet.util.CollisionShapeFactory
import com.jme3.input.KeyInput
import com.jme3.input.controls.{ActionListener, KeyTrigger}
import com.jme3.light.{AmbientLight, DirectionalLight}
import com.jme3.math.{ColorRGBA, Vector3f}
import com.jme3.scene.Spatial

/**
  * Example 9 - How to make walls and floors solid.
  * This collision code uses Physics and a custom Action Listener.
  *
  * @author normen, with edits by Zathras
  */
object HelloCollision {
  def main(args: Array[String]): Unit = {
    val app: HelloCollision = new HelloCollision
    app.start()
  }
}

class HelloCollision extends SimpleApplication with ActionListener {
  private var sceneModel: Spatial = null
  private var bulletAppState: BulletAppState = null
  private var landscape: RigidBodyControl = null
  private var player: CharacterControl = null
  private val walkDirection: Vector3f = new Vector3f
  private var left: Boolean = false
  private var right: Boolean = false
  private var up: Boolean = false
  private var down: Boolean = false
  //Temporary vectors used on each frame.
  //They here to avoid instanciating new vectors on each frame
  private val camDir: Vector3f = new Vector3f
  private val camLeft: Vector3f = new Vector3f

  def simpleInitApp(): Unit = {
    /** Set up Physics */
    bulletAppState = new BulletAppState
    stateManager.attach(bulletAppState)
    //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
    // We re-use the flyby camera for rotation, while positioning is handled by physics
    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f))
    flyCam.setMoveSpeed(100)
    setUpKeys()
    setUpLight()
    // We load the scene from the zip file and adjust its size.
    assetManager.registerLocator("town.zip", classOf[ZipLocator])
    sceneModel = assetManager.loadModel("main.scene")
    sceneModel.setLocalScale(2f)
    // We set up collision detection for the scene by creating a
    // compound collision shape and a static RigidBodyControl with mass zero.
    val sceneShape: CollisionShape = CollisionShapeFactory.createMeshShape(sceneModel)
    landscape = new RigidBodyControl(sceneShape, 0)
    sceneModel.addControl(landscape)
    // We set up collision detection for the player by creating
    // a capsule collision shape and a CharacterControl.
    // The CharacterControl offers extra settings for
    // size, stepheight, jumping, falling, and gravity.
    // We also put the player in its starting position.
    val capsuleShape: CapsuleCollisionShape = new CapsuleCollisionShape(1.5f, 6f, 1)
    player = new CharacterControl(capsuleShape, 0.05f)
    player.setJumpSpeed(20)
    player.setFallSpeed(30)
    player.setGravity(30)
    player.setPhysicsLocation(new Vector3f(0, 10, 0))
    // We attach the scene and the player to the rootnode and the physics space,
    // to make them appear in the game world.
    rootNode.attachChild(sceneModel)
    bulletAppState.getPhysicsSpace.add(landscape)
    bulletAppState.getPhysicsSpace.add(player)
  }

  private def setUpLight(): Unit = {
    // We add light so we see the scene
    val al: AmbientLight = new AmbientLight
    al.setColor(ColorRGBA.White.mult(1.3f))
    rootNode.addLight(al)
    val dl: DirectionalLight = new DirectionalLight
    dl.setColor(ColorRGBA.White)
    dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal)
    rootNode.addLight(dl)
  }

  /** We over-write some navigational key mappings here, so we can
    * add physics-controlled walking and jumping: */
  private def setUpKeys(): Unit = {
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A))
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D))
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W))
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S))
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE))
    inputManager.addListener(this, "Left")
    inputManager.addListener(this, "Right")
    inputManager.addListener(this, "Up")
    inputManager.addListener(this, "Down")
    inputManager.addListener(this, "Jump")
  }

  /** These are our custom actions triggered by key presses.
    * We do not walk yet, we just keep track of the direction the user pressed. */
  def onAction(binding: String, isPressed: Boolean, tpf: Float): Unit = {
    if (binding == "Left") left = isPressed
    else if (binding == "Right") right = isPressed
    else if (binding == "Up") up = isPressed
    else if (binding == "Down") down = isPressed
    else if (binding == "Jump") if (isPressed) player.jump()
  }

  /**
    * This is the main event loop--walking happens here.
    * We check in which direction the player is walking by interpreting
    * the camera direction forward (camDir) and to the side (camLeft).
    * The setWalkDirection() command is what lets a physics-controlled player walk.
    * We also make sure here that the camera moves with player.
    */
  override def simpleUpdate(tpf: Float): Unit = {
    camDir.set(cam.getDirection).multLocal(0.6f)
    camLeft.set(cam.getLeft).multLocal(0.4f)
    walkDirection.set(0, 0, 0)
    if (left) walkDirection.addLocal(camLeft)
    if (right) walkDirection.addLocal(camLeft.negate)
    if (up) walkDirection.addLocal(camDir)
    if (down) walkDirection.addLocal(camDir.negate)
    player.setWalkDirection(walkDirection)
    cam.setLocation(player.getPhysicsLocation)
  }
}
