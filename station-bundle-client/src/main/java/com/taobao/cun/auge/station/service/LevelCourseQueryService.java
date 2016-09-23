package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.condition.LevelCourseCondition;
import com.taobao.cun.auge.station.dto.LevelCourseLearningDto;
import com.taobao.cun.auge.station.dto.LevelCourseLearningStatisticsDto;

/**
 * 层级课程查询服务
 * @author xujianhui 2016年9月22日 下午2:43:01
 */
public interface LevelCourseQueryService {

    /**
     * 根据condition查询层级课程列表
     * @param condition
     * @return
     */
    List<LevelCourseLearningDto> queryLevelCourse(LevelCourseCondition condition);
    
    LevelCourseLearningStatisticsDto queryStatisticsInfo(Long userId, String userLevel);
}
