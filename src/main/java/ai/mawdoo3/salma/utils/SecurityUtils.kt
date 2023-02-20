package ai.mawdoo3.salma.utils

import ai.mawdoo3.salma.BuildConfig
import android.content.Context
import android.util.Base64
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object SecurityUtils {

    //algorithm
    private const val RSA_ALGORITHM = "RSA"

    //algorithm/mode/padding
    private const val RSA_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"

    fun rsaEncrypt(textToEncrypt: String, c: Context): String {
        return try {
            //Encrypt Message
            val cipher: Cipher = Cipher.getInstance(RSA_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getPemPublicKey(c))
            val encryptedBytes: ByteArray = cipher.doFinal(textToEncrypt.toByteArray())
            Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
                .replace(System.lineSeparator().toRegex(), "")
        } catch (e: Exception) {
            textToEncrypt
        }
    }

    @Throws(java.lang.Exception::class)
    private fun getPemPublicKey(c: Context): PublicKey? {
        val pemFile =
            c.resources.assets.open(BuildConfig.MASA_PUB_KEY_FILE_NAME).bufferedReader().use {
                it.readText()
            }
        val publicKeyPEM = pemFile.replace("-----BEGIN PUBLIC KEY-----", "")
            .replace(System.lineSeparator().toRegex(), "")
            .replace("-----END PUBLIC KEY-----", "")
        val decoded: ByteArray = Base64.decode(publicKeyPEM, Base64.DEFAULT)
        val spec = X509EncodedKeySpec(decoded)
        val kf = KeyFactory.getInstance(RSA_ALGORITHM)
        return kf.generatePublic(spec)
    }
}