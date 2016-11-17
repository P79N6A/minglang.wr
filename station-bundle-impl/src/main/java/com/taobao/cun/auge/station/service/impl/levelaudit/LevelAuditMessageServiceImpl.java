package com.taobao.cun.auge.station.service.impl.levelaudit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.dal.domain.PartnerInstanceLevel;
import com.taobao.cun.auge.station.bo.PartnerInstanceLevelBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.station.service.interfaces.LevelAuditMessageService;

/**
 * S7 S8需要省长和大区经理审批
 * 类LevelAuditMessageServiceImpl.java的实现描述：TODO 类实现描述 
 * @author xujianhui 2016年11月17日 下午2:45:11
 */

@Service("levelAuditMessageService")
public class LevelAuditMessageServiceImpl implements LevelAuditMessageService {

    private static final Logger logger = LoggerFactory.getLogger(LevelAuditMessageServiceImpl.class);
    
    @Autowired
    private LevelAuditProcessStartService levelAuditProcessStartService;
    
    @Autowired
    PartnerInstanceService partnerInstanceService;
    
    @Autowired
    PartnerInstanceLevelBO partnerInstanceLevelBO;
    
    @Override
    public void handleApprove(JSONObject ob) {
        try {
                PartnerInstanceLevelDto partnerInstanceLevelDto = JSON.parseObject(ob.getString("evaluateInfo"), PartnerInstanceLevelDto.class);
                String adjustLevel = ob.getString("adjustLevel");
                partnerInstanceLevelDto.setPreLevel(partnerInstanceLevelDto.getCurrentLevel());
                PartnerInstanceLevelEnum expectedLevel = partnerInstanceLevelDto.getExpectedLevel();
                if (StringUtils.isNotBlank(adjustLevel)) {
                    String levelStr = "";
                    if(expectedLevel!=null){
                        levelStr = expectedLevel.getLevel().toString();
                    }
                    String remark = "申请层级为: " + levelStr  + ", 人工调整为 : " + adjustLevel;
                    partnerInstanceLevelDto.setCurrentLevel(PartnerInstanceLevelEnum.valueof(adjustLevel));
                    partnerInstanceLevelDto.setRemark(remark);
                }else{
                    partnerInstanceLevelDto.setCurrentLevel(expectedLevel);
                }
                partnerInstanceLevelDto.setExpectedLevel(null);
                partnerInstanceService.evaluatePartnerInstanceLevel(partnerInstanceLevelDto);
        } catch (Exception e) {
            logger.error("handleApprove: " + ob.toJSONString(), e);
            throw e;
        }

    }

    @Override
    public void handleRefuse(JSONObject ob) {
        try {
            PartnerInstanceLevelDto partnerInstanceLevelDto = JSON.parseObject(ob.getString("evaluateInfo"), PartnerInstanceLevelDto.class);
            PartnerInstanceLevel level = partnerInstanceLevelBO.getPartnerInstanceLevelByPartnerInstanceId(partnerInstanceLevelDto.getPartnerInstanceId());
            level.setExpectedLevel(null);
            String remark = "申请合伙人层级 " + partnerInstanceLevelDto.getCurrentLevel().getLevel().toString() + " 被拒绝";
            level.setRemark(remark);
            partnerInstanceLevelBO.updatePartnerInstanceLevel(level);
        } catch (Exception e) {
            logger.error("handRefuse: " + ob.toJSONString(), e);
            throw e;
        }
    }

    @Override
    public void startApproveProcess(PartnerInstanceLevelProcessDto levelProcessDto) {
        levelAuditProcessStartService.startApproveProcess(levelProcessDto);
    }

}
