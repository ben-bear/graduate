package com.hnu.graduate.net_disk.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hnu.graduate.net_disk.experiment.FileDo;
import com.hnu.graduate.net_disk.experiment.FileTreeNode;
import com.hnu.graduate.net_disk.experiment.RandomUtils;
import com.hnu.graduate.net_disk.model.FileMeterial;
import com.hnu.graduate.net_disk.service.FileService;
import com.hnu.graduate.net_disk.service.JpbcService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 系统controller，接受来自前端的http请求。实现CRUD
 * @author muyunhao
 * @date 2020/4/18 10:20 下午
 */
@RestController
public class FileController {

    @Resource
    JpbcService jpbcService;

    @Resource
    FileService fileService;

    /**
     * 根据字串查询文件
     * @param kw
     * @return
     */
    @RequestMapping("/query")
    @CrossOrigin
    public List<FileDo> queryController(@RequestParam String kw) {
        System.out.println(kw);
        return fileService.query(kw);
    }

    /**
     * 上传文件
     * @param fileDo
     */
    @PostMapping("/insert")
    @CrossOrigin
    public void insertController(@RequestBody FileDo fileDo) throws Exception {
        fileService.insert(fileDo);
    }

    /**
     * 输入树形式文件
     */
    @RequestMapping("/console/tree-view")
    public String testController() {
        FileTreeNode root = fileService.treeView();

        return traverseTreePerlevel(root);
    }

    /**
     * 层序遍历
     */
    private String traverseTreePerlevel(FileTreeNode node) {
        StringBuilder res = new StringBuilder();
        Queue<FileTreeNode> queue = new LinkedList<>();
        queue.offer(node);
        int level = 1;
        int sz = queue.size();
        while (sz != 0) {
            StringBuilder outputStr = new StringBuilder();
            outputStr.append("【Level").append(level).append("】");
            for (int i = 0; i < sz; i++) {
                FileTreeNode head = queue.poll();
                assert head != null;

                Map<String, Boolean> mm = Maps.newHashMap();
                head.getSubIndexMap().entrySet().forEach(entry -> {
                    mm.put(RandomUtils.md5(entry.getKey()), entry.getValue());
                });
                // 如果需要明文展示，将下方#{mm}换成head.getSubIndexMap()
                outputStr.append(mm).append("   ");
                if (head.getLeft() != null) {
                    queue.offer(head.getLeft());
                }
                if (head.getRight() != null) {
                    queue.offer(head.getRight());
                }
            }
            level++;
            res.append(outputStr.toString()).append("\r\n");
            sz = queue.size();
        }
        System.out.println("【文件树打印】");
        System.out.println(res);
        return res.toString();
    }

    /**
     * 中序遍历
     * @param node
     */
    private void traverseTree(FileTreeNode node) {
        if (node == null) {
            return;
        }
        traverseTree(node.getLeft());
        System.out.println(node.getSubIndexMap() + "/n");
        traverseTree(node.getRight());
    }

    @RequestMapping("/test")
    public void testController(int i, int k) {
        jpbcService.setUpParams(i, k);
    }

    @PostMapping("/uploadText")
    public void uploadTextController(FileMeterial file) {
        jpbcService.insertFile(file);
    }

    @GetMapping("/delete/{fileId}")
    public void delete(@PathVariable("fileId") Integer fileId) {
        fileService.del(fileId);
    }
}
