package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.dal.domain.PeixunPurchase;
import com.taobao.cun.auge.dal.mapper.PeixunPurchaseMapper;
import com.taobao.cun.auge.station.bo.PeixunPurchaseBO;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PartnerPeixunListDetailDto;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;
import com.taobao.cun.auge.station.enums.PeixunPurchaseStatusEnum;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.dto.flow.enums.CuntaoFlowTargetTypeEnum;
@Component("peixunPurchaseBO")
public class PeixunPurchaseBOImpl implements PeixunPurchaseBO{

	@Autowired
	PeixunPurchaseMapper peixunPurchaseMapper;
	
	@Autowired
    private CuntaoWorkFlowService  cuntaoWorkFlowService;
	
	public static String FLOW_BUSINESS_CODE="peixun_purchase";
	
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
			//生成流程
			createFlow(record.getId(),dto.getLoginId(),dto.getApplyOrgId());
			return record.getId();
		}else{
			PeixunPurchase record=peixunPurchaseMapper.selectByPrimaryKey(dto.getId());
			if(record==null){
				throw new AugeServiceException("not find record");
			}
			//判断修改人和提交人是否一致
			if(!record.getCreator().equals(dto.getOperator())){
				throw new AugeServiceException("与提交人不一致，无权限修改");
			}
			//判断是否是可编辑状态
			if (!PeixunPurchaseStatusEnum.AUDIT_NOT_PASS.getCode().equals(
					record.getStatus())
					|| !PeixunPurchaseStatusEnum.ROLLBACK.getCode().equals(
							record.getStatus())) {
				throw new AugeServiceException("状态不可编辑");
			}
			copyForUpdate(record,dto);
			peixunPurchaseMapper.updateByPrimaryKey(record);
			//生成流程
			createFlow(record.getId(),dto.getLoginId(),dto.getApplyOrgId());
			return record.getId();
		}
	}
	
	private void createFlow(Long applyId,String loginId,Long orgId){
		Map<String,String> initData = new HashMap<String, String>();
		initData.put("orgId", String.valueOf(orgId));
		ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService
				.startProcessInstance(FLOW_BUSINESS_CODE,
						String.valueOf(applyId), loginId, null);
	}
	private void validateForCreate(PeixunPurchaseDto dto){
		if(StringUtils.isEmpty(dto.getMasterWorkNo())){
			throw new AugeServiceException("masterWorkNo is null");
		}
		if(StringUtils.isEmpty(dto.getLoginId())){
			throw new AugeServiceException("loginId is null");
		}
		if(StringUtils.isEmpty(dto.getIsShare())){
			throw new AugeServiceException("isShare is null");
		}
//		if(StringUtils.isEmpty(dto.getCategoryId())){
//			throw new AugeServiceException("categoryId is null");
//		}
//		if(StringUtils.isEmpty(dto.getOuCode())){
//			throw new AugeServiceException("ouCode is null");
//		}
		if(StringUtils.isEmpty(dto.getPurchaseType())){
			throw new AugeServiceException("purchaseType is null");
		}
//		if(StringUtils.isEmpty(dto.getReceiveAddress())){
//			throw new AugeServiceException("receiveAddress is null");
//		}
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
		param.put("orgPath", condition.getOrgPath());
		param.put("recevieWorkNo", condition.getReceiveWorkNo());
		param.put("status", condition.getStatus());
		param.put("gmtExceptStart", condition.getGmtExceptStart());
		param.put("gmtExceptEnd", condition.getGmtExceptEnd());
		param.put("pageNum", condition.getPageNum());
		param.put("pageSize", condition.getPageSize());
		int count=peixunPurchaseMapper.queryPurchaseListCount(param);
		List<PeixunPurchaseDto> records = peixunPurchaseMapper
				.queryListByCondition(param);
		PageDto<PeixunPurchaseDto> result=new PageDto<PeixunPurchaseDto>();
		result.setTotal(count);
		result.setItems(records);
		return result;
	}

	@Override
	public PeixunPurchaseDto queryById(Long id) {
		Assert.notNull(id);
		PeixunPurchase pp=peixunPurchaseMapper.selectByPrimaryKey(id);
		if(pp==null){
			throw new AugeServiceException("not find record");
		}
		PeixunPurchaseDto result=new PeixunPurchaseDto();
		BeanUtils.copyProperties(pp, result);
		return result;
	}

}
