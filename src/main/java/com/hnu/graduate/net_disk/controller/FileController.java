package com.hnu.graduate.net_disk.controller;

import com.hnu.graduate.net_disk.experiment.FileDo;
import com.hnu.graduate.net_disk.model.FileMeterial;
import com.hnu.graduate.net_disk.service.FileService;
import com.hnu.graduate.net_disk.service.JpbcService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author muyunhao
 * @date 2020/4/18 10:20 下午
 */
@RestController
public class FileController {

    @Resource
    JpbcService jpbcService;

    @Resource
    FileService fileService;

    @RequestMapping("/query")
    @CrossOrigin
    public List<FileDo> queryController(@RequestParam String kw) {
        System.out.println(kw);
        return fileService.query(kw);
    }

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

    @DeleteMapping("/delete/{alias}")
    public void delete(@PathVariable("alias") String alias) {
        System.out.println(alias);
    }
}
