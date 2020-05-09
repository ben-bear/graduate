package com.hnu.graduate.net_disk.service;

import com.google.common.collect.Lists;
import com.hnu.graduate.net_disk.experiment.FileDo;
import com.hnu.graduate.net_disk.experiment.TDo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author muyunhao
 * @date 2020/5/9 12:23 下午
 */
@Service
public class FileService {

    static List<FileDo> fileList = Lists.newArrayList();

    @PostConstruct
    public void init(){
        fileList.add(FileDo.builder().author("王二虎").authorName("王二虎").kw("cloud").kwList(Lists.newArrayList("cloud, love")).desc("云计算安全").fileId(12).fileName("云计算安全").build());
        fileList.add(FileDo.builder().author("王二虎").authorName("王二虎").kw("love").kwList(Lists.newArrayList("cloud, love")).desc("云计算安全").fileId(12).fileName("云计算安全").build());
        fileList.add(FileDo.builder().author("王二虎").authorName("王二虎").kw("test").kwList(Lists.newArrayList("cloud, love")).desc("云计算安全").fileId(12).fileName("云计算安全").build());
    }



    public List<FileDo> query(String kw) {
        if (StringUtils.isEmpty(kw)) {
            return fileList;
        }
        return fileList.stream().filter(fileDo -> fileDo.getKw().contains(kw)).collect(Collectors.toList());
    }

    public void insert(FileDo tDo) {
        fileList.add(tDo);
    }
}
