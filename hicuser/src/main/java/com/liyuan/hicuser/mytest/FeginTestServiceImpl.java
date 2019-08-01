package com.liyuan.hicuser.mytest;

import org.springframework.stereotype.Component;

/**
 * Created by weiyuan on 2019/7/23/023.
 */
@Component
public class FeginTestServiceImpl implements FeginTestService {
    @Override
    public String testFegin() {
        return "哎。。有异常。。";
    }
}
