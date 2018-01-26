package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.taobao.cun.auge.client.exception.DefaultServiceException;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
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
		StationModifyApply s = StationModifyApplyConverter.toStationModifyApply(dto);
		DomainUtils.beforeInsert(s, dto.getOperator());
		stationModifyApplyMapper.insert(s);
		
		if (StationModifyApplyBusitypeEnum.NAME_MODIFY.equals(dto.getBusiType())) {
			//启动流程
			StartProcessDto startProcessDto =new StartProcessDto();
			startProcessDto.setBusiness(ProcessBusinessEnum.stationInfoApply);
			startProcessDto.setBusinessId(s.getId());
			startProcessDto.setBusinessName("村点名称修改");
			startProcessDto.setBusinessOrgId(dto.getOperatorOrgId());
			startProcessDto.copyOperatorDto(dto);
			processService.startApproveProcess(startProcessDto);
		}
		
		return s.getId();
	}
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void auditForName(Long id, StationModifyApplyStatusEnum status) {
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
			if (sma != null) {
				if (sma.getInfoMap() != null && sma.getInfoMap().get("name") != null) {
					StationDto stationDto = new StationDto();
					stationDto.setName(sma.getInfoMap().get("name"));
					stationDto.setId(sma.getStationId());
					stationDto.copyOperatorDto(OperatorDto.defaultOperator());
					stationBO.updateStation(stationDto);
					
					Long insId = partnerInstanceBO.findPartnerInstanceIdByStationId(sma.getStationId());
					if (insId != null) {
						//同步菜鸟
					    generalTaskSubmitService.submitUpdateCainiaoStation(insId, OperatorDto.DEFAULT_OPERATOR);
					}else {
						 logger.error("StationModifyApplyBO.updateStationName error: insId is null stationId=" + sma.getStationId());
					}
					
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

}
