package helloassets

import com.jme3.app.SimpleApplication
import com.jme3.font.BitmapText
import com.jme3.light.DirectionalLight
import com.jme3.material.Material
import com.jme3.math.Vector3f
import com.jme3.scene.{Geometry, Spatial}
import com.jme3.scene.shape.Box

object HelloAssets {
  def main(args: Array[String]): Unit = {
    val app: HelloAssets = new HelloAssets
    app.start()
  }
}
class HelloAssets extends SimpleApplication {

  override def simpleInitApp(): Unit = {
    val teapot: Spatial = assetManager.loadModel("Models/Teapot/Teapot.obj")
    val mat_default: Material = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md")
    teapot.setMaterial(mat_default)
    rootNode.attachChild(teapot)

    val box: Box = new Box(2.5f, 2.5f, 1.0f)
    val wall: Spatial = new Geometry("Box", box)
    val mat_brick: Material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat_brick.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"))
    wall.setMaterial(mat_brick)
    wall.setLocalTranslation(2.0f, -2.5f, 0.0f)
    rootNode.attachChild(wall)

    guiNode.detachAllChildren()
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt")
    val helloText: BitmapText = new BitmapText(guiFont, false)
    helloText.setSize(guiFont.getCharSet.getRenderedSize)
    helloText.setText("Hello World")
    helloText.setLocalTranslation(300, helloText.getLineHeight, 0)
    guiNode.attachChild(helloText)

    val ninja: Spatial = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml")
    ninja.scale(0.05f, 0.05f, 0.05f)
    ninja.rotate(0.0f, -3.0f, 0.0f)
    ninja.setLocalTranslation(0.0f, -5.0f, -2.0f)
    rootNode.attachChild(ninja)

    val sun: DirectionalLight = new DirectionalLight
    sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f))
    rootNode.addLight(sun)
  }
}
