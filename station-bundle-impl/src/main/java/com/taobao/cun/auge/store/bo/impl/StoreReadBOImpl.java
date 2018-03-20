package com.taobao.cun.auge.store.bo.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.POIUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CuntaoStore;
import com.taobao.cun.auge.dal.domain.CuntaoStoreExample;
import com.taobao.cun.auge.dal.domain.CuntaoStoreExample.Criteria;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.CuntaoStoreMapper;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreCategory;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreQueryPageCondition;
import com.taobao.cun.auge.store.dto.StoreStatus;
import com.taobao.place.client.domain.dataobject.StoreDO;
import com.taobao.place.client.service.StoreService;
import com.taobao.place.util.DistanceUtil;
@Component
public class StoreReadBOImpl implements StoreReadBO {
	@Resource
	private CuntaoStoreMapper cuntaoStoreMapper;
	
	@Resource
	private StationBO stationBO;
	
	@Resource
	private PartnerBO partnerBO;
	
	@Resource
    private PartnerInstanceBO partnerInstanceBO;
	
	@Resource
	private StoreService StoreService;
	
	@Resource
	private DiamondConfiguredProperties diamondConfiguredProperties;
	@Override
	public StoreDto getStoreDtoByStationId(Long stationId) {
		Station station = stationBO.getStationById(stationId);
		StationDto stationDto = StationConverter.toStationDto(station);
		if(station == null){
			return null;
		}
		
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andStationIdEqualTo(stationId).andIsDeletedEqualTo("n").andStatusEqualTo(StoreStatus.NORMAL.getStatus());
		List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(cuntaoStores)){
			return null;
		}
		
		CuntaoStore cuntaoStore = cuntaoStores.iterator().next();
		Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(cuntaoStore.getTaobaoUserId());
		if(partner == null){
			return null;
		}
		StoreDto storeDto = toStoreDto(station, stationDto, cuntaoStore,partner);
		return storeDto;
	}

	private StoreDto toStoreDto(Station station, StationDto stationDto, CuntaoStore cuntaoStore,Partner partner) {
		StoreDto storeDto = new StoreDto();
		storeDto.setAddress(stationDto.getAddress());
		storeDto.setTaobaoUserId(station.getTaobaoUserId());
		storeDto.setStationId(station.getId());
		storeDto.setId(cuntaoStore.getId());
		storeDto.setName(cuntaoStore.getName());
		storeDto.setShareStoreId(cuntaoStore.getShareStoreId());
		storeDto.setScmCode(cuntaoStore.getScmCode());
		storeDto.setStoreCategory(StoreCategory.valueOf(cuntaoStore.getStoreCategory()));
		storeDto.setCategory(cuntaoStore.getStoreCategory());
		storeDto.setStoreStatus(StoreStatus.valueOf(cuntaoStore.getStatus()));
		storeDto.setStatus(cuntaoStore.getStatus());
		if(partner !=null){
			storeDto.setMobile(partner.getMobile());
			storeDto.setPartnerName(partner.getName());
		}
		storeDto.setSellerShareStoreId(cuntaoStore.getSellerShareStoreId());
		PartnerStationRel partnerStationRel = partnerInstanceBO.getActivePartnerInstance(station.getTaobaoUserId());
		if(partnerStationRel != null) {
			storeDto.setSellerId(partnerStationRel.getSellerId());
		}
		StoreDO storeDO = StoreService.getStoreById(cuntaoStore.getShareStoreId());
		if(storeDO != null){
			storeDto.setImage(diamondConfiguredProperties.getStoreImagePerfix()+storeDO.getPic());
		}
		return storeDto;
	}

	@Override
	public StoreDto getStoreDtoByTaobaoUserId(Long taobaoUserId) {
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andTaobaoUserIdEqualTo(taobaoUserId).andIsDeletedEqualTo("n").andStatusEqualTo(StoreStatus.NORMAL.getStatus());
		List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(cuntaoStores)){
			return null;
		}
		CuntaoStore cuntaoStore = cuntaoStores.iterator().next();
		Station station = stationBO.getStationById(cuntaoStore.getStationId());
		StationDto stationDto = StationConverter.toStationDto(station);
		if(station == null){
			return null;
		}
		Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(cuntaoStore.getTaobaoUserId());
		StoreDto storeDto = toStoreDto(station, stationDto, cuntaoStore,partner);
		
		return storeDto;
	}

	@Override
	public StoreDto getStoreByScmCode(String scmCode) {
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andScmCodeEqualTo(scmCode).andIsDeletedEqualTo("n").andStatusEqualTo(StoreStatus.NORMAL.getStatus());
		List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(cuntaoStores)){
			return null;
		}
		CuntaoStore cuntaoStore = cuntaoStores.iterator().next();
		Station station = stationBO.getStationById(cuntaoStore.getStationId());
		StationDto stationDto = StationConverter.toStationDto(station);
		if(station == null){
			return null;
		}
		Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(cuntaoStore.getTaobaoUserId());
		StoreDto storeDto = toStoreDto(station, stationDto, cuntaoStore,partner);
		return storeDto;
	}

	@Override
	public StoreDto getStoreBySharedStoreId(Long sharedStoreId) {
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andShareStoreIdEqualTo(sharedStoreId).andIsDeletedEqualTo("n").andStatusEqualTo(StoreStatus.NORMAL.getStatus());
		List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(cuntaoStores)){
			return null;
		}
		CuntaoStore cuntaoStore = cuntaoStores.iterator().next();
		Station station = stationBO.getStationById(cuntaoStore.getStationId());
		StationDto stationDto = StationConverter.toStationDto(station);
		if(station == null){
			return null;
		}
		Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(cuntaoStore.getTaobaoUserId());
		StoreDto storeDto = toStoreDto(station, stationDto, cuntaoStore,partner);
		return storeDto;
	}

	

	@Override
	public String[] getStationDistance(Long stationId, Double lng, Double lat) {
		if(stationId == null || lng == null || lat == null){
			return null;
		}
		Station station = stationBO.getStationById(stationId);
		if(station != null){
			if(station.getLat()== null || station.getLng() == null){
				return null;
			}
			double storeLat = POIUtils.toStanardPOI(station.getLat());
			double storeLng = POIUtils.toStanardPOI(station.getLng());
			int distance = (int) DistanceUtil.getDistance(storeLng, storeLat, lng, lat);
			if (distance <= 0) {
				distance = 0;
			}
			if (distance < 1000) {
				return new String[]{distance+"","米"};
			}else{
				return new String[]{distance/ 1000+"","公里"};
			}
		}
		return null;
	}

	@Override
    public List<Long> getAllStoreIdsByStatus(StoreStatus status) {
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andStatusEqualTo(status.getStatus()).andIsDeletedEqualTo("n").andStatusEqualTo(StoreStatus.NORMAL.getStatus());
		List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
		if(cuntaoStores != null){
			return cuntaoStores.stream().map(store -> store.getShareStoreId()).collect(Collectors.toList());
		}
		return Lists.newArrayList();
	}

	@Override
	public PageDto<StoreDto> queryStoreByPage(StoreQueryPageCondition storeQueryPageCondition) {
		PageHelper.startPage(storeQueryPageCondition.getPageNum(), storeQueryPageCondition.getPageSize());
		CuntaoStoreExample example = new CuntaoStoreExample();
		Criteria criteria = example.createCriteria().andIsDeletedEqualTo("n").andStatusEqualTo(StoreStatus.NORMAL.getStatus());
		if(StringUtils.isNotEmpty(storeQueryPageCondition.getName())){
			criteria.andNameLike("%"+storeQueryPageCondition.getName()+"%");
		}
		if(CollectionUtils.isNotEmpty(storeQueryPageCondition.getStoreCategorys())){
			criteria.andStoreCategoryIn(storeQueryPageCondition.getStoreCategorys());
		}
		Page<CuntaoStore> cuntaoStores = (Page<CuntaoStore>) cuntaoStoreMapper.selectByExample(example);
		List<StoreDto> stores = Lists.newArrayList();
		for(CuntaoStore store : cuntaoStores){
			Station station = stationBO.getStationById(store.getStationId());
			StationDto stationDto = StationConverter.toStationDto(station);
			Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(store.getTaobaoUserId());
			StoreDto storeDto = toStoreDto(station, stationDto, store,partner);
			stores.add(storeDto);
		}
		PageInfo<CuntaoStore> pageInfo = new PageInfo<CuntaoStore>(cuntaoStores);

		PageDto<StoreDto> result = new PageDto<StoreDto>();

		result.setPageNum(cuntaoStores.getPageNum());
		result.setPageSize(pageInfo.getPageSize());
		result.setTotal(pageInfo.getTotal());
		result.setCurPagesize(pageInfo.getSize());
		result.setPages(pageInfo.getPages());
		result.setFirstPage(pageInfo.getFirstPage());
		result.setLastPage(pageInfo.getLastPage());
		result.setNextPage(pageInfo.getNextPage());
		result.setPrePage(pageInfo.getPrePage());
		result.setItems(stores);
		return result;
	}

	@Override
	public List<StoreDto> getStoreByStationIds(List<Long> stationIds) {
		List<Station> stations = stationBO.getStationById(stationIds);
		List<StoreDto> stores = Lists.newArrayList();
		for(Station station : stations){
			StationDto stationDto = StationConverter.toStationDto(station);
				CuntaoStoreExample example = new CuntaoStoreExample();
				example.createCriteria().andStationIdEqualTo(station.getId()).andIsDeletedEqualTo("n").andStatusEqualTo(StoreStatus.NORMAL.getStatus());
				List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
				if(cuntaoStores != null && !cuntaoStores.isEmpty()){
					CuntaoStore store = cuntaoStores.iterator().next();
					Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(store.getTaobaoUserId());
					StoreDto storeDto = toStoreDto(station, stationDto, store,partner);
					stores.add(storeDto);
				}
		}
		return stores;
	}

	@Override
	public List<StoreDto> getStoreBySharedStoreIds(List<Long> sharedStoreIds) {
		List<StoreDto> stores = Lists.newArrayList();
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andShareStoreIdIn(sharedStoreIds).andIsDeletedEqualTo("n").andStatusEqualTo(StoreStatus.NORMAL.getStatus());
		List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
		for(CuntaoStore store :cuntaoStores){
			Station station = stationBO.getStationById(store.getStationId());
			StationDto stationDto = StationConverter.toStationDto(station);
			Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(store.getTaobaoUserId());
			StoreDto storeDto = toStoreDto(station, stationDto, store,partner);
			stores.add(storeDto);
		}
		return stores;
	}

}
