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

import org.apache.commons.cli

class Command {

  private val key = cli.Option.builder("key")
    .argName("key")
    .desc("KMS CMK key ARN or alias.")
    .hasArgs
    .required
    .build
  private val data = cli.Option.builder("data")
    .argName("decrypt")
    .desc("Data to encrypt or decrypt. This will output an encrypted or decrypted string.")
    .hasArg
    .required(false)
    .build
  private val file = cli.Option.builder("file")
    .argName("file")
    .desc("File to encrypt or decrypt. This will outpu an encrypted or decrypted file.")
    .hasArg
    .required(false)
    .build
  private val profile = cli.Option.builder("profile")
    .argName("profile")
    .desc("AWS Profile to use")
    .hasArg
    .required(false)
    .build
  private val encrypt = new cli.Option("encrypt", "Data or file should be encrypted.")
  private val decrypt = new cli.Option("decrypt", "Data or file should be decrypted.")

  def getParser(args: Array[String]): cli.CommandLine = {
    val options = new cli.Options()
    options
      .addOption(key)
      .addOption(data)
      .addOption(file)
      .addOption(encrypt)
      .addOption(decrypt)
      .addOption(profile)
    try {
      val parser = new cli.DefaultParser
      val commandLine = parser.parse(options, args)
      commandLine
    } catch {
      case e: Exception =>
        println(e.getMessage)
        throw e
    }
  }
}
