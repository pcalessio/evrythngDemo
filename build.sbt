name := "evrythng"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.json"%"org.json"%"chargebee-1.0"
)     


libraryDependencies += "com.evrythng" % "evrythng-java-wrapper" % "1.7.1"

resolvers += "evrythng-public-releases" at "https://internal.evrythng.net/nexus/content/repositories/evrythng-public-releases"

play.Project.playJavaSettings
