import sbt._
import Keys._

object BitcoinSRPCClientBuild extends Build {

  val appName = "bitcoin-s-rpc-client"
  val appV = "0.0.1" 
  val scalaV = "2.11.4"
  val organization = "org.bitcoins"
  val slf4jV = "1.7.5"
  val appDependencies = Seq(
    "org.scalatest" % "scalatest_2.11" % "2.2.0",
    //"org.slf4j" % "slf4j-api" % slf4jV /*% "provided"*/,
    "io.spray" %%  "spray-json" % "1.3.0" withSources() withJavadoc()
  )
  
  val main = Project(appName, file(".")).enablePlugins().settings(
    version := appV,
    scalaVersion := scalaV,
    resolvers += Resolver.sonatypeRepo("releases"),  
    libraryDependencies ++= appDependencies,
    scalacOptions ++= Seq("-unchecked", "-deprecation")  
  )
} 

