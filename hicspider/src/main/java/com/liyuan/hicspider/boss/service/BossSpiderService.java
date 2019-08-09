package com.liyuan.hicspider.boss.service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by weiyuan on 2019/8/5/005.
 */
public interface BossSpiderService {
    public List<HashMap<String, Object>> getcitypath(String citycode);
    /**
     * 保存获取到的城市信息
     * @param
     */
    public void addCity(List<HashMap<String, Object>> cityList);

    /**
     * 增加公司
     * @param gongsiList
     */
    public void addgongsi(List<HashMap<String,Object>> gongsiList);
    /**
     * 删除公司
     * @param citycode
     */
    public void delGongs(String citycode);
}
