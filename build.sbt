Nice.javaProject

name          := "angulillos-titan"
description   := "use Titan with a lot of angulillos!"
organization  := "bio4j"
bucketSuffix  := "era7.com"
javaVersion   := "1.8"

libraryDependencies ++= Seq(
  "bio4j"                   % "angulillos" % "0.6.0",
  "com.thinkaurelius.titan" % "titan-core" % "0.5.4"
)
