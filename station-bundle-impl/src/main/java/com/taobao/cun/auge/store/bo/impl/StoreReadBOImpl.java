package com.taobao.cun.auge.store.bo.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoStore;
import com.taobao.cun.auge.dal.domain.CuntaoStoreExample;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.CuntaoStoreMapper;
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
	
	@Override
	public StoreDto getStoreDtoByStation(Long stationId) {
		Station station = stationBO.getStationById(stationId);
		if(station == null){
			return null;
		}
		StationDto stationDto = StationConverter.toStationDto(station);
		if(!stationDto.isStore()){
			return null;
		}
		
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andStationIdEqualTo(stationId).andIsDeletedEqualTo("n");
		List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(cuntaoStores)){
			return null;
		}
		
		CuntaoStore cuntaoStore = cuntaoStores.get(0);
		StoreDto storeDto = new StoreDto();
		storeDto.setId(cuntaoStore.getId());
		storeDto.setAddress(stationDto.getAddress());
		storeDto.setName(cuntaoStore.getName());
		storeDto.setShareStoreId(cuntaoStore.getShareStoreId());
		storeDto.setScmId(cuntaoStore.getScmId());
		storeDto.setStoreCategory(StoreCategory.valueOf(cuntaoStore.getStoreCategory()));
		storeDto.setTaobaoUserId(station.getTaobaoUserId());
		storeDto.setStoreStatus(StoreStatus.valueOf(cuntaoStore.getStatus()));
		return storeDto;
	}

}
