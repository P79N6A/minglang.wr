package com.taobao.cun.auge.station.service;

import java.util.Map;

public interface LevelExamDispatchStrategy {

    boolean dispatchExamPaper(Long taoUserId, String nickName, Map<String, Object> context);
}
