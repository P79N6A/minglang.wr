package com.taobao.cun.auge.store.service.impl;

import com.alibaba.cuntao.ctsm.client.dto.enums.ChannelEnum;
import com.alibaba.cuntao.ctsm.client.dto.enums.ServiceCodeEnum;
import com.alibaba.cuntao.ctsm.client.dto.enums.WhiteListTypeEnum;
import com.alibaba.cuntao.ctsm.client.service.write.StoreSWriteService;
import com.alibaba.cuntao.ctsm.client.service.write.WhiteListWriteService;
import com.taobao.cun.auge.api.enums.station.IncomeModeEnum;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.CaiNiaoService;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.bo.StoreWriteBO;
import com.taobao.cun.auge.store.bo.StoreWriteV2BO;
import com.taobao.cun.auge.store.dto.StoreCategory;
import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreGroupInfoDto;
import com.taobao.cun.auge.store.service.StoreException;
import com.taobao.cun.auge.store.service.StoreWriteService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@HSFProvider(serviceInterface = StoreWriteService.class)
@Service("storeWriteService")
public class StoreWriteServiceImpl implements StoreWriteService {
	@Resource
	private StoreWriteBO storeWriteBO;

	@Autowired
	private StoreSWriteService storeSWriteService;
	@Autowired
	private StoreReadBO storeReadBO;
	@Autowired
	private WhiteListWriteService whiteListWriteService;
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	@Autowired
	private CaiNiaoService caiNiaoService;

	@Resource
	private StoreWriteV2BO storeWriteV2BO;

	private static final Logger logger = LoggerFactory.getLogger(StoreWriteServiceImpl.class);

	@Override
	public Long create(StoreCreateDto dto) throws StoreException {
		return storeWriteBO.create(dto);
	}

	@Override
	public Boolean updateStoreTag(Long shareStoreId, StoreCategory category) {
		return storeWriteBO.updateStoreTag(shareStoreId, category);
	}

	@Override
	public Boolean createSampleStore(Long stationId) {
		return storeWriteBO.createSampleStore(stationId);
	}

	@Override
	public Boolean createSupplyStore(Long stationId) {
		return storeWriteBO.createSupplyStore(stationId);
	}

	@Override
	public Boolean initSampleWarehouse(Long stationId) {
		return storeWriteBO.initSampleWarehouse(stationId);
	}

	@Override
	public Boolean initStoreWarehouse(Long stationId) {
		return storeWriteBO.initStoreWarehouse(stationId);
	}

	@Override
	public Boolean batchCreateSupplyStore(List<Long> stationIds) {
		return storeWriteBO.batchCreateSupplyStore(stationIds);
	}

	@Override
	public Long tb2gbCode(Long taobaocode) {
		return storeWriteBO.tb2gbCode(taobaocode);
	}

	@Override
	public Boolean batchUpdateStore(List<Long> sharedStoreIds) {
		return storeWriteBO.batchUpdateStore(sharedStoreIds);
	}

	@Override
	public Boolean batchRemoveCainiaoFeature(List<Long> stationIds) {
		return storeWriteBO.batchRemoveCainiaoFeature(stationIds);
	}

	@Override
	public Boolean batchInitStoreWarehouse(List<Long> stationIds) {
		for (Long stationId : stationIds) {
			try {
				storeWriteBO.initStoreWarehouse(stationId);
			} catch (Exception e) {
				logger.error("batchInitStoreWarehouse error[" + stationId + "]", e);
			}
		}
		return true;
	}

	@Override
	public Boolean initEndorOrg(Long stationId) {
		return storeWriteBO.initStoreEndorOrg(stationId);
	}

	@Override
	public void batchInitStoreEndorOrg() {
		storeWriteBO.batchInitStoreEndorOrg();
	}

	@Override
	public void initStoreEmployees(Long stationId) {
		storeWriteBO.initStoreEmployees(stationId);
	}

	@Override
	public void initGoodSupplyFeature(Long stationId) {
		storeWriteBO.initGoodSupplyFeature(stationId);
	}

	@Override
	public Integer getCountyCode(String countyCode, String countyDetail, String cityCode) {
		return storeWriteBO.getCountyCode(countyCode, countyDetail, cityCode);
	}

	@Override
	public void batchInitStoreEmployees(List<Long> stationIds) {
		for (Long stationId : stationIds) {
			storeWriteBO.initStoreEmployees(stationId);
		}
	}

	@Override
	public void batchInitStoreEndorOrg(List<Long> stationIds) {
		for (Long stationId : stationIds) {
			storeWriteBO.initStoreEndorOrg(stationId);
		}

	}

	@Override
	public void batchInitStoreEmployee() {
		storeWriteBO.batchInitStoreEmployee();
	}

	@Override
	public void syncStore(Long stationId) {
		storeWriteBO.syncStore(stationId);
	}

	@Override
	public void syncStore() {
		storeWriteBO.syncStore();
	}

	@Override
	public Boolean disableByTabaoUserId(Long taobaoUserId) {
		StoreDto dto = storeReadBO.getStoreDtoByTaobaoUserId(taobaoUserId);
		if (dto != null && dto.getShareStoreId() != null) {
			com.alibaba.cuntao.ctsm.client.common.Operator op = new com.alibaba.cuntao.ctsm.client.common.Operator();
			op.setChannelEnum(ChannelEnum.CTBOPS);
			op.setOperatorId(String.valueOf(taobaoUserId));
			op.setStoreId(dto.getShareStoreId());
			com.taobao.cun.shared.base.result.SimpleResult res = storeSWriteService.disableByStoreId(op,
					dto.getShareStoreId());
			if (res.isSuccess()) {
				return Boolean.TRUE;
			} else {
				throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
						res.getErrorCode() + res.getErrorMsg());
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean addWhiteListForSHRH(Long taobaoUserId) {
		PartnerStationRel r = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
		return addWhiteListForSHRHByPartnerInstance(r);
	}
	private Boolean addWhiteListForSHRHByPartnerInstance(PartnerStationRel r ){
		if (!PartnerInstanceStateEnum.DECORATING.getCode().equals(r.getState()) && !PartnerInstanceStateEnum.SERVICING.getCode().equals(r.getState())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
					"state not match");
		}
		Date d = new Date();
		if (!IncomeModeEnum.MODE_2019_NEW_STATION.getCode().equals(r.getIncomeMode())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
					"MODE_2019_NEW_STATION not equals");
		}
		if (r.getIncomeModeBeginTime().getTime() - d.getTime() > 0) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
					"IncomeModeBeginTime not begin");
		}
		if (caiNiaoService.checkCainiaoCountyIsOperating(r.getStationId())){
			StoreDto dto = storeReadBO.getStoreDtoByTaobaoUserId(r.getTaobaoUserId());
			if (dto != null && dto.getShareStoreId() != null) {
				com.taobao.cun.shared.base.result.SimpleResult res = whiteListWriteService.addWhiteList(
						ServiceCodeEnum.CTS_SHSM_DSBG, WhiteListTypeEnum.ABILITY_APPLY, dto.getShareStoreId());
				if (res.isSuccess()) {
					return Boolean.TRUE;
				} else {
					throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
							res.getErrorCode() + res.getErrorMsg());
				}
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean addWhiteListForSHRHByStationIds(List<Long> stationIds) {
		stationIds.forEach((Long t) -> addWhiteListForSHRHByStationId(t));
		return Boolean.TRUE;
	}

	private void addWhiteListForSHRHByStationId(Long stationId){
		PartnerStationRel r = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
		addWhiteListForSHRHByPartnerInstance(r);
	}

	@Override
	public Long createSupplyStoreByStationId(Long stationId) {
		return  storeWriteV2BO.createSupplyStoreByStationId(stationId);
	}

	@Override
	public Long createByStationId(Long stationId) {
		return storeWriteV2BO.createByStationId(stationId);
	}

	@Override
	public void closeStore(Long stationId)  {
		storeWriteV2BO.closeStore(stationId);
	}

	@Override
	public void modifyStationInfoForStore(Long instanceId){
		storeWriteV2BO.modifyStationInfoForStore(instanceId);
	}

	@Override
	public void uploadStoreImage(Long shareStoreId) {
		storeWriteV2BO.uploadStoreImage(shareStoreId);
	}

	@Override
	public void uploadStoreMainImage(Long shareStoreId) {
		storeWriteV2BO.uploadStoreMainImage(shareStoreId);
	}

	@Override
	public void uploadStoreSubImage(Long shareStoreId) {
		storeWriteV2BO.uploadStoreSubImage(shareStoreId);
	}

	@Override
	public Boolean syncAddStoreInfo(List<Long> stationIds) {
		return storeWriteV2BO.syncAddStoreInfo(stationIds);
	}

    @Override
    public StoreGroupInfoDto createStoreGroup(String title, String comment) {
        return storeWriteV2BO.createStoreGroup(title,comment);
    }

    @Override
    public Boolean bindStoreGroup(Long groupId, List<Long> shareStoreIds) {
        return storeWriteV2BO.bindStoreGroup(groupId,shareStoreIds);
    }

    @Override
    public Boolean unBindStoreGroup(Long groupId, List<Long> shareStoreIds) {
        return storeWriteV2BO.unBindStoreGroup(groupId,shareStoreIds);
    }

	@Override
	public void updateTaobaoUserIdById(Long id, Long taobaoUserId) {
		storeWriteBO.updateTaobaoUserIdById(id,taobaoUserId);
	}

	@Override
	public Map<String, Object> initSingleMiniapp(Long storeId) {
		return storeWriteV2BO.initSingleMiniapp(storeId);
	}

    @Override
    public void batchInitSingleMiniapp(List<Long> storeIds) {

    }

	@Override
	public Boolean initLightStore(Long taobaoUserId,Long taskInstanceId) {
		return storeWriteV2BO.initLightStore(taobaoUserId,taskInstanceId);
	}

	@Override
	public Boolean modifyStoreInfoForLightStore(Long storeId) {
		return storeWriteV2BO.modifyStoreInfoForLightStore(storeId);
	}

	@Override
	public Boolean modifyStoreSubImageFromTask(Long storeId) {
		return storeWriteV2BO.modifyStoreSubImageFromTask(storeId);
	}

	@Override
	public Boolean bindDefaultStoreGroup(Long storeId) {
		return storeWriteV2BO.bindDefaultStoreGroup(storeId);
	}

	@Override
	public Boolean initSingleMiniappForLightStore(Long storeId) {
		return storeWriteV2BO.initSingleMiniappForLightStore(storeId);
	}

	@Override
	public Boolean openLightStorePermission(Long storeId) {
		return storeWriteV2BO.openLightStorePermission(storeId);
	}


	@Override
	public Boolean runSyncStoreTask(String storeId){return storeWriteV2BO.runSyncStoreTask(storeId);}

}
