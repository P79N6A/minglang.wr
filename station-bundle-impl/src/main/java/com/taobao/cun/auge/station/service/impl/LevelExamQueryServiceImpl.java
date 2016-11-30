package com.taobao.cun.auge.station.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.convert.LevelExamUtil;
import com.taobao.cun.auge.station.dto.LevelExamingResult;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;
import com.taobao.cun.auge.station.service.LevelExamQueryService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.exam.dto.ExamDispatchDto;
import com.taobao.cun.crius.exam.dto.UserDispatchDto;
import com.taobao.cun.crius.exam.enums.ExamDispatchSourceEnum;
import com.taobao.cun.crius.exam.enums.ExamInstanceStatusEnum;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("levelExamQueryService")
@HSFProvider(serviceInterface = LevelExamQueryService.class, clientTimeout=10000)
public class LevelExamQueryServiceImpl implements LevelExamQueryService {

    private static final Logger logger = LoggerFactory.getLogger(LevelExamQueryServiceImpl.class);

    @Autowired
    private AppResourceBO appResourceBO;
    
    @Autowired
    ExamUserDispatchService  examUserDispatchService;
    
    /**
     * 判断是否通过了本层级晋升考试
     * 1.通过该层级考试
     * 2.没有分发该层级考试
     */
    @Override
    public LevelExamingResult queryLevelExamResult(Long taobaoUserId, String level) {
        Map<PartnerInstanceLevel, Long> dispatchedExamLevelAndPaper  = getDispatchedPaperInfo(taobaoUserId);
        if(dispatchedExamLevelAndPaper.isEmpty()){
            return new LevelExamingResult(true, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        }
        
        List<PartnerInstanceLevel> passedLevels = Lists.newArrayList();
        List<String> notPassExamLevels = Lists.newArrayList(), passedLevelStrList = Lists.newArrayList();
        for(Map.Entry<PartnerInstanceLevel, Long>entry:dispatchedExamLevelAndPaper.entrySet()){
            ResultModel<UserDispatchDto> resultModel = examUserDispatchService.queryExamUserDispatch(entry.getValue(), taobaoUserId);
            if(isPassExam(resultModel)){
                passedLevels.add(entry.getKey());
                passedLevelStrList.add(entry.getKey().name());
            }else {
                notPassExamLevels.add(entry.getKey().name());
            }
        }
        /**
         * 已通过层级里面包含目标层级或者没有通过层级不包含该层级(表示没分发该级别的试卷)
         */
        boolean isPassLevelExam = (passedLevelStrList.contains(level) || !notPassExamLevels.contains(level));
        List<String> dispathedLevels = Lists.newArrayList(passedLevelStrList);
        dispathedLevels.addAll(notPassExamLevels);
        return new LevelExamingResult(isPassLevelExam, notPassExamLevels, passedLevelStrList, dispathedLevels);
    }
    
    public boolean isOpenEvaluateCheckExamPass() {
        String isEvaluate = appResourceBO.queryAppResourceValue(LevelExamUtil.LEVEL_EXAM_CONFIG, LevelExamUtil.LEVEL_EXAM_EVALUATE_SWITCH);
        if(StringUtils.isNotBlank(isEvaluate)){
            return Boolean.parseBoolean(isEvaluate);
        }
        return false;
    }
    
    /**
     * 是否通过了从preLevel到newCurrentLevel之间的所有考试
     * 返回最低没有通过考试的层级
     */
    @Override
    public PartnerInstanceLevelEnum checkEvaluateLevelByExamResult(Long taobaoUserId, PartnerInstanceLevelEnum preLevel, PartnerInstanceLevelEnum newCurrentLevel) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(preLevel);
        Assert.notNull(newCurrentLevel);
        boolean isUpgrade = newCurrentLevel.getLevel().compareTo(preLevel.getLevel()) > 0;
        if(!isUpgrade){
            return newCurrentLevel;
        }
        LevelExamingResult examResult = queryLevelExamResult(taobaoUserId,  newCurrentLevel.getLevel().name());
        List<PartnerInstanceLevel> shouldPassLevelExam = enumsBetween(preLevel.getLevel(), newCurrentLevel.getLevel());
        List<String> passedLevelExam = examResult.getPassedLevelExams();
        List<String> dispatchedLevels = examResult.getDispatchedLevels();
        PartnerInstanceLevel newLevel = preLevel.getLevel();
        for (PartnerInstanceLevel tmp : shouldPassLevelExam) {
            if (!passedLevelExam.contains(tmp.name()) && dispatchedLevels.contains(tmp.name())) {
                return PartnerInstanceLevelEnum.valueof(newLevel);
            }
            newLevel = tmp;
        }
        return PartnerInstanceLevelEnum.valueof(newLevel);
    }
    
    /**
     * 排序好的中间枚举列表
     */
    private List<PartnerInstanceLevel> enumsBetween(PartnerInstanceLevel from, PartnerInstanceLevel to){
        if(from==null || to==null){
            return Collections.emptyList();
        }
        PartnerInstanceLevel up = (from.compareTo(to)<=0) ? to :from;
        PartnerInstanceLevel down = (from.compareTo(to)<=0) ? from :to;
        List<PartnerInstanceLevel> allEnumLevels = Arrays.asList(PartnerInstanceLevel.values());
        Collections.sort(allEnumLevels, new Comparator<PartnerInstanceLevel>() {
            @Override
            public int compare(PartnerInstanceLevel o1, PartnerInstanceLevel o2) {
                return o1.compareTo(o2);
            }
        });
        List<PartnerInstanceLevel> enums = Lists.newArrayList();
        for(PartnerInstanceLevel temp:allEnumLevels){
            if(temp.compareTo(down)>=0 && temp.compareTo(up)<=0){
                enums.add(temp);
            }
        }
        return enums;
    }
    
    /**
     * 查询某个合伙人分发的晋升试卷<层级,paperId>
     */
    private Map<PartnerInstanceLevel, Long> getDispatchedPaperInfo (Long taobaoUserId){
        ResultModel<List<ExamDispatchDto>>result = examUserDispatchService.listExamDispatchDto(taobaoUserId, ExamDispatchSourceEnum.promotion, 0, 100);
        if(result==null || !result.isSuccess()){
            throw new AugeServiceException("Query dispatched promotion exam fail!");
        }
        List<ExamDispatchDto> dispatchedExamList = result.getResult();
        if(CollectionUtils.isEmpty(dispatchedExamList)){
            return Collections.emptyMap();
        }
        return LevelExamUtil.parseDispatchedExamLevels(dispatchedExamList);
    }
    
    /**
     * 是否通过给定列表的所有试卷考试
     * @param resultModel
     * @return
     */
    private boolean isPassExam(ResultModel<UserDispatchDto> resultModel){
        if(resultModel!=null 
                && resultModel.isSuccess() 
                && resultModel.getResult()!=null 
                && resultModel.getResult().getCurrentInstance()!=null){
            return resultModel.getResult().getCurrentInstance().getStatus()!=null 
                    && ExamInstanceStatusEnum.PASS.getCode().equals( resultModel.getResult().getCurrentInstance().getStatus().getCode());
        }
        return false;
    }

}
