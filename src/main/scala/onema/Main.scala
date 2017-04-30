/**
  * This file is part of the ONEMA KmsEncryption4S Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2017, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <kinojman@gmail.com>
  */

package onema

import java.io.{BufferedWriter, File, FileWriter}

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.encryptionsdk.{AwsCrypto, MasterKeyProvider}
import com.amazonaws.encryptionsdk.kms.{KmsMasterKey, KmsMasterKeyProvider}
import com.amazonaws.regions._
import com.typesafe.scalalogging.Logger
import onema.Command.CommandArguments

import scala.io.Source

object Main {

  // --- Fields ---
  val log = Logger("crypto")
  private var arguments: CommandArguments = _

  def main(args: Array[String]): Unit = {
    val startTime = System.nanoTime()
    try {
      val command = new Command
      arguments = command.parse(args)
    } catch {
      case _: Exception =>
        System.exit(1)
    }

    // Get the data or file
    if (arguments.data.isDefined) {
      val encryptedData = processData(arguments.encrypt, arguments.data.get, arguments.key, arguments.profile)
      println(s"Data: > \n$encryptedData")
    } else if (arguments.file.isDefined) {
      processFile(arguments.encrypt, arguments.file.get, arguments.key, arguments.profile)
    }
    log.info(s"TIME ELAPSED: " + (System.nanoTime() - startTime).toDouble/1000000000 + " s")
  }

  // --- Methods ---
  private def processFile(encrypt: Boolean, file: String, key: String, profile: String) = {
    log.info(s"Processing file $file")
    val extension = if (encrypt) ".enc" else ".dec"
    val cannonicalPath = new File(file).getAbsolutePath
    val data = Source.fromFile(cannonicalPath).mkString
    val encryptedData = processData(encrypt, data, key, profile)
    val newFile = new File(cannonicalPath + extension)
    val bufferedWriter = new BufferedWriter(new FileWriter(newFile))
    bufferedWriter.write(encryptedData)
    bufferedWriter.close()
    log.info(s"File saved to: ${cannonicalPath + extension}")
  }

  private def processData(encrypt: Boolean, data: String, key: String, profile: String): String = {
    log.info("Processing data")
    val encryptionTool = new EncryptionTool(getAlgorithm(key, profile))
    run(encryptionTool, encrypt, data)
  }

  private def run(encryptionTool: EncryptionTool, encrypt: Boolean, data: String): String = {

    // Perform encryption/decryption
    if (encrypt) {
      log.info("Encrypting data")
      encryptionTool.encrypt(data)
    } else {
      log.info("Decrypting data")
      encryptionTool.decrypt(data)
    }
  }

  private def getAlgorithm(alias: String, profile: String) = {

    // Create objects required by the AwsEncryptionSdkAlgorithm
    val provider = getProviderFromProfile(profile, alias)
    new AwsEncryptionSdkAlgorithm(new AwsCrypto, provider)
  }

  private def getProviderFromProfile(profile: String, alias: String): MasterKeyProvider[KmsMasterKey] = {

    // Get credentials and region from profile
    val region = new AwsProfileRegionProvider(profile).getRegion
    val credentialsProvider = new ProfileCredentialsProvider(profile)
    val provider = new KmsMasterKeyProvider(credentialsProvider, alias)
    provider.setRegion(RegionUtils.getRegion(region))
    provider
  }
}
