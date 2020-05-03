package com.hnu.graduate.net_disk.experiment;

/**
 * @author muyunhao
 * @date 2020/5/2 12:07 下午
 */
public class RandomUtils {

    public static Integer ft(String v1, String v2) {
        return v1.chars().sum() + v2.chars().sum();
    }

    public static Integer fp(String v1, String v2) {
        return (v1.chars().sum() + v2.chars().sum()) * 2;
    }

    public static Integer enc(String v1, String v2) {
        return (v1.chars().sum() + v2.chars().sum()) * 3;
    }
}
