package com.hnu.graduate.net_disk.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hnu.graduate.net_disk.model.FileMeterial;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author muyunhao
 * @date 2020/4/18 10:37 下午
 */
@Service
public class JpbcService {

    /**
     * g1、g2的byte生成数组
     */
    private static final byte[] G1_BYTE_ARR = new byte[] {47, -87, -37, -95, 43, 8, -123, 39, 121, -127, -60, -13, 10, -108, -8, -53, -64, 23, -3, -71, -92, -16, 98, 62, 36, 94, 61, -19, -85, 80, 96, 18, -22, -43, -11, -119, -25, -125, 111, 60, -13, 19, 73, 28, 9, -96, 101, 42, -43, -111, -2, -121, 63, 47, 101, -35, -118, 2, -27, 90, 43, 47, 48, -122, 80, -56, 35, -42, -57, -118, -121, -47, 102, -29, -85, -33, -59, 1, 119, -32, 33, 7, -52, 13, 106, -18, -121, 87, -66, 116, -50, 99, -30, -40, -116, -29, -125, -44, 44, -50, -78, 49, -49, -103, -12, 116, 91, -94, 22, -125, 27, 33, 73, 91, -84, -86, 29, 107, 107, -17, -74, -81, -66, -64, 48, 0, 1, 115};
    private static final byte[] G2_BYTE_ARR = new byte[] {125, 47, -69, 27, 6, -112, 119, 4, -65, 50, 84, -37, 100, 86, -100, -51, 104, 49, -46, -47, 98, -32, 79, -18, 60, -15, 28, 85, 46, -7, -103, -105, -3, -19, -88, -41, 17, -108, 82, -39, 69, -71, -41, -79, -34, -100, 95, -119, 19, -22, 127, -9, -95, -10, 120, 21, -62, 49, -17, -128, -28, 41, 122, 54, 20, -94, -52, 40, 127, 54, -55, 80, 30, 113, 93, 64, -113, -38, -65, 51, 42, 27, 8, 119, -42, 118, -70, 59, -60, 63, -121, -14, 99, 58, -35, 68, 14, -67, 83, -92, 79, -19, -117, -29, -106, -76, 75, 28, 16, -43, -100, 127, 54, -2, -118, -28, 27, 58, 52, 7, 69, -47, -82, -61, -85, 46, -28, 16};
    private static final Pairing pairing;
    private static final Element e1;
    private static final Element e2;

    static {
        pairing = PairingFactory.getPairing("a.properties");
        PairingFactory.getInstance().setUsePBCWhenPossible(true);
        Field f1 = pairing.getG1();
        e1 = f1.newElementFromBytes(G1_BYTE_ARR);
        e2 = f1.newElementFromBytes(G2_BYTE_ARR);
    }

    public void setUpParams(int i, int k) {
        Element result1 = pairing.pairing(e1, e2).pow(new BigInteger(String.valueOf(i)).multiply(new BigInteger(String.valueOf(k)))).getImmutable();
        Element result2 = pairing.pairing(e1.pow(new BigInteger(String.valueOf(i))).getImmutable(), e2.pow(new BigInteger(String.valueOf(k))).getImmutable()).getImmutable();
        System.out.println(result1.isEqual(result2));
    }


    public void insertFile(FileMeterial fileMeterial) {
        List<String> keywordList = Arrays.asList(fileMeterial.getKeyword().split(","));
        Map<Integer, List<Element>> tSet = genTSet(keywordList);
        Set<Element> xSet = genXSet(tSet);
        System.out.println("tset:" + tSet);
        System.out.println("xset:" + xSet);
    }

    public Map<Integer, List<Element>> genTSet(List<String> keywordList) {
        Map<Integer, List<Element>> tSet = Maps.newHashMap();
        for (String word: keywordList) {
            int index = 0;
            if (word.length() >= 2) {
                // 拆分关键字为字串，放入tSet
                while(index <= word.length() - 2) {
                    String subStr = word.substring(index, index + 2);
                    List<Element> list = tSet.getOrDefault(str2Integer(subStr), Lists.newArrayList());
                    list.add(e2.getImmutable().pow(new BigInteger(String.valueOf(index))).getImmutable());
                    tSet.put(str2Integer(subStr), list);
                    index ++;
                }
            }
        }
        return tSet;
    }

    public Set<Element> genXSet(Map<Integer, List<Element>> tSet) {
        Set<Element> xSet = Sets.newHashSet();
        // 生成xSet
        tSet.forEach((fp, posList) ->
                posList.forEach(pos -> {
                    Element r1 = e1.getImmutable().pow(new BigInteger(String.valueOf(fp))).getImmutable();
                    Element r2 = pos.getImmutable();
                    Element result = pairing.pairing(r1.getImmutable(), r2.getImmutable()).getImmutable();
                    xSet.add(result);
                })
        );
        return xSet;
    }

    private Integer str2Integer(String str) {
//        System.out.println("str:" + str + "  val: "+ str.chars().sum());
        return str.chars().sum();
    }

}
