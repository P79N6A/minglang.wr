package com.taobao.cun.auge.store.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.cuntao.ctsm.client.dto.read.ServiceJudgmentForStoreQuitDTO;
import com.alibaba.cuntao.ctsm.client.service.read.StoreSReadService;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.api.enums.station.IncomeModeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeType;
import com.taobao.cun.auge.company.dto.CuntaoVendorEmployeeState;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeExample;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRel;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRelExample;
import com.taobao.cun.auge.dal.domain.CuntaoStore;
import com.taobao.cun.auge.dal.domain.CuntaoStoreExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample.Criteria;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeRelMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoStoreMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.CaiNiaoService;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreQueryPageCondition;
import com.taobao.cun.auge.store.dto.StoreStatus;
import com.taobao.cun.auge.store.service.StoreReadService;
import com.taobao.cun.shared.base.result.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.util.CollectionUtils;

@HSFProvider(serviceInterface = StoreReadService.class)
@Service("storeReadService")
public class StoreReadServiceImpl implements StoreReadService {
	
	@Autowired
	private StoreReadBO storeReadBO;
	
	@Autowired
	private CuntaoEmployeeMapper cuntaoEmployeeMapper;
	
	@Autowired
	private CuntaoEmployeeRelMapper cuntaoEmployeeRelMapper;
	
	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
	 @Autowired
	PartnerStationRelMapper partnerStationRelMapper;
	 @Autowired
	 CuntaoStoreMapper cuntaoStoreMapper;
	@Autowired
	private StationBO sationBO;
	@Autowired
	private StoreSReadService storeSReadService;
	
	@Autowired
	private CaiNiaoService caiNiaoService;
	
	private static final Logger logger = LoggerFactory.getLogger(StoreReadServiceImpl.class);
	@Override
	public StoreDto getStoreByStationId(Long stationId) {
		return storeReadBO.getStoreDtoByStationId(stationId);
	}

	@Override
	public StoreDto getStoreByTaobaoUserId(Long taobaoUserId) {
		return storeReadBO.getStoreDtoByTaobaoUserId(taobaoUserId);
	}

	@Override
	public StoreDto getStoreByScmCode(String scmCode) {
		return storeReadBO.getStoreByScmCode(scmCode);
	}

	@Override
	public StoreDto getStoreBySharedStoreId(Long sharedStoreId) {
		return storeReadBO.getStoreBySharedStoreId(sharedStoreId);
	}

	@Override
	public String[] getStationDistance(Long stationId, Double lng, Double lat) {
		return storeReadBO.getStationDistance(stationId, lng, lat);
	}

	@Override
	public List<Long> getAllStoreIdsByStatus(StoreStatus status) {
		return storeReadBO.getAllStoreIdsByStatus(status);
	}

	@Override
	public Result<PageDto<StoreDto>> queryStoreByPage(StoreQueryPageCondition storeQueryPageCondition) {
		try {
			PageDto<StoreDto> stores = storeReadBO.queryStoreByPage(storeQueryPageCondition);
			return Result.of(stores);
		} catch (Exception e) {
			logger.error("queryStoreByPage",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, "", "系统异常"));
		}
	}

	@Override
	public List<StoreDto> getStoreByStationIds(List<Long> stationIds) {
		return storeReadBO.getStoreByStationIds(stationIds);
	}

	@Override
	public List<StoreDto> getStoreBySharedStoreIds(List<Long> sharedStoreIds) {
		return storeReadBO.getStoreBySharedStoreIds(sharedStoreIds);
	}

	@Override
	public StoreDto getStoreByStoreEmployeeTaobaoUserId(Long employeeTaobaoUserId) {
		StoreDto store = this.getStoreByTaobaoUserId(employeeTaobaoUserId);
		if(store != null){
			return store;
		}
		CuntaoEmployeeExample example = new CuntaoEmployeeExample();
		example.createCriteria().andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(employeeTaobaoUserId).andTypeEqualTo(CuntaoEmployeeType.store.name());
		List<CuntaoEmployee> employees = cuntaoEmployeeMapper.selectByExample(example);
		if(employees != null && !employees.isEmpty()){
			CuntaoEmployee employee = employees.iterator().next();
			CuntaoEmployeeRelExample relExample  = new CuntaoEmployeeRelExample();
			relExample.createCriteria().andIsDeletedEqualTo("n").andEmployeeIdEqualTo(employee.getId()).andTypeEqualTo(CuntaoEmployeeType.store.name())
			.andStateEqualTo(CuntaoVendorEmployeeState.SERVICING.name());
			 List<CuntaoEmployeeRel>  cuntaoEmployeeRels = cuntaoEmployeeRelMapper.selectByExample(relExample);
			 if(cuntaoEmployeeRels != null  && !cuntaoEmployeeRels.isEmpty()){
				 CuntaoEmployeeRel rel = cuntaoEmployeeRels.iterator().next();
				 Long stationId =  rel.getOwnerId();
				 return this.getStoreByStationId(stationId);
				 
			 }
		}
		return null;
	}

	@Override
	public List<StoreDto> getStoreBySellerShareStoreIds(List<Long> sellerShareStoreId) {
		return storeReadBO.getStoreBySellerShareStoreIds(sellerShareStoreId);
	}

	@Override
	public boolean isTestStore(Long sharedStoreId) {
		StoreDto store = this.getStoreBySharedStoreId(sharedStoreId);
		Assert.notNull(store);
		Station station = this.sationBO.getStationById(store.getStationId());
		CuntaoOrgDto largearea = cuntaoOrgServiceClient.getAncestor(station.getApplyOrg(), OrgRangeType.LARGE_AREA);
		if (largearea != null) {
			return largearea.getId().equals(500004L);
		}
		return true;
		
		//return largearea.getId().equals(500004L);
//		if(station.getApplyOrg() == 99999L){
//				return true;
//		}
//		return false;
	}

	@Override
	public Boolean serviceJudgmentForStoreQuit(Long storeId) {
		
		ResultModel<ServiceJudgmentForStoreQuitDTO> rs = storeSReadService.serviceJudgmentForStoreQuit(storeId);
		if (rs.isSuccess() &&  rs.getModel() != null) {
			ServiceJudgmentForStoreQuitDTO dto = rs.getModel();
			if (dto.getCanQuit()) {
				return Boolean.TRUE;
			}
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
					dto.getReason());
		}
		throw new AugeBusinessException(AugeErrorCodes.SYSTEM_ERROR_CODE,
				"门店服务退出校验，系统异常");
	}

	@Override
	 public List<Long> queryListForShrhPermission(Date beginDate) {
		PartnerStationRelExample example = new PartnerStationRelExample();
		List<String> slist = new ArrayList<String>();
		slist.add(PartnerInstanceStateEnum.DECORATING.getCode());
		slist.add(PartnerInstanceStateEnum.SERVICING.getCode());
		 example.createCriteria().
				andIsDeletedEqualTo("n")
				.andStateIn(slist).andTypeEqualTo("TP").
				andIncomeModeEqualTo(IncomeModeEnum.MODE_2019_NEW_STATION.getCode())
				.andIncomeModeBeginTimeLessThanOrEqualTo(new Date())
				.andIncomeModeBeginTimeGreaterThanOrEqualTo(beginDate);
		
		List<PartnerStationRel>  rList = partnerStationRelMapper.selectByExample(example);
        List<Long> stationIds = Optional.ofNullable(rList).orElse(Lists.newArrayList()).stream().map(PartnerStationRel::getStationId).collect(Collectors.toList());

        List<Long> lastStationIds = stationIds.stream()
				.filter(i -> caiNiaoService.checkCainiaoCountyIsOperating(i))
				.collect(Collectors.toList());

        if(CollectionUtils.isEmpty(lastStationIds)){
        	return Lists.newArrayList();
		}

        CuntaoStoreExample csExample = new CuntaoStoreExample();
        CuntaoStoreExample.Criteria csCriteria = csExample.createCriteria();
        csCriteria.andIsDeletedEqualTo("n");
        csCriteria.andStationIdIn(lastStationIds);
        List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(csExample);

        return Optional.ofNullable(cuntaoStores).orElse(Lists.newArrayList()).stream().map(CuntaoStore::getShareStoreId).collect(Collectors.toList());
        
		
	}

}
