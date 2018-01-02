package com.taobao.cun.auge.store.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeType;
import com.taobao.cun.auge.company.dto.CuntaoVendorEmployeeState;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeExample;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRel;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRelExample;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeRelMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreQueryPageCondition;
import com.taobao.cun.auge.store.dto.StoreStatus;
import com.taobao.cun.auge.store.service.StoreReadService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = StoreReadService.class)
public class StoreReadServiceImpl implements StoreReadService {

	@Autowired
	private StoreReadBO storeReadBO;
	
	@Autowired
	private CuntaoEmployeeMapper cuntaoEmployeeMapper;
	
	@Autowired
	private CuntaoEmployeeRelMapper cuntaoEmployeeRelMapper;
	
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

}
