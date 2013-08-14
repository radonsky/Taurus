// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Typesafe artifactory" at "http://typesafe.artifactoryonline.com/typesafe/maven-releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.1.3")
