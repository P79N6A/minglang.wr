package com.taobao.cun.auge.station.bo.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.ceres.service.Result;
import com.alibaba.ceres.service.pr.PrService;
import com.alibaba.ceres.service.pr.model.PrDto;
import com.alibaba.ceres.service.pr.model.PrLineDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.PeixunPurchase;
import com.taobao.cun.auge.dal.mapper.PeixunPurchaseMapper;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.bo.PeixunPurchaseBO;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;
import com.taobao.cun.auge.station.enums.PeixunPurchaseStatusEnum;
import com.taobao.cun.auge.station.enums.PeixunPurchaseTypeEnum;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.dto.StartProcessInstanceDto;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.enums.UserTypeEnum;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
@Component("peixunPurchaseBO")
public class PeixunPurchaseBOImpl implements PeixunPurchaseBO{

	@Autowired
	PeixunPurchaseMapper peixunPurchaseMapper;
	
	@Autowired
    private CuntaoWorkFlowService  cuntaoWorkFlowService;
	
	public static String FLOW_BUSINESS_CODE="peixun_purchase";
	
	@Autowired
	private PrService prService;

	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
	@Autowired
	DiamondConfiguredProperties configuredProperties;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
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
			if(!record.getApplyWorkNo().equals(dto.getOperator())){
				throw new AugeServiceException("与提交人不一致，无权限修改");
			}
			//判断是否是可编辑状态
			if (!PeixunPurchaseStatusEnum.AUDIT_NOT_PASS.getCode().equals(
					record.getStatus())
					&&!PeixunPurchaseStatusEnum.ROLLBACK.getCode().equals(
							record.getStatus())) {
				throw new AugeServiceException("当前状态不可编辑");
			}
			copyForUpdate(record,dto);
			peixunPurchaseMapper.updateByPrimaryKey(record);
			//生成流程
			createFlow(record.getId(),dto.getLoginId(),dto.getApplyOrgId());
			return record.getId();
		}
	}
	
	private void createFlow(Long applyId, String loginId, Long orgId) {
		try {
			StartProcessInstanceDto startDto = new StartProcessInstanceDto();

			startDto.setBusinessCode(FLOW_BUSINESS_CODE);
			startDto.setBusinessId(String.valueOf(applyId));

			startDto.setCuntaoOrgId(orgId);
			startDto.setOperator(loginId);
			startDto.setUserType(UserTypeEnum.BUC);

			ResultModel<Boolean> rm = cuntaoWorkFlowService.startProcessInstance(startDto);
			if (!rm.isSuccess()) {
				throw new AugeServiceException(rm.getException());
			}
		} catch (Exception e) {
			throw new AugeServiceException("流程启动失败", e);
		}
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
		if(StringUtils.isEmpty(dto.getPurchaseType())){
			throw new AugeServiceException("purchaseType is null");
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
		if(StringUtils.isEmpty(dto.getPurchaseSupplier())){
			throw new AugeServiceException("purchaseSupplier is null");
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
		record.setApplyName(dto.getApplyName());
		record.setReceiverName(dto.getReceiverName());
		record.setMasterName(dto.getMasterName());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public boolean audit(Long id, String operate,String operateName,String auditDesc,Boolean isPass) {
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
		record.setAuditWorkNo(operate);
		record.setAuditName(operateName);
		record.setAuditDesc(auditDesc);
		if(isPass){
			record.setStatus(PeixunPurchaseStatusEnum.AUDIT_PASS.getCode());
		}else{
			record.setStatus(PeixunPurchaseStatusEnum.AUDIT_NOT_PASS.getCode());
		}
		peixunPurchaseMapper.updateByPrimaryKey(record);
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public boolean rollback(Long id, String operate) {
		if(id==null||StringUtils.isEmpty(operate)){
			throw new AugeServiceException("param is null");
		}
		PeixunPurchase record=peixunPurchaseMapper.selectByPrimaryKey(id);
		if(record==null){
			throw new AugeServiceException("not find record");
		}
		if (!PeixunPurchaseStatusEnum.WAIT_AUDIT.getCode().equals(
						record.getStatus())) {
			throw new AugeServiceException("当前状态无法撤回");
		}
		if(!operate.equals(record.getApplyWorkNo())){
			throw new AugeServiceException("操作人与提交人不一致，无法撤回");
		}
		record.setGmtModified(new Date());
		record.setModifier(operate);
		record.setStatus(PeixunPurchaseStatusEnum.ROLLBACK.getCode());
		peixunPurchaseMapper.updateByPrimaryKey(record);
		//终止流程
		endTask(id,operate);
		return true;
	}
	
	private void endTask(Long id, String operate) {
		try {
			ResultModel<CuntaoProcessInstance> instance = cuntaoWorkFlowService
					.findRunningProcessInstance(FLOW_BUSINESS_CODE,
							String.valueOf(id));
			String instanceId = instance.getResult().getProcessInstanceId();
			cuntaoWorkFlowService.teminateProcessInstance(instanceId, operate);
		} catch (Exception e) {
			throw new AugeServiceException("终止流程失败", e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public boolean createOrder(Long id, String operate) {
		Assert.notNull(id);
		Assert.notNull(operate);
		PeixunPurchase record=peixunPurchaseMapper.selectByPrimaryKey(id);
		if(record==null){
			throw new AugeServiceException("not find record");
		}
		if (!PeixunPurchaseStatusEnum.AUDIT_PASS.getCode().equals(
				record.getStatus())) {
			throw new AugeServiceException("还未审核通过，无法下单");
		}
		if(!operate.equals(record.getApplyWorkNo())){
			throw new AugeServiceException("提交人与申请人不一致，无法下单");
		}
		PrDto prDto = new PrDto();
		prDto.setActualRequestor(record.getApplyWorkNo());
		prDto.setApplicant(record.getApplyWorkNo());
		prDto.setDescription(applyReason(record));
		prDto.setOuCode("T62");
		List<PrLineDto> prLineList = getPrList(record);
		prDto.setPrLineList(prLineList);
		try {
			Result<?> result = prService.submitPr(prDto);
			
			if (!result.isSuccess()) {
				throw new RuntimeException("提交pr失败，失败原因：" + result.getMessage());
			}
			String prNo=(String)result.getValue();
			record.setPrNo(prNo);
		} catch (Exception e) {
			throw new RuntimeException("提交pr失败，失败原因：" + e);
		}
		record.setGmtModified(new Date());
		record.setModifier(operate);
		record.setStatus(PeixunPurchaseStatusEnum.ORDER.getCode());
		peixunPurchaseMapper.updateByPrimaryKey(record);
		return false;
	}
	
	private String applyReason(PeixunPurchase record){
		CuntaoOrgDto county = cuntaoOrgServiceClient.getCuntaoOrg(record.getApplyOrgId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder sb = new StringBuilder();
		sb.append("申请人:").append(record.getApplyName()).append("(").append(record.getApplyWorkNo()).append("),");
		sb.append("申请县域:").append(county.getFullNamePath()).append(",");
		sb.append("开班人数:").append(record.getExceptNum()).append(",");
		sb.append("期望开班时间:").append(sdf.format(record.getGmtExceptOpen())).append(",");
		if (!StringUtils.isEmpty(record.getPurchaseSupplier())) {
			sb.append("培训供应商:").append(configuredProperties.getSupplierMap().get(record.getPurchaseSupplier())).append(",");
		}
		sb.append("备注:").append(record.getDescription());
		return sb.toString();
	}
	
	private List<PrLineDto> getPrList(PeixunPurchase record) {
        Map<String, String> purchaseMap = configuredProperties.getPurchaseMap();
        String skuCode;
		if (!StringUtils.isEmpty(record.getPurchaseSupplier())) {
			skuCode = purchaseMap.get(record.getPurchaseSupplier() + "_" + record.getPurchaseType()+"_SKU");
		} else {
			skuCode = purchaseMap.get(record.getPurchaseType()+"_SKU");
		}
		String useCode = purchaseMap.get("USE_CODE");
        String address = purchaseMap.get("ADDRESS");
		List<PrLineDto> prLineList = new ArrayList<PrLineDto>();
		PrLineDto prLine = new PrLineDto();
		prLine.setSkuId(skuCode);
		prLine.setCategoryUse(useCode);
		prLine.setNeedByDate(record.getGmtExceptOpen());
		prLine.setDeliveryAddressId(new Long(address));
		prLine.setReceiver(record.getReceiverWorkNo());
		prLine.setQuantity(record.getExceptNum());
		prLine.setRemark(applyReason(record));
		prLineList.add(prLine);
		return prLineList;
	}

	@Override
	public PageDto<PeixunPurchaseDto> queryPeixunPurchaseList(
			PeixunPuchaseQueryCondition condition) {
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("orgId", condition.getOrgId());
		param.put("orgPath", condition.getOrgPath());
		param.put("applyName", condition.getApplyName());
		param.put("status", condition.getStatus());
		param.put("gmtExceptStart", condition.getGmtExceptStart());
		param.put("gmtExceptEnd", condition.getGmtExceptEnd());
		param.put("pageNum", condition.getPageNum());
		param.put("pageSize", condition.getPageSize());
		int count=peixunPurchaseMapper.queryPurchaseListCount(param);
		List<PeixunPurchaseDto> records = peixunPurchaseMapper
				.queryListByCondition(param);
		fillResult(records);
		PageDto<PeixunPurchaseDto> result=new PageDto<PeixunPurchaseDto>();
		result.setTotal(count);
		result.setItems(records);
		return result;
	}
	
	private void fillResult(List<PeixunPurchaseDto> records){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(PeixunPurchaseDto record:records){
			if(record.getGmtCreate()!=null){
				record.setGmtCreateDesc(sdf.format(record.getGmtCreate()));
			}
			if(record.getGmtExceptOpen()!=null){
				record.setGmtExceptOpenDesc(sdf.format(record.getGmtExceptOpen()));
			}
			record.setStatusDesc(PeixunPurchaseStatusEnum.valueof(record.getStatus()).getDesc());
			record.setPurchaseTypeDesc(PeixunPurchaseTypeEnum.valueof(record.getPurchaseType()).getDesc());
			record.setApplyWorkNo(record.getApplyWorkNo()+"-"+record.getApplyName());
		}
	}

	@Override
	public PeixunPurchaseDto queryById(Long id) {
		if(id==null){
			return null;
		}
		PeixunPurchase pp=peixunPurchaseMapper.selectByPrimaryKey(id);
		if(pp==null){
			throw new AugeServiceException("not find record");
		}
		PeixunPurchaseDto result=new PeixunPurchaseDto();
		BeanUtils.copyProperties(pp, result);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(result.getGmtCreate()!=null){
			result.setGmtCreateDesc(sdf.format(result.getGmtCreate()));
		}
		if(pp.getGmtExceptOpen()!=null){
			result.setGmtExceptOpenDesc(sdf.format(result.getGmtExceptOpen()));
		}
		CuntaoOrgDto county = cuntaoOrgServiceClient.getCuntaoOrg(pp.getApplyOrgId());
		result.setOrgFullName(county.getFullNamePath());
		result.setStatusDesc(PeixunPurchaseStatusEnum.valueof(result.getStatus()).getDesc());
		result.setPurchaseTypeDesc(PeixunPurchaseTypeEnum.valueof(result.getPurchaseType()).getDesc());
		if (!StringUtils.isEmpty(result.getPurchaseSupplier())) {
			result.setPurchaseSupplierName(configuredProperties.getSupplierMap().get(result.getPurchaseSupplier()));
		}
		return result;
	}

}
