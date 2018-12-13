package com.taobao.cun.auge.asset.bo.impl;

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
			record.setCheckerAreaId(addDto.getOperatorOrgId());
			record.setCheckerAreaName(
					t.getOrgName());
			record.setCheckerAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
			record.setCheckerId(addDto.getOperator());
			record.setCheckerName(getWorkerName(addDto.getOperator()));
			record.setCountyOrgId(addDto.getOperatorOrgId());
			record.setTaskType(AssetCheckTaskTaskTypeEnum.COUNTY_CHECK.getCode());
			
			if (AssetCheckTaskTaskStatusEnum.DOING.getCode().equals(t.getTaskStatus())) {
				assetCheckTaskBO.doingTask(t.getId());
			}
			
		} else if (OperatorTypeEnum.HAVANA.getCode().equals(addDto.getOperatorType().getCode())) {// 村盘点
			validateAddStation(addDto);
			AssetCheckTask at = assetCheckTaskBO.getTaskForStation(addDto.getOperator());
			record.setCheckerAreaId(at.getStationId());
			record.setCheckerAreaName(at.getStationName());
			record.setCheckerAreaType(AssetUseAreaTypeEnum.STATION.getCode());
			record.setCheckerId(addDto.getOperator());
			record.setCheckerName(at.getCheckerName());
			record.setCountyOrgId(at.getOrgId());
			record.setTaskType(AssetCheckTaskTaskTypeEnum.STATION_CHECK.getCode());
			if (AssetCheckTaskTaskStatusEnum.DOING.getCode().equals(at.getTaskStatus())) {
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
		//TODO:ddfgdfgd
		if (a == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
					"当前序列号[" + addDto.getSerialNo() + "]线上不存在，请操作异常盘点");
		}
		

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

		assetBO.confirmForZb(a.getId(), operator.getOperator());
		ai.setAssetId(a.getId());
		ai.setStatus(AssetCheckInfoStatusEnum.ZB_CONFIRM.getCode());
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
		return assetCheckInfoDto;
	}

	@Override
	public PageDto<AssetCheckInfoDto> listInfo(AssetCheckInfoCondition param) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		if (param.getOrgId() != null) {
			criteria.andCheckerAreaIdEqualTo(param.getOrgId());
			criteria.andCheckerAreaTypeEqualTo(AssetUseAreaTypeEnum.COUNTY.getCode());
		}
		// checkType 不为null表示 查询“异常提报”，“正常提报”中的一种
		// checkType 为null 表示查询“异常提报”和“正常提报”
		if (param.getCheckType() != null) {
			criteria.andCheckTypeEqualTo(param.getCheckType());
		}
		if (param.getCategoryType() != null) {
			criteria.andCategoryEqualTo(param.getCategoryType());
		}
		if (param.getTaobaoUserId() != null) {
			criteria.andCheckerIdEqualTo(String.valueOf(param.getTaobaoUserId()));
			criteria.andCheckerAreaTypeEqualTo(AssetUseAreaTypeEnum.STATION.getCode());
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
	public CountyFollowCheckCountDto getCountyFollowCheckCount(Long countyOrgId) {
		CountyFollowCheckCountDto fDto = new CountyFollowCheckCountDto();
		Long doneCount = 0L;
		List<AssetCheckInfo> l = getInfoByCountyOrgId(countyOrgId);
		if (CollectionUtils.isNotEmpty(l)) {
			doneCount = new Long(l.size());
		}
		List<Asset> a = assetBO.getCheckAsset(countyOrgId);
		Long count = new Long(a.size());
		Long doing = count - doneCount;
		fDto.setDoneCount(doneCount);
		fDto.setDoingCount(doing < 0 ? 0 : doing);
		Map<String, Long> aDtail = new HashMap<String, Long>();
		for (Asset b : a) {
			String categroyType = bulidCategoryType(b.getModel(), b.getCategory());
			if (aDtail.get(categroyType) != null) {
				aDtail.put(categroyType, aDtail.get(categroyType) + 1L);
			} else {
				aDtail.put(categroyType, 1L);
			}
		}
		if (doneCount > 0) {
			Map<String, Long> dDtail = l.stream()
					.collect(Collectors.groupingBy(AssetCheckInfo::getCategory, Collectors.counting()));
			fDto.setDoneDetail(dDtail);
			for (Map.Entry<String, Long> entry : aDtail.entrySet()) {
				if (dDtail.get(entry.getKey()) != null) {
					Long sub = entry.getValue() - dDtail.get(entry.getKey());
					aDtail.put(entry.getKey(), sub < 0 ? 0L : sub);
				}
			}
		}
		fDto.setDoingDetail(aDtail);
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
		if (!(categoryList.contains("TV") || !categoryList.contains("MAIN") || !categoryList.contains("DISPLAY"))) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "完成盘点失败：盘点资产必须为1台电视,1台显示器,1台主机");
		}
		for (AssetCheckInfo ai : aiList) {
			if (AssetCheckInfoCheckTypeEnum.COMMON.getCode().equals(ai.getCheckType())) {// 正常提报
				if ((AssetCheckInfoStatusEnum.SYS_CONFIRM.getCode().equals(ai.getStatus())
						|| AssetCheckInfoStatusEnum.ZB_CONFIRM.getCode().equals(ai.getStatus()))
						&& ai.getAssetId() != null) {// 已确认 直接返回
					continue;
				}
				if (StringUtils.isNoneEmpty(ai.getSerialNo())) {
					Asset a = assetBO.getAssetBySerialNo(ai.getSerialNo());
					if ((!AssetCheckStatusEnum.CHECKED.getCode().equals(a.getCheckStatus()))
							&& a.getUseAreaId().equals(ai.getCountyOrgId())) {// 同县
						assetBO.confrimCheckInfoForSystemToStation(a, stationId, checkerId, checkerName);
						confirmBySystem(ai.getId(), a.getId(), checkerId);
					}

				}
			}
		}
		return Boolean.TRUE;
	}

	private void confirmBySystem(Long aiId, Long assetId, String operator) {
		AssetCheckInfo ai = new AssetCheckInfo();
		ai.setAssetId(assetId);
		ai.setId(aiId);
		ai.setStatus(AssetCheckInfoStatusEnum.SYS_CONFIRM.getCode());
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
			if (AssetCheckInfoCheckTypeEnum.COMMON.getCode().equals(ai.getCheckType())) {// 正常提报
				if (StringUtils.isNoneEmpty(ai.getSerialNo())) {
					Asset a = assetBO.getAssetBySerialNo(ai.getSerialNo());
					if ((!AssetCheckStatusEnum.CHECKED.getCode().equals(a.getCheckStatus()))
							&& a.getUseAreaId().equals(ai.getCountyOrgId())) {// 同县
						assetBO.confrimCheckInfoForSystemToCounty(a);
						confirmBySystem(ai.getId(), a.getId(), operator);
					}
				}
			}
		}
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void confirmSelect(List<Long> infoIds, Long countyOrgId, String categoryType, OperatorDto ope) {
		if (CollectionUtils.isEmpty(infoIds)) {
			return;
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
		criteria.andCheckerAreaIdEqualTo(countyOrgId);
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

}
