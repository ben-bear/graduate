package com.hnu.graduate.net_disk.experiment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import javafx.util.Pair;
import org.omg.CORBA.INTERNAL;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author muyunhao
 * @date 2020/5/2 12:09 下午
 */
@Service
public class BuildIndex {


    static Integer ks=1;
    static Integer kt=2;
    static Integer ki=3;
    static Integer kx=4;
    static List<FileDo> fileList = Lists.newArrayList();
    static Map<Integer, List<TDo>> tMap = Maps.newHashMap();
    static Set<Integer> xSet = Sets.newHashSet();

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

    static {
        for (int i =0 ; i< 1000; i++) {
            fileList.add(FileDo.builder().fileId(i).kwList(Lists.newArrayList("cloud" + Math.random())).build());
            fileList.add(FileDo.builder().fileId(i).kwList(Lists.newArrayList("clock" + Math.random())).build());
        }
    }

    static {

    }

    public void buildIndexesForFile() {
        Long timeStart = System.currentTimeMillis();
        Map<String, List<Pair<Integer, Integer>>> map = Maps.newHashMap();
        // 建立倒排索引
        fileList.forEach(f -> {
            int fileId = f.getFileId();
            f.getKwList().forEach(kw -> {
                int start = 2;
                while (start <= kw.length()) {
                    int pos = start-1;
                    String subStr = kw.substring(start-2, start);
                    List<Pair<Integer, Integer>> list = map.getOrDefault(subStr, Lists.newArrayList());
                    Pair<Integer, Integer> p = new Pair<>(pos, fileId);
                    list.add(p);
                    map.put(subStr, list);

                    // 生成xtag
                    Integer count = list.size();
                    Integer strap = RandomUtils.ft(String.valueOf(ks), subStr);
                    Integer kzSub = RandomUtils.ft(String.valueOf(strap), "1");
                    Integer keSub = RandomUtils.ft(String.valueOf(strap), "2");
                    Integer kuSub = RandomUtils.ft(String.valueOf(strap), "3");
                    Integer x = RandomUtils.fp(ki.toString(), String.valueOf(fileId));
                    Integer z = RandomUtils.fp(kzSub.toString(), count.toString());
                    Integer u = RandomUtils.fp(kuSub.toString(), count.toString());


                    Integer xtag = RandomUtils.fp(kx.toString(), subStr) * x;
                    xSet.add(xtag);

                    // 生成tSet
                    List<TDo> list2 = tMap.getOrDefault(xtag, Lists.newArrayList());
                    TDo t = TDo.builder()
                            .e(RandomUtils.enc(keSub.toString(), String.valueOf(fileId)))
                            .y(x * (pos / z))
                            .v(x * (pos / u))
                            .build();
                    list2.add(t);
                    tMap.put(xtag, list2);
                    start++;
                }
            });
        });
        Long timeEnd = System.currentTimeMillis();
        System.out.println("builder index cost time:" + (timeEnd - timeStart));
    }

    public void query(String sub) {
        Long timeStart = System.currentTimeMillis();
        int start = 2;
        for (;start <= sub.length();start++) {
            Integer kkkz = RandomUtils.ft(sub.substring(start-2, start), "1");
            Integer kkke = RandomUtils.ft(sub.substring(start-2, start), "2");
            Integer kkku = RandomUtils.ft(sub.substring(start-2, start), "3");

            Integer strap = RandomUtils.ft(String.valueOf(ks), sub.substring(start-2, start));
            Integer kzSub = RandomUtils.ft(String.valueOf(strap), "1");
            Integer keSub = RandomUtils.ft(String.valueOf(strap), "2");
            Integer kuSub = RandomUtils.ft(String.valueOf(strap), "3");
            Integer x = RandomUtils.fp(ki.toString(), String.valueOf(fileList.stream().filter(fileDo -> fileDo.getKwList().contains(sub)).collect(Collectors.toList())));
            Integer z = RandomUtils.fp(kzSub.toString(), "1");
            Integer u = RandomUtils.fp(kuSub.toString(), "1");

            Integer xxxtrap =  RandomUtils.fp(kx.toString(), sub.substring(start-2, start));
            System.out.println(xSet.contains(xxxtrap));
        }
        Long timeEnd = System.currentTimeMillis();
        System.out.println("query index cost time:" + (timeEnd - timeStart));
    }
}
