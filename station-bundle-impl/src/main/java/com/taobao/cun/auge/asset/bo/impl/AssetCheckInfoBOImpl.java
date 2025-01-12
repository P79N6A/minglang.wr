package com.taobao.cun.auge.asset.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.buc.api.exception.BucException;
import com.alibaba.buc.api.model.enhanced.EnhancedUser;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetCheckInfoBO;
import com.taobao.cun.auge.asset.bo.AssetCheckTaskBO;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoAddDto;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.CheckCountDto;
import com.taobao.cun.auge.asset.dto.CountyFollowCheckCountDto;
import com.taobao.cun.auge.asset.enums.AssetCheckInfoAssetTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckInfoCategoryTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckInfoCheckTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckInfoStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckTaskTaskStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckTaskTaskTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetCheckInfo;
import com.taobao.cun.auge.dal.domain.AssetCheckInfoExample;
import com.taobao.cun.auge.dal.domain.AssetCheckInfoExample.Criteria;
import com.taobao.cun.auge.dal.domain.AssetCheckTask;
import com.taobao.cun.auge.dal.mapper.AssetCheckInfoMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

import net.sf.cglib.beans.BeanCopier;

@Component
public class AssetCheckInfoBOImpl implements AssetCheckInfoBO {

	private static BeanCopier assetCheckInfoVo2DtoCopier = BeanCopier.create(AssetCheckInfo.class,
			AssetCheckInfoDto.class, false);

	@Autowired
	private EnhancedUserQueryService enhancedUserQueryService;

	@Autowired
	private AssetCheckInfoMapper assetCheckInfoMapper;

	@Autowired
	private AssetCheckTaskBO assetCheckTaskBO;

	@Autowired
	private AssetBO assetBO;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Boolean addCheckInfo(AssetCheckInfoAddDto addDto) {
		Objects.requireNonNull(addDto, "参数不能为空");
		Objects.requireNonNull(addDto.getCheckType(), "盘点类型不能为空");
		Objects.requireNonNull(addDto.getCategoryType(), "资产类型不能为空");
		if (!OperatorTypeEnum.BUC.getCode().equals(addDto.getOperatorType().getCode())
				&& !OperatorTypeEnum.HAVANA.getCode().equals(addDto.getOperatorType().getCode())) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "盘点人必须是村小二,或者县小二");
		}
		validateParam(addDto);
		AssetCheckInfo record = new AssetCheckInfo();
		if (StringUtils.isNotEmpty(addDto.getAliNo())) {
			record.setAliNo(addDto.getAliNo());
		}
		record.setAssetType(AssetCheckInfoCategoryTypeEnum.getAssetType(addDto.getCategoryType()));
		record.setCategory(addDto.getCategoryType());
		if (addDto.getImages() != null && addDto.getImages().size()>0) {
			record.setAttFile(JSONObject.toJSONString(addDto.getImages()));
		}
		record.setCheckType(addDto.getCheckType());
		if (StringUtils.isNotEmpty(addDto.getSerialNo())) {
			record.setSerialNo(addDto.getSerialNo());
		}
		
		record.setCheckTime(new Date());
		record.setStatus(AssetCheckInfoStatusEnum.CHECKED.getCode());
		if (OperatorTypeEnum.BUC.getCode().equals(addDto.getOperatorType().getCode())) {// 县盘点
			AssetCheckTask t = assetCheckTaskBO.getTaskForCounty(addDto.getOperatorOrgId(), AssetCheckTaskTaskTypeEnum.COUNTY_CHECK.getCode());
			if(t == null) {
				throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "当前县域没有分发盘点任务，无需盘点");
			}
			record.setCheckerAreaId(addDto.getOperatorOrgId());
			record.setCheckerAreaName(
					t.getOrgName());
			record.setCheckerAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
			record.setCheckerId(addDto.getOperator());
			record.setCheckerName(getWorkerName(addDto.getOperator()));
			record.setCountyOrgId(addDto.getOperatorOrgId());
			record.setTaskType(AssetCheckTaskTaskTypeEnum.COUNTY_CHECK.getCode());
			
			if (AssetCheckTaskTaskStatusEnum.TODO.getCode().equals(t.getTaskStatus())) {
				assetCheckTaskBO.doingTask(t.getId());
			}
			
		} else if (OperatorTypeEnum.HAVANA.getCode().equals(addDto.getOperatorType().getCode())) {// 村盘点
			validateAddStation(addDto);
			AssetCheckTask at = assetCheckTaskBO.getTaskForStation(addDto.getOperator());
			if(at == null) {
				throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "当前村点没有分发盘点任务，无需盘点");
			}
			record.setCheckerAreaId(at.getStationId());
			record.setCheckerAreaName(at.getStationName());
			record.setCheckerAreaType(AssetUseAreaTypeEnum.STATION.getCode());
			record.setCheckerId(addDto.getOperator());
			record.setCheckerName(at.getCheckerName());
			record.setCountyOrgId(at.getOrgId());
			record.setTaskType(AssetCheckTaskTaskTypeEnum.STATION_CHECK.getCode());
			if (AssetCheckTaskTaskStatusEnum.TODO.getCode().equals(at.getTaskStatus())) {
				assetCheckTaskBO.doingTask(at.getId());
			}
		}
		DomainUtils.beforeInsert(record, addDto.getOperator());
		assetCheckInfoMapper.insert(record);
		return Boolean.TRUE;

	}

	private void validateParam(AssetCheckInfoAddDto addDto) {
		if (addDto.getSerialNo() == null) {
			return;
		}
		// 序列号，TD码规则校验
		// 序列号存在校验
		AssetCheckInfo ai = getCheckInfoBySerialNo(addDto.getSerialNo());
		if (ai != null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
					"当前序列号[" + addDto.getSerialNo() + "]已提交盘点信息");
		}

		Asset a = assetBO.getAssetBySerialNo(addDto.getSerialNo());
		if (a == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
					"当前序列号[" + addDto.getSerialNo() + "]线上不存在，请操作异常盘点");
		}
		String assetType =AssetCheckInfoCategoryTypeEnum.getAssetType(addDto.getCategoryType());
		if (assetType != null && 
				AssetCheckInfoAssetTypeEnum.ADMIN.getCode().equals(assetType)) {
			if (!AssetCheckInfoCategoryTypeEnum.valueof(addDto.getCategoryType()).getDesc().equals(a.getModel())) {
				throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
						"当前序列号[" + addDto.getSerialNo() + "]对应资产类型是"+getCategoryDesc(a)+",请重新操作盘点");
			}
			
		}
		if (assetType != null && 
				AssetCheckInfoAssetTypeEnum.IT.getCode().equals(assetType)) {
			if (!addDto.getCategoryType().equals(a.getCategory())) {
				throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
						"当前序列号[" + addDto.getSerialNo() + "]对应资产类型是"+getCategoryDesc(a)+",请重新操作盘点");
			}
			
		}
	}
	
	private String getCategoryDesc(Asset a) {
		if ("ADMINISTRATION".equals(a.getCategory())) {
			return a.getModel();
		}
		if (AssetCheckInfoCategoryTypeEnum.valueof(a.getCategory()) != null) {
			return AssetCheckInfoCategoryTypeEnum.valueof(a.getCategory()).getDesc();
		}
		return "";
		
		

	}

	private void validateAddStation(AssetCheckInfoAddDto addDto) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCategoryEqualTo(addDto.getCategoryType());
		criteria.andCheckerIdEqualTo(addDto.getOperator());
		criteria.andCheckerAreaTypeEqualTo(AssetUseAreaTypeEnum.STATION.getCode());
		AssetCheckInfo ai = ResultUtils.selectOne(assetCheckInfoMapper.selectByExample(example));
		if (ai != null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "当前类型["
					+ AssetCheckInfoCategoryTypeEnum.valueof(addDto.getCategoryType()).getDesc() + "]资产已提交盘点信息");

		}

	}

	private String getWorkerName(String workNo) {
		try {
			EnhancedUser enhancedUser = enhancedUserQueryService.getUser(workNo);
			return enhancedUser.getLastName();
		} catch (BucException e) {
		}
		return null;
	}
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Boolean delCheckInfo(Long infoId, OperatorDto operator) {
		AssetCheckInfo ai = getCheckInfoById(infoId);
		if (ai == null) {
			return Boolean.TRUE;
		}
		if (AssetCheckInfoStatusEnum.SYS_CONFIRM.getCode().equals(ai.getStatus())
				|| AssetCheckInfoStatusEnum.ZB_CONFIRM.getCode().equals(ai.getStatus())) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "该资产盘点信息总部已确认，不能删除");
		}
		DomainUtils.beforeDelete(ai, operator.getOperator());
		assetCheckInfoMapper.updateByPrimaryKeySelective(ai);
		return Boolean.TRUE;
	}
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Boolean confrimCheckInfo(Long infoId, String aliNo, OperatorDto operator) {
		AssetCheckInfo ai = getCheckInfoById(infoId);
		if (ai == null) {
			return Boolean.TRUE;
		}
		if (!AssetCheckInfoStatusEnum.TASK_DONE.getCode().equals(ai.getStatus())) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "该资产盘点信息不能操作确认");
		}
		//
		Asset a = assetBO.getAssetByAliNo(aliNo);
		if (a == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "该阿里编号[" + aliNo + "]查询不到资产信息");
		}
		if (AssetStatusEnum.SCRAP.getCode().equals(a.getStatus())) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "该资产[" + aliNo + "]已报废，不能盘点");
		}
		if (AssetCheckStatusEnum.CHECKED.getCode().equals(a.getCheckStatus())) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "该资产[" + aliNo + "]已盘点");
		}
		if (!ai.getCountyOrgId().equals(a.getOwnerOrgId())) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
					"该阿里编号[" + aliNo + "]所属县域和当前信息[" + infoId + "]不同，不能确认");
		}
		
		String assetType =ai.getAssetType();
		if (assetType != null && 
				AssetCheckInfoAssetTypeEnum.ADMIN.getCode().equals(assetType)) {
			if (!AssetCheckInfoCategoryTypeEnum.valueof(ai.getCategory()).getDesc().equals(a.getModel())) {
				throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
						"当前序列号[" + aliNo + "]对应资产类型是"+getCategoryDesc(a)+",请重新操作");
			}
			
		}
		if (assetType != null && 
				AssetCheckInfoAssetTypeEnum.IT.getCode().equals(assetType)) {
			if (!ai.getCategory().equals(a.getCategory())) {
				throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
						"当前序列号[" + aliNo+ "]对应资产类型是"+getCategoryDesc(a)+",请重新操作盘点");
			}
			
		}
		if (AssetUseAreaTypeEnum.STATION.getCode().equals(ai.getCheckerAreaType())) {
			assetBO.confrimCheckInfoForSystemToStation(a, ai.getCheckerAreaId(), ai.getCheckerId(), ai.getCheckerName());
		}else{
			assetBO.confrimCheckInfoForSystemToCounty(a);
		}

//		assetBO.confirmForZb(a.getId(), operator.getOperator());
		ai.setAssetId(a.getId());
		ai.setStatus(AssetCheckInfoStatusEnum.ZB_CONFIRM.getCode());
		ai.setBackReason("");
		DomainUtils.beforeUpdate(ai, operator.getOperator());
		assetCheckInfoMapper.updateByPrimaryKeySelective(ai);
		return Boolean.TRUE;
	}
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void backOne(Long infoId, String reason, OperatorDto ope) {
		AssetCheckInfo acInfo = getCheckInfoById(infoId);
		if (AssetCheckInfoStatusEnum.SYS_CONFIRM.getCode().equals(acInfo.getStatus())
				|| AssetCheckInfoStatusEnum.ZB_CONFIRM.getCode().equals(acInfo.getStatus())) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "该资产盘点信息总部已确认，不能打回");
		}
		acInfo.setStatus(AssetCheckInfoStatusEnum.ZB_BACK.getCode());
		acInfo.setBackReason(reason);
		DomainUtils.beforeUpdate(acInfo, ope.getOperator());
		assetCheckInfoMapper.updateByPrimaryKeySelective(acInfo);

	}

	@Override
	public PageDto<AssetCheckInfoDto> listInfoForOrg(AssetCheckInfoCondition param) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		if (param.getCheckerAreaType() != null) {
		criteria.andCheckerAreaTypeEqualTo(param.getCheckerAreaType());
		}
		if (param.getCheckerAreaId() != null) {
			criteria.andCheckerAreaIdEqualTo(param.getCheckerAreaId());
			}
		if (param.getOrgId() != null) {
			criteria.andCountyOrgIdEqualTo(param.getOrgId());
		}
		if (param.getAliNo() != null) {
			criteria.andAliNoEqualTo(param.getAliNo());
		}
		if (param.getSerialNo() != null) {
			criteria.andSerialNoEqualTo(param.getSerialNo());
		}
		// checkType 不为null表示 查询“异常提报”，“正常提报”中的一种
		// checkType 为null 表示查询“异常提报”和“正常提报”
		if (param.getCheckType() != null) {
			criteria.andCheckTypeEqualTo(param.getCheckType());
		}
		if(param.getCategoryType() != null){
			criteria.andCategoryEqualTo(param.getCategoryType());
		}
		if(param.getStatus() != null){
			criteria.andStatusEqualTo(param.getStatus());
		}
		example.setOrderByClause("id desc");
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		Page<AssetCheckInfo> page = (Page<AssetCheckInfo>) assetCheckInfoMapper.selectByExample(example);
		List<AssetCheckInfoDto> targetList = page.getResult().stream()
				.map(assetCheckInfo -> assetCheckInfo2Dto(assetCheckInfo)).collect(Collectors.toList());
		return PageDtoUtil.success(page, targetList);
	}

	private AssetCheckInfoDto assetCheckInfo2Dto(AssetCheckInfo assetCheckInfo) {
		AssetCheckInfoDto assetCheckInfoDto = new AssetCheckInfoDto();
		assetCheckInfoVo2DtoCopier.copy(assetCheckInfo, assetCheckInfoDto, null);
		assetCheckInfoDto.setCategoryType(assetCheckInfo.getCategory());
		assetCheckInfoDto.setInfoId(assetCheckInfo.getId());
		assetCheckInfoDto.setCheckerAreaId(assetCheckInfo.getCheckerAreaId() == null || assetCheckInfo.getCheckerAreaId()==0L? null:String.valueOf(assetCheckInfo.getCheckerAreaId()));
		assetCheckInfoDto.setImages(JSONObject.parseObject(assetCheckInfo.getAttFile(),new TypeReference<Map<String, List<String>>>() {
		}));
		return assetCheckInfoDto;
	}

	@Override
	public PageDto<AssetCheckInfoDto> listInfo(AssetCheckInfoCondition param) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		//店掌柜调用不会传orgId
		if (param.getOrgId() != null && param.getOrgId() !=0L) {
			//县点
			criteria.andCheckerAreaIdEqualTo(param.getOrgId());
			criteria.andCheckerAreaTypeEqualTo(AssetUseAreaTypeEnum.COUNTY.getCode());
		}else{
			//村点
			criteria.andCheckerIdEqualTo(param.getOperator());
			criteria.andCheckerAreaTypeEqualTo(AssetUseAreaTypeEnum.STATION.getCode());
		}
		// checkType 不为null表示 查询“异常提报”，“正常提报”中的一种
		// checkType 为null 表示查询“异常提报”和“正常提报”
		if (param.getCheckType() != null) {
			criteria.andCheckTypeEqualTo(param.getCheckType());
		}
		if (param.getCategoryType() != null) {
			criteria.andCategoryEqualTo(param.getCategoryType());
		}
		example.setOrderByClause("id desc");
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		Page<AssetCheckInfo> page = (Page<AssetCheckInfo>) assetCheckInfoMapper.selectByExample(example);
		List<AssetCheckInfoDto> targetList = page.getResult().stream()
				.map(assetCheckInfo -> assetCheckInfo2Dto(assetCheckInfo)).collect(Collectors.toList());
		return PageDtoUtil.success(page, targetList);
	}

	@Override
	public AssetCheckInfo getCheckInfoById(Long infoId) {
		return assetCheckInfoMapper.selectByPrimaryKey(infoId);
	}

	@Override
	public CheckCountDto getCountyCheckCount(Long countyOrgId) {
		CheckCountDto cd = new CheckCountDto();
		List<AssetCheckInfo> iList = getInfoByCountyOrgIdToCountyCheck(countyOrgId);
		if (CollectionUtils.isEmpty(iList)) {
			return cd;
		}
		cd.setDoneCount(new Long(iList.size()));
		List<AssetCheckInfo> itList = iList.stream()
				.filter(item -> AssetCheckInfoAssetTypeEnum.IT.getCode()
						.equals(AssetCheckInfoCategoryTypeEnum.getAssetType(item.getCategory())))
				.collect(Collectors.toList());
		cd.setItDoneCount(new Long(itList.size()));
		List<AssetCheckInfo> adminList = iList.stream()
				.filter(item -> AssetCheckInfoAssetTypeEnum.ADMIN.getCode()
						.equals(AssetCheckInfoCategoryTypeEnum.getAssetType(item.getCategory())))
				.collect(Collectors.toList());
		cd.setAdminDoneCount(new Long(adminList.size()));
		cd.setItDoneDetail(
				itList.stream().collect(Collectors.groupingBy(AssetCheckInfo::getCategory, Collectors.counting())));
		cd.setAdminDoneDetailt(
				adminList.stream().collect(Collectors.groupingBy(AssetCheckInfo::getCategory, Collectors.counting())));
		return cd;
	}
	@Override
	public CheckCountDto getCheckCountForStation(Long taobaoUserId) {
		CheckCountDto cd = new CheckCountDto();
		List<AssetCheckInfo> iList = getInfoByTaobaoUserIdToStationCheck(taobaoUserId);
		if (CollectionUtils.isEmpty(iList)) {
			return cd;
		}
		Long count = new Long(iList.size());
		cd.setDoneCount(count);
		cd.setItDoneCount(count);
		cd.setAdminDoneCount(0L);
		cd.setItDoneDetail(
				iList.stream().collect(Collectors.groupingBy(AssetCheckInfo::getCategory, Collectors.counting())));
//		cd.setAdminDoneDetail(null);
		return cd;
	}

	@Override
	public CountyFollowCheckCountDto getCountyFollowCheckCount(Long countyOrgId) {
		CountyFollowCheckCountDto fDto = new CountyFollowCheckCountDto();
		Long doneCount = 0L;
		List<AssetCheckInfo> l = getInfoByCountyOrgId(countyOrgId);
		if (CollectionUtils.isNotEmpty(l)) {
			doneCount = new Long(l.size());
		}
		List<Asset> a = assetBO.getCheckAsset(countyOrgId);
		fDto.setDoneCount(doneCount);
	
		Map<String, Long> aDtail = new HashMap<String, Long>();
		for (Asset b : a) {
			String categroyType = bulidCategoryType(b.getModel(), b.getCategory());
			if (categroyType == null) {
				continue;
			}
			if (aDtail.get(categroyType) != null) {
				aDtail.put(categroyType, aDtail.get(categroyType) + 1L);
			} else {
				aDtail.put(categroyType, 1L);
			}
		}
		List<Map<String,String>>  dd= new ArrayList<Map<String,String>>();
		if (doneCount > 0) {
			Map<String, Long> dDtail = l.stream()
					.collect(Collectors.groupingBy(AssetCheckInfo::getCategory, Collectors.counting()));
			
			for (Map.Entry<String, Long> entry : dDtail.entrySet()) {
				Map<String,String> t = new HashMap<String,String>();
				t.put("type", entry.getKey());
				t.put("count", String.valueOf(entry.getValue()));
				dd.add(t);
			}
			fDto.setDoneDetail(dd);
			for (Map.Entry<String, Long> entry : aDtail.entrySet()) {
				if (dDtail.get(entry.getKey()) != null) {
					Long sub = entry.getValue() - dDtail.get(entry.getKey());
					aDtail.put(entry.getKey(), sub < 0 ? 0L : sub);
				}
			}
		}else{
			fDto.setDoneDetail(dd);
		}
		List<Map<String,String>>  aa= new ArrayList<Map<String,String>>();
		Long doing =0L;
		for (Map.Entry<String, Long> entry : aDtail.entrySet()) {
			if(entry.getValue()!= null && entry.getValue() >0L) {
				Map<String,String> t = new HashMap<String,String>();
				t.put("type", entry.getKey());
				t.put("count", String.valueOf(entry.getValue()));
				aa.add(t);
				doing=doing+entry.getValue();
				fDto.setDoingCount(doing);
			}
		}
		fDto.setDoingDetail(aa);
		
		fDto.setDoingStationCount(new Long(assetCheckTaskBO.getWaitCheckStationCount(countyOrgId)));
		fDto.setDoneStationCount(new Long(assetCheckTaskBO.getDoneCheckStationCount(countyOrgId)));
		return fDto;
	}

	private String bulidCategoryType(String model, String category) {
		if ("ADMINISTRATION".equals(category)) {
			if (AssetCheckInfoCategoryTypeEnum.SHAFA.getDesc().equals(model)) {
				return AssetCheckInfoCategoryTypeEnum.SHAFA.getCode();
			} else if (AssetCheckInfoCategoryTypeEnum.KONGTIAO.getDesc().equals(model)) {
				return AssetCheckInfoCategoryTypeEnum.KONGTIAO.getCode();
			} else if (AssetCheckInfoCategoryTypeEnum.ZUHEYINXIANG.getDesc().equals(model)) {
				return AssetCheckInfoCategoryTypeEnum.ZUHEYINXIANG.getCode();
			} else if (AssetCheckInfoCategoryTypeEnum.WEIBOLU.getDesc().equals(model)) {
				return AssetCheckInfoCategoryTypeEnum.WEIBOLU.getCode();
			} else if (AssetCheckInfoCategoryTypeEnum.BINGXIANG.getDesc().equals(model)) {
				return AssetCheckInfoCategoryTypeEnum.BINGXIANG.getCode();
			} else if (AssetCheckInfoCategoryTypeEnum.XUNLUOJI.getDesc().equals(model)) {
				return AssetCheckInfoCategoryTypeEnum.XUNLUOJI.getCode();
			} else if (AssetCheckInfoCategoryTypeEnum.YINXIANG.getDesc().equals(model)) {
				return AssetCheckInfoCategoryTypeEnum.YINXIANG.getCode();
			}else{
				return null;
			}
		}
		return category;
	}

	@Override
	public AssetCheckInfo getCheckInfoBySerialNo(String serialNo) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andSerialNoEqualTo(serialNo);
		return ResultUtils.selectOne(assetCheckInfoMapper.selectByExample(example));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Boolean confrimCheckInfoForSystemToStation(Long stationId, String checkerId, String checkerName) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCheckerAreaIdEqualTo(stationId);
		criteria.andCheckerIdEqualTo(checkerId);
		criteria.andCheckerAreaTypeEqualTo(AssetUseAreaTypeEnum.STATION.getCode());
		List<AssetCheckInfo> aiList = assetCheckInfoMapper.selectByExample(example);
		List<String> categoryList = aiList.stream().map(AssetCheckInfo::getCategory).collect(Collectors.toList());
		if (!categoryList.contains("TV") || !categoryList.contains("MAIN") || !categoryList.contains("DISPLAY")) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "完成盘点失败：盘点资产必须为1台电视,1台显示器,1台主机");
		}
		List<String> statusList = aiList.stream().map(AssetCheckInfo::getStatus).collect(Collectors.toList());
		if (statusList.contains("ZB_BACK")) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "完成盘点失败：请删除总部打回资产，重新盘点");
		}
		for (AssetCheckInfo ai : aiList) {
			if ((AssetCheckInfoStatusEnum.SYS_CONFIRM.getCode().equals(ai.getStatus())
					|| AssetCheckInfoStatusEnum.ZB_CONFIRM.getCode().equals(ai.getStatus()))
					&& ai.getAssetId() != null) {// 已确认 直接返回
				continue;
			}
			if (AssetCheckInfoCheckTypeEnum.COMMON.getCode().equals(ai.getCheckType())) {// 正常提报
				if (StringUtils.isNotEmpty(ai.getSerialNo())) {
					Asset a = assetBO.getAssetBySerialNo(ai.getSerialNo());
					if ((!AssetCheckStatusEnum.CHECKED.getCode().equals(a.getCheckStatus()))
							&& a.getOwnerOrgId().equals(ai.getCountyOrgId())) {// 同县
						assetBO.confrimCheckInfoForSystemToStation(a, stationId, checkerId, checkerName);
						confirmBySystem(ai.getId(), a.getId(), checkerId);
						continue;
					}
				}
				
			}
			doneStation(ai.getId(), checkerId);
			

		}
		return Boolean.TRUE;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	private void confirmBySystem(Long aiId, Long assetId, String operator) {
		AssetCheckInfo ai = new AssetCheckInfo();
		ai.setAssetId(assetId);
		ai.setId(aiId);
		ai.setStatus(AssetCheckInfoStatusEnum.SYS_CONFIRM.getCode());
		DomainUtils.beforeUpdate(ai, operator);
		assetCheckInfoMapper.updateByPrimaryKeySelective(ai);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	private void doneStation(Long id, String operator) {
		AssetCheckInfo ai = new AssetCheckInfo();
		ai.setId(id);
		ai.setStatus(AssetCheckInfoStatusEnum.TASK_DONE.getCode());
		DomainUtils.beforeUpdate(ai, operator);
		assetCheckInfoMapper.updateByPrimaryKeySelective(ai);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Boolean confrimCheckInfoForSystemToCounty(Long countyOrgId, String operator) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCheckerAreaIdEqualTo(countyOrgId);
		criteria.andCheckerAreaTypeEqualTo(AssetUseAreaTypeEnum.COUNTY.getCode());
		criteria.andStatusIn(AssetCheckInfoStatusEnum.getCanConfirmList());
		List<AssetCheckInfo> aiList = assetCheckInfoMapper.selectByExample(example);
		for (AssetCheckInfo ai : aiList) {
			if ((AssetCheckInfoStatusEnum.SYS_CONFIRM.getCode().equals(ai.getStatus())
					|| AssetCheckInfoStatusEnum.ZB_CONFIRM.getCode().equals(ai.getStatus()))
					&& ai.getAssetId() != null) {// 已确认 直接返回
				continue;
			}
			if (AssetCheckInfoCheckTypeEnum.COMMON.getCode().equals(ai.getCheckType())) {// 正常提报
				if (StringUtils.isNoneEmpty(ai.getSerialNo())) {
					Asset a = assetBO.getAssetBySerialNo(ai.getSerialNo());
					if ((!AssetCheckStatusEnum.CHECKED.getCode().equals(a.getCheckStatus()))
							&& a.getUseAreaId().equals(ai.getCountyOrgId())) {// 同县
						assetBO.confrimCheckInfoForSystemToCounty(a);
						confirmBySystem(ai.getId(), a.getId(), operator);
						continue;
					}
				}
			}
			doneStation(ai.getId(), operator);
		}
		
		return Boolean.TRUE;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void confirmSelect(List<Long> infoIds, Long countyOrgId, String categoryType, OperatorDto ope) {
		if (CollectionUtils.isEmpty(infoIds)) {
			return;
		}
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andIdIn(infoIds);
		List<AssetCheckInfo> infoList =  assetCheckInfoMapper.selectByExample(example);
		
		List<String> assetTypeList = infoList.stream().map(AssetCheckInfo::getAssetType).collect(Collectors.toList());
		if (assetTypeList.contains(AssetCheckInfoAssetTypeEnum.IT.getCode())) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "选择的资产必须是行政资产");
		}
//		List<String> statusList = infoList.stream().map(AssetCheckInfo::getStatus).collect(Collectors.toList());
//		statusList.contains(AssetCheckInfoStatusEnum.SYS_CONFIRM.getCode()) ||
//		statusList.contains(AssetCheckInfoStatusEnum.ZB_CONFIRM.getCode())
		if (!infoList.stream().allMatch(assetCheckInfo -> AssetCheckInfoStatusEnum.TASK_DONE.getCode().equals(assetCheckInfo.getStatus()))) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "选择的资产状态必须是任务完成");
		}
		
		if (!infoList.stream().allMatch(assetCheckInfo -> countyOrgId.equals(assetCheckInfo.getCountyOrgId()))) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "选择的资产所属县域和条件不同");
		}
		if (!infoList.stream().allMatch(assetCheckInfo -> categoryType.equals(assetCheckInfo.getCategory()))) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "选择的资产类型和条件不同");
		}
		
		List<Asset> assets = assetBO.getWaitCheckAsset(categoryType, countyOrgId);
		if (CollectionUtils.isNotEmpty(assets)) {
			if (assets.size() >= infoIds.size()) {
				for (int i = 0; i < infoIds.size(); i++) {
					confirmForXzByZb(infoIds, ope, assets, i);
				}
			} else {
				for (int i = 0; i < assets.size(); i++) {
					confirmForXzByZb(infoIds, ope, assets, i);
				}
			}
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	private void confirmForXzByZb(List<Long> infoIds, OperatorDto ope, List<Asset> assets, int i) {
		Long infoId = infoIds.get(i);
		Asset a = assets.get(i);
		assetBO.confirmForZb(a.getId(), ope.getOperator());
		
		AssetCheckInfo ai = new AssetCheckInfo();
		ai.setId(infoId);
		ai.setAssetId(a.getId());
		ai.setStatus(AssetCheckInfoStatusEnum.ZB_CONFIRM.getCode());
		DomainUtils.beforeUpdate(ai, ope.getOperator());
		assetCheckInfoMapper.updateByPrimaryKeySelective(ai);
	}

	@Override
	public List<AssetCheckInfo> getInfoByCountyOrgId(Long countyOrgId) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCountyOrgIdEqualTo(countyOrgId);
		return assetCheckInfoMapper.selectByExample(example);
	}


	@Override
	public List<AssetCheckInfo> getInfoByCountyOrgIdToCountyCheck(Long countyOrgId) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCheckerAreaIdEqualTo(countyOrgId);
		criteria.andCheckerAreaTypeEqualTo(AssetUseAreaTypeEnum.COUNTY.getCode());
		return assetCheckInfoMapper.selectByExample(example);
	}
	
	
	private List<AssetCheckInfo> getInfoByTaobaoUserIdToStationCheck(Long taobaoUserId) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCheckerIdEqualTo(String.valueOf(taobaoUserId));
		criteria.andCheckerAreaTypeEqualTo(AssetUseAreaTypeEnum.STATION.getCode());
		return assetCheckInfoMapper.selectByExample(example);
	}

	

}
