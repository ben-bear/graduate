package com.hnu.graduate.net_disk.experiment;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * @author muyunhao
 * @date 2020/5/2 12:07 下午
 */
public class RandomUtils {

    /**
     * AES加密算法种子
     */
    static String KEY = "abcdef0123456789";
    static String IV = "0123456789abcdef";

    /**
     * 伪随机函数ft。用var1，var2的字符asi码之和作为随机结果,之后用对其md5值取模
     * @param v1 var1
     * @param v2 var2
     * @return 随机数
     */
    static Integer ft(String v1, String v2) {
        return Optional.ofNullable(md5(String.valueOf(v1.chars().sum() + v2.chars().sum()))).map(s -> s.chars().sum() / 1000).orElse(0);
    }

    /**
     * 伪随机函数fp。用var1，var2的字符asi码之和*2,作为随机结果,之后用对其md5值取模
     * @param v1 var1
     * @param v2 var2
     * @return 随机数
     */
    static Integer fp(String v1, String v2) {
        return Optional.ofNullable(md5(String.valueOf((v1.chars().sum() + v2.chars().sum()) * 2))).map(s -> s.chars().sum() / 1000).orElse(0);
    }

    /**
     * 加密函数enc。用var1，var2的字符asi码之和*3,作为随机结果，之后用对其md5值取模
     * @param v1 var1
     * @param v2 var2
     * @return 随机数
     */
    static Integer enc(String v1, String v2) {
        return Optional.ofNullable(md5(String.valueOf((v1.chars().sum() + v2.chars().sum()) * 3))).map(s -> s.chars().sum() / 1000).orElse(0);
    }

    public static String encryptAES(String data) throws Exception {
        try {
            //参数分别代表 算法名称/加密模式/数据填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPadding");
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new sun.misc.BASE64Encoder().encode(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptAES(String data) throws Exception {
        try {
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NOPadding");
            SecretKeySpec keyspec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String md5(String s) {
        try {
            MessageDigest m;
            m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            return new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
