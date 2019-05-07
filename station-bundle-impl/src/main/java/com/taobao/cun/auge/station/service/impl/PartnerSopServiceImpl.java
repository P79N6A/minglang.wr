package com.taobao.cun.auge.station.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerSopRltDto;
import com.taobao.cun.auge.station.enums.*;
import com.taobao.cun.auge.station.service.PartnerSopService;
import com.taobao.cun.recruit.partner.dto.PartnerApplyDto;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("partnerSopService")
@HSFProvider(serviceInterface = PartnerSopService.class)
public class PartnerSopServiceImpl implements PartnerSopService {

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private PartnerApplyService partnerApplyService;

    @Autowired
    private PartnerLifecycleBO partnerLifecycleBO;

    @Override
    public String getPartnerInfo(Long taobaoUserId) {
        if (taobaoUserId == null) {
        ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, "参数为空");
        return JSONObject.toJSONString(Result.of(errorInfo));
        }
        PartnerSopRltDto rs = new PartnerSopRltDto();

        PartnerApplyDto pa  = partnerApplyService.getPartnerApplyByTaobaoUserId(taobaoUserId);
        if(pa != null) {
            rs.setPartnerApplyStateDesc(pa.getPartnerApplyStateEnum().getDesc());
        }else {
            rs.setPartnerApplyStateDesc("未报名");
        }

        PartnerStationRel psr = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
        if (psr != null) {
            rs.setPartnerIsntanceStateDesc(buildInstanceState(psr.getState(),psr.getId()));
        }else {
            PartnerInstanceDto lastPsr = partnerInstanceBO.getLastPartnerInstance(taobaoUserId);
            if (lastPsr == null) {
                rs.setPartnerIsntanceStateDesc("未入驻");
            }

            rs.setPartnerIsntanceStateDesc(buildInstanceState(lastPsr.getState().getCode(),lastPsr.getId()));
        }
        return JSONObject.toJSONString(Result.of(rs));
    }

    private String buildInstanceState(String state,Long id){
        PartnerLifecycleItems item = null;
        if (PartnerInstanceStateEnum.SETTLING.getCode().equals(state)) {
           item = partnerLifecycleBO.getLifecycleItems(id, PartnerLifecycleBusinessTypeEnum.SETTLING);
            if (item != null) {
                if (PartnerLifecycleSettledProtocolEnum.SIGNING.getCode().equals(item.getSettledProtocol())) {
                    return "待签约人确认";
                }
                if (PartnerLifecycleSettledProtocolEnum.SIGNED.getCode().equals(item.getSettledProtocol()) &&
                        PartnerLifecycleBondEnum.WAIT_FROZEN.getCode().equals(item.getBond())) {
                    return "已确认待冻结";
                }
                if (PartnerLifecycleSystemEnum.WAIT_PROCESS.getCode().equals(item.getSystem()) &&
                        PartnerLifecycleBondEnum.HAS_FROZEN.getCode().equals(item.getBond())) {
                    return "已冻结待系统处理";
                }
            }
        }else if (PartnerInstanceStateEnum.DECORATING.getCode().equals(state)) {
            return "装修中";
        }else if (PartnerInstanceStateEnum.SERVICING.getCode().equals(state)) {
            return "服务中";
        }else if (PartnerInstanceStateEnum.CLOSING.getCode().equals(state)) {
            return "停业申请中";
        }else if (PartnerInstanceStateEnum.CLOSED.getCode().equals(state)) {
            return "已停业";
        }else if (PartnerInstanceStateEnum.QUITING.getCode().equals(state)) {
            item = partnerLifecycleBO.getLifecycleItems(id, PartnerLifecycleBusinessTypeEnum.QUITING);
            if (item != null) {
                if (!PartnerLifecycleRoleApproveEnum.AUDIT_PASS.getCode().equals(item.getRoleApprove())) {
                    return "退出待审批";
                }
                if (PartnerLifecycleRoleApproveEnum.AUDIT_PASS.getCode().equals(item.getRoleApprove()) &&
                        PartnerLifecycleBondEnum.WAIT_THAW.getCode().equals(item.getBond())) {
                    return "退出待解冻";
                }
            }
        }else if (PartnerInstanceStateEnum.QUIT.getCode().equals(state)) {
            return "已退出";
        }
        return "";

    }
}
