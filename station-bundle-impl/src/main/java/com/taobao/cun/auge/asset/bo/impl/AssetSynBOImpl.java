package com.taobao.cun.auge.asset.bo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.it.asset.api.CuntaoApiService;
import com.alibaba.it.asset.api.dto.AssetApiResultDO;
import com.alibaba.it.asset.api.dto.PubResourceDto;

import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.bo.AssetSynBO;
import com.taobao.cun.auge.asset.convert.AssetOwner;
import com.taobao.cun.auge.asset.convert.AssetOwnerParser;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.enums.AssetCheckStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeApplierAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeSignTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutReceiverAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.RecycleStatusEnum;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetExample;
import com.taobao.cun.auge.dal.domain.AssetIncome;
import com.taobao.cun.auge.dal.domain.AssetIncomeExample;
import com.taobao.cun.auge.dal.domain.AssetIncomeExample.Criteria;
import com.taobao.cun.auge.dal.domain.AssetRollout;
import com.taobao.cun.auge.dal.domain.AssetRolloutExample;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.dal.domain.CuntaoAsset;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.AssetIncomeMapper;
import com.taobao.cun.auge.dal.mapper.AssetMapper;
import com.taobao.cun.auge.dal.mapper.AssetRolloutMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.user.service.CuntaoUserService;
import com.taobao.hsf.app.spring.util.annotation.HSFConsumer;
import com.taobao.mmp.client.cuic.transferobject.ResultTO;
import com.taobao.mmp.client.permission.domainobject.MmpPermissionDO;
import com.taobao.mmp.client.permission.service.MmpAuthReadService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AssetSynBOImpl implements AssetSynBO {

	private static final Logger logger = LoggerFactory
			.getLogger(AssetSynBO.class);

	@Autowired
	private CuntaoAssetMapper cuntaoAssetMapper;

	@Autowired
	private AssetRolloutBO assetRolloutBO;

	@Autowired
	private AssetIncomeBO assetIncomeBO;
	
	@Autowired
	private AssetBO assetBO;

	@Autowired
	private AssetRolloutIncomeDetailBO assetRolloutIncomeDetailBO;

	@Autowired
	private AssetMapper assetMapper;
	
	@Autowired
	private AssetIncomeMapper assetIncomeMapper;
	
	@Autowired
	private AssetRolloutMapper assetRolloutMapper;

	@Autowired
	private UicReadAdapter uicReadAdapter;

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
	private final static String CREATOR = "syncNewAsset-wsh22";
	
	@Autowired
	private StationBO stationBO;
	
    @Autowired
    private CuntaoUserService cuntaoUserService;
    
    @Autowired
    private MmpAuthReadService mmpAuthReadService;
    
    @HSFConsumer(serviceGroup = "${it.service.group}", serviceVersion = "${it.service.version}")
    private CuntaoApiService cuntaoApiService;
    
    public final static Map<String,String> catMap = new HashMap<String,String>();
    static {
    	catMap.put("主机", "MAIN");
    	catMap.put("一体机", "AIO");
    	catMap.put("显示器", "DISPLAY");
    	catMap.put("投影仪", "PROJECTOR");
    	catMap.put("行政资产", "ADMINISTRATION");
    	catMap.put("电视机", "TV");
    	catMap.put("打印机", "PRINTER");
    }

	@Override
	public Boolean syncAsset(List<Long> cuntaoAssetIds) {
		List<CuntaoAsset> assetList = new ArrayList<CuntaoAsset>();
		if (CollectionUtils.isNotEmpty(cuntaoAssetIds)) {//指定参数
			CuntaoAssetExample cuntaoAssetExample = new CuntaoAssetExample();
			cuntaoAssetExample.createCriteria().andIsDeletedEqualTo("n")//.andCreatorNotEqualTo(CREATOR)
					.andIdIn(cuntaoAssetIds);
			assetList = cuntaoAssetMapper.selectByExample(cuntaoAssetExample);
			batchSyn(assetList);
		} else {
			CuntaoAssetExample cuntaoAssetExample = new CuntaoAssetExample();
			List<String> vaildStatus = Arrays.asList("COUNTY_SIGN",
					"STATION_SIGN", "UNSIGN", "WAIT_STATION_SIGN");
			cuntaoAssetExample.createCriteria().andIsDeletedEqualTo("n")//.andCreatorNotEqualTo(CREATOR)
					.andStatusIn(vaildStatus);
			cuntaoAssetExample.setOrderByClause("id asc");
			int count = cuntaoAssetMapper.countByExample(cuntaoAssetExample);
			logger.info("sync asset begin,count={}", count);
			int pageSize = 200;
			int pageNum = 1;
			int total = count % pageSize == 0 ? count / pageSize : count
					/ pageSize + 1;
			while (pageNum <= total) {
				logger.info("sync-asset-doing {},{}",pageNum,pageSize);
				PageHelper.startPage(pageNum, pageSize);
				assetList = cuntaoAssetMapper
						.selectByExample(cuntaoAssetExample);
				batchSyn(assetList);
				pageNum++;
			}
		}
		logger.info("sync-asset-finish");
		return Boolean.TRUE;
	}
	private void  batchSyn(List<CuntaoAsset> assetList) {
		if (CollectionUtils.isNotEmpty(assetList)) {
			for (CuntaoAsset ca :assetList) {
				logger.info("sync asset,asset={}", JSONObject.toJSONString(ca));
				try {
					syn(ca);
				} catch (Exception e) {
					logger.error("sync asset error,asset="+JSONObject.toJSONString(ca),e);
				}
			}
		}
	}
	
	// 标记已同步
	private void setCuntaoAssetCreator(Long id) {
		CuntaoAsset asset = new CuntaoAsset();
		asset.setId(id);
		asset.setCreator(CREATOR);
		cuntaoAssetMapper.updateByPrimaryKeySelective(asset);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class)
	private void syn(CuntaoAsset ca) {
		if (CREATOR.equals(ca.getCreator())) {
			return;
		}
		if ("STATION_SIGN".equals(ca.getStatus())) {
			Asset a = bulidStationUseAsset(ca);
			 DomainUtils.beforeInsert(a, "sys");
	         assetMapper.insert(a);
	         //集团资产变更责任人
	         changeOwner(a);
		} else if ("COUNTY_SIGN".equals(ca.getStatus())) {
			Asset a = bulidCountyUseAsset(ca);
			DomainUtils.beforeInsert(a,"sys");
	         assetMapper.insert(a);
	         if (a.getStatus().equals(AssetStatusEnum.SCRAP.getCode())) {
	        	 //报废资产
	        	 //这个不用掉，业务已经维护
	         }else {
	        	 //集团资产变更责任人
		         changeOwner(a);
	         }
	      
		} else if ("UNSIGN".equals(ca.getStatus())) {
			Asset a = bulidSIGNAsset(ca);
			DomainUtils.beforeInsert(a, "sys");
			assetMapper.insert(a);
			//插入采购入库单
			addPurchaseIncomeInfo(a);
		}else if ("WAIT_STATION_SIGN".equals(ca.getStatus())) {
			Asset a = bulidDistributeAsset(ca);
			DomainUtils.beforeInsert(a, "sys");
			assetMapper.insert(a);
			 //集团资产变更责任人
	         changeOwner(a);
			//插入分发出库单
			addDistributeRolloutInfo(a,ca);
		}
		//修改资产创建人  标记已同步
		setCuntaoAssetCreator(ca.getId());
	}
	
	private void changeOwner(Asset a) {
		AssetDto  aDto = new AssetDto();
		aDto.setAliNo(a.getAliNo());
		aDto.setOperator(a.getOwnerWorkno());
		assetBO.transferItAsset(aDto);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	private void addDistributeRolloutInfo(Asset a,CuntaoAsset ca) {
		Long stationId = ca.getNewStationId();
		Station s = stationBO.getStationById(stationId);
		if (s == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"分发失败，服务站信息异常");
		}
		String sName = s.getName();
		
		PartnerStationRel psl = partnerInstanceBO
				.findPartnerInstanceById(ca.getPartnerInstanceId());
		
		Long rolloutId = null;
		AssetRollout ar = queryRollout(stationId,String.valueOf(psl.getTaobaoUserId()));
		if (ar == null) {
			//创建出库单
			AssetRolloutDto roDto = new AssetRolloutDto();
			roDto.setApplierWorkno(a.getOwnerWorkno());
			roDto.setApplierName(a.getOwnerName());
			roDto.setStatus(AssetRolloutStatusEnum.WAIT_ROLLOUT);
			roDto.setApplierOrgId(a.getOwnerOrgId());
			roDto.setApplierOrgName(cuntaoOrgServiceClient.getCuntaoOrg(a.getOwnerOrgId()).getName());
			roDto.setReceiverId(String.valueOf(psl.getTaobaoUserId()));
			String name = uicReadAdapter.getFullName(psl.getTaobaoUserId());
			roDto.setReceiverName(name);
			roDto.setReceiverAreaType(AssetRolloutReceiverAreaTypeEnum.STATION);
			roDto.setReceiverAreaName(sName);
			roDto.setReceiverAreaId(stationId);
			roDto.setRemark("分发至 "+sName +"-" +name);
			roDto.setType(AssetRolloutTypeEnum.DISTRIBUTION);
			roDto.copyOperatorDto(OperatorDto.defaultOperator());
			rolloutId = assetRolloutBO.addRollout(roDto);
		}else {
			rolloutId= ar.getId();
		}
		
		// 出入库单详情  入库单id 在审批通过时 创建 并更新上去
		AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
		detail.setAssetId(a.getId());
		detail.setCategory(a.getCategory());
		detail.setRolloutId(rolloutId);
		detail.setStatus(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN);
		detail.setType(AssetRolloutIncomeDetailTypeEnum.DISTRIBUTION);
		detail.setOperatorTime(new Date());
		detail.copyOperatorDto(OperatorDto.defaultOperator());
		assetRolloutIncomeDetailBO.addDetail(detail);
	}
	private  AssetRollout queryRollout(Long stationId,String taobaoUserId) {
		AssetRolloutExample example = new AssetRolloutExample();
		com.taobao.cun.auge.dal.domain.AssetRolloutExample.Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andReceiverAreaTypeEqualTo(AssetRolloutReceiverAreaTypeEnum.STATION.getCode());
		criteria.andReceiverAreaIdEqualTo(stationId);
		criteria.andReceiverIdEqualTo(taobaoUserId);
		criteria.andStatusEqualTo(AssetRolloutStatusEnum.WAIT_ROLLOUT.getCode());
		List<AssetRollout> aiList = assetRolloutMapper.selectByExample(example);
		return ResultUtils.selectOne(aiList);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	private void addPurchaseIncomeInfo(Asset a) {
		Long incomeId = null;
		AssetIncome ai = queryIncome(a.getPoNo(),a.getOwnerOrgId());
		if (ai == null) {//新增入库单
			AssetIncomeDto icDto = new AssetIncomeDto();
	        icDto.setApplierAreaId(0L);
	        icDto.setApplierAreaName("采购系统");
	        icDto.setApplierAreaType(AssetIncomeApplierAreaTypeEnum.CAIGOU);
	        icDto.setApplierId(a.getOwnerWorkno());
	        icDto.setApplierName(a.getOwnerName());//直接设置了接收人
	        icDto.setReceiverName(a.getOwnerName());
	        icDto.setReceiverOrgId(a.getOwnerOrgId());
	        icDto.setReceiverOrgName(cuntaoOrgServiceClient.getCuntaoOrg(a.getOwnerOrgId()).getName());
	        icDto.setReceiverWorkno(a.getOwnerWorkno());
	        icDto.setRemark("采购");
	        icDto.setStatus(AssetIncomeStatusEnum.TODO);
	        icDto.setType(AssetIncomeTypeEnum.PURCHASE);
	        icDto.setSignType(AssetIncomeSignTypeEnum.SCAN);
	        icDto.setPoNo(a.getPoNo());
	        icDto.copyOperatorDto(OperatorDto.defaultOperator());
	        incomeId = assetIncomeBO.addIncome(icDto);
		}else {
			incomeId= ai.getId();
		}
        Long assetId = a.getId();

        AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
        detail.setAssetId(assetId);
        detail.setCategory(a.getCategory());
        detail.setIncomeId(incomeId);
        detail.setStatus(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN);
        detail.setType(AssetRolloutIncomeDetailTypeEnum.PURCHASE);
        detail.setOperatorTime(new Date());
        detail.copyOperatorDto(OperatorDto.defaultOperator());
        assetRolloutIncomeDetailBO.addDetail(detail);
	}
	
	private  AssetIncome queryIncome(String poNo, Long orgId) {
		AssetIncomeExample example = new AssetIncomeExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andApplierAreaTypeEqualTo(AssetIncomeApplierAreaTypeEnum.CAIGOU.getCode());
		criteria.andPoNoEqualTo(poNo);
		criteria.andReceiverOrgIdEqualTo(orgId);
		List<AssetIncome> aiList = assetIncomeMapper.selectByExample(example);
		return ResultUtils.selectOne(aiList);
	}

	private Asset bulidCountyUseAsset(CuntaoAsset cuntaoAsset) {
		Asset a = new Asset();
		a.setAliNo(cuntaoAsset.getAliNo());
		a.setBrand(cuntaoAsset.getBrand());
		a.setCategory(catMap.get(cuntaoAsset.getCategory()));
		a.setModel(cuntaoAsset.getModel());
		
		a.setOwnerOrgId(Long.parseLong(cuntaoAsset.getOrgId()));
		
		AssetOwner ao= getAssetOwnerInfo(cuntaoAsset.getOrgId());
		a.setOwnerName(ao.getOwnerName());
		a.setOwnerWorkno(ao.getOwnerWorkno());
		
		a.setPoNo(cuntaoAsset.getBoNo());
		a.setSerialNo(cuntaoAsset.getSerialNo());
		if (isScrap(cuntaoAsset.getAssetOwner())) {// 如果名字是黄继英的   要设置为 已报废
			a.setStatus(AssetStatusEnum.SCRAP.getCode());
		}else {
			a.setStatus(AssetStatusEnum.USE.getCode());
		}
		a.setUseAreaId(Long.parseLong(cuntaoAsset.getOrgId()));
		a.setUseAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
		a.setUserId(ao.getOwnerWorkno());
		a.setUserName(ao.getOwnerName());
		a.setCheckStatus(cuntaoAsset.getCheckStatus());
		a.setCheckTime(cuntaoAsset.getCheckTime());
		a.setRecycle(RecycleStatusEnum.N.getCode());
		return a;
	}
	private Boolean isScrap(String assetOwner) {
		if ("樱橴(20502)".equals(assetOwner) || "黄继英".equals(assetOwner)) {
			return true;
		}
		return false;
	}

	private Asset bulidSIGNAsset(CuntaoAsset cuntaoAsset) {
		Asset a = new Asset();
		a.setAliNo(cuntaoAsset.getAliNo());
		a.setBrand(cuntaoAsset.getBrand());
		a.setCategory(catMap.get(cuntaoAsset.getCategory()));
		a.setModel(cuntaoAsset.getModel());
		a.setOwnerOrgId(Long.parseLong(cuntaoAsset.getOrgId()));
		
		AssetOwner ao= getAssetOwnerInfo(cuntaoAsset.getOrgId());
		a.setOwnerName(ao.getOwnerName());
		a.setOwnerWorkno(ao.getOwnerWorkno());
		
		a.setPoNo(cuntaoAsset.getBoNo());
		a.setSerialNo(cuntaoAsset.getSerialNo());
		a.setStatus(AssetStatusEnum.SIGN.getCode());
		a.setUseAreaId(Long.parseLong(cuntaoAsset.getOrgId()));
		a.setUseAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
		a.setUserId(ao.getOwnerWorkno());
		a.setUserName(ao.getOwnerName());
		a.setCheckStatus(AssetCheckStatusEnum.UNCHECKED.getCode());
		a.setCheckTime(cuntaoAsset.getCheckTime());
		a.setRecycle(RecycleStatusEnum.N.getCode());
		return a;
	}

	private Asset bulidDistributeAsset(CuntaoAsset cuntaoAsset) {
		Asset a = new Asset();
		a.setAliNo(cuntaoAsset.getAliNo());
		a.setBrand(cuntaoAsset.getBrand());
		a.setCategory(catMap.get(cuntaoAsset.getCategory()));
		a.setModel(cuntaoAsset.getModel());
		a.setOwnerOrgId(Long.parseLong(cuntaoAsset.getOrgId()));
		
		AssetOwner ao= getAssetOwnerInfo(cuntaoAsset.getOrgId());
		a.setOwnerName(ao.getOwnerName());
		a.setOwnerWorkno(ao.getOwnerWorkno());
		
		a.setPoNo(cuntaoAsset.getBoNo());
		a.setSerialNo(cuntaoAsset.getSerialNo());
		a.setStatus(AssetStatusEnum.DISTRIBUTE.getCode());
		a.setUseAreaId(Long.parseLong(cuntaoAsset.getOrgId()));
		a.setUseAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
		a.setUserId(ao.getOwnerWorkno());
		a.setUserName(ao.getOwnerName());
		a.setCheckStatus(cuntaoAsset.getCheckStatus());
		a.setCheckTime(cuntaoAsset.getCheckTime());
		a.setRecycle(RecycleStatusEnum.N.getCode());
		return a;
	}

	private Asset bulidStationUseAsset(CuntaoAsset cuntaoAsset) {
		Asset a = new Asset();
		a.setAliNo(cuntaoAsset.getAliNo());
		a.setBrand(cuntaoAsset.getBrand());
		a.setCategory(catMap.get(cuntaoAsset.getCategory()));
		a.setModel(cuntaoAsset.getModel());
		a.setOwnerOrgId(Long.parseLong(cuntaoAsset.getOrgId()));
		
		AssetOwner ao= getAssetOwnerInfo(cuntaoAsset.getOrgId());
		a.setOwnerName(ao.getOwnerName());
		a.setOwnerWorkno(ao.getOwnerWorkno());
		
		
		a.setPoNo(cuntaoAsset.getBoNo());
		a.setSerialNo(cuntaoAsset.getSerialNo());
		a.setStatus(AssetStatusEnum.USE.getCode());

		a.setUseAreaType(AssetUseAreaTypeEnum.STATION.getCode());
		if (cuntaoAsset.getPartnerInstanceId() != null) {
			PartnerStationRel psl = partnerInstanceBO
					.findPartnerInstanceById(cuntaoAsset.getPartnerInstanceId());
			a.setUseAreaId(psl.getStationId());
			a.setUserId(String.valueOf(psl.getTaobaoUserId()));
			a.setUserName(uicReadAdapter.getFullName(psl.getTaobaoUserId()));
			//如果 村点已停业 设置成待会收
			if(PartnerInstanceStateEnum.CLOSED.getCode().equals(psl.getState())) {
				a.setRecycle(RecycleStatusEnum.Y.getCode());
			}else {
				a.setRecycle(RecycleStatusEnum.N.getCode());
			}
			
		}
		a.setCheckStatus(cuntaoAsset.getCheckStatus());
		a.setCheckTime(cuntaoAsset.getCheckTime());
		
		
		return a;
	}

//	private String getWorkerNo(Long orgId) {
//        List<CuntaoUser> userLists = cuntaoUserService.getCuntaoUsers(orgId, UserRole.COUNTY_LEADER);
//        if (CollectionUtils.isEmpty(userLists)) {
//        	return "";
//        }
//        CuntaoUser countyLeader = userLists.iterator().next();
//        return countyLeader.getLoginId();
//	}
	
	private AssetOwner getAssetOwnerInfo(String orgId) {
		Map<String, List<AssetOwner>> rdMap = AssetOwnerParser.getOwnerList();
		List<AssetOwner> rdList = rdMap.get("assetOwner");
		if (orgId== null) {
			return null;
		}
		if (CollectionUtils.isNotEmpty(rdList)) {
			for (AssetOwner rd : rdList) {
				if (org.apache.commons.lang3.StringUtils.equals(orgId, rd.getOrgId())) {
					return rd;
            	}
			}
		}
		return null;
	}
	@Override
	public Boolean changeOwner(Long orgId, String ownerWorkNo, String ownerName,List<Long> assetId) {
		
		  AssetExample assetExample = new AssetExample();
	        if (assetId != null) {
	        	 assetExample.createCriteria().andIsDeletedEqualTo("n").andStatusNotEqualTo(AssetStatusEnum.SCRAP.getCode()).andIdIn(assetId);
	        }else {
	        	 assetExample.createCriteria().andIsDeletedEqualTo("n")
		            .andOwnerOrgIdEqualTo(orgId).andStatusNotEqualTo(AssetStatusEnum.SCRAP.getCode());
	        }
	        List<Asset> assetList = assetMapper.selectByExample(assetExample);
	        
	        if (CollectionUtils.isNotEmpty(assetList)) {
	        	logger.info("sync asset begin,count={}", assetList.size());
	        	for(Asset a :assetList) {
	        		   try {
						Asset updateAsset = new Asset();
						    DomainUtils.beforeUpdate(updateAsset, "sys");
						    updateAsset.setId(a.getId());
						    updateAsset.setAliNo(a.getAliNo());
						    updateAsset.setOwnerName(ownerName);
						    updateAsset.setOwnerWorkno(ownerWorkNo);
						    if (AssetUseAreaTypeEnum.COUNTY.getCode().equals(a.getUseAreaType())) {
						    	updateAsset.setUseAreaId(a.getOwnerOrgId());
						    	updateAsset.setUserId(ownerWorkNo);
						    	updateAsset.setUserName(ownerName);
						    }
						    assetMapper.updateByPrimaryKeySelective(updateAsset);
						    
						  //集团资产变更责任人
						 changeOwner(updateAsset);
					} catch (Exception e) {
						logger.error("sync asset error,asset="+JSONObject.toJSONString(a),e);
					}
	        	}
	        }
	        
		return Boolean.TRUE;
	}
	@Override
	public void checkAssetInfo(List<Long> assetIds) {
		List<Asset> assetList = new ArrayList<Asset>();
		List<String> vaildStatus = AssetStatusEnum.getValidStatusList();
		if (CollectionUtils.isNotEmpty(assetIds)) {//指定参数
			AssetExample cuntaoAssetExample = new AssetExample();
			cuntaoAssetExample.createCriteria().andIsDeletedEqualTo("n")//.andStatusIn(vaildStatus)//.andCreatorNotEqualTo(CREATOR)
					.andIdIn(assetIds);
			assetList = assetMapper.selectByExample(cuntaoAssetExample);
			batchCheck(assetList);
		} else {
			AssetExample cuntaoAssetExample = new AssetExample();
			cuntaoAssetExample.createCriteria().andIsDeletedEqualTo("n")//.andCreatorNotEqualTo(CREATOR)
					.andStatusIn(vaildStatus);
			cuntaoAssetExample.setOrderByClause("id asc");
			int count = assetMapper.countByExample(cuntaoAssetExample);
			logger.info("check asset begin,count={}", count);
			int pageSize = 200;
			int pageNum = 1;
			int total = count % pageSize == 0 ? count / pageSize : count
					/ pageSize + 1;
			while (pageNum <= total) {
				logger.info("check-asset-doing {},{}",pageNum,pageSize);
				PageHelper.startPage(pageNum, pageSize);
				assetList = assetMapper
						.selectByExample(cuntaoAssetExample);
				batchCheck(assetList);
				pageNum++;
			}
		}
		logger.info("check-asset-finish");
	}
	
	private void batchCheck(List<Asset> assetList) {
		if (CollectionUtils.isNotEmpty(assetList)) {
			for (Asset ca :assetList) {
				try {
					check(ca);
				} catch (Exception e) {
					logger.error("check asset error,asset="+JSONObject.toJSONString(ca),e);
				}
			}
		}
	}
	
	private void check(Asset  a) {
		AssetApiResultDO<PubResourceDto>  resDto = cuntaoApiService.getAssetInfo(a.getAliNo(), "36821");
		if (resDto.isSuccess()) {
			PubResourceDto pDto = resDto.getResult();
			if (a.getStatus().equals(AssetStatusEnum.SIGN.getCode())) {
				if (!(pDto.getStatus().equals("Stocking")) || !(pDto.getStatusDetail().equals("Available"))) {
					logger.error("sign.check.asset.error,aliNo="+a.getAliNo()+":status="+pDto.getStatus()+":statusDetail="+pDto.getStatusDetail());
				}
			}
			
			if (a.getStatus().equals(AssetStatusEnum.SCRAP.getCode())) {
				if (!(pDto.getStatus().equals("Scrapped"))) {
					logger.error("scrap.check.asset.error,aliNo="+a.getAliNo()+":status="+pDto.getStatus()+":statusDetail="+pDto.getStatusDetail());
				}
			}
			
			if (a.getStatus().equals(AssetStatusEnum.USE.getCode())||
					a.getStatus().equals(AssetStatusEnum.DISTRIBUTE.getCode())||
					a.getStatus().equals(AssetStatusEnum.PEND.getCode())||
					a.getStatus().equals(AssetStatusEnum.TRANSFER.getCode())||
					a.getStatus().equals(AssetStatusEnum.SCRAPING.getCode())) {
				if (!(pDto.getStatus().equals("Using"))) {
					logger.error("use.check.asset.error,aliNo="+a.getAliNo()+":orgstatus="+a.getStatus()+":status="+pDto.getStatus()+":statusDetail="+pDto.getStatusDetail());
				}
			}
		}else {
			logger.error("getAssetInfo error,aliNo="+a.getAliNo()+":orgstatus="+a.getStatus(),resDto.getErrorMsg());
		}
	}
	
	public boolean scrapAssetByOrg(List<String> aliNoList) {
		AssetExample cuntaoAssetExample = new AssetExample();
		List<Asset> assetList = new ArrayList<Asset>();
		cuntaoAssetExample.createCriteria().andIsDeletedEqualTo("n")
				.andAliNoIn(aliNoList);
		assetList = assetMapper.selectByExample(cuntaoAssetExample);
		logger.info("scrapAssetByOrg asset begin,count={}", assetList.size());
		batchscrap(assetList);
		logger.info("scrapAssetByOrg asset end");
		return  true;
	}
	private void batchscrap(List<Asset> assetList) {
		if (CollectionUtils.isNotEmpty(assetList)) {
			for (Asset ca :assetList) {
				try {
					scrap(ca);
				} catch (Exception e) {
					logger.error("scrap asset error,asset="+JSONObject.toJSONString(ca),e);
				}
			}
		}
	}
	private void scrap(Asset  a) {
		Long  assetId  = a.getId();
		//如果有入库单   检查 是否删除入库单
        AssetRolloutIncomeDetail detail = assetRolloutIncomeDetailBO.queryWaitSignByAssetId(assetId);
        if (detail != null) {
        	assetRolloutIncomeDetailBO.deleteWaitSignDetail(assetId, "offlineScrap");
            Long incomeId = detail.getIncomeId();
            Long rolloutId = detail.getRolloutId();
            if (incomeId != null) {
            	 //更新出入库单状态
            	if (assetRolloutIncomeDetailBO.isAllSignByIncomeId(incomeId)) {
            		assetIncomeBO.updateStatus(incomeId, AssetIncomeStatusEnum.DONE, "offlineScrap");
        		}else {
        			assetIncomeBO.updateStatus(incomeId, AssetIncomeStatusEnum.DOING, "offlineScrap");
        		}
            }
            if (rolloutId != null) {
            	if (assetRolloutIncomeDetailBO.isAllSignByRolloutId(rolloutId)) {
					assetRolloutBO.updateStatus(rolloutId, AssetRolloutStatusEnum.ROLLOUT_DONE, "offlineScrap");
				}else{
					assetRolloutBO.updateStatus(rolloutId, AssetRolloutStatusEnum.ROLLOUT_ING, "offlineScrap");
				}
            }
           
        }
        
        a.setStatus(AssetStatusEnum.SCRAP.getCode());
        DomainUtils.beforeUpdate(a, "offlineScrap");
        assetMapper.updateByPrimaryKeySelective(a);
	}
	@Override
	public void checkAssetToAmpForBcp(Long assetId) {
		Asset a = assetBO.getAssetById(assetId);
		if (a == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "查询失败" + AssetBO.NO_EXIT_ASSET);
		}
		AssetApiResultDO<PubResourceDto>  resDto = cuntaoApiService.getAssetInfo(a.getAliNo(), "36821");
		if (resDto.isSuccess()) {
			PubResourceDto pDto = resDto.getResult();
			if (a.getStatus().equals(AssetStatusEnum.SIGN.getCode())) {
				if (!(pDto.getStatus().equals("Stocking")) || !(pDto.getStatusDetail().equals("Available"))) {
					logger.error("checkAssetToAmpForBcp.sign.check.error,aliNo="+a.getAliNo()+":status="+pDto.getStatus()+":statusDetail="+pDto.getStatusDetail());
					throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,  "【"+a.getAliNo()+"】是待县签收资产，集团状态必须是库存-可用。");
				}
			}
			
			/*if (a.getStatus().equals(AssetStatusEnum.SCRAP.getCode())) {
				if (!(pDto.getStatus().equals("Scrapped"))) {
					logger.error("scrap.check.asset.error,aliNo="+a.getAliNo()+":status="+pDto.getStatus()+":statusDetail="+pDto.getStatusDetail());
				}
			}*/
			
			if (a.getStatus().equals(AssetStatusEnum.USE.getCode())||
					a.getStatus().equals(AssetStatusEnum.DISTRIBUTE.getCode())||
					a.getStatus().equals(AssetStatusEnum.PEND.getCode())||
					a.getStatus().equals(AssetStatusEnum.TRANSFER.getCode())) {
				if (pDto.getStatus().equals("Using")) {
					if(!(a.getOwnerWorkno().equals(pDto.getOwner()))) {
						logger.error("checkAssetToAmpForBcp.use.check.owner.error,aliNo="+a.getAliNo()+":orgOwner="+a.getOwnerWorkno()+":ampOwner="+pDto.getOwner());
						throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,  "【"+a.getAliNo()+"】责任人不一致，org责任人是"+a.getOwnerWorkno()+":集团资产责任人="+pDto.getOwner());
					}
				}
				
				if (pDto.getStatus().equals("Scrapped") && pDto.getStatusDetail().equals("Sold")) {
					scrap(a);
				}
			}
		}else {
			logger.error("checkAssetToAmpForBcp.getAssetInfo.error,aliNo="+a.getAliNo()+":orgstatus="+a.getStatus(),resDto.getErrorMsg());
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,  "【"+a.getAliNo()+"】获取集团数据失败。");
		}
	}
	@Override
	public Boolean changeOwner(List<Long> assetIds) {
		List<Asset> assetList = new ArrayList<Asset>();
		//List<String> vaildStatus = AssetStatusEnum.getValidStatusList();
		if (CollectionUtils.isNotEmpty(assetIds)) {//指定参数
			AssetExample cuntaoAssetExample = new AssetExample();
			cuntaoAssetExample.createCriteria().andIsDeletedEqualTo("n")//.andStatusIn(vaildStatus)//.andCreatorNotEqualTo(CREATOR)
					.andIdIn(assetIds);
			assetList = assetMapper.selectByExample(cuntaoAssetExample);
			batchChange(assetList);
		} else {
			AssetExample cuntaoAssetExample = new AssetExample();
			cuntaoAssetExample.createCriteria().andIsDeletedEqualTo("n")//.andCreatorNotEqualTo(CREATOR)
					.andStatusNotEqualTo(AssetStatusEnum.SCRAP.getCode());
			cuntaoAssetExample.setOrderByClause("id asc");
			int count = assetMapper.countByExample(cuntaoAssetExample);
			logger.info("changeOwner asset begin,count={}", count);
			int pageSize = 200;
			int pageNum = 1;
			int total = count % pageSize == 0 ? count / pageSize : count
					/ pageSize + 1;
			while (pageNum <= total) {
				logger.info("changeOwner-asset-doing {},{}",pageNum,pageSize);
				PageHelper.startPage(pageNum, pageSize);
				assetList = assetMapper
						.selectByExample(cuntaoAssetExample);
				batchChange(assetList);
				pageNum++;
			}
		}
		logger.info("changeOwner-asset-finish");
		return true;
	}
	
	private void batchChange(List<Asset> assetList) {
		if (CollectionUtils.isNotEmpty(assetList)) {
			for (Asset ca :assetList) {
				try {
					change(ca);
				} catch (Exception e) {
					logger.error("changeOwner asset error,asset="+JSONObject.toJSONString(ca),e);
				}
			}
		}
	}
	
	private void change(Asset  a) {
		if (a.getOwnerWorkno() == null || a.getOwnerWorkno().length()==6) {
			logger.info(a.getAliNo()+"is.pass");
			return;
		}
		changeOwner(a);
	}
    @Override
    public void xxxxx(List<Long> uids) {
        for(Long a : uids){
            ResultTO<List<MmpPermissionDO>> rm =  mmpAuthReadService.getPermissionsByAccountId(a) ;
            for(MmpPermissionDO mmp : rm.getModule()){
                System.out.println();
                logger.info("yejinwang:"+a+":"+mmp.getPermissionName());
            }
        }
        
        
    }
}
