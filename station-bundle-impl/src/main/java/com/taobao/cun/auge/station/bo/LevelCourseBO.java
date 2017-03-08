package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.dal.domain.LevelCourse;
import com.taobao.cun.auge.dal.domain.LevelCourseExample;

public interface LevelCourseBO {

    boolean saveLevelCourse(LevelCourse levelCourse);
    
    List<LevelCourse> queryLevelCourse(LevelCourseExample condition);
    
    boolean deleteLevelCourse(String courseCode);
    
    List<LevelCourse> groupCoursesByTag(int topN);
}
