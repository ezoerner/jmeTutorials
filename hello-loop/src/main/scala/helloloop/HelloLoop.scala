package helloloop

import com.jme3.app.SimpleApplication
import com.jme3.material.Material
import com.jme3.math.ColorRGBA.{Black, Blue}
import com.jme3.math.{ColorRGBA, Vector3f}
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box

object HelloLoop {
  def main(args: Array[String]): Unit = {
    val app: HelloLoop = new HelloLoop
    app.start()
  }
}

class HelloLoop extends SimpleApplication {
  protected val player1: Geometry = new Geometry("color cube", new Box(1, 1, 1))
  protected val player2: Geometry = new Geometry("blue cube", new Box(1, 1, 1))

  def simpleInitApp(): Unit = {
    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    val c = Black
    mat.setColor("Color", c)
    player1.setMaterial(mat)
    player1.setLocalTranslation(new Vector3f(2, 0, 0))

    val mat2 = mat.clone
    mat2.setColor("Color", Blue)
    player2.setMaterial(mat2)
    player2.setLocalTranslation(new Vector3f(-2, 0, 0))

    rootNode.attachChild(player1)
    rootNode.attachChild(player2)
  }

  override def simpleUpdate(tpf: Float): Unit = {
    player1.rotate(0, 2 * tpf, 0)

    val color = Option[ColorRGBA](player1 getUserData "color") getOrElse Black
    val increment = tpf



    val newColor1 = new ColorRGBA(color.r + increment, color.g, color.b, 1.0f)
    val newColor2 = normalize(newColor1, increment)
    player1.setUserData("color", newColor2)
    player1.getMaterial.setColor("Color", newColor2)

    val prevScaleDirection: Int = Option[Int](player2 getUserData "scaleDirection") getOrElse 1
    val prevScale: Float = Option[Float](player2 getUserData "scale") getOrElse 1.0f
    val newDirection = prevScale match {
      case s if s <= 0.7f ⇒ 1
      case s if s >= 1.3f ⇒ -1
      case s ⇒ prevScaleDirection
    }

    val newScale = ((prevScale * (1 + tpf * newDirection * 0.5f)) min 1.3f) max 0.7f
    player2.setUserData("scaleDirection", newDirection)
    player2.setUserData("scale", newScale)
    player2.setLocalScale(newScale)
  }

  def normalize(color: ColorRGBA, increment: Float): ColorRGBA = color match {
    case c if c.r >= 1.0f ⇒ normalize(new ColorRGBA(0.0f, c.g + increment, c.b, 1.0f), increment)
    case c if c.g >= 1.0f ⇒ normalize(new ColorRGBA(0.0f, 0.0f, c.b + increment, 1.0f), increment)
    case c if c.b >= 1.0f ⇒ Black
    case _ ⇒ color
  }
}
