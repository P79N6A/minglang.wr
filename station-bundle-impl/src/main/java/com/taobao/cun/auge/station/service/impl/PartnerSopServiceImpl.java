package com.taobao.cun.auge.station.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.dal.domain.CuntaoOrg;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.dto.CuntaoUser;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerSopRltDto;
import com.taobao.cun.auge.station.enums.*;
import com.taobao.cun.auge.station.service.PartnerSopService;
import com.taobao.cun.auge.user.service.CuntaoUserService;
import com.taobao.cun.auge.user.service.UserRole;
import com.taobao.cun.recruit.partner.dto.PartnerApplyDto;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("partnerSopService")
@HSFProvider(serviceInterface = PartnerSopService.class)
public class PartnerSopServiceImpl implements PartnerSopService {

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private PartnerApplyService partnerApplyService;

    @Autowired
    private PartnerLifecycleBO partnerLifecycleBO;
    @Autowired
    private StationBO stationBO;

    @Autowired
    private CuntaoUserService cuntaoUserService;

    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;

    @Override
    public Map<String, String> getPartnerInfo(Long taobaoUserId) {
        if (taobaoUserId == null) {
        ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, "参数为空");
            return JSON.parseObject(JSONObject.toJSONString(Result.of(errorInfo)), new TypeReference<Map<String, String>>() {});
        }
        PartnerSopRltDto rs = new PartnerSopRltDto();
        //报名状态
        PartnerApplyDto pa  = partnerApplyService.getPartnerApplyByTaobaoUserId(taobaoUserId);
        if(pa != null) {
            rs.setPartnerApplyStateDesc(pa.getPartnerApplyStateEnum().getDesc());
        }else {
            rs.setPartnerApplyStateDesc("未报名");
        }
        //村小二状态
        PartnerStationRel psr = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
        if (psr != null) {
            bulidStationInfo(rs, psr.getState(),psr.getId(),psr.getStationId());
        }else {
            PartnerInstanceDto lastPsr = partnerInstanceBO.getLastPartnerInstance(taobaoUserId);
            if (lastPsr == null) {
                rs.setPartnerIsntanceStateDesc("未入驻");
            }else {
                bulidStationInfo(rs, lastPsr.getState().getCode(),lastPsr.getId(),lastPsr.getStationId());
            }
        }

        return JSON.parseObject(JSONObject.toJSONString(Result.of(rs)), new TypeReference<Map<String, String>>() {});
    }

    private void bulidStationInfo(PartnerSopRltDto rs, String state ,Long instanceId, Long stationId) {
        rs.setPartnerIsntanceStateDesc(buildInstanceState(state,instanceId));
        Station s = stationBO.getStationById(stationId);
        if (s != null) {
            rs.setStationName(s.getName());
            rs.setStationAddress(buildAddress(s));
            //构建县信息
            rs.setCountyLeaderName(bulidCountyLeader(s.getApplyOrg(), UserRole.COUNTY_LEADER));
            CuntaoOrgDto cuntaoOrg = cuntaoOrgServiceClient.getCuntaoOrg(s.getApplyOrg());
            CuntaoOrgDto org = cuntaoOrgServiceClient.getAncestor(s.getApplyOrg(), OrgRangeType.SPECIALTEAM);
            rs.setTeamLeaderName(bulidCountyLeader(org.getId(),UserRole.TEAM_LEADER));
            rs.setCountyName(cuntaoOrg == null ?"":cuntaoOrg.getName());
        }
    }

    private String bulidCountyLeader(Long orgId,UserRole userRole) {
        List<CuntaoUser> users = cuntaoUserService.getCuntaoUsers(orgId,userRole);
        if (CollectionUtils.isEmpty(users)){
            return "";
        }
        List<String> us= users.stream().map(t -> t.getUserName()+"["+t.getLoginId()+"]").collect(Collectors.toList());
        return StringUtils.join(us, ",");
    }

    private String buildAddress(Station s) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(s.getProvinceDetail())) {
            sb.append(s.getProvinceDetail());
        }
        if (StringUtils.isNotEmpty(s.getCityDetail())) {
            sb.append(s.getCityDetail());
        }
        if (StringUtils.isNotEmpty(s.getCountyDetail())) {
            sb.append(s.getCountyDetail());
        }
        if (StringUtils.isNotEmpty(s.getTownDetail())) {
            sb.append(s.getTownDetail());
        }
        if (StringUtils.isNotEmpty(s.getAddress())) {
            sb.append(s.getAddress());
        }
        return sb.toString();
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
