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

class EncryptionTool(algorithm: TAlgorithm) {

  def encrypt(input: String): String = {
    algorithm.encrypt(input)
  }

  def decrypt(encryptedText: String): String = {
    algorithm.decrypt(encryptedText)
  }
}
