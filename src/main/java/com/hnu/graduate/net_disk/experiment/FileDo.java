package com.hnu.graduate.net_disk.experiment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 文件实体类
 * @author muyunhao
 * @date 2020/5/2 12:11 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDo implements Serializable {
    int fileId;
    List<String> kwList;
    String kw;
    String author;
    String desc;
    String fileName;
    String authorName;
    String encContent;
    String originContent;
}
