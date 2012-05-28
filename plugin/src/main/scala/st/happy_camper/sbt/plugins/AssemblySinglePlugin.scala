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
package st.happy_camper.sbt.plugins

import sbt._
import Keys._

import scala.xml.XML

import org.codehaus.plexus.archiver.tar.TarArchiver
import org.codehaus.plexus.archiver.zip.ZipArchiver

/**
 * @author ueshin
 *
 */
object AssemblySinglePlugin extends Plugin {

  import AssemblySingleKeys._

  object AssemblySingleKeys {
    val finalName = SettingKey[String]("final-name", "The final name of the dest file.")
    val descriptors = SettingKey[Seq[File]]("descriptors", "A list of descriptor files to generate from.")

    val assemblySingle = TaskKey[Seq[File]]("assembly-single", "Assemble dist files behaving like 'mvn assembly:single'")
  }

  override lazy val settings = Seq(
    finalName <<= (name, version) { (name, version) => name + "-" + version },
    descriptors := Nil,

    assemblySingle <<= assemblySingleTask)

  val assemblySingleTask = (name, version, target, finalName, descriptors) map { (name, version, target, finalName, descriptors) =>
    descriptors.flatMap { descriptor =>
      val xml = XML.loadFile(descriptor)
      val id = (xml \\ "id").text
      val archivers = (xml \\ "format").map(_.text).map { format =>
        val archiver = format match {
          case "zip" => new ZipArchiver
          case "tar" => new TarArchiver
          case "tar.gz" =>
            val archiver = new TarArchiver
            val compressionMethod = new TarArchiver.TarCompressionMethod
            compressionMethod.setValue("gzip")
            archiver.setCompression(compressionMethod)
            archiver
          case "tar.bz2" =>
            val archiver = new TarArchiver
            val compressionMethod = new TarArchiver.TarCompressionMethod
            compressionMethod.setValue("bzip2")
            archiver.setCompression(compressionMethod)
            archiver
        }
        archiver.setDestFile(target / (finalName + "-" + id + "." + format))
        archiver
      }

      archivers.foreach(_.createArchive)
      archivers.map(_.getDestFile)
    }
  }

}
