package com.liyuan.hicspider.boss.service.impl;

import com.liyuan.hicspider.boss.dao.BossSpiderMapper;
import com.liyuan.hicspider.boss.service.BossSpiderService;
import com.liyuan.hicspider.threadexception.HanlderThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by weiyuan on 2019/8/5/005.
 */
@Service("bossSpiderServiceImpl")
public class BossSpiderServiceImpl implements BossSpiderService {
    @Autowired
    private BossSpiderMapper bossSpiderMapper;

    @Override
    public List<HashMap<String, Object>> getcitypath(String citycode) {
        return bossSpiderMapper.getcitypath(citycode);
    }

    @Override
    public void addCity(List<HashMap<String, Object>> cityList) {

        CountDownLatch latch = new CountDownLatch(cityList.size());
        int process = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(process, new HanlderThreadFactory());

        for (int i = 0; i < cityList.size(); i++) {
            HashMap<String, Object> cityMap = cityList.get(i);
            service.execute(new Runnable() {
                @Transactional
                @Override
                public void run() {
                    try {
                        bossSpiderMapper.delCity(cityMap.get("citycode") == null ? null : cityMap.get("citycode").toString());
                        bossSpiderMapper.addCity(cityMap);
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.shutdown();
    }

    @Override
    public void addgongsi(List<HashMap<String, Object>> gongsiList) {
        bossSpiderMapper.addgongsi(gongsiList);
    }

    @Override
    public void delGongs(String citycode) {
        bossSpiderMapper.delGongs(citycode);

    }
}
