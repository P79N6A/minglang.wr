package com.taobao.cun.auge.asset.bo.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import com.taobao.cun.crius.event.ExtEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.dto.AssetCheckDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetDistributeDto;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.AssetSignEvent;
import com.taobao.cun.auge.asset.dto.AssetSignEvent.Content;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.asset.enums.AssetCheckStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.RecycleStatusEnum;
import com.taobao.cun.auge.asset.service.AssetQueryCondition;
import com.taobao.cun.auge.asset.service.CuntaoAssetDto;
import com.taobao.cun.auge.asset.service.CuntaoAssetEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetExample;
import com.taobao.cun.auge.dal.domain.CuntaoAsset;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExample;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExample.Criteria;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExtExample;
import com.taobao.cun.auge.dal.mapper.AssetMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetExtMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetMapper;
import com.taobao.cun.auge.event.AssetChangeEvent;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.hsf.util.RequestCtxUtil;

@Component
public class AssetBOImpl implements AssetBO {

	@Autowired
	private AssetMapper assetMapper;

	@Autowired
	private CuntaoAssetMapper cuntaoAssetMapper;
	
	@Autowired
	private CuntaoAssetExtMapper cuntaoAssetExtMapper;

	@Autowired
	private PartnerInstanceQueryService partnerInstanceQueryService;

	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;

	@Autowired
    private StationBO stationBO;

	@Autowired
	private DiamondConfiguredProperties configuredProperties;

	private static final String ASSET_SIGN = "assetSign";
	
	private static final String ASSET_CHECK = "assetCheck";
	
	private static final String ASEET_CATEGORY_YUNOS = "云OS";
	
	@Autowired
	private Emp360Adapter emp360Adapter;
	
	@Autowired
	private UicReadAdapter uicReadAdapter;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void saveCuntaoAsset(CuntaoAssetDto cuntaoAssetDto,String operator) {
	
		Assert.notNull(cuntaoAssetDto,"cuntaoAssetDto can not be null");
		if(cuntaoAssetDto.getId() == null){
			if(cuntaoAssetDto.getStationId() != null){
				Long stationId = partnerInstanceQueryService.findStationIdByStationApplyId(Long.parseLong(cuntaoAssetDto.getStationId()));
				Long partnerInstanceId = partnerInstanceQueryService.getPartnerInstanceId(Long.valueOf(cuntaoAssetDto.getStationId()));
				cuntaoAssetDto.setNewStationId(stationId);
				cuntaoAssetDto.setPartnerInstanceId(partnerInstanceId);
			}
			CuntaoAsset asset = convert2CuntaoAsset(cuntaoAssetDto);
			cuntaoAssetMapper.insertSelective(asset);
		}else{
			CuntaoAsset cuntaoAsset = cuntaoAssetMapper.selectByPrimaryKey(cuntaoAssetDto.getId());
			cuntaoAssetMapper.updateByPrimaryKeySelective(convert2CuntaoAsset(cuntaoAssetDto));
			if (!CuntaoAssetEnum.STATION_SIGN.getCode().equals(cuntaoAsset.getStatus())&&CuntaoAssetEnum.STATION_SIGN.getCode().equals(cuntaoAssetDto.getStatus())){
				AssetChangeEvent event = buildAssetChangeEvent(cuntaoAssetDto.getId(),ASSET_SIGN,operator,cuntaoAssetDto.getStatus());
				EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
			}
			if (!CuntaoAssetEnum.CHECKED.getCode().equals(cuntaoAsset.getCheckStatus())&&CuntaoAssetEnum.CHECKED.getCode().equals(cuntaoAssetDto.getCheckStatus())){
				AssetChangeEvent event = buildAssetChangeEvent(cuntaoAssetDto.getId(),ASSET_CHECK,operator,cuntaoAssetDto.getCheckStatus());
				EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
			}
		}
	}

	private AssetChangeEvent buildAssetChangeEvent(Long assetId,String type,String operator,String desc) {
		AssetChangeEvent event = new AssetChangeEvent();
		event.setAssetId(assetId);
		event.setOperateTime(new Date());
		event.setDescription(desc);
		event.setType(type);
		event.setOperatorId(operator);
		if("cuntaobops".equals(RequestCtxUtil.getAppNameOfClient())){
			String operatorName = emp360Adapter.getName(operator);
			event.setOperator(operatorName);
		}else if("cuntaoadmin".equals(RequestCtxUtil.getAppNameOfClient())){
			String taobaoNick = uicReadAdapter.getTaobaoNickByTaobaoUserId(Long.parseLong(operator));
			event.setOperator(taobaoNick);
		}else{
			event.setOperator(operator);
		}
		return event;
	}

	@Override
	public CuntaoAssetDto getCuntaoAssetById(Long cuntaoAssetId) {
		Assert.notNull(cuntaoAssetId,"cuntaoAssetId can not be null");
		return convert2CuntaoAssetDto(cuntaoAssetMapper.selectByPrimaryKey(cuntaoAssetId));
	}

	@Override
	public PageDto<CuntaoAssetDto> queryByPage(AssetQueryCondition cuntaoAssetQueryCondition) {
		
		PageHelper.startPage(cuntaoAssetQueryCondition.getPageNum(), cuntaoAssetQueryCondition.getPageSize());
		CuntaoAssetExtExample example = new CuntaoAssetExtExample();
		Criteria cri = example.createCriteria();
		if(CollectionUtils.isNotEmpty(cuntaoAssetQueryCondition.getStates())){
			cri.andStatusIn(cuntaoAssetQueryCondition.getStates());
		}
		if(CollectionUtils.isNotEmpty(cuntaoAssetQueryCondition.getNoStates())){
			cri.andStatusNotIn(cuntaoAssetQueryCondition.getStates());
		}
		if(StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getRole())){
			cri.andOperatorRoleEqualTo(cuntaoAssetQueryCondition.getRole());
		}
		if(StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getAliNo())){
			cri.andAliNoEqualTo(cuntaoAssetQueryCondition.getAliNo());
		}
		
		if(StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getBoNo())){
			cri.andBoNoEqualTo(cuntaoAssetQueryCondition.getBoNo());
		}
		
		if(StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getStatus())){
			cri.andStatusEqualTo(cuntaoAssetQueryCondition.getStatus());
		}
		
		if(StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getProvince())){
			cri.andProvinceEqualTo(cuntaoAssetQueryCondition.getProvince());
		}
		
		if(StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getCounty())){
			cri.andCountyEqualTo(cuntaoAssetQueryCondition.getCounty());
		}
		
		if(StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getSerialNo())){
			cri.andSerialNoEqualTo(cuntaoAssetQueryCondition.getSerialNo());
		}
		
		if(StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getCheckStatus())){
			cri.andCheckStatusEqualTo(cuntaoAssetQueryCondition.getCheckStatus());
		}
		
		if(Objects.nonNull(cuntaoAssetQueryCondition.getStationId())){
			cri.andNewStationIdEqualTo(cuntaoAssetQueryCondition.getStationId());
		}
		
		if(Objects.nonNull(cuntaoAssetQueryCondition.getPartnerInstanceId())){
			cri.andPartnerInstanceIdEqualTo(cuntaoAssetQueryCondition.getPartnerInstanceId());
		}
		
		if(cuntaoAssetQueryCondition.getOrgId() != null){
			CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getCuntaoOrg(cuntaoAssetQueryCondition.getOrgId());
			if(cuntaoOrgDto!=null){
				example.setFullIdPath(cuntaoOrgDto.getFullIdPath());
			}
		}
		example.setOrderByClause("a.id desc");
		Page<CuntaoAsset> page =  (Page<CuntaoAsset>)cuntaoAssetExtMapper.selectByExample(example);
		List<CuntaoAssetDto> targetList = page.getResult().stream().map(source -> convert2CuntaoAssetDto(source)).collect(Collectors.toList());
		PageDto<CuntaoAssetDto> result = PageDtoUtil.success(page, targetList);
		return result;
	}



	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void signAsset(Long assetId, String operator) {
		Assert.notNull(assetId,"assetId can not be null");
		Assert.notNull(operator,"operator can not be null");
		CuntaoAsset asset = new CuntaoAsset();
		asset.setId(assetId);
		asset.setModifier(operator);
		asset.setGmtModified(new Date());
		asset.setStatus(CuntaoAssetEnum.STATION_SIGN.getCode());
		asset.setReceiver(operator);
		asset.setOperator(operator);
		asset.setOperatorRole(CuntaoAssetEnum.PARTNER.getCode());
		asset.setOperateTime(new Date());
		cuntaoAssetMapper.updateByPrimaryKeySelective(asset);
		AssetChangeEvent event = buildAssetChangeEvent(assetId,ASSET_CHECK,operator,asset.getCheckStatus());
		EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void checkAsset(Long assetId,String operator,CuntaoAssetEnum checkRole) {
		Assert.notNull(assetId,"assetId can not be null");
		Assert.notNull(operator,"operator can not be null");
		CuntaoAsset asset = new CuntaoAsset();
		asset.setModifier(operator);
		asset.setGmtModified(new Date());
		asset.setId(assetId);
		asset.setCheckStatus(CuntaoAssetEnum.CHECKED.getCode());
		asset.setCheckTime(new Date());
		asset.setCheckOperator(operator);
		asset.setCheckRole(checkRole.getCode());
		cuntaoAssetMapper.updateByPrimaryKeySelective(asset);
		AssetChangeEvent event = buildAssetChangeEvent(assetId,ASSET_SIGN,operator,asset.getStatus());
		EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void callbackAsset(Long assetId,String operator) {
		Assert.notNull(assetId,"assetId can not be null");
		Assert.notNull(operator,"operator can not be null");
		CuntaoAsset asset = cuntaoAssetMapper.selectByPrimaryKey(assetId);
		asset.setStatus(CuntaoAssetEnum.COUNTY_SIGN.getCode());
		asset.setModifier(operator);
		asset.setGmtModified(new Date());
		asset.setStationId(null);
		asset.setNewStationId(null);
		asset.setPartnerInstanceId(null);
		cuntaoAssetMapper.updateByPrimaryKey(asset);
		AssetChangeEvent event = buildAssetChangeEvent(assetId,ASSET_SIGN,operator,asset.getStatus());
		EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
	}

	@Override
	//TODO
	public void updateAssetByMobile(CuntaoAssetDto cuntaoAssetDto) {
		// TODO Auto-generated method stub
		//cuntaoAssetMapper.updateByPrimaryKey(asset);
	}

	@Override
	public void deleteAsset(Long assetId,String operator) {
		CuntaoAsset record = new CuntaoAsset();
		record.setId(assetId);
		record.setIsDeleted("y");
		record.setModifier(operator);
		record.setGmtModified(new Date());
		cuntaoAssetMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	@Deprecated
	public PageDto<CuntaoAssetDto> queryByPageMobile(AssetQueryCondition cuntaoAssetQueryCondition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDto<String> getBoNoByOrgId(Long orgId,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		CuntaoAssetExtExample example = new CuntaoAssetExtExample();
		Criteria cri = example.createCriteria();
		cri.andStatusNotEqualTo("UNMATCH");
		if(orgId != null){
			CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getCuntaoOrg(orgId);
			if(cuntaoOrgDto!=null){
				example.setFullIdPath(cuntaoOrgDto.getFullIdPath());
			}
		}
		cri.andIsDeletedEqualTo("n");
		Page<String> page =  (Page<String>)cuntaoAssetExtMapper.selectBoNoByExample(example);
		return PageDtoUtil.success(page, page.getResult());
	}

	@Override
	public void checkingAssetBatch(List<Long> assetIds,String operator) {
		Assert.notNull(assetIds);
		Assert.notNull(operator);
		CuntaoAsset record = new CuntaoAsset();
		record.setCheckStatus(CuntaoAssetEnum.CHECKING.getCode());
		record.setOperator(operator);
		CuntaoAssetExample example = new CuntaoAssetExample();
		example.createCriteria().andIdIn(assetIds);
		this.cuntaoAssetMapper.updateByExampleSelective(record, example);
	}

	@Override
	public CuntaoAssetDto queryAssetByUserAndCategory(Long userid) {
		Long stationId = partnerInstanceQueryService.getCurStationIdByTaobaoUserId(userid);
		CuntaoAssetExample example = new CuntaoAssetExample();
		example.createCriteria().andNewStationIdEqualTo(stationId).andCategoryEqualTo(ASEET_CATEGORY_YUNOS);
		List<CuntaoAsset>  assets = cuntaoAssetMapper.selectByExample(example);
		if(CollectionUtils.isNotEmpty(assets)){
			return this.convert2CuntaoAssetDto(assets.iterator().next());
		}
		return null;
	}

	@Override
	public CuntaoAssetDto queryAssetBySerialNo(String serialNo) {
		CuntaoAssetExample example = new CuntaoAssetExample();
		example.createCriteria().andSerialNoEqualTo(serialNo).andCategoryEqualTo(ASEET_CATEGORY_YUNOS);
		List<CuntaoAsset>  assets = cuntaoAssetMapper.selectByExample(example);
		if(CollectionUtils.isNotEmpty(assets)){
			return this.convert2CuntaoAssetDto(assets.iterator().next());
		}
		return null;
	}

	
	private CuntaoAsset convert2CuntaoAsset(CuntaoAssetDto cuntaoAssetDto) {
		CuntaoAsset cuntaoAsset = new CuntaoAsset();
		cuntaoAsset.setId(cuntaoAssetDto.getId());
		cuntaoAsset.setModifier("system");
		cuntaoAsset.setGmtModified(new Date());
		cuntaoAsset.setIsDeleted("n");
		cuntaoAsset.setCreator("system");
		cuntaoAsset.setGmtCreate(new Date());
		cuntaoAsset.setAliNo(cuntaoAssetDto.getAliNo());
		cuntaoAsset.setSerialNo(cuntaoAssetDto.getSerialNo());
		cuntaoAsset.setBrand(cuntaoAssetDto.getBrand());
		cuntaoAsset.setModel(cuntaoAssetDto.getModel());
		cuntaoAsset.setCategory(cuntaoAssetDto.getCategory());
		cuntaoAsset.setStatus(cuntaoAssetDto.getStatus());
		cuntaoAsset.setReceiver(cuntaoAssetDto.getReceiver());
		cuntaoAsset.setOperator(cuntaoAssetDto.getOperator());
		cuntaoAsset.setOperateTime(cuntaoAssetDto.getOperateTime());
		cuntaoAsset.setCounty(cuntaoAssetDto.getCounty());
		cuntaoAsset.setOrgId(cuntaoAssetDto.getOrgId());
		cuntaoAsset.setProvince(cuntaoAssetDto.getProvince());
		cuntaoAsset.setRemark(cuntaoAssetDto.getRemark());
		cuntaoAsset.setBoNo(cuntaoAssetDto.getBoNo());
		cuntaoAsset.setAssetOwner(cuntaoAssetDto.getAssetOwner());
		cuntaoAsset.setStationId(cuntaoAssetDto.getStationId());
		cuntaoAsset.setStationName(cuntaoAssetDto.getStationName());
		cuntaoAsset.setCheckStatus(cuntaoAssetDto.getCheckStatus());
		cuntaoAsset.setCheckTime(cuntaoAssetDto.getCheckTime());
		cuntaoAsset.setOperatorRole(cuntaoAssetDto.getOperatorRole());
		cuntaoAsset.setCheckOperator(cuntaoAssetDto.getCheckOperator());
		cuntaoAsset.setCheckRole(cuntaoAssetDto.getCheckRole());
		cuntaoAsset.setNewStationId(cuntaoAssetDto.getNewStationId());
		cuntaoAsset.setPartnerInstanceId(cuntaoAssetDto.getPartnerInstanceId());
		return cuntaoAsset;
	}
	
	private CuntaoAssetDto convert2CuntaoAssetDto(CuntaoAsset cuntaoAsset) {
		CuntaoAssetDto cuntaoAssetDto = new CuntaoAssetDto();
		if(cuntaoAsset != null){
			cuntaoAssetDto.setId(cuntaoAsset.getId());
			cuntaoAssetDto.setAliNo(cuntaoAsset.getAliNo());
			cuntaoAssetDto.setSerialNo(cuntaoAsset.getSerialNo());
			cuntaoAssetDto.setBrand(cuntaoAsset.getBrand());
			cuntaoAssetDto.setModel(cuntaoAsset.getModel());
			cuntaoAssetDto.setCategory(cuntaoAsset.getCategory());
			cuntaoAssetDto.setStatus(cuntaoAsset.getStatus());
			cuntaoAssetDto.setReceiver(cuntaoAsset.getReceiver());
			cuntaoAssetDto.setOperator(cuntaoAsset.getOperator());
			cuntaoAssetDto.setOperateTime(cuntaoAsset.getOperateTime());
			cuntaoAssetDto.setCounty(cuntaoAsset.getCounty());
			cuntaoAssetDto.setOrgId(cuntaoAsset.getOrgId());
			cuntaoAssetDto.setProvince(cuntaoAsset.getProvince());
			cuntaoAssetDto.setRemark(cuntaoAsset.getRemark());
			cuntaoAssetDto.setBoNo(cuntaoAsset.getBoNo());
			cuntaoAssetDto.setAssetOwner(cuntaoAsset.getAssetOwner());
			cuntaoAssetDto.setStationId(cuntaoAsset.getStationId());
			cuntaoAssetDto.setStationName(cuntaoAsset.getStationName());
			cuntaoAssetDto.setCheckStatus(cuntaoAsset.getCheckStatus());
			cuntaoAssetDto.setCheckTime(cuntaoAsset.getCheckTime());
			cuntaoAssetDto.setOperatorRole(cuntaoAsset.getOperatorRole());
			cuntaoAssetDto.setCheckOperator(cuntaoAsset.getCheckOperator());
			cuntaoAssetDto.setCheckRole(cuntaoAsset.getCheckRole());
		}
		return cuntaoAssetDto;
	}

	@Override
	public CuntaoAssetDto queryAssetByAliNoOrSerialNo(String serialNoOrAliNo) {
		CuntaoAssetExample example = new CuntaoAssetExample();
		List<CuntaoAsset>  assets = cuntaoAssetMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(assets)){
			throw new AugeBusinessException("can not find biz by serialNoOrAliNo["+serialNoOrAliNo+"]");
		}
		return convert2CuntaoAssetDto(assets.iterator().next());
	}

	@Override
	public List<CategoryAssetListDto> getCategoryAssetList(AssetOperatorDto operatorDto) {
		Objects.requireNonNull(operatorDto.getWorkNo(), "工号不能为空");
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(operatorDto.getWorkNo()).andStatusIn(AssetStatusEnum.getValidStatusList());
		List<Asset> assetList = assetMapper.selectByExample(assetExample);
		Map<String, List<Asset>> listMap = new HashMap<>();
		for (Asset asset : assetList) {
			listMap.computeIfAbsent(asset.getCategory(), k -> new ArrayList<>()).add(asset);
		}
		List<CategoryAssetListDto> categoryAssetList = new ArrayList<>();
		for (Entry<String, List<Asset>> entry : listMap.entrySet()) {
			CategoryAssetListDto listDto = new CategoryAssetListDto();
			List<Asset> list = entry.getValue();
			Asset asset = list.get(0);
			listDto.setCategory(entry.getKey());
			listDto.setCategoryName(configuredProperties.getCategoryMap().get(entry.getKey()));
			listDto.setOwnerArea(cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName());
			listDto.setOwner(asset.getOwnerName());
			listDto.setTotal(String.valueOf(list.size()));
			listDto.setPutAway(String.valueOf(
				list.stream().filter(i -> AssetStatusEnum.DISTRIBUTE.getCode().equals(i.getStatus()) || AssetStatusEnum.TRANSFER.getCode().equals(i.getStatus())).count()));
			categoryAssetList.add(listDto);
		}
		categoryAssetList.sort(Comparator.comparing(CategoryAssetListDto::getCategory));
		return categoryAssetList;
	}

	@Override
	public List<AreaAssetListDto> getAreaAssetList(AssetOperatorDto operatorDto) {
		Objects.requireNonNull(operatorDto.getWorkNo(), "工号不能为空");
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(operatorDto.getWorkNo()).andStatusIn(AssetStatusEnum.getValidStatusList());
		List<Asset> assetList = assetMapper.selectByExample(assetExample);
		Map<Long, List<Asset>> listMap = new HashMap<>();
		for (Asset asset : assetList) {
			listMap.computeIfAbsent(asset.getUseAreaId(), k -> new ArrayList<>()).add(asset);
		}
		List<AreaAssetListDto> dtoList = new ArrayList<>();
		for (Entry<Long, List<Asset>> entry : listMap.entrySet()) {
			AreaAssetListDto dto = new AreaAssetListDto();
			List<Asset> list = entry.getValue();
			Asset asset = list.get(0);
            dto.setOwnerArea(cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName());
			dto.setOwner(asset.getOwnerName());
            if (AssetUseAreaTypeEnum.COUNTY.getCode().equals(asset.getUseAreaType())) {
                dto.setUseArea(cuntaoOrgServiceClient.getCuntaoOrg(asset.getUseAreaId()).getName());
            } else if (AssetUseAreaTypeEnum.STATION.getCode().equals(asset.getUseAreaType())) {
                dto.setUseArea(stationBO.getStationById(asset.getUseAreaId()).getName());
            }
			dto.setUseAreaType(asset.getUseAreaType());
			dto.setUseAreaId(asset.getUseAreaId());
			dto.setPutAway(String.valueOf(
				list.stream().filter(i -> AssetStatusEnum.DISTRIBUTE.getCode().equals(i.getStatus()) || AssetStatusEnum.TRANSFER.getCode().equals(i.getStatus())).count()));
			dto.setCountList(buildAssetCountDtoList(list));
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public CategoryAssetDetailDto getCategoryAssetDetail(AssetDetailQueryCondition condition) {
		Objects.requireNonNull(condition.getWorkNo(), "工号不能为空");
		Objects.requireNonNull(condition.getCategory(), "资产种类不能为空");
		Objects.requireNonNull(condition.getUseAreaType(), "区域类型不能为空");
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(condition.getWorkNo()).
			andCategoryEqualTo(condition.getCategory()).andStatusIn(AssetStatusEnum.getValidStatusList());
		//组织头部
		List<Asset> preAssets = assetMapper.selectByExample(assetExample);
		if (CollectionUtils.isEmpty(preAssets)) {
			return null;
		}
		CategoryAssetDetailDto assetDetailDto = new CategoryAssetDetailDto();
		assetDetailDto.setCategory(condition.getCategory());
		assetDetailDto.setCategoryName(configuredProperties.getCategoryMap().get(condition.getCategory()));
		assetDetailDto.setTotal(String.valueOf(preAssets.size()));
		assetDetailDto.setPutAway(String.valueOf(
			preAssets.stream().filter(i -> AssetStatusEnum.DISTRIBUTE.getCode().equals(i.getStatus()) || AssetStatusEnum.TRANSFER.getCode().equals(i.getStatus())).count()));
		assetDetailDto.setOwnerArea(cuntaoOrgServiceClient.getCuntaoOrg(preAssets.get(0).getOwnerOrgId()).getName());
		assetDetailDto.setOwner(preAssets.get(0).getOwnerName());
		AssetExample conditionExample = new AssetExample();
		AssetExample.Criteria criteria = conditionExample.createCriteria();
		criteria.andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(condition.getWorkNo()).
			andCategoryEqualTo(condition.getCategory());
		//组织尾巴
		if (StringUtils.isNotEmpty(condition.getStatus())) {
			if ("Y".equals(condition.getStatus())) {
				criteria.andRecycleEqualTo(condition.getStatus());
				criteria.andStatusIn(AssetStatusEnum.getValidStatusList());
			} else if ("UNCHECKED".equals(condition.getStatus())) {
				criteria.andCheckStatusEqualTo(condition.getStatus());
				criteria.andStatusIn(AssetStatusEnum.getValidStatusList());
			} else {
				criteria.andStatusEqualTo(condition.getStatus());
			}
		} else {
			criteria.andStatusIn(AssetStatusEnum.getValidStatusList());
		}
		if (StringUtils.isNotEmpty(condition.getAliNo())) {
			criteria.andAliNoEqualTo(condition.getAliNo());
		}
		criteria.andUseAreaTypeEqualTo(condition.getUseAreaType());
		PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
		List<Asset> assets = assetMapper.selectByExample(conditionExample);
		Page<Asset> assetPage = (Page<Asset>) assets;
		assetDetailDto.setDetailList(PageDtoUtil.success(assetPage, buildAssetDetailDtoList(assets)));
		return assetDetailDto;
	}

	@Override
	public AreaAssetDetailDto getAreaAssetDetail(AssetDetailQueryCondition condition) {
		Objects.requireNonNull(condition.getWorkNo(), "工号不能为空");
		Objects.requireNonNull(condition.getUseAreaId(), "区域不能为空");
		Objects.requireNonNull(condition.getUseAreaType(), "区域类型不能为空");
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andUseAreaTypeEqualTo(condition.getUseAreaType()).andOwnerWorknoEqualTo(condition.getWorkNo()).andUseAreaIdEqualTo(condition.getUseAreaId()).andStatusIn(AssetStatusEnum.getValidStatusList());
		//组织头部
		List<Asset> preAssets = assetMapper.selectByExample(assetExample);
		if (CollectionUtils.isEmpty(preAssets)) {
			return null;
		}
		AreaAssetDetailDto assetDetailDto = new AreaAssetDetailDto();
		assetDetailDto.setOwnerArea(cuntaoOrgServiceClient.getCuntaoOrg(preAssets.get(0).getOwnerOrgId()).getName());
		assetDetailDto.setOwner(preAssets.get(0).getOwnerName());
		assetDetailDto.setCategoryCountDtoList(buildAssetCountDtoList(preAssets));
		AssetExample conditionExample = new AssetExample();
		AssetExample.Criteria criteria = conditionExample.createCriteria();
		criteria.andIsDeletedEqualTo("n").andUseAreaTypeEqualTo(condition.getUseAreaType()).andOwnerWorknoEqualTo(condition.getWorkNo()).andUseAreaIdEqualTo(condition.getUseAreaId());
		//组织尾巴
		if (StringUtils.isNotEmpty(condition.getStatus())) {
			if ("Y".equals(condition.getStatus())) {
				criteria.andRecycleEqualTo(condition.getStatus());
			} else if ("UNCHECKED".equals(condition.getStatus())) {
				criteria.andCheckStatusEqualTo(condition.getStatus());
			} else {
				criteria.andStatusEqualTo(condition.getStatus());
			}
		} else {
			criteria.andStatusIn(AssetStatusEnum.getValidStatusList());
		}
		if (StringUtils.isNotEmpty(condition.getAliNo())) {
			criteria.andAliNoEqualTo(condition.getAliNo());
		}
		if (StringUtils.isNotEmpty(condition.getCategory())) {
			criteria.andCategoryEqualTo(condition.getCategory());
		}
		PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
		List<Asset> assets = assetMapper.selectByExample(conditionExample);
		Page<Asset> assetPage = (Page<Asset>) assets;
		assetDetailDto.setDetailList(PageDtoUtil.success(assetPage, buildAssetDetailDtoList(assets)));
		return assetDetailDto;
	}

	@Override
	public AssetDetailDto signAssetByCounty(AssetDto signDto) {
		Objects.requireNonNull(signDto.getAliNo(), "编号不能为空");
		Objects.requireNonNull(signDto.getOperator(), "用户不能为空");
		Objects.requireNonNull(signDto.getOperatorOrgId(), "组织不能为空");
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andAliNoEqualTo(signDto.getAliNo()).andStatusIn(AssetStatusEnum.getCanCountySignStatusList());
		Asset asset = ResultUtils.selectOne(assetMapper.selectByExample(assetExample));
		if (asset == null) {
			throw new AugeBusinessException("入库失败"+AssetBO.NO_EXIT_ASSET+getPromptInfo(asset));
		}
		Asset updateAsset = new Asset();
		DomainUtils.beforeUpdate(updateAsset, signDto.getOperator());
		updateAsset.setStatus(AssetStatusEnum.USE.getCode());
		updateAsset.setId(asset.getId());
		updateAsset.setUseAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
		updateAsset.setUserId(signDto.getOperator());
		updateAsset.setUseAreaId(signDto.getOperatorOrgId());
		updateAsset.setUserName(emp360Adapter.getName(signDto.getOperator()));
		updateAsset.setOwnerName(emp360Adapter.getName(signDto.getOperator()));
		updateAsset.setOwnerOrgId(signDto.getOperatorOrgId());
		updateAsset.setOwnerWorkno(signDto.getOperator());
		boolean res = assetMapper.updateByPrimaryKeySelective(updateAsset) > 0;
		if (AssetStatusEnum.TRANSFER.getCode().equals(asset.getStatus()) && res) {
			sendSignMessage(asset.getOwnerWorkno(), updateAsset);
		}
		return buildAssetDetail(updateAsset);
	}

	private void sendSignMessage(String owner, Asset asset) {
		AssetSignEvent signEvent = new AssetSignEvent();
		signEvent.setAppId("cuntaoCRM");
		signEvent.setReceivers(Collections.singletonList(Long.valueOf(owner)));
		signEvent.setReceiverType("EMPIDS");
		signEvent.setMsgType("ASSET");
		signEvent.setMsgTypeDetail("SIGN");
		signEvent.setAction("all");
		Content content = signEvent.new Content();
		content.setBizId(asset.getId());
		content.setPublishTime(new Date());
		content.setTitle("您转移的资产已被对方签收，请关注！");
		content.setContent("您转移至" + cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName()+" " + emp360Adapter.getName(asset.getOwnerWorkno())+"的资产已被对方签收，查看详情");
		content.setRouteUrl("url");
		signEvent.setContent(content);
		EventDispatcherUtil.dispatch("CRM_ASSET_SIGN", new ExtEvent(JSON.toJSONString(signEvent)));
	}

	@Override
	public Boolean signAssetByStation(AssetDto signDto) {
		Objects.requireNonNull(signDto.getAliNo(), "编号不能为空");
		Objects.requireNonNull(signDto.getOperator(), "用户不能为空");
		Objects.requireNonNull(signDto.getOperatorOrgId(), "组织不能为空");
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andAliNoEqualTo(signDto.getAliNo()).andStatusEqualTo(AssetStatusEnum.DISTRIBUTE.getCode());
		Asset asset = ResultUtils.selectOne(assetMapper.selectByExample(assetExample));
		if (asset == null) {
			throw new AugeBusinessException("入库失败"+AssetBO.NO_EXIT_ASSET+getPromptInfo(asset));
		}
		if (!asset.getUserId().equals(signDto.getOperator()) || !asset.getUseAreaId().equals(signDto.getOperatorOrgId())) {
			throw new AugeBusinessException("入库失败"+AssetBO.NOT_OPERATOR+getPromptInfo(asset));
		}
		Asset updateAsset = new Asset();
		DomainUtils.beforeUpdate(updateAsset, signDto.getOperator());
		updateAsset.setStatus(AssetStatusEnum.USE.getCode());
		updateAsset.setId(asset.getId());
		updateAsset.setUseAreaType(AssetUseAreaTypeEnum.STATION.getCode());
		updateAsset.setUserId(signDto.getOperator());
		updateAsset.setUseAreaId(signDto.getOperatorOrgId());
		updateAsset.setUserName(uicReadAdapter.getFullName(Long.valueOf(signDto.getOperator())));
		return assetMapper.updateByPrimaryKeySelective(updateAsset) > 0;
	}

	@Override
	public AssetDetailDto recycleAsset(AssetDto signDto) {
		Objects.requireNonNull(signDto.getAliNo(), "编号不能为空");
		Objects.requireNonNull(signDto.getOperator(), "操作人不能为空");
		Objects.requireNonNull(signDto.getOperatorOrgId(), "组织不能为空");
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andAliNoEqualTo(signDto.getAliNo()).andRecycleEqualTo(
			RecycleStatusEnum.Y.getCode());
		Asset asset = ResultUtils.selectOne(assetMapper.selectByExample(assetExample));
		if (asset == null) {
			throw new AugeBusinessException("入库失败"+AssetBO.NO_EXIT_ASSET+getPromptInfo(asset));
		}
		if (!asset.getOwnerWorkno().equals(signDto.getOperator()) || !asset.getOwnerOrgId().equals(signDto.getOperatorOrgId())) {
			throw new AugeBusinessException("入库失败"+AssetBO.NOT_OPERATOR+getPromptInfo(asset));
		}
		Asset updateAsset = new Asset();
		DomainUtils.beforeUpdate(updateAsset, signDto.getOperator());
		updateAsset.setStatus(AssetStatusEnum.USE.getCode());
		updateAsset.setId(asset.getId());
		updateAsset.setUseAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
		updateAsset.setUserId(signDto.getOperator());
		updateAsset.setUseAreaId(signDto.getOperatorOrgId());
		updateAsset.setUserName(emp360Adapter.getName(signDto.getOperator()));
		assetMapper.updateByPrimaryKeySelective(updateAsset);
		return buildAssetDetail(updateAsset);
	}

	@Override
	public PageDto<AssetDetailDto> getTransferAssetList(AssetOperatorDto operator) {
		Objects.requireNonNull(operator.getWorkNo(), "工号不能为空");
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(operator.getWorkNo()).andStatusIn(AssetStatusEnum.getValidStatusList());
		PageHelper.startPage(operator.getPageNum(), operator.getPageSize());
		PageHelper.orderBy("status asc");
		List<Asset> assetList = assetMapper.selectByExample(assetExample);
		Page<Asset> assetPage = (Page<Asset>) assetList;
		return PageDtoUtil.success(assetPage, buildAssetDetailDtoList(assetList));
	}

	@Override
	public List<Asset> transferAssetSelfCounty(AssetTransferDto transferDto) {
		Objects.requireNonNull(transferDto.getOperator(), "工号不能为空");
		Objects.requireNonNull(transferDto.getReason(), "转移原因不能为空");
		Objects.requireNonNull(transferDto.getReceiverAreaId(), "接受区域不能为空");
		Objects.requireNonNull(transferDto.getReceiverWorkNo(), "接受人工号不能为空");
		AssetExample assetExample = new AssetExample();
		AssetExample.Criteria criteria = assetExample.createCriteria();
		criteria.andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(transferDto.getOperator());
		if (CollectionUtils.isNotEmpty(transferDto.getUnTransferAssetIdList())) {
			criteria.andIdNotIn(transferDto.getUnTransferAssetIdList());
		}
		List<Asset> assetList = assetMapper.selectByExample(assetExample);
		if (!assetList.stream().allMatch(asset -> AssetStatusEnum.USE.getCode().equals(asset.getStatus()))) {
			throw new AugeBusinessException("您转移的资产中包含待对方入库的资产");
		}
		Asset asset = new Asset();
		DomainUtils.beforeUpdate(asset, transferDto.getOperator());
		asset.setStatus(AssetStatusEnum.TRANSFER.getCode());
		assetMapper.updateByExampleSelective(asset, assetExample);
		return assetList;
	}
	

	@Override
	public List<Asset> transferAssetOtherCounty(AssetTransferDto transferDto) {
		Objects.requireNonNull(transferDto.getOperator(), "工号不能为空");
		Objects.requireNonNull(transferDto.getReason(), "转移原因不能为空");
		Objects.requireNonNull(transferDto.getReceiverAreaId(), "接受区域不能为空");
		Objects.requireNonNull(transferDto.getReceiverWorkNo(), "接受人工号不能为空");
		Objects.requireNonNull(transferDto.getPayment(), "物流费用不能为空");
		Objects.requireNonNull(transferDto.getDistance(), "运输距离不能为空");
		AssetExample assetExample = new AssetExample();
		AssetExample.Criteria criteria = assetExample.createCriteria();
		criteria.andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(transferDto.getOperator()).andIdIn(transferDto.getTransferAssetIdList());
		List<Asset> assetList = assetMapper.selectByExample(assetExample);
		if (!assetList.stream().allMatch(asset -> AssetStatusEnum.USE.getCode().equals(asset.getStatus()))) {
			throw new AugeBusinessException("您转移的资产中包含待对方入库的资产");
		}
		if (!assetList.stream().allMatch(asset -> AssetUseAreaTypeEnum.COUNTY.getCode().equals(asset.getUseAreaType()))) {
			throw new AugeBusinessException("您转移的资产中包含已下发至村点的资产");
		}
		Asset asset = new Asset();
		DomainUtils.beforeUpdate(asset, transferDto.getOperator());
		asset.setStatus(AssetStatusEnum.PEND.getCode());
		assetMapper.updateByExampleSelective(asset, assetExample);
		return assetList;
	}

	@Override
	public void agreeTransferAsset(AssetTransferDto transferDto) {
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andIdIn(transferDto.getTransferAssetIdList())
			.andStatusEqualTo(AssetStatusEnum.PEND.getCode());
		String name = emp360Adapter.getName(transferDto.getOperator());
		Asset asset = new Asset();
		asset.setStatus(AssetStatusEnum.USE.getCode());
		asset.setOwnerWorkno(transferDto.getOperator());
		asset.setOwnerOrgId(transferDto.getOperatorOrgId());
		asset.setOwnerName(name);
		asset.setUserName(name);
		asset.setUseAreaId(transferDto.getOperatorOrgId());
		asset.setUserId(transferDto.getOperator());
		DomainUtils.beforeUpdate(asset, transferDto.getOperator());
		assetMapper.updateByExampleSelective(asset, assetExample);
	}

	@Override
	public void disagreeTransferAsset(AssetTransferDto transferDto) {
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andIdIn(transferDto.getTransferAssetIdList())
			.andStatusEqualTo(AssetStatusEnum.PEND.getCode());
		Asset asset = new Asset();
		asset.setStatus(AssetStatusEnum.USE.getCode());
		DomainUtils.beforeUpdate(asset, transferDto.getOperator());
		assetMapper.updateByExampleSelective(asset, assetExample);
	}

	@Override
	public AssetDetailDto judgeTransfer(AssetDto assetDto) {
		Objects.requireNonNull(assetDto.getAliNo(), "编号不能为空");
		Objects.requireNonNull(assetDto.getOperator(), "操作人不能为空");
		Asset asset = getAssetByAliNo(assetDto.getAliNo());
		if (asset == null) {
			throw new AugeBusinessException("录入失败"+AssetBO.NO_EXIT_ASSET+getPromptInfo(asset));
		}
		if (!assetDto.getOperator().equals(assetDto.getOperator())) {
			throw new AugeBusinessException("录入失败"+AssetBO.NOT_OPERATOR+getPromptInfo(asset));
		}
		if (!AssetStatusEnum.USE.getCode().equals(asset.getStatus())) {
			throw new AugeBusinessException("录入失败，该资产正处于分发、转移中！"+getPromptInfo(asset));
		}
		return buildAssetDetail(asset);
	}
	
	private Asset getAssetByAliNo(String aliNo) {
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andAliNoEqualTo(aliNo);
		return ResultUtils.selectOne(assetMapper.selectByExample(assetExample));
	}

	/**
	 * 计算出不同类型的资产数量 电脑->20 显示器->10
	 * @param list
	 * @return
	 */
	private List<AssetCategoryCountDto> buildAssetCountDtoList(List<Asset> list) {
		Map<String, List<Asset>> countListMap = new HashMap<>();
		for (Asset countAsset : list) {
			countListMap.computeIfAbsent(countAsset.getCategory(), k -> new ArrayList<>()).add(countAsset);
		}
		List<AssetCategoryCountDto> countList = new ArrayList<>();
		for (Entry<String, List<Asset>> countEntry : countListMap.entrySet()) {
			AssetCategoryCountDto assetCountDto = new AssetCategoryCountDto();
			assetCountDto.setCategory(countEntry.getKey());
			assetCountDto.setCategoryName(configuredProperties.getCategoryMap().get(countEntry.getKey()));
			assetCountDto.setTotal(String.valueOf(countEntry.getValue().size()));
			assetCountDto.setPutAway(String.valueOf(
				countEntry.getValue().stream().filter(i -> AssetStatusEnum.DISTRIBUTE.getCode().equals(i.getStatus()) || AssetStatusEnum.TRANSFER.getCode().equals(i.getStatus())).count()));
			countList.add(assetCountDto);
		}
		return countList;
	}

	private List<AssetDetailDto> buildAssetDetailDtoList(List<Asset> assetList) {
        return assetList.stream().map(this::buildAssetDetail).collect(Collectors.toList());
	}
	@Override
	public AssetDetailDto buildAssetDetail(Asset asset) {
		AssetDetailDto detailDto = new AssetDetailDto();
		BeanUtils.copyProperties(asset, detailDto);
		if (AssetUseAreaTypeEnum.COUNTY.getCode().equals(asset.getUseAreaType())) {
			detailDto.setUseArea(cuntaoOrgServiceClient.getCuntaoOrg(asset.getUseAreaId()).getName());
		} else if (AssetUseAreaTypeEnum.STATION.getCode().equals(asset.getUseAreaType())) {
			detailDto.setUseArea(stationBO.getStationById(asset.getUseAreaId()).getName());
		}
		detailDto.setStatus(AssetStatusEnum.valueOf(asset.getStatus()));
		detailDto.setCategoryName(configuredProperties.getCategoryMap().get(asset.getCategory()));
		detailDto.setOwner(emp360Adapter.getName(asset.getOwnerWorkno()));
		detailDto.setOwnerArea(cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName());
		detailDto.setId(asset.getId());
		return detailDto;
	}

	@Override
	public Asset getAssetById(Long assetId) {
		Objects.requireNonNull(assetId, "资产id不能为空");
		return assetMapper.selectByPrimaryKey(assetId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void cancelAsset(List<Long> assetIds,String operator) {
		Objects.requireNonNull(assetIds, "资产列表不能为空");
		Objects.requireNonNull(operator, "操作人不能为空");
		Asset asset = new Asset();
		asset.setStatus(AssetStatusEnum.USE.getCode());
		DomainUtils.beforeUpdate(asset, operator);
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andIdIn(assetIds);
		assetMapper.updateByExampleSelective(asset, assetExample);
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<Asset> distributeAsset(AssetDistributeDto distributeDto) {
		ValidateUtils.validateParam(distributeDto);
		Objects.requireNonNull(distributeDto.getStationId(), "服务站id不能为空");
		Objects.requireNonNull(distributeDto.getAssetIdList(), "待分发资产不能为空");
		
		AssetExample assetExample = new AssetExample();
		AssetExample.Criteria criteria = assetExample.createCriteria();
		criteria.andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(distributeDto.getOperator()).andIdIn(distributeDto.getAssetIdList());
		List<Asset> assetList = assetMapper.selectByExample(assetExample);
		if (!assetList.stream().allMatch(asset -> AssetStatusEnum.USE.getCode().equals(asset.getStatus()))) {
			throw new AugeBusinessException("您分发的资产中包含待对方入库的资产!");
		}
		if (!assetList.stream().allMatch(asset -> AssetUseAreaTypeEnum.COUNTY.getCode().equals(asset.getUseAreaType()))) {
			throw new AugeBusinessException("您分发的资产中包含已下发至村点的资产!");
		}
		Asset asset = new Asset();
		DomainUtils.beforeUpdate(asset, distributeDto.getOperator());
		asset.setStatus(AssetStatusEnum.DISTRIBUTE.getCode());
		assetMapper.updateByExampleSelective(asset, assetExample);
		return assetList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Boolean checkAsset(AssetCheckDto checkDto) {
		ValidateUtils.validateParam(checkDto);
		Objects.requireNonNull(checkDto.getAliNo(), "盘点资产不能为空");
		Objects.requireNonNull(checkDto.getUserId(), "盘点人不能为空");
		Objects.requireNonNull(checkDto.getUseAreaType(), "盘点人区域类型不能为空");
		String aliNo = checkDto.getAliNo();
		String userId = checkDto.getUserId();
		String useAreaType = checkDto.getUseAreaType().getCode();
		
		Asset asset  = validateUserIdForAssetCheck(userId,useAreaType,aliNo);	
		
		Asset record = new Asset();
		DomainUtils.beforeUpdate(record, checkDto.getOperator());
		record.setCheckStatus(AssetCheckStatusEnum.CHECKED.getCode());
		record.setCheckTime(new Date());
		record.setId(asset.getId());
		assetMapper.updateByPrimaryKeySelective(record);
		return Boolean.TRUE;
	}
	
	private Asset validateUserIdForAssetCheck(String userId,String useAreaType,String aliNo) {
		Asset asset = getAssetByAliNo(aliNo);
		if (asset== null) {
			throw new AugeBusinessException("盘点失败"+AssetBO.NO_EXIT_ASSET+getPromptInfo(asset));
		}
		if (!asset.getUseAreaType().equals(useAreaType) && !asset.getUserId().equals(userId)) {
			throw new AugeBusinessException("盘点失败"+AssetBO.NOT_OPERATOR+getPromptInfo(asset));
		}
		return asset;
	}
	private String getPromptInfo(Asset asset) {
		StringBuilder sb =new StringBuilder();
		sb.append("资产编号：");
		sb.append(asset.getAliNo());
		sb.append(",资产名称：[");
		sb.append(configuredProperties.getCategoryMap().get(asset.getCategory()));
		sb.append("]");
		sb.append(asset.getBrand());
		sb.append(" ");
		sb.append(asset.getModel());
		sb.append(",责任地点：");
		sb.append(cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName());
		sb.append(",责任人：");
		sb.append(asset.getOwnerName());
		return sb.toString();
		

	}
}
