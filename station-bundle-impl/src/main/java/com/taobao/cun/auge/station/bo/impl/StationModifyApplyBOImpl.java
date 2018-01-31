package com.taobao.cun.auge.station.bo.impl;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.client.exception.DefaultServiceException;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.StationModifyApply;
import com.taobao.cun.auge.dal.domain.StationModifyApplyExample;
import com.taobao.cun.auge.dal.mapper.StationModifyApplyMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationModifyApplyBO;
import com.taobao.cun.auge.station.convert.StationModifyApplyConverter;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.StationModifyApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationModifyApplyBusitypeEnum;
import com.taobao.cun.auge.station.enums.StationModifyApplyStatusEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.ProcessService;
import com.taobao.cun.common.util.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Component("stationModifyApplyBO")
public class StationModifyApplyBOImpl implements StationModifyApplyBO {
	
	 private static final Logger logger = LoggerFactory.getLogger(StationModifyApplyBO.class);
	@Autowired
	private StationModifyApplyMapper stationModifyApplyMapper;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private  GeneralTaskSubmitService generalTaskSubmitService;
	 
    @Autowired
    private  PartnerInstanceBO partnerInstanceBO;
	
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long addStationModifyApply(StationModifyApplyDto dto) {
    	if (getApplyInfoByStationId(dto.getBusiType(),  dto.getStationId(), StationModifyApplyStatusEnum.AUDITING) != null) {
    		throw new DefaultServiceException(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE, "当前村点修改审批已提交");
    	}
		StationModifyApply s = StationModifyApplyConverter.toStationModifyApply(dto);
		s.setStatus(StationModifyApplyStatusEnum.AUDITING.getCode());
		DomainUtils.beforeInsert(s, dto.getOperator());
		stationModifyApplyMapper.insert(s);
		
		if (StationModifyApplyBusitypeEnum.NAME_ADDRESS_MODIFY.equals(dto.getBusiType())) {
			//启动流程
			StartProcessDto startProcessDto =new StartProcessDto();
			startProcessDto.setBusiness(ProcessBusinessEnum.stationInfoApply);
			startProcessDto.setBusinessId(s.getId());
			startProcessDto.setBusinessName("村点名称和地址修改");
			startProcessDto.setBusinessOrgId(dto.getOperatorOrgId());
			startProcessDto.copyOperatorDto(dto);
			processService.startApproveProcess(startProcessDto);
		}
		
		return s.getId();
	}
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void auditForNameAndAddress(Long id, StationModifyApplyStatusEnum status) {
		ValidateUtils.notNull(id);
		ValidateUtils.notNull(status);
		if ((!status.equals(StationModifyApplyStatusEnum.AUDIT_PASS)) && (!status.equals(StationModifyApplyStatusEnum.AUDIT_NOT_PASS))) {
            throw new DefaultServiceException(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE, "审批状态必须为通过或不通过");

		}
		StationModifyApply s = new StationModifyApply();
		s.setId(id);
		s.setStatus(status.getCode());
		DomainUtils.beforeUpdate(s, OperatorDto.DEFAULT_OPERATOR);
		stationModifyApplyMapper.updateByPrimaryKeySelective(s);
		
		//更新station表名称
		if (status.equals(StationModifyApplyStatusEnum.AUDIT_PASS)) {
			StationModifyApplyDto sma = getApplyInfoById(id);
			if (sma != null && sma.getInfoMap() != null) {
				Map<String,String> infoMap = sma.getInfoMap();
				StationDto stationDto = new StationDto();
				if (infoMap.get("name") != null) {
					stationDto.setName(sma.getInfoMap().get("name"));
				}
				Address  address  = new  Address();
				if (infoMap.get("address") != null) {
					address.setAddressDetail(sma.getInfoMap().get("address"));
				}
				if (infoMap.get("village") != null && infoMap.get("villageDetail") != null) {
					address.setVillage(sma.getInfoMap().get("village"));
					address.setVillageDetail(sma.getInfoMap().get("villageDetail"));
				}
				stationDto.setAddress(address);
				stationDto.setId(sma.getStationId());
				stationDto.copyOperatorDto(OperatorDto.defaultOperator());
				stationBO.updateStation(stationDto);
				
				Long insId = partnerInstanceBO.findPartnerInstanceIdByStationId(sma.getStationId());
				if (insId != null) {
					//同步菜鸟
				    generalTaskSubmitService.submitUpdateCainiaoStation(insId, OperatorDto.DEFAULT_OPERATOR);
				}else {
					 logger.error("StationModifyApplyBO.auditForNameAndAddress error: insId is null stationId=" + sma.getStationId());
				}
			}
		}
	}

	@Override
	public StationModifyApplyDto getApplyInfoByStationId(
			StationModifyApplyBusitypeEnum type, Long stationId,StationModifyApplyStatusEnum status) {
		ValidateUtils.notNull(stationId);
		ValidateUtils.notNull(status);
		ValidateUtils.notNull(type);
		  StationModifyApplyExample applyExample = new StationModifyApplyExample();
		 applyExample.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(stationId).andStatusEqualTo(status.getCode());
		 List<StationModifyApply> applyList = stationModifyApplyMapper.selectByExample(applyExample);
		 if (!CollectionUtils.isEmpty(applyList)) {
            return  StationModifyApplyConverter.toStationModifyApplyDto(applyList.get(0));
		 }
	        return null;
	}

	@Override
	public StationModifyApplyDto getApplyInfoById(Long id) {
		StationModifyApply s = getStationModifyApplyById(id);
		return  StationModifyApplyConverter.toStationModifyApplyDto(s);
	}
	@Override
	public StationModifyApply getStationModifyApplyById(Long id) {
		ValidateUtils.notNull(id);
		return stationModifyApplyMapper.selectByPrimaryKey(id);
	}
	@Override
	public void updateName(StationModifyApplyDto dto) {
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(dto.getStationId());
		if (rel == null) {
			logger.error("StationModifyApplyBO.updateStationName error: insId is null stationId=" + dto.getStationId());
			 throw new DefaultServiceException(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE, "合伙人实例不存在");
		}
		if (!PartnerInstanceStateEnum.getStateForCanUpdateStationName().contains(rel.getState())) {
			 throw new DefaultServiceException(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE, "当前状态不能编辑村点名称");
		}
		StationDto stationDto = new StationDto();
		stationDto.setName(dto.getInfoMap().get("name"));
		stationDto.setId(dto.getStationId());
		stationDto.copyOperatorDto(OperatorDto.defaultOperator());
		stationBO.updateStation(stationDto);
		//同步菜鸟
	    generalTaskSubmitService.submitUpdateCainiaoStation(rel.getId(), OperatorDto.DEFAULT_OPERATOR);
		
	}

}
