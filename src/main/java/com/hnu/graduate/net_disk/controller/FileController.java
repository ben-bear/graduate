package com.hnu.graduate.net_disk.controller;

import com.hnu.graduate.net_disk.experiment.FileDo;
import com.hnu.graduate.net_disk.model.FileMeterial;
import com.hnu.graduate.net_disk.service.FileService;
import com.hnu.graduate.net_disk.service.JpbcService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    public void insertController(@RequestBody FileDo fileDo) {
        fileService.insert(fileDo);
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
