package com.taobao.cun.auge.station.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.cache.TairCache;
import com.taobao.cun.auge.station.dto.UserFilterRuleDto;
import com.taobao.cun.auge.station.service.PartnerApplyWhitenameApplyService;
import com.taobao.cun.auge.station.service.UserFilterService;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.dto.StartProcessInstanceDto;
import com.taobao.cun.crius.bpm.enums.CuntaoProcessInstanceStatus;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.recruit.partner.dto.PartnerApplyDto;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = PartnerApplyWhitenameApplyService.class)
public class PartnerApplyWhitenameApplyServiceImpl implements PartnerApplyWhitenameApplyService {
	@Resource
	private TairCache tairCache;
	@Resource
	private CuntaoWorkFlowService cuntaoWorkFlowService;
	@Resource
	private UserFilterService userFilterService;
	@Resource
	private PartnerApplyService partnerApplyService;
	
	private static final String TASK_CODE = "partnerApplyWhitenameApply";
	
	@Override
	public boolean apply(Long partnerApplyId, String taobaoUserId, String applierId) {
		if(tairCache.get("process:" + partnerApplyId) != null) {
			return false;
		}
		ResultModel<CuntaoProcessInstance> result = cuntaoWorkFlowService.findRunningProcessInstance(TASK_CODE, String.valueOf(partnerApplyId));
		if(result.isSuccess()) {
			CuntaoProcessInstance instance = result.getResult();
			if(result.getResult() != null) {
				if(CuntaoProcessInstanceStatus.RUNNING.getCode().equals(instance.getStatus().getCode())){
					return false;
				}
			}
		}
		
		PartnerApplyDto partnerApplyDto = partnerApplyService.getPartnerApplyById(partnerApplyId);
		
		StartProcessInstanceDto startDto = new StartProcessInstanceDto();

		startDto.setBusinessCode(TASK_CODE);
		startDto.setBusinessId(String.valueOf(partnerApplyId));
		startDto.setApplierId(applierId);
		startDto.setApplierUserType(UserTypeEnum.BUC);
		startDto.setBusinessName("[" + partnerApplyDto.getStationAddressDetail() + "]申请开点白名单");
		Map<String, String> initData = Maps.newHashMap();
		initData.put("taobaoUserId", String.valueOf(taobaoUserId));
		startDto.setInitData(initData);
		ResultModel<Boolean> resultModel = cuntaoWorkFlowService.startProcessInstance(startDto);
		if(!resultModel.isSuccess()) {
			throw new RuntimeException(result.getException());
		}
		tairCache.put("process:" + partnerApplyId, partnerApplyId, 60);
		return true;
	}

	@Override
	public void agree(String taobaoUserId) {
		UserFilterRuleDto userFilterRuleDto = new UserFilterRuleDto();
		userFilterRuleDto.setBizType("partner-tbid-whitename");
		userFilterRuleDto.setFilterRule("trueRule");
		userFilterRuleDto.setUserId(taobaoUserId);
		userFilterService.addUserFilterRule(userFilterRuleDto);
	}
}
