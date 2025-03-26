package com.btm.back.utils

import org.apache.commons.codec.binary.Hex
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec


object AesUtil {

    /**
     * 生成key
     * @return
     */
    fun generateKey(): String? {
        try {
            val keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(SecureRandom())
            val secretKey = keyGenerator.generateKey()
            val byteKey: kotlin.ByteArray? = secretKey.encoded
            return Hex.encodeHexString(byteKey)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * AES加密
     * @param thisKey
     * @param data
     * @return
     */
    fun encode(thisKey: String?, data: String): String? {
        try { // 转换KEY
            val key = SecretKeySpec(Hex.decodeHex(thisKey), "AES")
            //System.out.println(thisKey);
// 加密
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val result: kotlin.ByteArray? = cipher.doFinal(data.toByteArray())
            return Hex.encodeHexString(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * AES解密
     * @param thisKey
     * @param data
     * @return
     */
    fun decode(thisKey: String?, data: String?): String? {
        try { // 转换KEY
            val key = SecretKeySpec(Hex.decodeHex(thisKey), "AES")
            // 解密
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, key)
            val result: kotlin.ByteArray? = cipher.doFinal(Hex.decodeHex(data))
            return result?.let { String(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
