// support Scalatra development (including JRebel plugin)
addSbtPlugin("org.scalatra.sbt" % "scalatra-sbt" % "0.2.0")

// precompile Scalate templates
addSbtPlugin("com.mojolly.scalate" % "xsbt-scalate-generator" % "0.4.2")

// sbt stage -> creates start script in target/start
addSbtPlugin("com.typesafe.sbt" % "sbt-start-script" % "0.7.0")

// sbt eclipse -> creates Eclipse project files
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.2")

// sbt dependency-updates -> checks which dependencies can be updates
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.0")
