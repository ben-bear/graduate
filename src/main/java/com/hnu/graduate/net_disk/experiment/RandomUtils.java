package com.hnu.graduate.net_disk.experiment;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author muyunhao
 * @date 2020/5/2 12:07 下午
 */
public class RandomUtils {

    /**
     * 伪随机函数ft。用var1，var2的字符asi码之和作为随机结果
     * @param v1 var1
     * @param v2 var2
     * @return 随机数
     */
    static Integer ft(String v1, String v2) {
        return v1.chars().sum() + v2.chars().sum();
//        String s = String.valueOf(v1.chars().sum() + v2.chars().sum());
//        return md(s);
    }

    /**
     * 伪随机函数fp。用var1，var2的字符asi码之和*2,作为随机结果
     * @param v1 var1
     * @param v2 var2
     * @return 随机数
     */
    static Integer fp(String v1, String v2) {
        return (v1.chars().sum() + v2.chars().sum()) * 2;
//        String s = String.valueOf((v1.chars().sum() + v2.chars().sum()) * 2);
//        return md(s);
    }

    /**
     * 加密函数enc。用var1，var2的字符asi码之和*3,作为随机结果
     * @param v1 var1
     * @param v2 var2
     * @return 随机数
     */
    static Integer enc(String v1, String v2) {
        return v1.chars().sum() + v2.chars().sum() * 3;
//        String s = String.valueOf((v1.chars().sum() + v2.chars().sum()) * 3);
//        return md(s);
    }

    private static String md(String s) {
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
