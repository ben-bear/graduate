package com.hnu.graduate.net_disk.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hnu.graduate.net_disk.experiment.BuildIndex;
import com.hnu.graduate.net_disk.experiment.FileDo;
import com.hnu.graduate.net_disk.experiment.FileTreeNode;
import com.hnu.graduate.net_disk.experiment.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author muyunhao
 * @date 2020/5/9 12:23 下午
 */
@Service
@Slf4j
public class FileService {

    @Resource
    BuildIndex buildIndex;

    static List<FileDo> fileList = Lists.newArrayList();

    /**
     * 原始数据初始化
     */
    @PostConstruct
    public void init(){
        fileList.add(FileDo.builder().author("王二虎").authorName("王二虎").kw("cloud").kwList(Lists.newArrayList("cloud, love")).desc("云计算安全").fileId(12).fileName("云计算安全").build());
        fileList.add(FileDo.builder().author("王二虎").authorName("王二虎").kw("love").kwList(Lists.newArrayList("cloud, love")).desc("云计算安全").fileId(13).fileName("云计算安全").build());
        fileList.add(FileDo.builder().author("王二虎").authorName("王二虎").kw("test").kwList(Lists.newArrayList("cloud, love")).desc("云计算安全").fileId(14).fileName("云计算安全").build());
    }

    /**
     * 根据关键字字串进行查询
     * @param kw 关键字字串
     * @return
     */
    public List<FileDo> query(String kw) {
        log.info("client端传入的查询关键字为:{}", kw);
        if (StringUtils.isEmpty(kw)) {
            return fileList;
        }
        return fileList.stream().filter(fileDo -> fileDo.getKw().contains(kw)).collect(Collectors.toList());
    }

    /**
     * 上传文件
     * @param tDo 文件实体类
     */
    public void insert(FileDo tDo) throws Exception {
        log.info("上传文件内容为:{}", tDo);
        String content = tDo.getOriginContent();
        String encContent = RandomUtils.encryptAES(content);
        tDo.setEncContent(encContent);
        log.info("加密前的文件内容:{}, 加密后的文件内容:{}", tDo.getOriginContent(), tDo.getEncContent());
        fileList.add(tDo);
        // 在client端上传文件后，重新构建文件加密索引
        BuildIndex buildIndex = new BuildIndex();
        buildIndex.buildIndexesForFile();
    }

    /**
     * 删除文件
     */
    public void del(Integer fileId) {
        log.info("想要删除的文件id为:{}", fileId);
        fileList.removeIf(f -> f.getFileId() == fileId);
    }

    /**
     * 返回root
     */
    public FileTreeNode treeView() {
        Set<String> subStringSet = Sets.newHashSet();
        Queue<FileTreeNode> queue = new LinkedList<>();
        // 所有字串集合
        fileList.forEach(fileDo -> {
            String kw = fileDo.getKw();
            for (int i = 0; i < kw.length() - 1; i++) {
                subStringSet.add(kw.substring(i, i + 2));
            }
        });
        fileList.forEach(fileDo -> {
            FileTreeNode fileTreeNode = new FileTreeNode();
            fileTreeNode.setLeft(null);
            fileTreeNode.setRight(null);
            fileTreeNode.setLeaf(true);
            String kw = fileDo.getKw();
            Map<String, Boolean> indexMap = Maps.newHashMap();
            for (int i = 0; i < kw.length() - 1; i++) {
                indexMap.put(kw.substring(i, i + 2), true);
            }
            fileTreeNode.setSubIndexMap(indexMap);
            queue.offer(fileTreeNode);
        });

        // 算出最底层叶子节点的个数
        int sz = queue.size();
        int level = 0;
        int extraNum = 0;
        for (int k = 1; k < 10; k++) {
            if (Math.pow(2, k) >= sz) {
                level = k + 1;
                extraNum = (int) ((Math.pow(2, k) - sz) * 2);
                break;
            }
        }
        log.info("Tree level:" + level);

        // 构造最底层的叶子结点
        for (int k = 0; k < extraNum; k++) {
            FileTreeNode leftNode = queue.poll();
            FileTreeNode rightNode = queue.poll();
            FileTreeNode curNode = new FileTreeNode();
            curNode.setLeaf(false);
            curNode.setLeft(leftNode);
            curNode.setRight(rightNode);
            Map<String, Boolean> m = Maps.newHashMap();
            m.putAll(leftNode.getSubIndexMap());
            m.putAll(rightNode.getSubIndexMap());
            curNode.setSubIndexMap(m);
            queue.offer(curNode);
            k += 2;
        }

        while (queue.size() != 1) {
            FileTreeNode leftNode = queue.poll();
            FileTreeNode rightNode = queue.poll();
            FileTreeNode curNode = new FileTreeNode();
            curNode.setLeaf(false);
            curNode.setLeft(leftNode);
            curNode.setRight(rightNode);
            Map<String, Boolean> m = Maps.newHashMap();
            m.putAll(leftNode.getSubIndexMap());
            m.putAll(rightNode.getSubIndexMap());
            curNode.setSubIndexMap(m);
            queue.offer(curNode);
        }

        FileTreeNode root = queue.peek();
        return root;
    }
}
