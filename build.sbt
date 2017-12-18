import sbt.Keys._
import sbt.Resolver

lazy val commonSettings = Seq(
  organization := "org.combinators",
  releaseVersionBump := sbtrelease.Version.Bump.Minor,
  releaseIgnoreUntrackedFiles := true,

  scalaVersion := "2.12.4",
  crossScalaVersions := Seq("2.11.12", scalaVersion.value),
  releaseCrossBuild := true,

  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.typesafeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.typesafeRepo("snapshots")
  ),

  headerLicense := Some(HeaderLicense.ALv2("2017", "Jan Bessai")),

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

      crossScalaVersions := Seq("2.11.11", scalaVersion.value),
      libraryDependencies ++= Seq(
        "org.scalactic" %% "scalactic" % "3.0.4" % "test",
        "org.scalatest" %% "scalatest" % "3.0.4" % "test",
        "com.github.javaparser" % "javaparser-core" % "3.5.4",
        "org.apache.commons" % "commons-text" % "1.1"
      ),

      sourceDirectories in (Test, TwirlKeys.compileTemplates) += sourceDirectory.value / "test" / "java-templates",
      resourceDirectories in Test += sourceDirectory.value / "resources",
      TwirlKeys.templateImports := Seq(),
      TwirlKeys.templateFormats += ("java" -> "org.combinators.templating.twirl.JavaFormat"),
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
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishTo := { version { (v: String) =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }.value },
  homepage := Some(url("https://combinators.org")),
  licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  scmInfo := Some(ScmInfo(url("https://www.github.com/combinators/templating"), "scm:git:git@github.com:combinators/templating.git")),
  pomExtra := (
    <developers>
      <developer>
        <id>JanBessai</id>
        <name>Jan Bessai</name>
        <url>http://janbessai.github.io/</url>
      </developer>
      <developer>
        <id>BorisDuedder</id>
        <name>Boris Düdder</name>
        <url>http://duedder.net/</url>
      </developer>
      <developer>
        <id>heineman</id>
        <name>George T. Heineman</name>
        <url>http://www.cs.wpi.edu/~heineman</url>
      </developer>
    </developers>
    )
)

lazy val noPublishSettings = Seq(
  publish := Seq.empty,
  publishLocal := Seq.empty,
  publishArtifact := false
)

credentials in ThisBuild ++= (for {
  username <- Option(System.getenv().get("SONATYPE_USERNAME"))
  password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
} yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
