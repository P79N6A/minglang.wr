package com.taobao.cun.auge.station.service.impl.levelaudit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.station.bo.PartnerInstanceLevelBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.station.service.interfaces.LevelAuditMessageService;

/**
 * 晋升S6需要后续县小二review下
 * 类LevelReviewMessageServiceImpl.java的实现描述：TODO 类实现描述 
 * @author xujianhui 2016年11月17日 下午2:44:46
 */

@Service("levelReviewMessageService")
public class LevelReviewMessageServiceImpl implements LevelAuditMessageService {

    private static final Logger logger = LoggerFactory.getLogger(LevelReviewMessageServiceImpl.class);
  
    @Autowired
    PartnerInstanceService partnerInstanceService;
    
    @Autowired
    PartnerInstanceLevelBO partnerInstanceLevelBO;
    
    @Override
    public void handleApprove(PartnerInstanceLevelDto partnerInstanceLevelDto, String adjustLevel) {
            logger.error("Review Level Evaluate Pass for partnerInstanceId:{}, currentLevel:{}" + partnerInstanceLevelDto.getPartnerInstanceId(), partnerInstanceLevelDto.getCurrentLevel());
    }

    /**
     * S6晋升review只能同意不能拒绝 仅仅是一个review而已
     */
    @Override
    public void handleRefuse(PartnerInstanceLevelDto partnerInstanceLevelDto, String adjustLevel) {
    }

}
