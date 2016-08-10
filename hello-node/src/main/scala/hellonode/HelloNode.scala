package hellonode

import com.jme3.app.SimpleApplication
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Box

object HelloNode extends SimpleApplication {

  def main(args: Array[String]): Unit = {
    HelloNode.start()
  }

  override def simpleInitApp(): Unit = {
    val box1: Box = new Box(1, 1, 1)
    val blue: Geometry = new Geometry("Box", box1)
    blue.setLocalTranslation(new Vector3f(1, -1, 1))
    val mat1: Material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat1.setColor("Color", ColorRGBA.Blue)
    blue.setMaterial(mat1)
    val box2: Box = new Box(1, 1, 1)
    val red: Geometry = new Geometry("Box", box2)
    red.setLocalTranslation(new Vector3f(1, 3, 1))
    val mat2: Material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat2.setColor("Color", ColorRGBA.Red)
    red.setMaterial(mat2)
    val pivot: Node = new Node("pivot")
    rootNode.attachChild(pivot)
    pivot.attachChild(blue)
    pivot.attachChild(red)
    pivot.rotate(.4f, .4f, 0f)
  }
}
