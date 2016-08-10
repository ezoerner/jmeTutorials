package hellosimple

import com.jme3.app.SimpleApplication
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box

object HelloJME3 extends SimpleApplication {

  def main(args: Array[String]): Unit = {
    start()
  }

  override def simpleInitApp(): Unit = {
    val b: Box = new Box(1, 1, 1)
    val geom: Geometry = new Geometry("Box", b)
    val mat: Material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setColor("Color", ColorRGBA.Blue)
    geom.setMaterial(mat)
    rootNode.attachChild(geom)
  }
}
