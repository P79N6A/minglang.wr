package com.taobao.cun.auge.store.bo.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.taobao.cun.auge.common.utils.POIUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CuntaoStore;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.CuntaoStoreMapper;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationType;
import com.taobao.cun.auge.store.bo.InventoryStoreWriteBo;
import com.taobao.cun.auge.store.bo.StoreWriteBO;
import com.taobao.cun.auge.store.dto.InventoryStoreCreateDto;
import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.dto.StoreStatus;
import com.taobao.cun.auge.store.service.StoreException;
import com.taobao.cun.auge.tag.UserTag;
import com.taobao.cun.auge.tag.service.UserTagService;
import com.taobao.place.client.domain.ResultDO;
import com.taobao.place.client.domain.dto.StoreDTO;
import com.taobao.place.client.domain.enumtype.StoreBizType;
import com.taobao.place.client.domain.enumtype.StoreCheckStatus;
import com.taobao.place.client.domain.result.ResultCode;
import com.taobao.place.client.service.StoreCreateService;
import com.taobao.place.client.service.StoreUpdateService;

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
	@Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long create(StoreCreateDto storeCreateDto) throws StoreException{
		Station station = stationBO.getStationById(storeCreateDto.getStationId());
		if(station == null){
			throw new StoreException("服务站不存在,station_id=" + storeCreateDto.getStationId());
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
			storeDTO.setProv(Integer.parseInt(station.getProvince()));
			storeDTO.setProvName(station.getProvinceDetail());
			areaId = station.getProvince();
		}
		//市
		if(!Strings.isNullOrEmpty(station.getCity())){
			storeDTO.setCity(Integer.parseInt(station.getCity()));
			storeDTO.setCityName(station.getCityDetail());
			areaId = station.getCity();
		}
		//区/县
		if(!Strings.isNullOrEmpty(station.getCounty())){
			storeDTO.setDistrict(Integer.parseInt(station.getCounty()));
			storeDTO.setDistrictName(station.getCountyDetail());
			areaId = station.getCounty();
		}
		if(!Strings.isNullOrEmpty(station.getTown())){
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
		
		storeDTO.setStatus(com.taobao.place.client.domain.enumtype.StoreStatus.NORMAL.getValue());
		storeDTO.setCheckStatus(StoreCheckStatus.CHECKED.getValue());
		ResultDO<Long> result = storeCreateService.create(storeDTO, station.getTaobaoUserId(), StoreBizType.STORE_ITEM_BIZ.getValue());
		if(result.isFailured()){
			throw new StoreException(result.getFullErrorMsg());
		}
		
		if (ResultCode.STORE_REPEAT.getCode().equals(result.getResultCode())) {
            storeDTO.setStoreId(result.getResult());
            // 更新
            ResultDO<Boolean> updateResult = storeUpdateService.update(storeDTO, diamondConfiguredProperties.getStoreMainUserId(), StoreBizType.STORE_ITEM_BIZ.getValue());
            if(updateResult.isFailured()){
            	throw new StoreException(updateResult.getFullErrorMsg());
            }
        }
		
		String scmCode = createInventoryStore(storeCreateDto, station.getTaobaoUserId(), areaId);
		//打标
		if(!userTagService.hasTag(station.getTaobaoUserId(), UserTag.TPS_USER_TAG.getTag())){
			userTagService.addTag(station.getTaobaoUserId(), UserTag.TPS_USER_TAG.getTag());
		}
		
		//本地存储
		CuntaoStore cuntaoStore = new CuntaoStore();
		cuntaoStore.setShareStoreId(result.getResult());
		cuntaoStore.setModifier(storeCreateDto.getCreator());
		cuntaoStore.setCreator(storeCreateDto.getCreator());
		cuntaoStore.setModifier(storeCreateDto.getCreator());
		cuntaoStore.setName(storeCreateDto.getName());
		cuntaoStore.setStationId(storeCreateDto.getStationId());
		cuntaoStore.setGmtCreate(new Date());
		cuntaoStore.setGmtModified(new Date());
		cuntaoStore.setIsDeleted("n");
		cuntaoStore.setStatus(StoreStatus.NORMAL.getStatus());
		cuntaoStore.setStoreCategory(storeCreateDto.getStoreCategory().getCategory());
		cuntaoStore.setTaobaoUserId(station.getTaobaoUserId());
		cuntaoStore.setScmCode(scmCode);
		cuntaoStoreMapper.insert(cuntaoStore);
		
		return result.getResult();
	}

	private String createInventoryStore(StoreCreateDto storeCreateDto, Long userId, String areaId) throws StoreException {
		InventoryStoreCreateDto inventoryStoreCreateDto = new InventoryStoreCreateDto();
		inventoryStoreCreateDto.setName(storeCreateDto.getName());
		inventoryStoreCreateDto.setAlias(storeCreateDto.getName());
		inventoryStoreCreateDto.setUserId(userId);
		inventoryStoreCreateDto.setAreaId(Long.parseLong(areaId));
		return inventoryStoreWriteBo.create(inventoryStoreCreateDto);
	}

	
}
