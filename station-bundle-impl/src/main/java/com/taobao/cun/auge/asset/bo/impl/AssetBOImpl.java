package com.taobao.cun.auge.asset.bo.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
	private CuntaoAssetMapper cuntaoAssetMapper;
	
	@Autowired
	private CuntaoAssetExtMapper cuntaoAssetExtMapper;
	
	private PartnerInstanceQueryService partnerInstanceQueryService;
	@Resource
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
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
		record.setStatus(CuntaoAssetEnum.CHECKING.getCode());
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

	
	
}
