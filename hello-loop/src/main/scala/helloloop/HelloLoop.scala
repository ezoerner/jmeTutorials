package helloloop

import com.jme3.app.SimpleApplication
import com.jme3.material.Material
import com.jme3.math.ColorRGBA._
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
  protected val player3: Geometry = new Geometry("rolling cube", new Box(0.5f, 0.5f, 0.5f))

  def simpleInitApp(): Unit = {
    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setColor("Color", currentColorTarget)
    player1.setMaterial(mat)
    player1.setLocalTranslation(new Vector3f(2, 0, 0))

    val mat2 = mat.clone
    mat2.setColor("Color", Blue)
    player2.setMaterial(mat2)
    player2.setLocalTranslation(new Vector3f(-2, 0, 0))

    val mat3 = mat.clone
    mat3.setColor("Color", Cyan)
    player3.setMaterial(mat3)
    player3.setLocalTranslation(new Vector3f(0, 2, -15))
    rootNode.attachChild(player3)
    rootNode.attachChild(player1)
    rootNode.attachChild(player2)
  }

  override def simpleUpdate(tpf: Float): Unit = {
    player1.rotate(0, 2 * tpf, 0)

    val color1 = Option[ColorRGBA](player1 getUserData "color") getOrElse currentColorTarget
    val color2 = nextColor(color1, tpf / 2)
    player1.setUserData("color", color2)
    player1.getMaterial.setColor("Color", color2)

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

    player3.rotate(2*tpf, 0 , 0)
    player3.move(0, 0, 2*tpf)
  }

  val Purple = new ColorRGBA(85f / 255f, 26f / 255f, 139f / 255f, 1.0f)
  val colorTargets = Red :: Orange :: Yellow :: Green :: Blue :: Purple :: Nil

  def newTargetIterator = colorTargets.iterator

  var currentTargets = newTargetIterator
  val targetIterator = Iterator.continually {
    if (currentTargets.hasNext) {
      currentTargets.next()
    } else {
      currentTargets = newTargetIterator
      currentTargets.next()
    }
  }
  var currentColorTarget = targetIterator.next()

  def nextColor(color: ColorRGBA, changeAmount: Float): ColorRGBA = {
    if (color == currentColorTarget) {
      currentColorTarget = targetIterator.next()
    }
    incrementTowards(color, currentColorTarget, changeAmount)
  }

  def incrementTowards(beginColor: ColorRGBA, finalColor: ColorRGBA, increment: Float): ColorRGBA = {
    def nextV(s: Float, f: Float): Float = {
      val sgn = math.signum(f.compare(s))
      val n = s + sgn * increment
      sgn match {
        case 1 if n > f ⇒ f
        case -1 if n < f ⇒ f
        case _ ⇒ n
      }
    }
    val next = new ColorRGBA(
      nextV(beginColor.r, finalColor.r),
      nextV(beginColor.g, finalColor.g),
      nextV(beginColor.b, finalColor.b),
      1.0f)
    next
  }
}
