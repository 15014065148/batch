package com.eveb.saasops.batch.sys.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Date;


public class SiteCodeAesUtil {

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码 ,时间
     * @return
     */
    public static byte[] encrypt(String content, String password) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        byte[] byteContent = content.getBytes("utf-8");
        byte[] result = cipher.doFinal(byteContent);
        return result;
    }

    /**
     * 当前时间戳为密码进行编码AES
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content) {
        try {
            String time = String.valueOf(new Date().getTime());
            byte[] code = encrypt(content + time, time);
            return parseByte2HexStr(code) + time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
