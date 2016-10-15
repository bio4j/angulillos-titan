enablePlugins(JavaOnlySettings)

name          := "angulillos-titan"
description   := "use Titan with a lot of angulillos!"
organization  := "bio4j"
bucketSuffix  := "era7.com"
javaVersion   := "1.8"

libraryDependencies ++= Seq(
  "bio4j"                   % "angulillos" % "0.9.1",
  "com.thinkaurelius.titan" % "titan-core" % "1.0.0"
)
