name := "jmeTutorials"
version := "1.0"

val commonSettings = Seq(
  scalaVersion := "2.12.0-M5",
  resolvers += Resolver.bintrayRepo("jmonkeyengine", "com.jme3"),
  unmanagedBase := (baseDirectory in ThisBuild).value / "lib",
  libraryDependencies ++= Seq(
    jME3("core"), // Core libraries needed for all jME3 projects
    jME3("desktop", Runtime), // Parts of the jME3 API that are only compatible with desktop renderers, needed for image loading on desktop
    jME3("lwjgl", Runtime), // Desktop renderer for jME3
    jME3("plugins", Runtime), // Loader plugins for OgreXML and jME-XML
    jME3("effects"), // Effects libraries for water and other post filters
    jME3("networking"), // jME3 networking libraries (aka spidermonkey)
    jME3("jogg"), // Loader for jogg audio files
    jME3("terrain"), // Terrain generation API
    jME3("blender"), // Blender file loader, only works on desktop renderers
    jME3("jbullet"), // Physics support using jbullet (desktop only) Only jme3-jbullet OR jme3-bullet can be used
    jME3("niftygui") // NiftyGUI support for jME3
  ))

val `hello-simple` = project
    .settings(commonSettings: _*)

val `hello-node` = project
    .settings(commonSettings: _*)

val `hello-assets` = project
    .settings(commonSettings: _*)

val `hello-loop` = project
    .settings(commonSettings: _*)

val `hello-input` = project
    .settings(commonSettings: _*)

val `hello-materials` = project
    .settings(commonSettings: _*)

val `hello-animation` = project
    .settings(commonSettings: _*)

val `hello-picking` = project
    .settings(commonSettings: _*)

val `hello-collision` = project
    .settings(commonSettings: _*)

val `jme-test` = project
    .settings(commonSettings: _*)

def jME3(name: String, config: Configuration = Compile) = "com.jme3" % s"jme3-$name" % "3.0.10" % config
