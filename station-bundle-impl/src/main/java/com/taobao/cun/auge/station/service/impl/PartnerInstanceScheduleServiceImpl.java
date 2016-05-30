package com.taobao.cun.auge.station.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceScheduleService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = PartnerInstanceScheduleService.class)
public class PartnerInstanceScheduleServiceImpl implements PartnerInstanceScheduleService {
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	@Autowired
	PartnerInstanceService partnerInstanceService;
	
	@Override
	public List<Long> getWaitOpenStationList(int fetchNum)
			throws AugeServiceException {
		return partnerInstanceBO.getWaitOpenStationList(fetchNum);
	}

	@Override
	public Boolean openStation(Long instanceId) throws AugeServiceException {
		// TODO:检查开业包
		if (!partnerInstanceService.checkKyPackage()) {
			//开业时间置为空
			//TODO：发短信
		}
		partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.DECORATING,
				PartnerInstanceStateEnum.SERVICING, DomainUtils.DEFAULT_OPERATOR);
		OperatorDto operatorDto= new OperatorDto();
		operatorDto.setOperator(DomainUtils.DEFAULT_OPERATOR);
		operatorDto.setOperatorType(OperatorTypeEnum.SYSTEM);
		operatorDto.setOperatorOrgId(0L);
		// 记录村点状态变化
		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
				PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.START_SERVICING,
						partnerInstanceBO.getPartnerInstanceById(instanceId),operatorDto));
		return true;
	}

}
