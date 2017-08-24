package com.taobao.cun.auge.store.bo.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoStore;
import com.taobao.cun.auge.dal.domain.CuntaoStoreExample;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.CuntaoStoreMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreCategory;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreStatus;
@Component
public class StoreReadBOImpl implements StoreReadBO {
	@Resource
	private CuntaoStoreMapper cuntaoStoreMapper;
	
	@Resource
	private StationBO stationBO;
	
	@Resource
	private PartnerInstanceBO partnerInstanceBO;
	
	@Override
	public StoreDto getStoreDtoByStationId(Long stationId) {
		Station station = stationBO.getStationById(stationId);
		StationDto stationDto = StationConverter.toStationDto(station);
		if(station == null||!stationDto.isStore()){
			return null;
		}
		
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andStationIdEqualTo(stationId).andIsDeletedEqualTo("n");
		List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(cuntaoStores)){
			return null;
		}
		
		CuntaoStore cuntaoStore = cuntaoStores.iterator().next();
		StoreDto storeDto = toStoreDto(station, stationDto, cuntaoStore);
		return storeDto;
	}

	private StoreDto toStoreDto(Station station, StationDto stationDto, CuntaoStore cuntaoStore) {
		StoreDto storeDto = new StoreDto();
		storeDto.setId(cuntaoStore.getId());
		storeDto.setAddress(stationDto.getAddress());
		storeDto.setName(cuntaoStore.getName());
		storeDto.setShareStoreId(cuntaoStore.getShareStoreId());
		storeDto.setScmCode(cuntaoStore.getScmCode());
		storeDto.setStoreCategory(StoreCategory.valueOf(cuntaoStore.getStoreCategory()));
		storeDto.setTaobaoUserId(station.getTaobaoUserId());
		storeDto.setStoreStatus(StoreStatus.valueOf(cuntaoStore.getStatus()));
		storeDto.setStationId(station.getId());
		return storeDto;
	}

	@Override
	public StoreDto getStoreDtoByTaobaoUserId(Long taobaoUserId) {
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andTaobaoUserIdEqualTo(taobaoUserId).andIsDeletedEqualTo("n");
		List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(cuntaoStores)){
			return null;
		}
		CuntaoStore cuntaoStore = cuntaoStores.iterator().next();
		Station station = stationBO.getStationById(cuntaoStore.getStationId());
		StationDto stationDto = StationConverter.toStationDto(station);
		if(station == null||!stationDto.isStore()){
			return null;
		}
		StoreDto storeDto = toStoreDto(station, stationDto, cuntaoStore);
		return storeDto;
	}

}
