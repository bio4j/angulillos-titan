Nice.javaProject

name          := "angulillos-titan"
description   := "use Titan with a lot of angulillos!"
organization  := "bio4j"
bucketSuffix  := "era7.com"
javaVersion   := "1.8"

libraryDependencies ++= Seq(
  "bio4j"                   % "angulillos" % "0.7.0-SNAPSHOT",
  "com.thinkaurelius.titan" % "titan-core" % "1.0.0"
)

excludeFilter in unmanagedSources :=
  (excludeFilter in unmanagedSources).value ||
  "*Index.java"
