package com.liyuan.hiclogio.mytest;

import org.springframework.stereotype.Component;

/**
 * Created by weiyuan on 2019/7/6/006.
 */
@Component
public class SchedualServiceHiImpl implements SchedualServiceHi{
    @Override
    public String testFeign() {
        return "狗屎的居然异常了。。";
    }
}
