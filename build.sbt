import sbt.Keys._
import sbt.Resolver

lazy val commonSettings = Seq(
  organization := "org.combinators",

  scalaVersion := "3.9.0",
  crossScalaVersions := Seq("2.13.18", "3.3.9", scalaVersion.value),

  resolvers ++= Resolver.sonatypeOssRepos("releases"),
  resolvers += Resolver.typesafeRepo("releases"),

  headerLicense := Some(HeaderLicense.ALv2("2017-2026", "Jan Bessai")),

  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-language:implicitConversions"
  ),
  scalacOptions ++= {
    if (scalaVersion.value >= "2.13.14" && scalaVersion.value < "3.0") Seq(
      // "-Xsource:3",
      "-Xsource:3-cross"
    )
    else Nil
  },
) ++ publishSettings

lazy val root = (Project(id = "templating", base = file(".")))
    .settings(commonSettings: _*)
    .enablePlugins(SbtTwirl)

    .settings(
      moduleName := "templating",
      libraryDependencies ++= Seq(
        "org.scalactic" %% "scalactic" % "3.2.20" % "test",
        "org.scalatest" %% "scalatest" % "3.2.20" % "test",
        "com.github.javaparser" % "javaparser-core" % "3.28.2",
        "org.apache.commons" % "commons-text" % "1.15.0",
        "commons-io" % "commons-io" % "2.22.0" % "test"
      ),

      Test / TwirlKeys.compileTemplates / sourceDirectories += sourceDirectory.value / "test" / "java-templates",
      Test / resourceDirectories += sourceDirectory.value / "resources",
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

      Test / TwirlKeys.compileTemplates / sourceDirectories += sourceDirectory.value / "test" / "python-templates",
      TwirlKeys.templateFormats += ("py" -> "org.combinators.templating.twirl.PythonFormat"),
      TwirlKeys.templateImports += "org.combinators.templating.twirl.Python"
    )


lazy val publishSettings = Seq(
  homepage := Some(url("https://combinators.org")),
  licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  scmInfo := Some(ScmInfo(url("https://www.github.com/combinators/templating"), "scm:git:git@github.com:combinators/templating.git")),
  developers := List(
    Developer("JanBessai", "Jan Bessai", "jan.bessai@tu-dortmund.de", url("http://noprotocol.net/jan")),
    Developer("heineman", "George T. Heineman", "heineman@wpi.edu", url("http://www.cs.wpi.edu/~heineman")),
    Developer("BorisDuedder", "Boris Düdder", "boris.d@di.ku.dk", url("http://duedder.net"))
  ),
  publishTo := {
    val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
    if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
    else localStaging.value
  },
  sonatypeCredentialHost := "central.sonatype.com",
) ++ sys.env.get("PGP_KEY_HEX").map(h => usePgpKeyHex(h)).seq

lazy val noPublishSettings = Seq(
  publish := Seq.empty,
  publishLocal := Seq.empty,
  publishArtifact := false
)
