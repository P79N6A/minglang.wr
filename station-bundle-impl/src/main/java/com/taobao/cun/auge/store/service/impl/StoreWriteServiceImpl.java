package com.taobao.cun.auge.store.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.taobao.cun.auge.store.dto.StoreCategory;
import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.service.StoreException;
import com.taobao.cun.auge.store.service.StoreWriteService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

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
			StoreDto dto = storeReadBO.getStoreDtoByTaobaoUserId(taobaoUserId);
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
}
