name := "iris"

version := "0.1"

scalaVersion := "2.12.12"

val versionSpark = "3.1.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % versionSpark,
  "org.apache.spark" %% "spark-core" % versionSpark,
  "org.apache.spark" %% "spark-mllib" % versionSpark,
  "org.apache.spark" %% "spark-sql-kafka-0-10" % versionSpark

)
libraryDependencies += "org.scalanlp" %% "breeze" % "1.2"

// https://mvnrepository.com/artifact/org.scalanlp/breeze-natives
libraryDependencies += "org.scalanlp" %% "breeze-natives" % "1.2"

// https://mvnrepository.com/artifact/org.scalanlp/breeze-viz
libraryDependencies += "org.scalanlp" %% "breeze-viz" % "1.2"
// https://mvnrepository.com/artifact/joda-time/joda-time
libraryDependencies += "joda-time" % "joda-time" % "2.10.10"



