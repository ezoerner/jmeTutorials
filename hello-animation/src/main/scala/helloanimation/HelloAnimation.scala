package helloanimation

import com.jme3.animation.LoopMode.{DontLoop, Loop}
import com.jme3.animation.{AnimChannel, AnimControl, AnimEventListener}
import com.jme3.app.SimpleApplication
import com.jme3.input.KeyInput._
import com.jme3.input.controls.{ActionListener, KeyTrigger}
import com.jme3.light.DirectionalLight
import com.jme3.material.Material
import com.jme3.math.ColorRGBA.Green
import com.jme3.math.{ColorRGBA, Vector3f}
import com.jme3.scene.Node
import com.jme3.scene.debug.SkeletonDebugger

object HelloAnimation {
  def main(args: Array[String]): Unit = {
    val app: HelloAnimation = new HelloAnimation
    app.start()
  }
}

/** Sample 7 - how to load an OgreXML model and play an animation,
  * using channels, a controller, and an AnimEventListener. */
class HelloAnimation extends SimpleApplication with AnimEventListener {
  private var control: AnimControl = _
  var player: Node = _
  val animations: IndexedSeq[String] = IndexedSeq("Dodge", "pull", "Walk", "push")
  var channels: IndexedSeq[AnimChannel] = _

  private val defaultAnimation = "stand"

  def simpleInitApp(): Unit = {
    viewPort.setBackgroundColor(ColorRGBA.LightGray)
    initKeys()
    /* Displaying the model requires a light source */
    val dl: DirectionalLight = new DirectionalLight
    dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal)
    rootNode.addLight(dl)
    /* load and attach the model as usual */
    player = assetManager.loadModel("Models/Oto/Oto.mesh.xml").asInstanceOf[Node]
    player.setLocalScale(0.5f)
    rootNode.attachChild(player)
    control = player.getControl(classOf[AnimControl])
    control.addListener(this)

    channels = animations map { anim ⇒
      val channel = control.createChannel
      resetChannel(channel)
      channel
    }

    val skeletonDebug = new SkeletonDebugger("skeleton", control.getSkeleton)
    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setColor("Color", Green)
    mat.getAdditionalRenderState.setDepthTest(false)
    skeletonDebug.setMaterial(mat)
    player.attachChild(skeletonDebug)
  }

  override def onAnimCycleDone(control: AnimControl, channel: AnimChannel, animName: String): Unit = resetChannel(channel)

  def resetChannel(channel: AnimChannel): Unit = {
    channel.setAnim(defaultAnimation, 0.50f)
    channel.setLoopMode(DontLoop)
    channel.setSpeed(1f)
  }

  override def onAnimChange(control: AnimControl, channel: AnimChannel, animName: String): Unit = { }

  /** Custom Keybinding: Map named actions to inputs. */
  private def initKeys(): Unit = {
    animations zip List(KEY_1, KEY_2, KEY_3, KEY_4) map {
      case (anim, k) ⇒
        inputManager.addMapping(anim, new KeyTrigger(k))
        inputManager.addListener(actionListener, anim)
    }
  }

  private def actionListener: ActionListener = (name: String, keyPressed: Boolean, tpf: Float) ⇒ {
    val channel = channels(animations indexOf name)
    if (!keyPressed && channel.getAnimationName != name) {
      channel.setAnim(name, 0.50f)
      channel.setLoopMode(Loop)
    }
  }
}
