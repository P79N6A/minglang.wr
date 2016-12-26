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
     * 查询当前层级课程学习统计信息以及指标分类课程信息
     */
    LevelCourseLearningStatisticsDto getCourseLearningInfo(LevelCourseCondition condition);
    
    /**
     * 按照层级搜索培训课程
     */
    List<LevelCourseLearningDto> searchCourseLearningInfoByUserLevel(Long userId, String userLevel);
    
    /**
     * 指标分类查找培训课程
     */
    List<LevelCourseLearningDto> searchCourseLearningInfoByTag(Long userId, String tag);
}
