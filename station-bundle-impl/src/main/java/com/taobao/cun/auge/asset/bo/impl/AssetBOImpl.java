package com.taobao.cun.auge.asset.bo.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.AssetSignDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.asset.enums.AssetStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetExample;
import com.taobao.cun.auge.dal.mapper.AssetMapper;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.bo.StationBO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.service.AssetQueryCondition;
import com.taobao.cun.auge.asset.service.CuntaoAssetDto;
import com.taobao.cun.auge.asset.service.CuntaoAssetEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.CuntaoAsset;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExample;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExample.Criteria;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExtExample;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetExtMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetMapper;
import com.taobao.cun.auge.event.AssetChangeEvent;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
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

	@Resource
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;

	@Autowired
	private CountyStationBO countyStationBO;

	@Autowired
    private StationBO stationBO;

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
		PageDto<String> result = PageDtoUtil.success(page, page.getResult());
		return result;
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
			listDto.setOwnerArea(countyStationBO.getCountyStationById(asset.getOwnerOrgId()).getName());
			listDto.setOwner(asset.getOwnerName());
			listDto.setTotal(String.valueOf(list.size()));
			listDto.setStatusMap(buildStatusMap(list));
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
            dto.setOwnerArea(countyStationBO.getCountyStationById(asset.getOwnerOrgId()).getName());
			dto.setOwner(asset.getOwnerName());
            if (AssetUseAreaTypeEnum.COUNTY.getCode().equals(asset.getUseAreaType())) {
                dto.setUseArea(countyStationBO.getCountyStationById(asset.getUseAreaId()).getName());
            } else if (AssetUseAreaTypeEnum.STATION.getCode().equals(asset.getUseAreaType())) {
                dto.setUseArea(stationBO.getStationById(asset.getUseAreaId()).getName());
            }
			dto.setUseAreaType(asset.getUseAreaType());
			dto.setUseAreaId(asset.getUseAreaId());
			dto.setStatusMap(buildStatusMap(list));
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
		AssetExample.Criteria criteria = assetExample.createCriteria();
		criteria.andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(condition.getWorkNo()).
			andCategoryEqualTo(condition.getCategory()).andUseAreaTypeEqualTo(condition.getUseAreaType());
		//组织头部
		List<Asset> preAssets = assetMapper.selectByExample(assetExample);
		if (CollectionUtils.isEmpty(preAssets)) {
			return null;
		}
		CategoryAssetDetailDto assetDetailDto = new CategoryAssetDetailDto();
		assetDetailDto.setCategory(condition.getCategory());
		assetDetailDto.setTotal(String.valueOf(preAssets.size()));
		assetDetailDto.setStatusMap(buildStatusMap(preAssets));
		assetDetailDto.setOwnerArea(countyStationBO.getCountyStationById(preAssets.get(0).getOwnerOrgId()).getName());
		assetDetailDto.setOwner(preAssets.get(0).getOwnerName());
		//组织尾巴
		if (StringUtils.isNotEmpty(condition.getCheckStatus())) {
			criteria.andCheckStatusEqualTo(condition.getCheckStatus());
		}
		if (StringUtils.isNotEmpty(condition.getStatus())) {
			criteria.andStatusEqualTo(condition.getStatus());
		}
		if (StringUtils.isNotEmpty(condition.getAliNo())) {
			criteria.andAliNoEqualTo(condition.getAliNo());
		}
		PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
		List<Asset> assets = assetMapper.selectByExample(assetExample);
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
		AssetExample.Criteria criteria = assetExample.createCriteria();
		criteria.andIsDeletedEqualTo("n").andUseAreaTypeEqualTo(condition.getUseAreaType()).andOwnerWorknoEqualTo(condition.getWorkNo()).andUseAreaIdEqualTo(condition.getUseAreaId());
		//组织头部
		List<Asset> preAssets = assetMapper.selectByExample(assetExample);
		if (CollectionUtils.isEmpty(preAssets)) {
			return null;
		}
		AreaAssetDetailDto assetDetailDto = new AreaAssetDetailDto();
		assetDetailDto.setOwnerArea(countyStationBO.getCountyStationById(preAssets.get(0).getOwnerOrgId()).getName());
		assetDetailDto.setOwner(preAssets.get(0).getOwnerName());
		assetDetailDto.setCategoryCountDtoList(buildAssetCountDtoList(preAssets));
		//组织尾巴
		if (StringUtils.isNotEmpty(condition.getCheckStatus())) {
			criteria.andCheckStatusEqualTo(condition.getCheckStatus());
		}
		if (StringUtils.isNotEmpty(condition.getStatus())) {
			criteria.andStatusEqualTo(condition.getStatus());
		}
		if (StringUtils.isNotEmpty(condition.getAliNo())) {
			criteria.andAliNoEqualTo(condition.getAliNo());
		}
		PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
		List<Asset> assets = assetMapper.selectByExample(assetExample);
		Page<Asset> assetPage = (Page<Asset>) assets;
		assetDetailDto.setDetailList(PageDtoUtil.success(assetPage, buildAssetDetailDtoList(assets)));
		return assetDetailDto;
	}

	@Override
	public Boolean signAsset(AssetSignDto signDto) {
		Objects.requireNonNull(signDto.getAliNo(), "编号不能为空");
		Objects.requireNonNull(signDto.getWorkNo(), "工号不能为空");
		Objects.requireNonNull(signDto.getOperatorOrgId(), "组织不能为空");
		AssetExample assetExample = new AssetExample();
		assetExample.createCriteria().andIsDeletedEqualTo("n").andAliNoEqualTo(signDto.getAliNo()).andStatusIn(AssetStatusEnum.getCanSignStatusList());
		Asset asset = ResultUtils.selectOne(assetMapper.selectByExample(assetExample));
		if (asset == null) {
			throw new AugeBusinessException("入库失败，该资产不在系统中，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		if (!asset.getOwnerWorkno().equals(signDto.getWorkNo()) || !asset.getOwnerOrgId().equals(signDto.getOperatorOrgId())) {
			throw new AugeBusinessException("入库失败，该资产不属于您，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		Asset updateAsset = new Asset();
		updateAsset.setStatus(AssetStatusEnum.USE.getCode());
		updateAsset.setId(asset.getId());
		return assetMapper.updateByPrimaryKeySelective(updateAsset) > 0;
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
			assetCountDto.setTotal(String.valueOf(countEntry.getValue().size()));
			assetCountDto.setStatusMap(buildStatusMap(countEntry.getValue()));
			countList.add(assetCountDto);
		}
		return countList;
	}

	private List<AssetDetailDto> buildAssetDetailDtoList(List<Asset> assetList) {
        return assetList.stream().map(asset -> {
            AssetDetailDto detailDto = new AssetDetailDto();
            BeanUtils.copyProperties(asset, detailDto);
            if (AssetUseAreaTypeEnum.COUNTY.getCode().equals(asset.getUseAreaType())) {
                detailDto.setUseArea(countyStationBO.getCountyStationById(asset.getUseAreaId()).getName());
            } else if (AssetUseAreaTypeEnum.STATION.getCode().equals(asset.getUseAreaType())) {
                detailDto.setUseArea(stationBO.getStationById(asset.getUseAreaId()).getName());
            }
            detailDto.setStatus(AssetStatusEnum.valueof(asset.getStatus()));
            return detailDto;
        }).collect(Collectors.toList());
	}

	private Map<AssetStatusEnum, String> buildStatusMap(List<Asset> assetList) {
		Map<AssetStatusEnum, String> statusMap = new LinkedHashMap<>();
		if (CollectionUtils.isNotEmpty(assetList)) {
			statusMap.put(AssetStatusEnum.RECYCLE, String.valueOf(
				assetList.stream().filter(i -> AssetStatusEnum.RECYCLE.getCode().equals(i.getStatus())).count()));
			statusMap.put(AssetStatusEnum.DISTRIBUTE, String.valueOf(
				assetList.stream().filter(i -> AssetStatusEnum.DISTRIBUTE.getCode().equals(i.getStatus())).count()));
			statusMap.put(AssetStatusEnum.TRANSFER, String.valueOf(
				assetList.stream().filter(i -> AssetStatusEnum.TRANSFER.getCode().equals(i.getStatus())).count()));
		}
		return statusMap;
	}
}
