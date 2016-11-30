package com.taobao.cun.auge.station.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ali.com.google.common.collect.Lists;
import com.ali.com.google.common.collect.Sets;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.LevelCourse;
import com.taobao.cun.auge.dal.domain.LevelCourseExample;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.LevelCourseBO;
import com.taobao.cun.auge.station.bo.impl.LevelCourseBOImpl;
import com.taobao.cun.auge.station.condition.LevelCourseCondition;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil;
import com.taobao.cun.auge.station.convert.LevelCourseConvertor;
import com.taobao.cun.auge.station.dto.LevelCourseLearningDto;
import com.taobao.cun.auge.station.dto.LevelCourseLearningStatisticsDto;
import com.taobao.cun.auge.station.dto.PartnerOnlinePeixunDto;
import com.taobao.cun.auge.station.enums.LevelCourseTypeEnum;
import com.taobao.cun.auge.station.service.LevelCourseQueryService;
import com.taobao.cun.auge.station.service.PartnerPeixunService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("levelCourseQueryService")
@HSFProvider(serviceInterface = LevelCourseQueryService.class)
public class LevelCourseQueryServiceImpl implements LevelCourseQueryService {

    private static final Logger logger = LoggerFactory.getLogger(LevelCourseBOImpl.class);

    @Autowired
    private LevelCourseBO courseBo;
    
    @Autowired
    private AppResourceBO appResourceBo;
    
    @Autowired
    private PartnerPeixunService partnerPeixunService;
    
    /**
     * 获取合伙人必修课程学习数据
     */
    @Override
    public LevelCourseLearningStatisticsDto getCourseLearningInfo(LevelCourseCondition condition) {
        if(!LevelCourseCondition.isValid(condition)){
            return LevelCourseLearningStatisticsDto.getNullDto();
        }
        String key = LevelCourseConfigUtil.getResourceKey(condition.getUserLevel(), LevelCourseTypeEnum.REQUIRED);
        AppResource resource = appResourceBo.queryAppResource(LevelCourseConfigUtil.getResourceType(), key);
        Set<String> totalCodeSet = Collections.emptySet();
        if(resource!=null){
            totalCodeSet = LevelCourseConfigUtil.parseCourseCodeSet(resource.getValue());
        }
        Map<String, PartnerOnlinePeixunDto> courseCodePeixunDtoMap = queryBatchOnlinePeixunProcess(condition.getUserId(), Lists.newArrayList(totalCodeSet));
        int toLearningCount = LevelCourseConvertor.computeLearningCount(totalCodeSet, courseCodePeixunDtoMap);
        return new LevelCourseLearningStatisticsDto(null, toLearningCount);
    }

    @Override
    public List<LevelCourseLearningDto> searchCourseLearningInfoByUserLevel(Long userId, String userLevel) {
        if(StringUtils.isEmpty(userLevel)){
            return Collections.emptyList();
        }
        Set<String> requiredCourseCodeSet = getCourseCodeList(userLevel, LevelCourseTypeEnum.REQUIRED);
        Set<String> electiveCourseCodeSet = getCourseCodeList(userLevel, LevelCourseTypeEnum.ELECTIVE);
        LevelCourseExample lce = new LevelCourseExample();
        List<String> totalCourseCodeList = new ArrayList<>(requiredCourseCodeSet);
        totalCourseCodeList.addAll(electiveCourseCodeSet);
        if(totalCourseCodeList.size()>0){
            lce.createCriteria().andCourseCodeIn(totalCourseCodeList);
            List<LevelCourse> levelCourseList = courseBo.queryLevelCourse(lce);
            Map<String, PartnerOnlinePeixunDto> peixunDtoMap = queryBatchOnlinePeixunProcess(userId, new ArrayList<>(totalCourseCodeList));
            return LevelCourseConvertor.convertToCourseLearningDto(levelCourseList, peixunDtoMap, requiredCourseCodeSet, electiveCourseCodeSet);
        }
        return Collections.emptyList();
    }
    
    @Override
    public List<LevelCourseLearningDto> searchCourseLearningInfoByTag(Long userId, String tag){
        if(StringUtils.isBlank(tag)){
            return Collections.emptyList();
        }

        LevelCourseExample lce = new LevelCourseExample();
        lce.createCriteria().andTagEqualTo(tag);
        List<LevelCourse> tagCourseList = courseBo.queryLevelCourse(lce);
        Set<String> courseCodeSet = LevelCourseConvertor.extractCourseCode(tagCourseList);
        Map<String, PartnerOnlinePeixunDto> peixunDtoMap = queryBatchOnlinePeixunProcess(userId, new ArrayList<>(courseCodeSet));
        return LevelCourseConvertor.convertToCourseLearningDto(tagCourseList, peixunDtoMap, null, null);
    }
    
    private Set<String> getCourseCodeList(String level, LevelCourseTypeEnum type) {
        AppResource resource = appResourceBo.queryAppResource(LevelCourseConfigUtil.getResourceType(), LevelCourseConfigUtil.getResourceKey(level, type));
        if(resource==null || StringUtils.isBlank(resource.getValue())){
            return Sets.newHashSet();
        }
        return LevelCourseConfigUtil.parseCourseCodeSet(resource.getValue());
    }
    
    public Map<String, PartnerOnlinePeixunDto> queryBatchOnlinePeixunProcess(Long userId, List<String> courseCodes){
        try{
            if(userId == null || CollectionUtils.isEmpty(courseCodes)){
                return Collections.emptyMap();
            }
            Set<String> strSet = new HashSet<String>(courseCodes);
            Map<String, PartnerOnlinePeixunDto> peixunDtos = partnerPeixunService.queryBatchOnlinePeixunProcess(userId, new ArrayList<String>(strSet));
            return peixunDtos;
        }catch(Throwable t){
            logger.error("LevelCourseServiceImpl.queryBatchOnlinePeixunProcess error!", t);
            return Collections.emptyMap();
        }
    }

}
