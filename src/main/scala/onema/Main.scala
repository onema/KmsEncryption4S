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

import scala.io.Source

object Main {

  // --- Fields ---
  val log = Logger("KmsEncryption4S")

  def main(args: Array[String]): Unit = {

    val startTime = System.nanoTime()
    val command = new Command
    val cli = command.getParser(args)
    val key = cli.getOptionValue("key")
    val data = Option[String](cli.getOptionValue("data"))
    val file = Option[String](cli.getOptionValue("file"))
    val encrypt =cli.hasOption("encrypt")
    val decrypt = cli.hasOption("decrypt")
    val profile = Option[String](cli.getOptionValue("profile")).getOrElse("default")

    // Validate encrypt and decrypt values
    if (encrypt && decrypt) throw new IllegalArgumentException("Either encrypt or decrypt values are required.")
    if (data.isEmpty && file.isEmpty) throw new IllegalArgumentException("You must define data or a file to encrypt/decrypt")

    // Get the data or file
    if (data.isDefined) {
      val encryptedData = processData(encrypt, data.get, key, profile)
      log.info(s"Encrypted data: > \n$encryptedData")
    } else if (file.isDefined) {
      processFile(encrypt, file.get, key, profile)
    }
    log.info(s"TIME ELAPSED: " + (System.nanoTime() - startTime).toDouble/1000000000 + " s")
  }

  // --- Methods ---
  private def processFile(encrypt: Boolean, file: String, key: String, profile: String) = {
    log.info(s"Processing fiel $file")
    val extension = if (encrypt) ".enc" else ".dec"
    val cannonicalPath = new File(file).getAbsolutePath
    val data = Source.fromFile(cannonicalPath).mkString
    val encryptedData = processData(encrypt, data, key, profile)
    val newFile = new File(cannonicalPath + extension)
    val bufferedWriter = new BufferedWriter(new FileWriter(newFile))
    bufferedWriter.write(encryptedData)
    bufferedWriter.close()
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
