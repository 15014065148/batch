package com.eveb.saasops.batch.sys.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

public class AesUtil {

    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(encrypt(content, encryptKey));
    }
    /**
     * AES加密为base 64 code  32位密钥
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥32位
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt32(String content, String encryptKey) throws Exception {
        return base64Encode(encrypt32(content, encryptKey));
    }

    /**
     * AES加密为hex code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesHexEncrypt(String content, String encryptKey) throws Exception {
        return hexEncode(encrypt(content, encryptKey));
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return encryptStr == null ? null : decrypt(base64Decode(encryptStr), decryptKey);
    }
    /**
     * 将base 64 code AES解密 32位密钥
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt32(String encryptStr, String decryptKey) throws Exception {
        return encryptStr == null ? null : decrypt32(base64Decode(encryptStr), decryptKey);
    }

    /**
     * hex encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的hex code
     */
    public static String hexEncode(byte[] bytes){
        return Hex.encodeHexString(bytes);
    }

    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return new Base64().encodeToString(bytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception {
        return base64Code == null ? null : new Base64().decode(base64Code);
    }

    /**
     * 加密
     *
     * @param content
     * @param strKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String content, String strKey) throws Exception {
        SecretKeySpec skeySpec = getKey(strKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(strKey.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return encrypted;
    }
    /**
     * 加密32位密钥
     *
     * @param content
     * @param
     * @return
     * @throws Exception
     */
    public static byte[] encrypt32(String content, String key_32) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key_32.getBytes("utf-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypt = cipher.doFinal(content.getBytes(), 0, content.getBytes().length);
        return encrypt;
    }

    /**
     * 解密
     *
     * @param strKey
     * @param content
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] content, String strKey) throws Exception {
        SecretKeySpec skeySpec = getKey(strKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(strKey.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(content);
        String originalString = new String(original);
        return originalString;
    }
    /**
     * 解密32位
     *
     * @param
     * @param content
     * @return
     * @throws Exception
     */
    public static String decrypt32(byte[] content, String Key_32) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(Key_32.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);//使用解密模式初始化 密钥
        byte[] decrypt = cipher.doFinal(content);
        String originalString = new String(decrypt,"utf-8");
        return originalString;
    }

    private static SecretKeySpec getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");
        return skeySpec;
    }
}
