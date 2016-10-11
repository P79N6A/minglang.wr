package com.taobao.cun.auge.station.service.impl.exam;

import java.util.Map;

import com.taobao.cun.auge.station.service.LevelExamDispatchStrategy;


public class UpgradeLevelExamDispatchStrategy implements LevelExamDispatchStrategy {

    @Override
    public boolean dispatchExamPaper(Long taoUserId, String nickName, Map<String, Object> context) {

        return false;
    }

}
