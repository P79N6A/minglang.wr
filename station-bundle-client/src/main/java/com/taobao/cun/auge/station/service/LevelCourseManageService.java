package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.LevelCourseEditDto;

public interface LevelCourseManageService {

    /**
     * 保存层级课程信息（新增或者更新）
     */
    boolean saveCourse(LevelCourseEditDto course);
    
    /**
     * 删除一个课程
     */
    boolean deleteCourse(String courseCode);
    
    /**
     * list所有层级晋升的培训课程
     * 课程暂时很少不需要分页，最多输出200条
     */
    List<LevelCourseEditDto> listLevelCourses();
}
