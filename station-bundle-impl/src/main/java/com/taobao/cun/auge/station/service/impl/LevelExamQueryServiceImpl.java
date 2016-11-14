package com.taobao.cun.auge.station.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.station.convert.LevelExamUtil;
import com.taobao.cun.auge.station.dto.LevelExamingResult;
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
@HSFProvider(serviceInterface = LevelExamQueryService.class)
public class LevelExamQueryServiceImpl implements LevelExamQueryService {

    private static final Logger logger = LoggerFactory.getLogger(LevelExamQueryServiceImpl.class);

    @Autowired
    ExamUserDispatchService  examUserDispatchService;
    
    /**
     * 判断是否通过了本层级所有晋升考试
     * 如果没有那么打印日志
     */
    @Override
    public LevelExamingResult queryLevelExamResult(Long taobaoUserId, PartnerInstanceLevel level) {
        Map<PartnerInstanceLevel, Long> dispatchedExamLevelAndPaper  = getDispatchedPaperInfo(taobaoUserId);
        if(dispatchedExamLevelAndPaper==null) {
            logger.error("LevelExamDispatchServiceImpl query dispatch record error, taobaoUserId:{}", taobaoUserId);
            return new LevelExamingResult(false, Lists.newArrayList());
        }
        List<PartnerInstanceLevel> passedLevels = Lists.newArrayList();
        List<String> notPassExamLevels = Lists.newArrayList();
        for(Map.Entry<PartnerInstanceLevel, Long>entry:dispatchedExamLevelAndPaper.entrySet()){
            ResultModel<UserDispatchDto> resultModel = examUserDispatchService.queryExamUserDispatch(entry.getValue(), taobaoUserId);
            if(isPassExam(resultModel)){
                passedLevels.add(entry.getKey());
            }else {
                notPassExamLevels.add(entry.getKey().name());
            }
        }
        boolean isPassAllDispatchedExam = passedLevels.containsAll(dispatchedExamLevelAndPaper.keySet());
        List<PartnerInstanceLevel> allLevelExams = LevelExamUtil.computeShouldTakeExamList(level);
        boolean isPassAllLevelExam = passedLevels.containsAll(allLevelExams);
        if(!isPassAllDispatchedExam || !isPassAllLevelExam){
            allLevelExams.removeAll(passedLevels);
            logger.error(" LevelExamDispatchServiceImpl taobaoUserId:{}, level:{}, not pass dispatched level:{}, not dispatch levels:{}", taobaoUserId,  level, notPassExamLevels, allLevelExams);
        }
        return new LevelExamingResult(isPassAllDispatchedExam, notPassExamLevels);
    }
    
    /**
     * 查询某个合伙人分发的晋升试卷<层级,paperId>
     */
    private Map<PartnerInstanceLevel, Long> getDispatchedPaperInfo (Long taobaoUserId){
        ResultModel<List<ExamDispatchDto>>result = examUserDispatchService.listExamDispatchDto(taobaoUserId, ExamDispatchSourceEnum.promotion, 0, 100);
        if(result==null || !result.isSuccess()){
            return null;
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
