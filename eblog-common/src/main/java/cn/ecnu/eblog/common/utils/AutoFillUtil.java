package cn.ecnu.eblog.common.utils;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;

import java.time.LocalDateTime;

public class AutoFillUtil {
    public static void update(BaseDO baseDO){
        baseDO.setUpdateTime(LocalDateTime.now());
    }
    public static void init(BaseDO baseDO){
        baseDO.setCreateTime(LocalDateTime.now());
        baseDO.setUpdateTime(LocalDateTime.now());
    }
}
