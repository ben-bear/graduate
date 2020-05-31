package com.hnu.graduate.net_disk.service;

import com.google.common.collect.Lists;
import com.hnu.graduate.net_disk.experiment.BuildIndex;
import com.hnu.graduate.net_disk.experiment.FileDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
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
    public void insert(FileDo tDo) {
        log.info("上传文件内容为:{}", tDo);
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
}
