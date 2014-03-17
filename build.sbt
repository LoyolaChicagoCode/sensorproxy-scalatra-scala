import com.typesafe.sbt.SbtStartScript
import org.scalatra.sbt.ScalatraPlugin

organization := "edu.luc.etl"

name := "sensorproxy-scalatra-scala"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions := Seq(
  "-language:implicitConversions",
  "-language:postfixOps",
  "-language:higherKinds",
  "-language:reflectiveCalls",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-encoding",
  "utf8"
)

seq(webSettings :_*)

classpathTypes ~= (_ + "orbit")

seq(Twirl.settings: _*)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= {
  object V {
    val scalaz = "7.0.5"
    val scalatra = "2.2.2"
  }
  Seq(
    "org.scalaz"              %% "scalaz-core"       % V.scalaz,
    "org.scalatra"            %% "scalatra"          % V.scalatra,
    "org.scalatra"            %% "scalatra-specs2"   % V.scalatra            % "test",
    "org.scalatra"            %% "scalatra-swagger"  % V.scalatra,
    "org.scalatra"            %% "scalatra-json"     % V.scalatra,
    "org.json4s"              %% "json4s-jackson"    % "3.2.7",
    "com.novus"               %% "salat"             % "1.9.5",
    "org.zeromq"              %  "zeromq-scala-binding_2.10" % "0.0.7",
    "ch.qos.logback"          %  "logback-classic"   % "1.1.1"               % "runtime",
    "org.eclipse.jetty"       %  "jetty-webapp"      % "8.1.14.v20131031"    % "compile;container",
    "org.eclipse.jetty.orbit" %  "javax.servlet"     % "3.0.0.v201112011016" % "compile;container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
  )
}

apps in container.Configuration <<= (deployment in Compile) map (d => Seq("/api" -> d))

// TODO figure out this logging dependency problem
ivyXML := <dependencies><exclude module="slf4j-log4j12"/></dependencies>

seq(ScalatraPlugin.scalatraWithJRebel: _*)

seq(SbtStartScript.startScriptForClassesSettings: _*)
