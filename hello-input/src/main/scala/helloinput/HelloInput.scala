package helloinput

import com.jme3.app.SimpleApplication
import com.jme3.input.controls.{ActionListener, AnalogListener, KeyTrigger, MouseButtonTrigger}
import com.jme3.input.{KeyInput, MouseInput}
import com.jme3.material.Material
import com.jme3.math.ColorRGBA.Blue
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box

object HelloInput {
  def main(args: Array[String]): Unit = {
    val app: HelloInput = new HelloInput
    app.start()
  }
}
class HelloInput extends SimpleApplication {
  protected var player: Geometry = null
  var isRunning: Boolean = true

  def simpleInitApp(): Unit = {
    val b: Box = new Box(1, 1, 1)
    player = new Geometry("Player", b)
    val mat: Material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setColor("Color", Blue)
    player.setMaterial(mat)
    rootNode.attachChild(player)
    initKeys()
  }

  private def initKeys(): Unit = {
    inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P))
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J))
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K))
    inputManager
      .addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE), new MouseButtonTrigger(MouseInput.BUTTON_LEFT))
    inputManager.addListener(actionListener, "Pause")
    inputManager.addListener(analogListener, "Left", "Right", "Rotate")
  }

  private val actionListener: ActionListener = (name: String, keyPressed: Boolean, tpf: Float) ⇒
    if (name == "Pause" && !keyPressed) {
      isRunning = !isRunning
    }

  private val analogListener: AnalogListener = (name: String, value: Float, tpf: Float) ⇒ {
      if (isRunning) {
        if (name == "Rotate") {
          player.rotate(0, value * speed, 0)
        }
        if (name == "Right") {
          val v: Vector3f = player.getLocalTranslation
          player.setLocalTranslation(v.x + value * speed, v.y, v.z)
        }
        if (name == "Left") {
          val v: Vector3f = player.getLocalTranslation
          player.setLocalTranslation(v.x - value * speed, v.y, v.z)
        }
      }
      else {
        System.out.println("Press P to unpause.")
      }
    }
}
