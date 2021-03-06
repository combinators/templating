import sbt.Keys._
import sbt.Resolver

lazy val commonSettings = Seq(
  organization := "org.combinators",

  scalaVersion := "2.13.1",
  crossScalaVersions := Seq("2.11.12", "2.12.10", scalaVersion.value),

  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.typesafeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.typesafeRepo("snapshots")
  ),

  headerLicense := Some(HeaderLicense.ALv2("2017-2019", "Jan Bessai")),

  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-language:implicitConversions"
  )
) ++ publishSettings

lazy val root = (Project(id = "templating", base = file(".")))
    .settings(commonSettings: _*)
    .enablePlugins(SbtTwirl)

    .settings(
      moduleName := "templating",
      libraryDependencies ++= Seq(
        "org.scalactic" %% "scalactic" % "3.0.8" % "test",
        "org.scalatest" %% "scalatest" % "3.0.8" % "test",
        "com.github.javaparser" % "javaparser-core" % "3.14.14",
        "org.apache.commons" % "commons-text" % "1.8",
        "commons-io" % "commons-io" % "2.6" % "test",
        "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2"
      ),

      sourceDirectories in (Test, TwirlKeys.compileTemplates) += sourceDirectory.value / "test" / "java-templates",
      resourceDirectories in Test += sourceDirectory.value / "resources",
      TwirlKeys.templateImports := Seq(),
      TwirlKeys.templateFormats += ("java" -> "org.combinators.templating.twirl.JavaFormat"),
      TwirlKeys.templateImports += "scala.collection.immutable._",
      TwirlKeys.templateImports += "org.combinators.templating.twirl.Java",

      TwirlKeys.templateImports += "com.github.javaparser.ast._",
      TwirlKeys.templateImports += "com.github.javaparser.ast.body._",
      TwirlKeys.templateImports += "com.github.javaparser.ast.comments._",
      TwirlKeys.templateImports += "com.github.javaparser.ast.expr._",
      TwirlKeys.templateImports += "com.github.javaparser.ast.stmt._",
      TwirlKeys.templateImports += "com.github.javaparser.ast.`type`._",

      sourceDirectories in (Test, TwirlKeys.compileTemplates) += sourceDirectory.value / "test" / "python-templates",
      TwirlKeys.templateFormats += ("py" -> "org.combinators.templating.twirl.PythonFormat"),
      TwirlKeys.templateImports += "org.combinators.templating.twirl.Python"
    )


lazy val publishSettings = Seq(
  homepage := Some(url("https://combinators.org")),
  licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  scmInfo := Some(ScmInfo(url("https://www.github.com/combinators/templating"), "scm:git:git@github.com:combinators/templating.git")),
  developers := List(
    Developer("JanBessai", "Jan Bessai", "jan.bessai@tu-dortmund.de", url("http://janbessai.github.io")),
    Developer("heineman", "George T. Heineman", "heineman@wpi.edu", url("http://www.cs.wpi.edu/~heineman")),
    Developer("BorisDuedder", "Boris Düdder", "boris.d@di.ku.dk", url("http://duedder.net"))
  ),

  pgpPublicRing := file("travis/local.pubring.asc"),
  pgpSecretRing := file("travis/local.secring.asc"),
)

lazy val noPublishSettings = Seq(
  publish := Seq.empty,
  publishLocal := Seq.empty,
  publishArtifact := false
)
