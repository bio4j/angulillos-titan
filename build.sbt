Nice.javaProject

javaVersion := "1.8"

name := "angulillos-titan"

description := "use Titan with a lot of angulillos!"

organization := "bio4j"

bucketSuffix := "era7.com"

libraryDependencies += "bio4j" % "angulillos" % "0.4.0"

libraryDependencies += "com.thinkaurelius.titan" % "titan-core" % "0.5.2"

dependencyOverrides += "com.tinkerpop.blueprints" % "blueprints-core" % "2.5.0"
