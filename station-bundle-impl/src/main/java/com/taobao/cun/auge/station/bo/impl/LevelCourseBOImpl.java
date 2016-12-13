package com.taobao.cun.auge.station.bo.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.LevelCourse;
import com.taobao.cun.auge.dal.domain.LevelCourseExample;
import com.taobao.cun.auge.dal.mapper.LevelCourseMapper;
import com.taobao.cun.auge.station.bo.LevelCourseBO;
import com.taobao.vipserver.client.utils.CollectionUtils;

@Component("levelCourseBO")
public class LevelCourseBOImpl implements LevelCourseBO {

    @Autowired
    private LevelCourseMapper   levelCourseMapper;

    @Override
    public boolean saveLevelCourse(LevelCourse levelCourse) {
        if (levelCourse == null || StringUtils.isEmpty(levelCourse.getCourseCode())) {
            return false;
        }
        LevelCourseExample example = new LevelCourseExample();
        example.createCriteria().andCourseCodeEqualTo(levelCourse.getCourseCode());
        List<LevelCourse> courses = levelCourseMapper.selectByExample(example);
        boolean isNotExist = CollectionUtils.isEmpty(courses);
        if (isNotExist) {
            levelCourseMapper.insert(levelCourse);
        } else {
            LevelCourse existCourse = courses.get(0);
            levelCourse.setId(existCourse.getId());
            levelCourse.setGmtModified(new Date());
            levelCourse.setGmtCreate(existCourse.getGmtModified());
            levelCourseMapper.updateByPrimaryKey(levelCourse);
        }
        return true;
    }

    @Override
    public List<LevelCourse> queryLevelCourse(LevelCourseExample example) {
        List<LevelCourse> courses = levelCourseMapper.selectByExample(example);
        return courses;
    }

    @Override
    public boolean deleteLevelCourse(String courseCode) {
        LevelCourseExample example = new LevelCourseExample();
        example.createCriteria().andCourseCodeEqualTo(courseCode);
        return levelCourseMapper.deleteByExample(example)>0;
    }

    @Override
    public List<LevelCourse> groupCoursesByTag(int topN) {
        if(topN<=0){
            return Collections.emptyList();
        }
        return levelCourseMapper.groupCoursesByTag(topN);
    }

}
