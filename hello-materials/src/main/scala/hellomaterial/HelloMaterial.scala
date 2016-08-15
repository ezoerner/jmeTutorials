package hellomaterial

import com.jme3.app.SimpleApplication
import com.jme3.light.DirectionalLight
import com.jme3.material.Material
import com.jme3.material.RenderState.BlendMode
import com.jme3.math.{ColorRGBA, Vector3f}
import com.jme3.renderer.queue.RenderQueue.Bucket
import com.jme3.scene.Geometry
import com.jme3.scene.shape.{Box, Sphere}
import com.jme3.texture.Texture
import com.jme3.util.TangentBinormalGenerator

object HelloMaterial {
  def main(args: Array[String]): Unit = {
    val app: HelloMaterial = new HelloMaterial
    app.start()
  }
}
class HelloMaterial extends SimpleApplication {
  override def simpleInitApp(): Unit = {

    /** A simple textured cube -- in good MIP map quality. */
    val cube1Mesh: Box = new Box(1f, 1f, 1f)
    val cube1Geo: Geometry = new Geometry("My Textured Box", cube1Mesh)
    cube1Geo.setLocalTranslation(new Vector3f(-3f, 1.1f, 0f))
    val cube1Mat: Material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    val cube1Tex: Texture = assetManager.loadTexture("Interface/Logo/Monkey.jpg")
    cube1Mat.setTexture("ColorMap", cube1Tex)
    cube1Geo.setMaterial(cube1Mat)
    rootNode.attachChild(cube1Geo)

    /** A translucent/transparent texture, similar to a window frame. */
    val cube2Mesh: Box = new Box(1f, 1f, 0.01f)
    val cube2Geo: Geometry = new Geometry("window frame", cube2Mesh)
    val cube2Mat: Material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    cube2Mat.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"))
    cube2Mat.getAdditionalRenderState.setBlendMode(BlendMode.Alpha)
    cube2Geo.setQueueBucket(Bucket.Transparent)
    cube2Geo.setMaterial(cube2Mat)
    rootNode.attachChild(cube2Geo)

    /** A bumpy rock with a shiny light effect.*/
    val sphereMesh: Sphere = new Sphere(32, 32, 2f)
    val sphereGeo: Geometry = new Geometry("Shiny rock", sphereMesh)
    sphereMesh.setTextureMode(Sphere.TextureMode.Projected)
    TangentBinormalGenerator.generate(sphereMesh)
/*
    val sphereMat: Material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md")
    sphereMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"))
    sphereMat.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"))
    sphereMat.setBoolean("UseMaterialColors", true)
    sphereMat.setColor("Diffuse", ColorRGBA.White)
    sphereMat.setColor("Specular", ColorRGBA.White)
    sphereMat.setFloat("Shininess", 64f)
    sphereGeo.setMaterial(sphereMat)
*/
    sphereGeo.setMaterial(assetManager.loadMaterial("Materials/MyCustomMaterial.j3m"))

    sphereGeo.setLocalTranslation(0, 2, -2)
    sphereGeo.rotate(1.6f, 0, 0)
    rootNode.attachChild(sphereGeo)

    /** Must add a light to make the lit object visible! */
    val sun: DirectionalLight = new DirectionalLight
    sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal)
    sun.setColor(ColorRGBA.White)
    rootNode.addLight(sun)
  }
}
