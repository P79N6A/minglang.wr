package com.taobao.cun.auge.store.bo.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.ali.com.google.common.collect.Sets;
import com.google.common.base.Strings;
import com.taobao.biz.common.division.impl.DefaultDivisionAdapterManager;
import com.taobao.cun.auge.common.utils.POIUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CuntaoStore;
import com.taobao.cun.auge.dal.domain.CuntaoStoreExample;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StoreCreateError;
import com.taobao.cun.auge.dal.domain.StoreCreateErrorExample;
import com.taobao.cun.auge.dal.mapper.CuntaoStoreMapper;
import com.taobao.cun.auge.dal.mapper.StoreCreateErrorMapper;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.store.bo.InventoryStoreWriteBo;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.bo.StoreWriteBO;
import com.taobao.cun.auge.store.dto.InventoryStoreCreateDto;
import com.taobao.cun.auge.store.dto.StoreCategory;
import com.taobao.cun.auge.store.dto.StoreCategoryConstants;
import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreStatus;
import com.taobao.cun.auge.store.dto.StoreTags;
import com.taobao.cun.auge.store.service.StoreException;
import com.taobao.cun.auge.tag.UserTag;
import com.taobao.cun.auge.tag.service.UserTagService;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.dto.OrgAddDto;
import com.taobao.cun.endor.base.dto.OrgUpdateDto;
import com.taobao.cun.mdjxc.api.CtMdJxcWarehouseApi;
import com.taobao.cun.mdjxc.common.result.DataResult;
import com.taobao.cun.mdjxc.enums.BooleanStatusEnum;
import com.taobao.cun.mdjxc.model.CtMdJxcWarehouseDTO;
import com.taobao.place.client.domain.ResultDO;
import com.taobao.place.client.domain.dataobject.StandardAreaDO;
import com.taobao.place.client.domain.dto.StoreDTO;
import com.taobao.place.client.domain.enumtype.StoreAuthenStatus;
import com.taobao.place.client.domain.enumtype.StoreBizType;
import com.taobao.place.client.domain.enumtype.StoreCheckStatus;
import com.taobao.place.client.domain.result.ResultCode;
import com.taobao.place.client.service.StoreCreateService;
import com.taobao.place.client.service.StoreUpdateService;
import com.taobao.place.client.service.area.StandardAreaService;
import com.taobao.tddl.client.sequence.impl.GroupSequence;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StoreWriteBOImpl implements StoreWriteBO {
	@Resource
	private StoreCreateService storeCreateService;
	@Resource
	private StoreUpdateService storeUpdateService;
	@Resource
	private CuntaoStoreMapper cuntaoStoreMapper;
	@Resource
	private StationBO stationBO;
	@Resource
	private UserTagService userTagService;
	@Resource
	private InventoryStoreWriteBo inventoryStoreWriteBo;
	@Resource
	private DiamondConfiguredProperties diamondConfiguredProperties;
	
	@Resource
	private StoreReadBO storeReadBO;
	
	@Resource
	private PartnerInstanceQueryService partnerInstanceQueryService;
	
	@Autowired
	@Qualifier("storeEndorOrgIdSequence")
	private GroupSequence groupSequence;
	
	@Autowired
	@Qualifier("storeEndorApiClient")
	private EndorApiClient storeEndorApiClient;
	
	@Autowired
	CaiNiaoAdapter caiNiaoAdapter;
	
	@Autowired
	CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	
	@Autowired
	CtMdJxcWarehouseApi ctMdJxcWarehouseApi;
	
	@Autowired
	private StoreCreateErrorMapper storeCreateErrorMapper;
	
	@Autowired
	private DefaultDivisionAdapterManager defaultDivisionAdapterManager;
	
	@Autowired
	private StandardAreaService standardAreaService;
	private static final Logger logger = LoggerFactory.getLogger(StoreWriteBOImpl.class);
	@Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long create(StoreCreateDto storeCreateDto) throws StoreException{
		
		Station station = stationBO.getStationById(storeCreateDto.getStationId());
		if(station == null){
			throw new StoreException("服务站不存在,station_id=" + storeCreateDto.getStationId());
		}
		StoreDto store = storeReadBO.getStoreDtoByStationId(station.getId());
		if(store != null ){
			return store.getId();
		}
		StoreDTO storeDTO = new StoreDTO();
		storeDTO.setName(storeCreateDto.getName());
		storeDTO.setCategoryId(storeCreateDto.getCategoryId());
		storeDTO.setAddress(station.getAddress());
		storeDTO.setOuterId(String.valueOf(storeCreateDto.getStationId()));
		//仓库的区域CODE，取叶子节点
		String areaId = null;
		//省
		if(!Strings.isNullOrEmpty(station.getProvince())){
			Long cityCode = tb2gbCode(Long.parseLong(station.getCity()));
			if (cityCode == null) {
				cityCode = Long.parseLong(station.getCity());
			}
			StandardAreaDO  standardAreaDO = standardAreaService.getStandardAreaDOById(cityCode);
			if(standardAreaDO != null && standardAreaDO.getParentId()!=null){
				storeDTO.setProv(standardAreaDO.getParentId().intValue());
			}else{
				storeDTO.setProv(Integer.parseInt(station.getProvince()));
			}
			storeDTO.setProvName(station.getProvinceDetail());
			areaId = station.getProvince();
		}
		//市
		if(!Strings.isNullOrEmpty(station.getCity())){
			Long gbCode = tb2gbCode(Long.parseLong(station.getCity()));
			if (gbCode != null) {
				storeDTO.setCity(gbCode.intValue());
			}else{
				//重庆市特殊处理，共享需要500200标准code
				if("500100".equals(station.getCity())){
					Long countyCode = tb2gbCode(Long.parseLong(station.getCounty()));
					if(countyCode == null){
						countyCode = Long.parseLong(station.getCounty());
					}
					StandardAreaDO  standardAreaDO = standardAreaService.getStandardAreaDOById(countyCode);
					storeDTO.setCity(standardAreaDO.getParentId().intValue());
				}else{
					storeDTO.setCity(Integer.parseInt(station.getCity()));
				}
			}
			storeDTO.setCityName(station.getCityDetail());
			areaId = station.getCity();
		}
		//区/县
		if(!Strings.isNullOrEmpty(station.getCounty())){
			Long gbCode = tb2gbCode(Long.parseLong(station.getCounty()));
			if (gbCode != null) {
				storeDTO.setDistrict(gbCode.intValue());
			}else{
				storeDTO.setDistrict(Integer.parseInt(station.getCounty()));
			}
			storeDTO.setDistrictName(station.getCountyDetail());
			areaId = station.getCounty();
		}
		if(!Strings.isNullOrEmpty(station.getTown()) && !diamondConfiguredProperties.getIgnoreSupplyStoreTownList().contains(station.getId())){
			storeDTO.setTown(Integer.parseInt(station.getTown()));
			storeDTO.setTownName(station.getTownDetail());
			areaId = station.getTown();
		}
		
		//如果areaId为空，则无法创建仓库，这里直接终止以下流程
		if(Strings.isNullOrEmpty(areaId)){
			throw new StoreException("缺少行政地址CODE，无法创建仓库");
		}
		if(!Strings.isNullOrEmpty(station.getLat())){
			storeDTO.setPosy(POIUtils.toStanardPOI(station.getLat()));
		}
		if(!Strings.isNullOrEmpty(station.getLng())){
			storeDTO.setPosx(POIUtils.toStanardPOI(station.getLng()));
		}
		switch (storeCreateDto.getStoreCategory()){
		   case FMCG:
			   storeDTO.addTag(StoreTags.FMCG_TAG);
			   break;
		   case MOMBABY:
			   storeDTO.addTag(StoreTags.MOMBABY_TAG);
			   break;
		   case ELEC:
			   storeDTO.addTag(StoreTags.ELEC_TAG);
			   break;   
		}
		storeDTO.addTag(diamondConfiguredProperties.getStoreTag());
		storeDTO.setStatus(com.taobao.place.client.domain.enumtype.StoreStatus.NORMAL.getValue());
		storeDTO.setCheckStatus(StoreCheckStatus.CHECKED.getValue());
		storeDTO.setAuthenStatus(StoreAuthenStatus.PASS.getValue());
		ResultDO<Long> result = storeCreateService.create(storeDTO, diamondConfiguredProperties.getStoreMainUserId(), StoreBizType.STORE_ITEM_BIZ.getValue());
		if(result.isFailured()){
			addStoreCreateError(station.getId(), result);
			throw new StoreException(result.getFullErrorMsg());
		}else{
			fixStoreCreateError(station.getId());
		}
		if (ResultCode.STORE_REPEAT.getCode().equals(result.getResultCode())) {
            storeDTO.setStoreId(result.getResult());
            // 更新
            ResultDO<Boolean> updateResult = storeUpdateService.update(storeDTO, diamondConfiguredProperties.getStoreMainUserId(), StoreBizType.STORE_ITEM_BIZ.getValue());
            if(updateResult.isFailured()){
            	throw new StoreException(updateResult.getFullErrorMsg());
            }
            CuntaoStoreExample example = new CuntaoStoreExample();
            example.createCriteria().andShareStoreIdEqualTo(result.getResult());
            List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
            if(cuntaoStores == null || cuntaoStores.isEmpty()){
            	throw new StoreException("共享门店已经创建，但本地无该门店信息。");
            }
            CuntaoStore cuntaoStore = cuntaoStores.get(0);
            cuntaoStore.setShareStoreId(result.getResult());
			cuntaoStore.setModifier(storeCreateDto.getCreator());
			cuntaoStore.setName(storeCreateDto.getName());
			cuntaoStore.setStationId(storeCreateDto.getStationId());
			cuntaoStore.setGmtModified(new Date());
			cuntaoStore.setStoreCategory(storeCreateDto.getStoreCategory().getCategory());
			cuntaoStore.setTaobaoUserId(station.getTaobaoUserId());
            cuntaoStoreMapper.updateByPrimaryKey(cuntaoStore);
            updateOrg(cuntaoStore);
        }else{
			String scmCode = createInventoryStore(storeCreateDto, station.getTaobaoUserId(),areaId);
			//打标
			if(!userTagService.hasTag(station.getTaobaoUserId(), UserTag.TPS_USER_TAG.getTag())){
				userTagService.addTag(station.getTaobaoUserId(), UserTag.TPS_USER_TAG.getTag());
			}
			if(!userTagService.hasTag(station.getTaobaoUserId(), UserTag.SAMPLE_SELLER_TAG.getTag())){
				userTagService.addTag(station.getTaobaoUserId(), UserTag.SAMPLE_SELLER_TAG.getTag());
			}
			//本地存储
			CuntaoStore cuntaoStore = new CuntaoStore();
			cuntaoStore.setShareStoreId(result.getResult());
			cuntaoStore.setModifier(storeCreateDto.getCreator());
			cuntaoStore.setCreator(storeCreateDto.getCreator());
			cuntaoStore.setName(storeCreateDto.getName());
			cuntaoStore.setStationId(storeCreateDto.getStationId());
			cuntaoStore.setGmtCreate(new Date());
			cuntaoStore.setGmtModified(new Date());
			cuntaoStore.setIsDeleted("n");
			cuntaoStore.setStatus(StoreStatus.NORMAL.getStatus());
			cuntaoStore.setStoreCategory(storeCreateDto.getStoreCategory().getCategory());
			cuntaoStore.setTaobaoUserId(station.getTaobaoUserId());
			cuntaoStore.setScmCode(scmCode);
			cuntaoStore.setEndorOrgId(groupSequence.nextValue());
			cuntaoStoreMapper.insert(cuntaoStore);
			addOrg(cuntaoStore);
        }
		initStoreWarehouse(station.getId());
		return result.getResult();
	}

	private String createInventoryStore(StoreCreateDto storeCreateDto, Long userId,String areaId) throws StoreException {
		InventoryStoreCreateDto inventoryStoreCreateDto = new InventoryStoreCreateDto();
		inventoryStoreCreateDto.setName(storeCreateDto.getName());
		inventoryStoreCreateDto.setAlias(storeCreateDto.getName());
		inventoryStoreCreateDto.setUserId(userId);
		inventoryStoreCreateDto.setAreaId(Long.parseLong(areaId));
		return inventoryStoreWriteBo.create(inventoryStoreCreateDto);
	}

	private void addOrg(CuntaoStore cuntaoStore) {
		OrgAddDto orgAddDto = new OrgAddDto();
		orgAddDto.setOrgId(cuntaoStore.getEndorOrgId());
		orgAddDto.setParentId(3L);
		orgAddDto.setOrgName(cuntaoStore.getName());
		orgAddDto.setCreator(cuntaoStore.getCreator());
		storeEndorApiClient.getOrgServiceClient().insert(orgAddDto, null);
	}
	
	private void updateOrg(CuntaoStore cuntaoStore) {
		OrgUpdateDto orgDtoUpdate = new OrgUpdateDto();
		orgDtoUpdate.setModifier(cuntaoStore.getModifier());
		orgDtoUpdate.setOrgId(cuntaoStore.getEndorOrgId());
		orgDtoUpdate.setParentId(3L);
		orgDtoUpdate.setOrgName(cuntaoStore.getName());
		storeEndorApiClient.getOrgServiceClient().update(orgDtoUpdate, null);
	}

	@Override
	public Boolean updateStoreTag(Long shareStoreId,StoreCategory category) {
		//StoreDto store = storeReadBO.getStoreBySharedStoreId(shareStoreId);
		StoreDTO storeDTO = new StoreDTO();
		storeDTO.setStoreId(shareStoreId);
		storeDTO.setUserId(diamondConfiguredProperties.getStoreMainUserId());
		//storeDTO.setAuthenStatus(StoreAuthenStatus.PASS.getValue());
		//storeDTO.addTag(3300);
		//storeDTO.addTag(3000);
        ResultDO<Boolean> updateResult = storeUpdateService.update(storeDTO, diamondConfiguredProperties.getStoreMainUserId(), StoreBizType.STORE_ITEM_BIZ.getValue());
	    if(!updateResult.isSuccess()){
	    	logger.error("updateStore error "+updateResult.getErrorMsg());
	    }
	    return updateResult.isSuccess();
	}

	
	public Boolean batchUpdateStore(List<Long> sharedStoreIds){
		for(Long shareStoreId:sharedStoreIds){
			this.updateStoreTag(shareStoreId, null);
		}
		logger.info("batchUpdateStore finish");
		return true;
	}
	
	
	@Override
	public Boolean createSampleStore(Long stationId) {

		Station station = stationBO.getStationById(stationId);
		PartnerInstanceDto partnerInstance = partnerInstanceQueryService
				.getCurrentPartnerInstanceByStationId(stationId);
		if (station == null || partnerInstance == null || partnerInstance.getSellerId() == null) {
			return false;
		}

		StoreDTO storeDTO = new StoreDTO();
		storeDTO.setName(station.getName());
		storeDTO.setCategoryId(diamondConfiguredProperties.getStoreCategoryId());
		storeDTO.setAddress(station.getAddress());
		storeDTO.setOuterId(String.valueOf(stationId));
		// 仓库的区域CODE，取叶子节点
		String areaId = null;
		// 省
		if (!Strings.isNullOrEmpty(station.getProvince())) {
			Long cityCode = tb2gbCode(Long.parseLong(station.getCity()));
			if (cityCode == null) {
				cityCode = Long.parseLong(station.getCity());
			}
			StandardAreaDO  standardAreaDO = standardAreaService.getStandardAreaDOById(cityCode);
			if(standardAreaDO != null && standardAreaDO.getParentId()!=null){
				storeDTO.setProv(standardAreaDO.getParentId().intValue());
			}else{
				storeDTO.setProv(Integer.parseInt(station.getProvince()));
			}
			storeDTO.setProvName(station.getProvinceDetail());
			areaId = station.getProvince();
		}
		// 市
		if (!Strings.isNullOrEmpty(station.getCity())) {
			Long gbCode = tb2gbCode(Long.parseLong(station.getCity()));
			if (gbCode != null) {
				storeDTO.setCity(gbCode.intValue());
			}else{
				//重庆市特殊处理，共享需要500200标准code
				if("500100".equals(station.getCity())){
					Long countyCode = tb2gbCode(Long.parseLong(station.getCounty()));
					if(countyCode == null){
						countyCode = Long.parseLong(station.getCounty());
					}
					StandardAreaDO  standardAreaDO = standardAreaService.getStandardAreaDOById(countyCode);
					storeDTO.setCity(standardAreaDO.getParentId().intValue());
				}else{
					storeDTO.setCity(Integer.parseInt(station.getCity()));
				}
			}
			storeDTO.setCityName(station.getCityDetail());
			areaId = station.getCity();
		}
		// 区/县
		// 区/县
		if (!Strings.isNullOrEmpty(station.getCounty())) {
			Long gbCode = tb2gbCode(Long.parseLong(station.getCounty()));
			if (gbCode != null) {
				storeDTO.setDistrict(gbCode.intValue());
			}else{
				storeDTO.setDistrict(Integer.parseInt(station.getCounty()));
			}
			storeDTO.setDistrictName(station.getCountyDetail());
			areaId = station.getCounty();
		}
		if (!Strings.isNullOrEmpty(station.getTown()) && !diamondConfiguredProperties.getIgnoreSupplyStoreTownList().contains(stationId)) {
			storeDTO.setTown(Integer.parseInt(station.getTown()));
			storeDTO.setTownName(station.getTownDetail());
			areaId = station.getTown();
		}

		// 如果areaId为空，则无法创建仓库，这里直接终止以下流程
		if (Strings.isNullOrEmpty(areaId)) {
			logger.error("createSampleStore error[" + stationId + "]: areaId is null");
			return false;
		}
		if (!Strings.isNullOrEmpty(station.getLat())) {
			storeDTO.setPosy(POIUtils.toStanardPOI(station.getLat()));
		}
		if (!Strings.isNullOrEmpty(station.getLng())) {
			storeDTO.setPosx(POIUtils.toStanardPOI(station.getLng()));
		}

		storeDTO.addTag(diamondConfiguredProperties.getStoreTag());
		storeDTO.setStatus(com.taobao.place.client.domain.enumtype.StoreStatus.NORMAL.getValue());
		storeDTO.setCheckStatus(StoreCheckStatus.CHECKED.getValue());
		storeDTO.setAuthenStatus(StoreAuthenStatus.PASS.getValue());
		ResultDO<Long> result = storeCreateService.create(storeDTO, partnerInstance.getSellerId(),
				StoreBizType.CUN_TAO.getValue());
		if (result.isFailured()) {
			logger.error("createSampleStore error[" + stationId + "]:" + result.getFullErrorMsg());
			return false;
		}
		if (result.isFailured()) {
			addStoreCreateError(stationId, result);
			logger.error("createSupplyStore error[" + stationId + "]:" + result.getFullErrorMsg());
			return false;
		} else {
			fixStoreCreateError(stationId);
		}
		StoreDto storeDto = storeReadBO.getStoreDtoByStationId(stationId);
		CuntaoStore record = new CuntaoStore();
		record.setId(storeDto.getId());
		record.setSellerShareStoreId(result.getResult());
		cuntaoStoreMapper.updateByPrimaryKeySelective(record);

		initSampleWarehouse(stationId);
		return true;
	}

	@Override
	public Boolean createSupplyStore(Long stationId) {
		StoreDto store = storeReadBO.getStoreDtoByStationId(stationId);
		if (store != null) {
			return true;
		}
		Station station = stationBO.getStationById(stationId);
		PartnerInstanceDto partnerInstance = partnerInstanceQueryService
				.getCurrentPartnerInstanceByStationId(stationId);
		if (station == null || partnerInstance == null) {
			logger.error("createSupplyStore error station or partnerInstance is Null");
			return false;
		}

		StoreDTO storeDTO = new StoreDTO();
		storeDTO.setName(station.getName());
		storeDTO.setCategoryId(diamondConfiguredProperties.getStoreCategoryId());
		storeDTO.setAddress(station.getAddress());
		storeDTO.setOuterId(String.valueOf(stationId));
		// 仓库的区域CODE，取叶子节点
		String areaId = null;
		// 省
		if (!Strings.isNullOrEmpty(station.getProvince())) {
			Long cityCode = tb2gbCode(Long.parseLong(station.getCity()));
			if (cityCode == null) {
				cityCode = Long.parseLong(station.getCity());
			}
			StandardAreaDO  standardAreaDO = standardAreaService.getStandardAreaDOById(cityCode);
			if(standardAreaDO != null && standardAreaDO.getParentId()!=null){
				storeDTO.setProv(standardAreaDO.getParentId().intValue());
			}else{
				storeDTO.setProv(Integer.parseInt(station.getProvince()));
			}
			storeDTO.setProvName(station.getProvinceDetail());
			areaId = station.getProvince();
		}
		// 市
		if (!Strings.isNullOrEmpty(station.getCity())) {
			Long gbCode = tb2gbCode(Long.parseLong(station.getCity()));
			if (gbCode != null) {
				storeDTO.setCity(gbCode.intValue());
			}else{
				//重庆市特殊处理，共享需要500200标准code
				if("500100".equals(station.getCity())){
					Long countyCode = tb2gbCode(Long.parseLong(station.getCounty()));
					if(countyCode == null){
						countyCode = Long.parseLong(station.getCounty());
					}
					StandardAreaDO  standardAreaDO = standardAreaService.getStandardAreaDOById(countyCode);
					storeDTO.setCity(standardAreaDO.getParentId().intValue());
				}else{
					storeDTO.setCity(Integer.parseInt(station.getCity()));
				}
			}
			storeDTO.setCityName(station.getCityDetail());
			areaId = station.getCity();
		}
		// 区/县
		// 区/县
		if (!Strings.isNullOrEmpty(station.getCounty())) {
			Long gbCode = tb2gbCode(Long.parseLong(station.getCounty()));
			if (gbCode != null) {
				storeDTO.setDistrict(gbCode.intValue());
			}else{
				storeDTO.setDistrict(Integer.parseInt(station.getCounty()));
			}
			storeDTO.setDistrictName(station.getCountyDetail());
			areaId = station.getCounty();
		}
		if (!Strings.isNullOrEmpty(station.getTown()) && !diamondConfiguredProperties.getIgnoreSupplyStoreTownList().contains(stationId)) {
			storeDTO.setTown(Integer.parseInt(station.getTown()));
			storeDTO.setTownName(station.getTownDetail());
			areaId = station.getTown();
		}

		// 如果areaId为空，则无法创建仓库，这里直接终止以下流程
		if (Strings.isNullOrEmpty(areaId)) {
			logger.error("createSupplyStore error[" + stationId + "]: areaId is null");
			return false;
		}
		if (!Strings.isNullOrEmpty(station.getLat())) {
			storeDTO.setPosy(POIUtils.toStanardPOI(station.getLat()));
		}
		if (!Strings.isNullOrEmpty(station.getLng())) {
			storeDTO.setPosx(POIUtils.toStanardPOI(station.getLng()));
		}

		storeDTO.addTag(diamondConfiguredProperties.getStoreTag());
		storeDTO.addTag(StoreTags.SUPPLY_STATION_TAG);// 村点补货
		storeDTO.setStatus(com.taobao.place.client.domain.enumtype.StoreStatus.NORMAL.getValue());
		storeDTO.setCheckStatus(StoreCheckStatus.CHECKED.getValue());
		storeDTO.setAuthenStatus(StoreAuthenStatus.PASS.getValue());
		ResultDO<Long> result = storeCreateService.create(storeDTO, diamondConfiguredProperties.getStoreMainUserId(),
				StoreBizType.STORE_ITEM_BIZ.getValue());
		if (result.isFailured()) {
			addStoreCreateError(stationId, result);
			logger.error("createSupplyStore error[" + stationId + "]:" + result.getFullErrorMsg());
			return false;
		} else {
			fixStoreCreateError(stationId);
		}
		// 本地存储
		CuntaoStore cuntaoStore = new CuntaoStore();
		cuntaoStore.setShareStoreId(result.getResult());
		cuntaoStore.setModifier("system");
		cuntaoStore.setCreator("system");
		cuntaoStore.setName(station.getName());
		cuntaoStore.setStationId(station.getId());
		cuntaoStore.setGmtCreate(new Date());
		cuntaoStore.setGmtModified(new Date());
		cuntaoStore.setIsDeleted("n");
		cuntaoStore.setStatus(StoreStatus.NORMAL.getStatus());
		cuntaoStore.setStoreCategory(StoreCategoryConstants.FMCG);
		cuntaoStore.setTaobaoUserId(station.getTaobaoUserId());
		cuntaoStore.setScmCode("");
		cuntaoStore.setEndorOrgId(groupSequence.nextValue());
		cuntaoStoreMapper.insert(cuntaoStore);

		Long cainiaoStationId = cuntaoCainiaoStationRelBO.getCainiaoStationId(station.getId());
		if (cainiaoStationId != null) {
			LinkedHashMap<String, String> features = new LinkedHashMap<String, String>();
			features.put("goodsSupply", "y");
			caiNiaoAdapter.updateStationFeatures(cainiaoStationId, features);
		}
		return true;
	}

	private void fixStoreCreateError(Long stationId) {
		StoreCreateErrorExample example = new StoreCreateErrorExample();
		example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(stationId);
		StoreCreateError error = new StoreCreateError();
		error.setIsFixed("y");
		storeCreateErrorMapper.updateByExampleSelective(error, example);
	}

	private void addStoreCreateError(Long stationId, ResultDO<Long> result) {
		StoreCreateErrorExample example = new StoreCreateErrorExample();
		example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(stationId);
		List<StoreCreateError> errors = storeCreateErrorMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(errors)){
			StoreCreateError error = new StoreCreateError();
			error.setIsDeleted("n");
			error.setCreator("system");
			error.setModifier("system");
			error.setGmtCreate(new Date());
			error.setGmtModified(new Date());
			error.setStationId(stationId);
			error.setErrorInfo(result.getErrorMsg());
			error.setErrorCode(result.getResultCode());
			storeCreateErrorMapper.insertSelective(error);
		}else{
			StoreCreateError error = new StoreCreateError();
			error.setId(errors.iterator().next().getId());
			error.setGmtModified(new Date());
			error.setErrorInfo(result.getErrorMsg());
			error.setErrorCode(result.getResultCode());
			storeCreateErrorMapper.updateByPrimaryKeySelective(error);
		}
	}

	
	public Boolean initSampleWarehouse(Long stationId){
		try {
		StoreDto storeDto = storeReadBO.getStoreDtoByStationId(stationId);
		PartnerInstanceDto partnerInstance  = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(stationId);
		if(storeDto == null || partnerInstance == null){
			logger.error("initSampleWarehouse error storeDto or partnerInstance is Null");
			return false; 
		}
		if(storeDto.getSellerShareStoreId()==null||partnerInstance.getSellerId() == null){
			logger.error("initSampleWarehouse error SellerShareStoreId or SellerId is Null");
			return false;
		}
		
			return initCtMdJxcWarehouse(BooleanStatusEnum.NO,storeDto.getSellerShareStoreId()+"",partnerInstance.getSellerId());
		} catch (Exception e) {
			logger.error("initSampleWarehouse error["+stationId+"]",e);
			return false; 
		}
	}
	
	public Boolean initStoreWarehouse(Long stationId){
		try {
			StoreDto storeDto = storeReadBO.getStoreDtoByStationId(stationId);
			if(storeDto == null|| storeDto.getShareStoreId() == null){
				logger.error("initStoreWarehouse error storeDto or sharedStoreId is Null");
				return false; 
			}
			return initCtMdJxcWarehouse(BooleanStatusEnum.YES,storeDto.getShareStoreId()+"",storeDto.getTaobaoUserId());
		} catch (Exception e) {
			logger.error("initStoreWarehouse error["+stationId+"]",e);
			return false; 
		}
	}
	
	public Long tb2gbCode(Long taobaocode){
		if(taobaocode == null){
			return null;
		}
		//地址库接口转不出来，人工转一下
		//江苏淮安清江浦
		if(taobaocode == 320802l){
			return 320812l;
		}
		//江西九江庐山
		if(taobaocode == 360427l){
			return 360483l;
		}
		if(taobaocode != null){
			return defaultDivisionAdapterManager.tbCodeToGbCode(taobaocode);
		}
		return null;
	}
	
	/**
	 * 初始化门店库存
	 */
	public boolean initCtMdJxcWarehouse(BooleanStatusEnum buyerIden,String storeId,Long userId){
		CtMdJxcWarehouseDTO ctMdJxcWarehouseDTO = new CtMdJxcWarehouseDTO();
		ctMdJxcWarehouseDTO.setBuyerIden(buyerIden);
		ctMdJxcWarehouseDTO.setStoreId(storeId);
		ctMdJxcWarehouseDTO.setUserId(userId);
		DataResult<Boolean> result = ctMdJxcWarehouseApi.createWarehouse(ctMdJxcWarehouseDTO);
		if(!result.isSuccess()){
			logger.error("initCtMdJxcWarehouse error["+storeId+"]:"+result.getMessage());
		}
		return result.isSuccess();
	}

	@Override
	public Boolean batchCreateSupplyStore(List<Long> stationIds) {
		for (Long stationId : stationIds) {
			try {
				createSupplyStore(stationId);
				logger.info("create supply store success["+stationId+"]");
			} catch (Exception e) {
				logger.error("batchCreateSupplyStore error[" + stationId + "]", e);
			}
		}
		logger.info("finish create supply store!");
		return true;
	}

	@Override
	public Boolean batchRemoveCainiaoFeature(List<Long> stationIds) {
		for(Long stationId : stationIds){
			try {
				Set<String> keys = Sets.newHashSet("goodsSupply");
				Long cainiaoStationId = cuntaoCainiaoStationRelBO.getCainiaoStationId(stationId);
				if(cainiaoStationId != null){
					boolean result = caiNiaoAdapter.removeStationFeatures(cainiaoStationId, keys);
					if(!result){
						logger.info("batchRemoveCainiaoFeature failed[" + stationId + "]");
					}
				}
			} catch (Exception e) {
				logger.error("batchRemoveCainiaoFeature error[" + stationId + "]", e);
			}
		}
		logger.info("finish create supply store!");
		return true;
	}
}
