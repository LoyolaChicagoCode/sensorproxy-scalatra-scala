// support Scalatra development (including JRebel plugin)
addSbtPlugin("org.scalatra.sbt" % "scalatra-sbt" % "0.2.0")

// support Twirl templates (statically typed)
addSbtPlugin("io.spray" % "sbt-twirl" % "0.6.1")

// sbt stage -> creates start script in target/start
addSbtPlugin("com.typesafe.sbt" % "sbt-start-script" % "0.7.0")
