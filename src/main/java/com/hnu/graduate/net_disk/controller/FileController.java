package com.hnu.graduate.net_disk.controller;

import com.hnu.graduate.net_disk.model.FileMeterial;
import com.hnu.graduate.net_disk.service.JpbcService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author muyunhao
 * @date 2020/4/18 10:20 下午
 */
@RestController
public class FileController {

    @Resource
    JpbcService jpbcService;

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
