package com.taobao.cun.auge.station.strategy;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.condition.UnionMemberPageCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.um.UnionMemberQueryService;
import com.taobao.cun.auge.station.um.dto.UnionMemberDto;

@Component("tpsStrategy")
public class TpsStrategy  extends CommonStrategy implements PartnerInstanceStrategy{
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private UnionMemberQueryService unionMemberQueryService;
	
	@Override
	public void applySettle(PartnerInstanceDto partnerInstanceDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applySettleNewly(PartnerInstanceDto partnerInstanceDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void settleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto, PartnerStationRel rel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyQuit(QuitStationApplyDto quitDto, PartnerInstanceTypeEnum typeEnum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDifferQuitAuditPass(Long partnerInstanceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quit(PartnerInstanceQuitDto partnerInstanceQuitDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto, PartnerStationRel rel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateExistChildrenForQuit(PartnerStationRel instance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateClosePreCondition(PartnerStationRel partnerStationRel) {
		List<PartnerInstanceStateEnum> states = PartnerInstanceStateEnum.getPartnerStatusForValidateClose();
		List<PartnerStationRel> children = partnerInstanceBO.findChildPartners(partnerStationRel.getId(), states);
		
		if (CollectionUtils.isNotEmpty(children)) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"该村小二下存在未停业的淘帮手，请先将其淘帮手停业后，才可以停业。");
		}
		UnionMemberPageCondition  con = new UnionMemberPageCondition();
		con.setOperator(com.taobao.cun.auge.station.enums.OperatorTypeEnum.SYSTEM.getCode());
		con.setOperatorType(com.taobao.cun.auge.station.enums.OperatorTypeEnum.SYSTEM);
		con.setParentStationId(partnerStationRel.getStationId());
		con.setPageNum(1);
		con.setPageSize(10);
		PageDto<UnionMemberDto> umList = unionMemberQueryService.queryByPage(con);
		if (CollectionUtils.isNotEmpty(umList.getItems())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"该村小二下存在优盟，请先删除优盟，才可以停业。");
		}
		
	}

	@Override
	public Boolean validateUpdateSettle(Long instanceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startClosing(Long instanceId, String stationName, OperatorDto operatorDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startQuiting(Long instanceId, String stationName, OperatorDto operatorDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateAssetBack(Long instanceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateOtherPartnerQuit(Long instanceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startService(Long instanceId, Long taobaoUserId, OperatorDto operatorDto) {
		// TODO Auto-generated method stub
		
	}

}
