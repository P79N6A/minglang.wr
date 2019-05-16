package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.condition.UnionMemberPageCondition;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.um.UnionMemberQueryService;
import com.taobao.cun.auge.station.um.dto.UnionMemberDto;
import com.taobao.cun.auge.station.um.enums.UnionMemberStateEnum;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonStrategy implements PartnerInstanceStrategy{
	
	@Autowired
    PartnerInstanceQueryService partnerInstanceQueryService;
	
	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Autowired
	private DiamondConfiguredProperties diamondConfiguredProperties;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	UnionMemberQueryService unionMemberQueryService;

	public String findCloseReason(Long instanceId) {
		// 获取停业原因
		CloseStationApplyDto forcedCloseDto = partnerInstanceQueryService.getCloseStationApply(instanceId);
		String remark;
		if (null == forcedCloseDto) {
			remark = "";
		} else {
			remark = CloseStationApplyCloseReasonEnum.OTHER.equals(forcedCloseDto.getCloseReason())
					? forcedCloseDto.getOtherReason() : forcedCloseDto.getCloseReason().getDesc();
		}
		return remark;
	}
	
	public Long findCloseApplyId(Long instanceId) {
		// 获取停业原因
		CloseStationApplyDto forcedCloseDto = partnerInstanceQueryService.getCloseStationApply(instanceId);
		if (null == forcedCloseDto) {
			return 0L;
		} 
		return forcedCloseDto.getId();
	}
	
	public Long findQuitApplyId(Long instanceId) {
		QuitStationApplyDto quitApply = partnerInstanceQueryService.getQuitStationApply(instanceId);
		if(null == quitApply){
			return 0L;
		}
		return quitApply.getId();
	}
	
	@Override
	public void partnerClosing(Long instanceId, OperatorDto operatorDto) {
	}
	
	@Override
	public void autoClosing(Long instanceId, OperatorDto operatorDto){
		
	}

	@Override
	public void closed(Long instanceId, Long taobaoUserId, String taobaoNick, PartnerInstanceTypeEnum typeEnum, OperatorDto operatorDto) {
		generalTaskSubmitService.submitRemoveUserTagTasks(taobaoUserId, taobaoNick, typeEnum, operatorDto.getOperator(), instanceId);
		generalTaskSubmitService.submitClosedCainiaoStation(instanceId, operatorDto.getOperator());

//		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);

		//关闭优盟，通过事件关闭优盟，保证时效性，但是优盟数量限制在200以下，否则等待定时钟来关闭优盟
//		Long parentStationId = instance.getStationId();
//		PageDto<UnionMemberDto> umList = getUnionMembers(parentStationId, UnionMemberStateEnum.SERVICING, 1);
//
//		Integer closeUmMaxNum = diamondConfiguredProperties.getBatchCloseOrQuitUmNum();
//		if (null != umList && umList.getTotal() < closeUmMaxNum) {
//			generalTaskSubmitService.submitClosedUmTask(parentStationId, operatorDto);
//		}
	}

	@Override
	public void quited(Long instanceId, OperatorDto operatorDto) {
//		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
//		Long parentStationId = instance.getStationId();

		//退出优盟，通过事件关闭优盟，保证时效性，但是优盟数量限制在200以下，否则等待定时钟来关闭优盟
//		PageDto<UnionMemberDto> umList = getUnionMembers(parentStationId, UnionMemberStateEnum.CLOSED, 1);
//		Integer quitUmMaxNum = diamondConfiguredProperties.getBatchCloseOrQuitUmNum();
//		if (null != umList && umList.getTotal() < quitUmMaxNum) {
//			generalTaskSubmitService.submitQuitUmTask(parentStationId, operatorDto);
//		}
	}

	private PageDto<UnionMemberDto> getUnionMembers(Long parentStationId, UnionMemberStateEnum state, Integer pageNum) {
		UnionMemberPageCondition con = new UnionMemberPageCondition();
		con.setOperator(OperatorTypeEnum.SYSTEM.getCode());
		con.setOperatorType(OperatorTypeEnum.SYSTEM);
		con.setParentStationId(parentStationId);
		con.setState(state);
		con.setPageNum(pageNum);
		con.setPageSize(10);
		PageDto<UnionMemberDto> umList = unionMemberQueryService.queryByPage(con);
		return umList;
	}
}
