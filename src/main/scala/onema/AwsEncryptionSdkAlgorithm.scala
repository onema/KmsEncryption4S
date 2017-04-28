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

import scala.collection.JavaConverters._
import com.amazonaws.encryptionsdk.{AwsCrypto, MasterKeyProvider}
import com.amazonaws.encryptionsdk.kms.KmsMasterKey

class AwsEncryptionSdkAlgorithm(crypto: AwsCrypto, provider: MasterKeyProvider[KmsMasterKey]) extends TAlgorithm {

  override def encrypt(plainText: String, context: Map[String, String]): String = {
    val cipherText = crypto.encryptString(provider, plainText, context.asJava)
    cipherText.getResult
  }

  override def decrypt(encryptedText: String, context: Map[String, String]): String = {
    val decryptResults = crypto.decryptString(provider, encryptedText)
    decryptResults.getResult
  }
}
