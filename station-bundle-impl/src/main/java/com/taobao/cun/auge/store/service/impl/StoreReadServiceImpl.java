package com.taobao.cun.auge.store.service.impl;

import com.alibaba.cuntao.ctsm.client.dto.read.ServiceJudgmentForStoreQuitDTO;
import com.alibaba.cuntao.ctsm.client.service.read.StoreSReadService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.api.enums.station.IncomeModeEnum;
import com.taobao.cun.auge.client.operator.DefaultOperatorEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.*;
import com.taobao.cun.auge.dal.mapper.CuntaoStoreMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.CaiNiaoService;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreQueryPageCondition;
import com.taobao.cun.auge.store.dto.StoreStatus;
import com.taobao.cun.auge.store.service.StoreReadService;
import com.taobao.cun.auge.task.dto.TaskElementDto;
import com.taobao.cun.auge.task.dto.TaskInstanceDto;
import com.taobao.cun.auge.task.dto.TaskNodeDto;
import com.taobao.cun.auge.task.enums.TaskElementType;
import com.taobao.cun.auge.task.enums.TaskNodeTypeEnum;
import com.taobao.cun.auge.task.service.TaskInstanceService;
import com.taobao.cun.recruit.ability.dto.ServiceAbilityEmployeeInfoDto;
import com.taobao.cun.recruit.ability.service.ServiceAbilityEmployeeInfoService;
import com.taobao.cun.shared.base.result.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.place.client.domain.enumtype.v2.StoreExtendsTypeV2;
import com.taobao.place.client.service.v2.StoreExtendServiceV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.taobao.cun.auge.common.utils.PageDtoUtil.success;

@HSFProvider(serviceInterface = StoreReadService.class)
@Service("storeReadService")
public class StoreReadServiceImpl implements StoreReadService {

	@Resource
	private DiamondConfiguredProperties diamondConfiguredProperties;
	
	@Autowired
	private StoreReadBO storeReadBO;

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

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	private ServiceAbilityEmployeeInfoService serviceAbilityEmployeeInfoService;

	@Autowired
	private TaskInstanceService taskInstanceService;

	@Autowired
	private StoreExtendServiceV2 storeExtendServiceV2;
	
	private static final Logger logger = LoggerFactory.getLogger(StoreReadServiceImpl.class);
	@Override
	public StoreDto getStoreByStationId(Long stationId) {
		return storeReadBO.getStoreDtoByStationId(stationId);
	}

	@Override
	public StoreDto getStoreByTaobaoUserId(Long taobaoUserId) {
		PartnerStationRel p = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if (p == null ){
			return null;
		}
		return storeReadBO.getStoreDtoByStationId(p.getStationId());
		//return storeReadBO.getStoreDtoByTaobaoUserId(taobaoUserId);
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
		ServiceAbilityEmployeeInfoDto infoDto = serviceAbilityEmployeeInfoService
			.getEmployeeInfoByTaobaoUserId(employeeTaobaoUserId);
		if (infoDto != null && infoDto.getStationId() != null) {
			return getStoreByStationId(infoDto.getStationId());
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

	private List<Long> getstoreId(List<Long> stationIds){
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
		csCriteria.andStatusEqualTo("NORMAL");
		List<CuntaoStore> cuntaoStores = cuntaoStoreMapper.selectByExample(csExample);
		return Optional.ofNullable(cuntaoStores).orElse(Lists.newArrayList()).stream().map(CuntaoStore::getShareStoreId).collect(Collectors.toList());

	}

	@Override
	public PageDto<Long> queryByPageForShrhPermission(Date beginDate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		PartnerStationRelExample example = new PartnerStationRelExample();
		List<String> slist = new ArrayList<String>();
		slist.add(PartnerInstanceStateEnum.DECORATING.getCode());
		slist.add(PartnerInstanceStateEnum.SERVICING.getCode());
        PartnerStationRelExample.Criteria  csCriteria = example.createCriteria();
        csCriteria.andIsDeletedEqualTo("n");
        csCriteria.andStateIn(slist).andTypeEqualTo("TP");
        csCriteria.andIncomeModeEqualTo(IncomeModeEnum.MODE_2019_NEW_STATION.getCode());
        csCriteria.andIncomeModeBeginTimeLessThanOrEqualTo(new Date());
        if ( beginDate != null) {
            csCriteria.andIncomeModeBeginTimeGreaterThanOrEqualTo(beginDate);
        }
		Page<PartnerStationRel> rList = (Page<PartnerStationRel>)partnerStationRelMapper.selectByExample(example);
		List<Long> stationIds = rList.stream().map(PartnerStationRel::getStationId).collect(Collectors.toList());

		return PageDtoUtil.success(rList, getstoreId(stationIds));
	}

	@Override
	public PageDto<Long> queryByPageForShrhPermissionToTPS(Date beginDate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		PartnerStationRelExample example = new PartnerStationRelExample();
		List<String> slist = new ArrayList<>();
		slist.add(PartnerInstanceStateEnum.DECORATING.getCode());
		slist.add(PartnerInstanceStateEnum.SERVICING.getCode());
		PartnerStationRelExample.Criteria  csCriteria = example.createCriteria();
		csCriteria.andIsDeletedEqualTo("n");
		csCriteria.andStateIn(slist).andTypeEqualTo("TPS");
		csCriteria.andServiceBeginTimeLessThanOrEqualTo(new Date());
		if ( beginDate != null) {
			csCriteria.andServiceBeginTimeGreaterThanOrEqualTo(beginDate);
		}
		Page<PartnerStationRel> rList = (Page<PartnerStationRel>)partnerStationRelMapper.selectByExample(example);
		List<Long> stationIds = rList.stream().map(PartnerStationRel::getStationId).collect(Collectors.toList());

		return PageDtoUtil.success(rList, getstoreId(stationIds));
	}

	@Override
	public List<Map<String,String>>  getSubImageFromTask(Long taskInstanceId,Long taobaoUserId) {
		List<Map<String,String>> res = new ArrayList<>();
		com.taobao.cun.auge.common.dto.OperatorDto od = new com.taobao.cun.auge.common.dto.OperatorDto();
		od.setOperator(String.valueOf(taobaoUserId));
		od.setOperatorType(DefaultOperatorEnum.HAVANA);
		//od.copyOperatorDto(com.taobao.cun.auge.common.dto.OperatorDto.defaultOperator());
		TaskInstanceDto dto = taskInstanceService.getTaskInstanceById(taskInstanceId, od);
		if (dto == null ) {
			return res;
		}
		List<TaskNodeDto> busiModeList = dto.getTaskNodes().stream().filter(i -> TaskNodeTypeEnum.TASK_NODE.equals(i.getNodeType())).collect(Collectors.toList());
		if (org.apache.commons.collections.CollectionUtils.isEmpty(busiModeList)) {
			return res;
		}
		int seq = 1;
		for (TaskNodeDto d : busiModeList) {
			if (org.apache.commons.collections.CollectionUtils.isEmpty(d.getTaskElements())) {
				continue;
			}
			for (TaskElementDto d1 : d.getTaskElements()) {
				if (TaskElementType.PHOTO.equals(d1.getElementType()) && !"PHOTO4".equals(d1.getElementKey())) {
					List<Map<String, String>> l = JSON.parseObject(d1.getResult(), new TypeReference<List<Map<String, String>>>() {
					});
					for (int i=0;i<l.size();i++) {
						Map<String,String> p = new HashMap<>();
						String url = l.get(i).get("url").replace(diamondConfiguredProperties.getStoreImagePerfix(), "");
						p.put("url",url);
						p.put("name","子图"+seq);
						p.put("seq",""+seq);
						res.add(p);
						seq++;
					}
				}
			}
		}
		return res;
	}

	@Override
	public Object getImageFromPlace(Long storeId) {
		return storeExtendServiceV2.getStoreExtends(storeId, StoreExtendsTypeV2.STORE_ENVIRONMENT_PICS);
	}
}
