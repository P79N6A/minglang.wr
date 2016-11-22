package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.dal.domain.PeixunPurchase;
import com.taobao.cun.auge.dal.mapper.PeixunPurchaseMapper;
import com.taobao.cun.auge.station.bo.PeixunPurchaseBO;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;
import com.taobao.cun.auge.station.enums.PeixunPurchaseStatusEnum;
@Component("peixunPurchaseBO")
public class PeixunPurchaseBOImpl implements PeixunPurchaseBO{

	@Autowired
	PeixunPurchaseMapper peixunPurchaseMapper;
	
	
	@Override
	public Long createOrUpdatePeixunPurchase(PeixunPurchaseDto dto) {
		validateForCreate(dto);
		if(dto.getId()==null){
			//新增
			dto.setGmtCreate(new Date());
			dto.setGmtModified(new Date());
			dto.setIsDeleted("n");
			dto.setCreator(dto.getOperator());
			dto.setModifier(dto.getOperator());
			PeixunPurchase record=new PeixunPurchase();
			BeanUtils.copyProperties(dto, record);
			peixunPurchaseMapper.insert(record);
			return record.getId();
		}else{
			PeixunPurchase record=peixunPurchaseMapper.selectByPrimaryKey(dto.getId());
			if(record==null){
				throw new AugeServiceException("not find record");
			}
			copyForUpdate(record,dto);
			peixunPurchaseMapper.updateByPrimaryKey(record);
			return record.getId();
		}
	}
	
	private void validateForCreate(PeixunPurchaseDto dto){
		if(StringUtils.isEmpty(dto.getMasterWorkNo())){
			throw new AugeServiceException("masterWorkNo is null");
		}
		if(StringUtils.isEmpty(dto.getIsShare())){
			throw new AugeServiceException("isShare is null");
		}
		if(StringUtils.isEmpty(dto.getCategoryId())){
			throw new AugeServiceException("categoryId is null");
		}
		if(StringUtils.isEmpty(dto.getOuCode())){
			throw new AugeServiceException("ouCode is null");
		}
		if(StringUtils.isEmpty(dto.getPurchaseType())){
			throw new AugeServiceException("purchaseType is null");
		}
		if(StringUtils.isEmpty(dto.getReceiveAddress())){
			throw new AugeServiceException("receiveAddress is null");
		}
		if(StringUtils.isEmpty(dto.getReceiverMobile())){
			throw new AugeServiceException("recevieMobile is null");
		}
		if(StringUtils.isEmpty(dto.getReceiverWorkNo())){
			throw new AugeServiceException("recevieWorkNo is null");
		}
		if(StringUtils.isEmpty(dto.getStatus())){
			throw new AugeServiceException("status is null");
		}
		if(dto.getExceptNum()==null){
			throw new AugeServiceException("exceptNum is null");
		}
		if(PeixunPurchaseStatusEnum.valueof(dto.getStatus())==null){
			throw new AugeServiceException("status is not right");
		}
		if(StringUtils.isEmpty(dto.getOperator())){
			throw new AugeServiceException("operator is null");
		}
	}
	
	private void copyForUpdate(PeixunPurchase record,PeixunPurchaseDto dto){
		record.setGmtModified(new Date());
		record.setModifier(dto.getOperator());
		record.setApplyOrgId(dto.getApplyOrgId());
		record.setAuditDesc(dto.getAuditDesc());
		record.setAuditWorkNo(dto.getAuditWorkNo());
		record.setBillMethod(dto.getBillMethod());
		record.setCategoryId(dto.getCategoryId());
		record.setDescription(dto.getDescription());
		record.setExceptNum(dto.getExceptNum());
		record.setGmtAudit(dto.getGmtAudit());
		record.setGmtExceptOpen(dto.getGmtExceptOpen());
		record.setIsShare(dto.getIsShare());
		record.setMasterWorkNo(dto.getMasterWorkNo());
		record.setOuCode(dto.getOuCode());
		record.setPurchaseType(dto.getPurchaseType());
		record.setReceiveAddress(dto.getReceiveAddress());
		record.setReceiverMobile(dto.getReceiverMobile());
		record.setReceiverWorkNo(dto.getReceiverWorkNo());
		record.setShareDesc(dto.getShareDesc());
		record.setStatus(dto.getStatus());
	}

	@Override
	public boolean audit(Long id, String operate, String auditDesc,Boolean isPass) {
		if(id==null||StringUtils.isEmpty(operate)){
			throw new AugeServiceException("param is null");
		}
		PeixunPurchase record=peixunPurchaseMapper.selectByPrimaryKey(id);
		if(record==null){
			throw new AugeServiceException("not find record");
		}
		if(!PeixunPurchaseStatusEnum.WAIT_AUDIT.getCode().equals(record.getStatus())){
			throw new AugeServiceException("非待审核状态");
		}
		record.setGmtModified(new Date());
		record.setModifier(operate);
		if(isPass){
			record.setStatus(PeixunPurchaseStatusEnum.AUDIT_PASS.getCode());
		}else{
			record.setStatus(PeixunPurchaseStatusEnum.AUDIT_NOT_PASS.getCode());
		}
		peixunPurchaseMapper.updateByPrimaryKey(record);
		return true;
	}

	@Override
	public boolean rollback(Long id, String operate) {
		if(id==null||StringUtils.isEmpty(operate)){
			throw new AugeServiceException("param is null");
		}
		PeixunPurchase record=peixunPurchaseMapper.selectByPrimaryKey(id);
		if(record==null){
			throw new AugeServiceException("not find record");
		}
		if(!PeixunPurchaseStatusEnum.AUDIT_PASS.getCode().equals(record.getStatus())){
			throw new AugeServiceException("非审核通过状态,无法撤回");
		}
		record.setGmtModified(new Date());
		record.setModifier(operate);
		record.setStatus(PeixunPurchaseStatusEnum.ROLLBACK.getCode());
		peixunPurchaseMapper.updateByPrimaryKey(record);
		return true;
	}

	@Override
	public boolean createOrder(Long id, String operate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PageDto<PeixunPurchaseDto> queryPeixunPurchaseList(
			PeixunPuchaseQueryCondition condition) {
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("orgId", condition.getOrgId());
		param.put("recevieWorkNo", condition.getReceiveWorkNo());
		param.put("status", condition.getStatus());
		param.put("gmtExceptStart", condition.getGmtExceptStart());
		param.put("gmtExceptEnd", condition.getGmtExceptEnd());
		param.put("pageNum", condition.getPageNum());
		param.put("pageSize", condition.getPageSize());
		return null;
	}

}
