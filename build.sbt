val root = (project in file("."))
    .settings(name := "jmeTutorials",
      version := "1.0")

val commonSettings = Seq(
  scalaVersion := "2.11.8",
  resolvers += Resolver.bintrayRepo("jmonkeyengine", "com.jme3"),
  libraryDependencies ++= Seq(
  jME3("core"),
  jME3("desktop", Runtime),
  jME3("lwjgl", Runtime),
  jME3("plugins", Runtime) // needed for OgreXML and jME-XML support in tutorials
  ))

val `hello-simple` = project
    .settings(commonSettings: _*)

val `hello-node` = project
    .settings(commonSettings: _*)

val `hello-assets` = project
    .settings(commonSettings: _*)

def jME3(name: String, config: Configuration = Compile) = "com.jme3" % s"jme3-$name" % "3.0.10" % config
