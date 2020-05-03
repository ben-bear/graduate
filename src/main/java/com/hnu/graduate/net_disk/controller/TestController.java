package com.hnu.graduate.net_disk.controller;

import com.hnu.graduate.net_disk.experiment.BuildIndex;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author muyunhao
 * @date 2020/5/3 6:13 下午
 */
@RestController
public class TestController {

    @Resource
    BuildIndex buildIndex;

    @RequestMapping("/test/exp")
    public void test() {
        buildIndex.buildIndexesForFile();
        buildIndex.query("loud");
    }

}
