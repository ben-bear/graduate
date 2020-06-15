package com.hnu.graduate.net_disk.experiment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author muyunhao
 * @date 2020/6/15 3:49 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileTreeNode {

    FileTreeNode left;

    FileTreeNode right;

    Map<String, Boolean> subIndexMap;

    boolean isLeaf;


}
