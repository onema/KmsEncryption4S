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


trait TAlgorithm {

  val EmptyMap: Map[String, String] = Map[String, String]()

  def encrypt(plainText: String, context: Map[String, String] = EmptyMap): String

  def decrypt(encryptedText: String, context: Map[String, String] = EmptyMap): String
}
