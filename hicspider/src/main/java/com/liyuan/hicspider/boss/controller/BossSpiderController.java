package com.liyuan.hicspider.boss.controller;

import com.alibaba.fastjson.JSON;
import com.liyuan.hicspider.boss.service.impl.BossSpiderServiceImpl;
import com.liyuan.hicspider.threadexception.HanlderThreadFactory;
import com.liyuan.hicspider.util.BossSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by weiyuan on 2019/8/5/005.
 */
@RestController
@RequestMapping(value = "/boss")
@Slf4j
public class BossSpiderController {
    @Autowired
    @Qualifier("bossSpiderServiceImpl")
    private BossSpiderServiceImpl bossSpiderService;


    @RequestMapping(value = "/gongsi")
    public String gongsi() {
        System.out.println(bossSpiderService.getcitypath(null));
        return "";
    }

    /**
     * 获取城市
     */
    @GetMapping("/spidercity")
    public String spiderCity() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 1);
        resultMap.put("msg", "成功");
        //生产数据
        List<HashMap<String, Object>> cityList = new ArrayList<>();
        BossSpider bossSpider = new BossSpider(cityList);
        bossSpider.spiderBossCity("https://www.zhipin.com/gongsi/?ka=header_brand");

        try {

            bossSpiderService.addCity(cityList);
        } catch (Exception e) {
            log.error("爬取BOSS城市异常", e);
            resultMap.put("code", -1);
            resultMap.put("msg", "爬取BOSS城市异常");
        }
        return JSON.toJSONString(resultMap);
    }

    @GetMapping("/spiderGongsi")
    public String spiderGongsi() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 1);
        resultMap.put("msg", "成功");

        //查询城市
        List<HashMap<String, Object>> cityList = bossSpiderService.getcitypath(null);
        CountDownLatch latch = new CountDownLatch(cityList.size());
        int process = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(process, new HanlderThreadFactory());
        //生产数据
        List<HashMap<String, Object>> gongsiList = new ArrayList<>();
        for (int i = 0; i < cityList.size(); i++) {
            HashMap<String, Object> cityMap = cityList.get(i);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String citycode = cityMap.get("CITYCODE").toString();
                    String href = cityMap.get("HREF").toString();
                    BossSpider bossSpider = new BossSpider(gongsiList);
                    bossSpider.cityGongsi(BossSpider.bossUrl + href, citycode);
                    if (gongsiList.size() > 0) {
                        bossSpiderService.addgongsi(gongsiList);
                    } else {
                        log.error("无公司数据,citycode:" + citycode);

                    }
                }
            });

        }
        return JSON.toJSONString(resultMap);
    }


}
