package com.liyuan.hicspider.boss.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * Created by weiyuan on 2019/8/1/001.
 */
@Mapper
@Component
public interface BossSpiderMapper {
    public List<HashMap<String, Object>> getcitypath(String citycode);

    /**
     * 保存获取到的城市信息
     * @param addMap
     */
    public void addCity(HashMap<String, Object> addMap);

    /**
     * 清空
     * @return
     */
    public int delCity(@Param("citycode") String citycode);
    /**
     * 增加公司
     * @param gongsiList
     */
    public void addgongsi(List<HashMap<String,Object>> gongsiList);
    /**
     * 删除公司
     * @param citycode
     */
    public void delGongs(@Param("citycode") String citycode);
}
