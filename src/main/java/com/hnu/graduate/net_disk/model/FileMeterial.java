package com.hnu.graduate.net_disk.model;

import lombok.Data;

import java.util.Date;

/**
 * @author muyunhao
 * @date 2020/4/24 3:33 下午
 */
@Data
public class FileMeterial {
    Long id;
    String fileId;
    String keyword;
    String author;
    String itemName;
    String descri;
    String tSet;
    String xSet;

    Date mtime;
}
