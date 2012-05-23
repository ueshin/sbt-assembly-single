/*
 * Copyright 2012 Happy-Camper Street.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

/**
 * @author ueshin
 *
 */
import sbt._
import Keys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

object SbtAssemblySinglePluginBuild extends Build {

  lazy val root = Project(
    "sbt-assembly-single",
    file("."),
    settings = Defaults.defaultSettings ++ Seq(
      publishArtifact := false
    ),
    aggregate = Seq(core, plugin)
  )

  lazy val core = Project(
    "sbt-assembly-single-core",
    file("core"),
    settings = Defaults.defaultSettings ++ Seq(
      libraryDependencies += "org.codehaus.plexus" % "plexus-archiver" % "2.1.1"
    )
  )

  lazy val plugin = Project(
    "sbt-assembly-single-plugin",
    file("plugin"),
    dependencies = Seq(core),
    settings = Defaults.defaultSettings ++ Seq(
      sbtPlugin := true
    )
  )

  override lazy val settings = super.settings ++ Seq(

    organization := "st.happy_camper.sbt.plugins",
    version := "0.0.1-SNAPSHOT",

    description := "Plugin for sbt to assemble dist files behaving like 'mvn assembly:single'",
    homepage := Some(url("https://github.com/ueshin/sbt-assembly-single")),
    startYear := Some(2012),
    licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),

    organizationName := "Happy-Camper Street",
    organizationHomepage := Some(url("http://happy-camper.st/")),

    EclipseKeys.withSource := true)

}
