package com.taobao.cun.auge.station.service.impl.exam;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.taobao.cun.auge.station.dto.LevelExamConfigurationDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;
import com.taobao.cun.auge.station.service.LevelExamDispatchStrategy;
import com.taobao.cun.auge.station.service.LevelExamManageService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.exam.dto.ExamDispatchDto;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;;

public class PartnerServicingLevelExamDispatchStrategy implements LevelExamDispatchStrategy {

    private static final Logger logger = LoggerFactory.getLogger(PartnerServicingLevelExamDispatchStrategy.class);
    
    @Autowired
    ExamUserDispatchService  examUserDispatchService;
    
    @Autowired
    PartnerInstanceQueryService partnerInstanceQueryService;
    
    @Autowired
    LevelExamManageService levelExamManageService;
    
    @Override
    public boolean dispatchExamPaper(Long taobaoUserId, String nickName, Map<String, Object> context) {
        Assert.notNull(taobaoUserId);
        PartnerInstanceLevelDto partnerInstanceLevelDto = partnerInstanceQueryService.getPartnerInstanceLevel(taobaoUserId);
        if(partnerInstanceLevelDto == null || partnerInstanceLevelDto.getCurrentLevel()==null){
            return false;
        }
        
        LevelExamConfigurationDto configurationDto = levelExamManageService.queryConfigure();
        if(configurationDto==null || configurationDto.isDeleted()){
            return false;
        }
        
        Long s5PaperId = configurationDto.getExamPaperId(PartnerInstanceLevel.S5);
        Long s6PaperId = configurationDto.getExamPaperId(PartnerInstanceLevel.S6);
        return dispatchExamPapers(taobaoUserId, nickName, s5PaperId, s6PaperId);
    }
    
    private boolean dispatchExamPapers(Long taobaoUserId, String nickName, Long ... paperIds){
        for(Long paperId:paperIds){
            if(paperId!=null){
                ResultModel<Boolean> result = examUserDispatchService.dispatchExam(newExamDispatchDto(taobaoUserId, nickName, paperId, "PartnerServicingLevelExamDispatchStrategy"));
                if(result == null || !result.isSuccess() || result.getResult()==Boolean.FALSE){
                    logger.error("PartnerServicingLevelExamDispatchStrategy dispatch paper:{} exception:{}", paperId, result.getException());
                    return false;
                }
            }
        }
        return true;
    }

    private ExamDispatchDto newExamDispatchDto(Long userId, String nickName, Long paperId, String dispatcher){
        ExamDispatchDto edd =  new ExamDispatchDto();
        edd.setUserId(userId);
        edd.setTaobaoNick(nickName);
        edd.setPaperId(paperId);
        edd.setDispatcher(dispatcher);
        return edd;
    }
}
