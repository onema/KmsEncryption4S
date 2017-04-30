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

import com.typesafe.scalalogging.Logger
import onema.Command.CommandArguments
import org.apache.commons.cli
import org.apache.commons.cli.{CommandLine, HelpFormatter, Options}

object Command {
  case class CommandArguments(key: String , data: Option[String], file: Option[String], encrypt: Boolean, decrypt: Boolean, profile: String)
  private val key = cli.Option.builder("k")
    .longOpt("key")
    .argName("key")
    .desc("KMS CMK key ARN or alias.")
    .hasArgs
    .required
    .build
  private val data = cli.Option.builder("d")
    .longOpt("data")
    .argName("data")
    .desc("Data to encrypt or decrypt. This will output an encrypted or decrypted string.")
    .hasArg
    .required(false)
    .build
  private val file = cli.Option.builder("f")
    .longOpt("file")
    .argName("file")
    .desc("File to encrypt or decrypt. This will outpu an encrypted or decrypted file.")
    .hasArg
    .required(false)
    .build
  private val profile = cli.Option.builder("p")
    .longOpt("profile")
    .argName("profile")
    .desc("AWS Profile to use.")
    .hasArg
    .required(false)
    .build
  private val encrypt = new cli.Option("E", "encrypt", false, "Data or file should be encrypted.")
  private val decrypt = new cli.Option("D", "decrypt", false, "Data or file should be decrypted.")

  // Help options
  val help = new cli.Option("h", "help", false, "print this message")
  val version = new cli.Option("v", "version", false, "print the version information and exit")
  val options = new cli.Options
  options
    .addOption(key)
    .addOption(data)
    .addOption(file)
    .addOption(profile)
    .addOption(encrypt)
    .addOption(decrypt)
    .addOption(profile)
    .addOption(help)
    .addOption(version)
    .addOption(help)
    .addOption(version)
  val helpOptions = new cli.Options
  helpOptions
    .addOption(help)
    .addOption(version)
}

class Command {

  // --- Fields ---
  val log = Logger("Command")
  val parser = new cli.DefaultParser

  // --- Methods ---
  def parse(args: Array[String]): CommandArguments = {
    try {
      displayHelp(args)
      val cmd = parser.parse(Command.options, args)
      val arguments = CommandArguments(
        key = cmd.getOptionValue("key"),
        data = Option[String](cmd.getOptionValue("data")),
        file = Option[String](cmd.getOptionValue("file")),
        encrypt = cmd.hasOption("encrypt"),
        decrypt = cmd.hasOption("decrypt"),
        profile = Option[String](cmd.getOptionValue("profile")).getOrElse("default")
      )
      validate(arguments)
      arguments
    } catch {
      case e: Exception =>
        log.error(e.getMessage)
        throw e
    }
  }

  private def displayHelp(args: Array[String]) = {
    val cmd = parser.parse(Command.helpOptions, args, true)
    if (cmd.hasOption(Command.help.getOpt) || cmd.hasOption(Command.help.getLongOpt)) {
      val formatter = new HelpFormatter()
      formatter.printHelp("crypto", Command.options)
      System.exit(0)
    }
  }

  private def validate(arguments: CommandArguments) = {
    if (arguments.encrypt && arguments.decrypt) throw new IllegalArgumentException("Either \"encrypt\" or \"decrypt\" values are required.")
    if (arguments.data.isEmpty && arguments.file.isEmpty) throw new IllegalArgumentException("You must define \"data\" or  \"file\" to encrypt/decrypt")
  }
}
