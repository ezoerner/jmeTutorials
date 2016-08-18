package hellopicking

import com.jme3.app.SimpleApplication
import com.jme3.collision.{CollisionResult, CollisionResults}
import com.jme3.font.BitmapText
import com.jme3.input.controls.{ActionListener, KeyTrigger, MouseButtonTrigger}
import com.jme3.input.{KeyInput, MouseInput}
import com.jme3.light.DirectionalLight
import com.jme3.material.Material
import com.jme3.math.{ColorRGBA, Ray, Vector3f}
import com.jme3.scene.shape.{Box, Sphere}
import com.jme3.scene.{Geometry, Node, Spatial}

import scala.collection.JavaConverters._

/** Sample 8 - how to let the user pick (select) objects in the scene
  * using the mouse or key presses. Can be used for shooting, opening doors, etc. */
object HelloPicking {
  def main(args: Array[String]): Unit = {
    (new HelloPicking).start()
  }
}

class HelloPicking extends SimpleApplication {
  private var shootables: Node = _
  private var mark: Geometry = _

  def simpleInitApp(): Unit = {
    initCrossHairs() // a "+" in the middle of the screen to help aiming
    initKeys() // load custom key mappings
    initMark() // a red sphere to mark the hit

    /** create four colored boxes and a floor to shoot at: */
    shootables = new Node("Shootables")
    rootNode.attachChild(shootables)
    shootables.attachChild(makeCube("a Dragon", -2f, 0f, 1f))
    shootables.attachChild(makeCube("a tin can", 1f, -2f, 0f))
    shootables.attachChild(makeCube("the Sheriff", 0f, 1f, -2f))
    shootables.attachChild(makeCube("the Deputy", 1f, 0f, -4f))
    shootables.attachChild(makeFloor)
    shootables.attachChild(makeCharacter)
  }

  /** Declaring the "Shoot" action and mapping to its triggers. */
  private def initKeys(): Unit = {
    inputManager.addMapping(
      "Shoot", new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
      new MouseButtonTrigger(MouseInput.BUTTON_LEFT)) // trigger 2: left-button click
    inputManager.addListener(actionListener, "Shoot")
  }

  /** Defining the "Shoot" action: Determine what was hit and how to respond. */
  final private val actionListener: ActionListener = new ActionListener() {
    def onAction(name: String, keyPressed: Boolean, tpf: Float): Unit = {
      if ("Shoot" == name && !keyPressed) {

        // 1. Reset results list.
        val results: CollisionResults = new CollisionResults

        // 2. Aim the ray from cam loc to cam direction.
        val ray: Ray = new Ray(cam.getLocation, cam.getDirection)

        // 3. Collect intersections between Ray and Shootables in results list.
        // DO NOT check collision with the root node, or else ALL collisions will hit the skybox!
        // Always make a separate node for objects you want to collide with.
        shootables.collideWith(ray, results)
        val resultsAsScala: Iterable[(CollisionResult, Int)] = results.asScala.zipWithIndex

        // 4. Print the results
        println(s"----- Collisions? ${results.size}-----")
        resultsAsScala foreach { case (collisionResult, i) ⇒
          // For each hit, we know distance, impact point, name of geometry.
          val dist: Float = collisionResult.getDistance
          val pt: Vector3f = collisionResult.getContactPoint
          val hit: String = collisionResult.getGeometry.getName
          println(s"* Collision #$i")
          println(s"  You shot $hit at $pt, $dist wu away.")
        }

        // 5. Use the results (we mark the hit object)
        if (results.size > 0) {
          // The closest collision point is what was truly hit:
          val closest: CollisionResult = results.getClosestCollision
          // Let's interact - we mark the hit with a red dot.
          mark.setLocalTranslation(closest.getContactPoint)
          rootNode.attachChild(mark)
        } else {
          // No hits? Then remove the red mark.
          rootNode.detachChild(mark)
        }
      }
    }
  }

  /** A cube object for target practice */
  protected def makeCube(name: String, x: Float, y: Float, z: Float): Geometry = {
    val box: Box = new Box(1, 1, 1)
    val cube: Geometry = new Geometry(name, box)
    cube.setLocalTranslation(x, y, z)
    val mat1: Material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat1.setColor("Color", ColorRGBA.randomColor)
    cube.setMaterial(mat1)
    cube
  }

  /** A floor to show that the "shot" can go through several objects. */
  protected def makeFloor: Geometry = {
    val box: Box = new Box(15, .2f, 15)
    val floor: Geometry = new Geometry("the Floor", box)
    floor.setLocalTranslation(0, -4, -5)
    val mat1: Material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat1.setColor("Color", ColorRGBA.Gray)
    floor.setMaterial(mat1)
    floor
  }

  /** A red ball that marks the last spot that was "hit" by the "shot". */
  protected def initMark(): Unit = {
    val sphere: Sphere = new Sphere(30, 30, 0.2f)
    mark = new Geometry("BOOM!", sphere)
    val markMat: Material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    markMat.setColor("Color", ColorRGBA.Red)
    mark.setMaterial(markMat)
  }

  /** A centred plus sign to help the player aim. */
  protected def initCrossHairs(): Unit = {
    setDisplayStatView(false)
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt")
    val ch: BitmapText = new BitmapText(guiFont, false)
    ch.setSize(guiFont.getCharSet.getRenderedSize * 2)
    ch.setText("+") // crosshairs
    ch.setLocalTranslation(
      // center
      settings.getWidth / 2 - ch.getLineWidth / 2, settings.getHeight / 2 + ch.getLineHeight / 2, 0)
    guiNode.attachChild(ch)
  }

  protected def makeCharacter: Spatial = {
    // load a character from jme3test-test-data
    val golem: Spatial = assetManager.loadModel("Models/Oto/Oto.mesh.xml")
    golem.scale(0.5f)
    golem.setLocalTranslation(-1.0f, -1.5f, -0.6f)
    // We must add a light to make the model visible
    val sun: DirectionalLight = new DirectionalLight
    sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f))
    golem.addLight(sun)
    golem
  }
}
