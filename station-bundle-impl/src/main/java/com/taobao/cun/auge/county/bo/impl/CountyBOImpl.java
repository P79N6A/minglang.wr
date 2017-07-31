package com.taobao.cun.auge.county.bo.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.cainiao.cuntaonetwork.constants.warehouse.WarehouseConst.WarehouseStatus;
import com.alibaba.cainiao.cuntaonetwork.dto.warehouse.WarehouseDTO;
import com.alibaba.common.lang.StringUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.taobao.biz.common.division.ChinaDivisionManager;
import com.taobao.biz.common.division.DivisionVO;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.attachment.dto.AttachmentDeleteDto;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.cache.TairCache;
import com.taobao.cun.auge.client.address.DefaultAddress;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.county.bo.CountyBO;
import com.taobao.cun.auge.county.dto.AddressDto;
import com.taobao.cun.auge.county.dto.CnWarehouseDto;
import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.county.dto.CountyQueryCondition;
import com.taobao.cun.auge.county.dto.CountyStationQueryCondition;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationExample;
import com.taobao.cun.auge.dal.domain.CountyStationExample.Criteria;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.CuntaoOrg;
import com.taobao.cun.auge.dal.domain.CuntaoOrgAdminAddress;
import com.taobao.cun.auge.dal.domain.CuntaoOrgAdminAddressExample;
import com.taobao.cun.auge.dal.domain.CuntaoOrgExample;
import com.taobao.cun.auge.dal.mapper.CountyStationMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoOrgAdminAddressMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoOrgMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.msg.dto.MailSendDto;
import com.taobao.cun.auge.msg.service.MessageService;
import com.taobao.cun.auge.org.bo.CuntaoOrgBO;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.dto.CaiNiaoStationDto;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.dto.EmpInfoDto;
import com.taobao.cun.auge.station.enums.CountyStationLeaseTypeEnum;
import com.taobao.cun.auge.station.enums.CountyStationManageModelEnum;
import com.taobao.cun.auge.station.enums.CountyStationManageStatusEnum;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.common.exception.ParamException;
import com.taobao.cun.common.util.ListUtils;
import com.taobao.cun.dto.org.enums.CuntaoOrgDeptProEnum;
import com.taobao.cun.dto.org.enums.CuntaoOrgTypeEnum;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;
@Component("countyBO")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class CountyBOImpl implements CountyBO {

	public static final String SP = ";";
	public static final String SSP = ":";
	ChinaDivisionManager chinaDivisionManager;
	@Autowired
	CountyStationMapper countyStationMapper;
	@Autowired
	CuntaoOrgMapper cuntaoOrgMapper;
	@Autowired
	CuntaoOrgAdminAddressMapper cuntaoOrgAdminAddressMapper;
	@Autowired
	CuntaoOrgBO cuntaoOrgBO;
	@Autowired
	TairCache tairCache;
    UicReadServiceClient uicReadServiceClient;
	@Autowired
	CountyStationBO countyStationBO;
	@Autowired
	CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	@Autowired
	CaiNiaoAdapter caiNiaoAdapter;
    @Autowired
    AttachmentService criusAttachmentService;
    @Autowired
    Emp360Adapter emp360Adapter;
    @Autowired
	AppResourceService appResourceService;
    @Autowired
    PartnerApplyService partnerApplyService;
    @Autowired
    MessageService messageService;
	private static final String TEMPLATE_ID = "580107779";
    private static final String SOURCE_ID = "cuntao_org*edit_addr";
    private static final String MESSAGE_TYPE_ID = "120975556";
	
	
	public List<CountyDto> getProvinceList(List<Long> areaOrgIds) {
		if(areaOrgIds==null||areaOrgIds.size()==0){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"areaOrgIds is null");
		}
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("areaOrgIds", areaOrgIds);
		List<CountyStation> countys=countyStationMapper.getProvinceList(param);
		List<CountyDto> result=new ArrayList<CountyDto>();
		for(CountyStation county:countys){
			CountyDto  csdto = new CountyDto();
			csdto.setId(Long.parseLong(county.getProvince())); //省行政编码
			csdto.setName(county.getProvinceDetail()); //省名称
			result.add(csdto);
		}
		return result;
	}
	
	public List<CountyDto> getCountyStationByProvince(String provinceCode) {
		Validate.notNull(provinceCode, "provinceCode is null");
		CountyStationExample example = new CountyStationExample();
		Criteria c = example.createCriteria();
		c.andIsDeletedEqualTo("n").andManageStatusEqualTo("OPERATING")
				.andProvinceEqualTo(provinceCode)
				.andParentIdNotEqualTo(new Long(25));
		List<CountyStation> stations = countyStationMapper
				.selectByExample(example);
		List<CountyDto> result = new ArrayList<CountyDto>();
		List<Long> orgids = new ArrayList<Long>();
		for (CountyStation csdo : stations) {
			CountyDto county = new CountyDto();
			Long orgId = csdo.getOrgId();
			if (!orgids.contains(orgId)) {
				orgids.add(orgId);
				county.setOrgId(orgId);
				county.setCountyDetail(csdo.getName());
				result.add(county);
			}
		}
		return result;
	}
    
    
	public List<CountyDto> getCountyStationList(List<Long> areaIds){
		Validate.notNull(areaIds, "areaIds is null");
		CountyStationExample example = new CountyStationExample();
		Criteria c = example.createCriteria();
		c.andIsDeletedEqualTo("n").andManageStatusEqualTo("OPERATING").andParentIdIn(areaIds).andCountyDetailIsNotNull();
		List<CountyStation> stations = countyStationMapper.selectByExample(example);
		List<CountyDto> result = new ArrayList<CountyDto>();
        for(CountyStation station:stations){
        	result.add(toCountyDto(station));
        }
        return result;
	}
	
	public CountyDto getCountyStation(Long id) {
		Validate.notNull(id, "id is null");
		CountyStation county = countyStationMapper.selectByPrimaryKey(id);
		if (county != null) {
			CountyDto dto = toCountyDto(county);
			List<CnWarehouseDto> warehouses = getWarehouses(id);
			dto.setWarehouseDtos(warehouses);
			return dto;
		} else {
			return null;
		}
	}

	public CountyDto getCountyStationByOrgId(Long id) {
		Validate.notNull(id, "id is null");
		CountyStationExample example = new CountyStationExample();
		Criteria c = example.createCriteria();
		c.andIsDeletedEqualTo("n").andOrgIdEqualTo(id);
		List<CountyStation> stations = countyStationMapper
				.selectByExample(example);
		if (CollectionUtils.isNotEmpty(stations)) {
			CountyDto dto = toCountyDto(stations.get(0));
			return dto;
		} else {
			return null;
		}
	}
	
	public List<CountyDto> getCountyStationByOrgIds(List<Long> ids) {
		Validate.notNull(ids, "ids is null");
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.<CountyDto> emptyList();
		}
		CountyStationExample example = new CountyStationExample();
		Criteria c = example.createCriteria();
		c.andIsDeletedEqualTo("n").andOrgIdIn(ids);
		List<CountyStation> stations = countyStationMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(stations)) {
			List<CountyDto> rst = new ArrayList<CountyDto>();
			for (CountyStation cs : stations) {
				CountyDto dto = toCountyDto(cs);
				rst.add(dto);
			}
			return rst;
		} else {
			return Collections.<CountyDto> emptyList();
		}
	}
	
	public PageDto<CountyDto> getCountyStationList(CountyStationQueryCondition queryCondition){
        Validate.notNull(queryCondition, "queryCondition is null");
        Validate.notNull(queryCondition.getParentId(), "queryCondition.parentId is null");
        if (queryCondition.getPageStart() < 0) {
            queryCondition.setPageStart(0);
        }
        if (queryCondition.getPageSize() <= 0) {
            queryCondition.setPageSize(10);
        }
        if (queryCondition.getPageSize() > 100) {
            queryCondition.setPageSize(100);
        }
        //移动端，特殊处理
        dealWithMobile(queryCondition);
        CountyStationExample example =new CountyStationExample();
        Criteria c = example.createCriteria().andIsDeletedEqualTo("n");
        if(queryCondition.getParentId()!=null){
            c.andParentIdEqualTo(queryCondition.getParentId());
        }
        if(StringUtils.isNotEmpty(queryCondition.getName())){
        	c.andNameEqualTo(queryCondition.getName());
        }
        if(queryCondition.getStatusArray()!=null&&queryCondition.getStatusArray().size()>0){
        	c.andManageStatusIn(queryCondition.getStatusArray());
        }
		if(StringUtils.isNotEmpty(queryCondition.getProvinceCode())){
			c.andProvinceEqualTo(queryCondition.getProvinceCode());
		}
		if(StringUtils.isNotEmpty(queryCondition.getCityCode())){
			c.andCityEqualTo(queryCondition.getCityCode());
		}
		if(StringUtils.isNotEmpty(queryCondition.getCountyCode())){
			c.andCountyEqualTo(queryCondition.getCountyCode());
		}
        int total=countyStationMapper.countByExample(example);
        if (null == queryCondition.getOrderByEnum()) {
        	  example.setOrderByClause("gmt_modified desc");
        } else {
        	example.setOrderByClause(queryCondition.getOrderByEnum().toOrderBySQL());
        }
        PageHelper.startPage(queryCondition.getPageStart(), queryCondition.getPageSize());
		List<CountyStation> countys = countyStationMapper.selectByExample(example);
        List<CountyDto> rst = new ArrayList<CountyDto>();
        for (CountyStation cs : countys) {
            CountyDto dto = toCountyDto(cs);
            rst.add(dto);
        }
        PageDto<CountyDto> returnModel = new PageDto<CountyDto>();
        returnModel.setItems(rst);
		returnModel.setTotal(new Long(total));
        return returnModel;
	}
	
	public PageDto<CountyDto> queryCountyStation(CountyQueryCondition queryCondition){
		Assert.notNull(queryCondition);
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("countyOfficial", queryCondition.getCountyOfficial());
		param.put("teamLeader", queryCondition.getTeamLeader());
		param.put("state", queryCondition.getState());
        param.put("fullIdPaths", queryCondition.getFullIdPaths());
        param.put("countyName", queryCondition.getCountyName());
        param.put("leaseProtocolEndTime", queryCondition.getLeaseProtocolEndTime());
		int total = countyStationMapper.countCountyStation(param);
		List<CountyDto> countyStationDtos = null;
		if(total > 0){
			param.put("startItem", queryCondition.getPageSize() * (queryCondition.getPage() - 1));
			param.put("pageSize", queryCondition.getPageSize());
			List<CountyStation> countyStations = countyStationMapper.queryCountyStation(param);
			countyStationDtos = Lists.transform(countyStations, new Function<CountyStation, CountyDto>(){
				@Override
				public CountyDto apply(CountyStation countyStation) {
					CountyDto countyStationDto = new CountyDto();
					countyStationDto.setId(countyStation.getId());
					countyStationDto.setOrgId(countyStation.getOrgId());
					countyStationDto.setEmployeeId(countyStation.getEmployeeId());
					countyStationDto.setFreeDeadline(countyStation.getFreeDeadline());
					countyStationDto.setGmtStartOperation(countyStation.getGmtStartOperation());
					countyStationDto.setAcreage(countyStation.getAcreage());
					countyStationDto.setLeasingModel(countyStation.getLeasingModel());
					countyStationDto.setLogisticsOperator(countyStation.getLogisticsOperator());
					countyStationDto.setLogisticsPhone(countyStation.getLogisticsPhone());
					countyStationDto.setManageModel(CountyStationManageModelEnum.valueof(countyStation.getManageModel()));
					countyStationDto.setManageStatus(CountyStationManageStatusEnum.valueof(countyStation.getManageStatus()));
					countyStationDto.setName(countyStation.getName());
					countyStationDto.setOfficeDetail(countyStation.getOfficeDetail());
					countyStationDto.setOperator(countyStation.getCreator());
					countyStationDto.setOrgId(countyStation.getOrgId());
					countyStationDto.setName(countyStation.getName());
					AddressDto addressDto=new AddressDto();
					BeanUtils.copyProperties(countyStation, addressDto);
					countyStationDto.setAddressDto(addressDto);
					countyStationDto.setLeaseProtocolBeginTime(countyStation.getLeaseProtocolBeginTime());
					countyStationDto.setLeaseProtocolEndTime(countyStation.getLeaseProtocolEndTime());
					countyStationDto.setLeaseProtocolBeginTime(countyStation.getLeaseProtocolBeginTime());
					countyStationDto.setLeaseProtocolEndTime(countyStation.getLeaseProtocolEndTime());
					countyStationDto.setLeaseProtocolBeginTimeFormat(formatDate(countyStation.getLeaseProtocolBeginTime()));
					countyStationDto.setLeaseProtocolEndTimeFormat(formatDate(countyStation.getLeaseProtocolEndTime()));
					countyStationDto.setLeaseTypeEnum(CountyStationLeaseTypeEnum.valueof(countyStation.getLeaseType()));
					countyStationDto.setLeasePayment(countyStation.getLeasePayment());
					return countyStationDto;
				}
			});
		}
		PageDto<CountyDto> result= new PageDto<CountyDto>();
		result.setTotal(total);
		result.setItems(countyStationDtos);
		return result;
	}
	
	private String formatDate(Date date) {
		if (date==null) {
			return null;
		}
		SimpleDateFormat sdf =	new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	private void dealWithMobile(CountyStationQueryCondition queryCondition) {
		// 移动端，parentId==orgId,如果当前组织是县服务中心，则查询的parentId设置为该县所属大区的Id
		// pc端parentId最低级别为大区id,不存在该问题
		if (queryCondition.isMobile()&&null != queryCondition.getParentId()) {
			CuntaoOrg queryOrg = cuntaoOrgMapper
					.selectByPrimaryKey(queryCondition.getParentId());
			// parentId为全国，则设置为null
			if (queryOrg.getOrderLevel()==1) {
				queryCondition.setParentId(null);
				// parentId为县点，则设置为大区Id
			} else if (queryOrg.getOrderLevel()==3) {
				queryCondition.setParentId(queryOrg.getParentId());
			}
		}
	}

	 
	private CountyDto toCountyDto(CountyStation cs) {
		CountyDto dto = new CountyDto();
		BeanUtils.copyProperties(cs, dto);
		dto.setManageModel(CountyStationManageModelEnum.valueof(cs
				.getManageModel()));
		dto.setManageStatus(CountyStationManageStatusEnum.valueof(cs
				.getManageStatus()));
		dto.setLeaseTypeEnum(CountyStationLeaseTypeEnum.valueof(cs
				.getLeaseType()));
		if (dto.getManageModel() == null) {
			dto.setManageModel(CountyStationManageModelEnum.SELF);
		}
		if (dto.getManageStatus() == null) {
			dto.setManageStatus(CountyStationManageStatusEnum.PLANNING);
		}
		// 创建时间
		dto.setCreateTime(cs.getGmtCreate());
		// 开始运营时间
		dto.setStartOperationTime(cs.getGmtStartOperation());
		if (cs.getEmployeeId() != null) {
			String empName = emp360Adapter.getName(cs.getEmployeeId());
			if (!StringUtils.isEmpty(empName)) {
				dto.setEmployeeName(empName);
			}
		}

		if (dto.getParentId() != null) {
			CuntaoOrgExample example = new CuntaoOrgExample();
			com.taobao.cun.auge.dal.domain.CuntaoOrgExample.Criteria c = example
					.createCriteria();
			c.andIsDeletedEqualTo("n").andIdEqualTo(dto.getParentId());
			List<CuntaoOrg> list = cuntaoOrgMapper.selectByExample(example);
			if (CollectionUtils.isNotEmpty(list)) {
				CuntaoOrg cuntaoOrg = list.get(0);
				dto.setParentName(cuntaoOrg.getName());
			}
		}
		// 查询附件
		dto.setAttachments(criusAttachmentService.getAttachmentList(cs.getId(),
				AttachmentBizTypeEnum.COUNTY_STATION));
		// 构建featureMap
		if (StringUtils.isNotEmpty(cs.getFeature())) {
			Map<String, String> featureMap = toMap(cs.getFeature());
			dto.setFeatureMap(featureMap);
		}
		dto.setWarehouseDtos(getWarehouses(cs.getOrgId()));
		AddressDto addressDto=new AddressDto();
		BeanUtils.copyProperties(cs, addressDto);
		dto.setAddressDto(addressDto);
		return dto;
	}

	private List<CnWarehouseDto> getWarehouses(Long countyId) {
		List<WarehouseDTO> results = getCountyWarehouseDto(countyId);
		List<CnWarehouseDto> list = new ArrayList<CnWarehouseDto>();
		for (WarehouseDTO dto : results) {
			list.add(convert2CnWarehouseDto(dto));
		}
		return list;
	}
	 
	private CnWarehouseDto convert2CnWarehouseDto(WarehouseDTO warehouseDto) {
		CnWarehouseDto cnWarehouseDto = new CnWarehouseDto();
		cnWarehouseDto.setId(warehouseDto.getId());
		cnWarehouseDto.setName(warehouseDto.getName());
		cnWarehouseDto.setAddress(warehouseDto.getAddress());
		cnWarehouseDto.setProvinceId(warehouseDto.getProvinceId());
		cnWarehouseDto.setCityId(warehouseDto.getCityId());
		cnWarehouseDto.setCountyId(warehouseDto.getCountyId());
		cnWarehouseDto.setTownId(warehouseDto.getTownId());
		cnWarehouseDto.setResCode(warehouseDto.getResCode());
		cnWarehouseDto.setDescription(warehouseDto.getDescription());
		cnWarehouseDto.setLng(warehouseDto.getLng());
		cnWarehouseDto.setLat(warehouseDto.getLat());
		cnWarehouseDto.setBizStart(warehouseDto.getBizStart());
		cnWarehouseDto.setBizEnd(warehouseDto.getBizEnd());
		cnWarehouseDto.setZipCode(warehouseDto.getZipCode());
		cnWarehouseDto.setCountyDomainId(warehouseDto.getCountyDomainId());
		cnWarehouseDto.setStationCount(warehouseDto.getStationCount());
		cnWarehouseDto.setCnUserName(warehouseDto.getCnUserName());
		cnWarehouseDto.setWhOwnerName(warehouseDto.getWhOwnerName());
		cnWarehouseDto.setWhOwnerMobile(warehouseDto.getWhOwnerMobile());
		cnWarehouseDto.setWhOwnerEmail(warehouseDto.getWhOwnerEmail());
		cnWarehouseDto.setCtCode(warehouseDto.getCtCode());
		cnWarehouseDto.setWarehouseStatus(convertStatus2String(warehouseDto
				.getWarehouseStatus()));
		return cnWarehouseDto;
	}
	 
	private String convertStatus2String(WarehouseStatus status) {
    	switch(status) {
    	case CLOSE:
    		return "已关闭";
    	case DELETE:
    		return "已删除";
    	case INIT:
    		return "装修中";
    	case USE:
    		return "运营中";
		default:
			return "";
    	}
    }
	/**
	 * 通过菜鸟获取县仓信息
	 * 
	 * @param countyId
	 * @return
	 */
	private List<WarehouseDTO> getCountyWarehouseDto(Long countyId) {
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO
				.queryCuntaoCainiaoStationRel(countyId,
						CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
		if (rel == null) {
			return new ArrayList<WarehouseDTO>(0);
		}
		long stationId = rel.getObjectId();
		List<WarehouseDTO> warehouseResults = caiNiaoAdapter
				.queryWarehouseById(stationId);
		return warehouseResults;
	}
	    
	private Map<String, String> toMap(final String featureString) {
		if (StringUtils.isBlank(featureString)) {
			return Collections.<String, String> emptyMap();
		}

		final Map<String, String> map = new HashMap<String, String>();
		for (String kv : StringUtils.split(featureString, SP)) {
			if (StringUtils.isBlank(kv)) {
				// 过滤掉为空的字符串片段
				continue;
			}

			final String[] ar = StringUtils.split(kv, SSP, 2);
			if (ar.length != 2) {
				// 过滤掉不符合K:V单目的情况
				continue;
			}

			final String k = ar[0];
			final String v = ar[1];
			if (StringUtils.isNotBlank(k) && StringUtils.isNotBlank(v)) {
				try {
					decode(map, k, v);
				} catch (UnsupportedEncodingException e) {
					// TODO : log this
				}
			}
		}
		return map;
	}

	private static void decode(final Map<String, String> map, final String k,
			final String v) throws UnsupportedEncodingException {
		map.put(URLDecoder.decode(k, "UTF-8"), URLDecoder.decode(v, "UTF-8"));
	}
	 
	public CountyDto saveCountyStation(String operator,CountyDto countyDto){
		validateSaveCountyStationParam(countyDto);
		//TODO 解决前台没有传入detail问题
//        converDetail(countyDto);
        //在绑定组织树之前做
        if (countyDto.getOrgId() == null && countyDto.getId() != null) {
        	CountyStation old=countyStationMapper.selectByPrimaryKey(countyDto.getId());
            if (old != null) {
            	countyDto.setOrgId(old.getOrgId());
            }
        }
        sameNameValidate(countyDto);
        //绑定 县运营中心 到村淘组织树上
        bindCuntaoCountyOrg(operator, countyDto);
        Long taobaoUserId = getTaobaoUserId(countyDto.getTaobaoNick());
        //新建县服务中心
        if (countyDto.getId() == null || countyDto.getId() <= 0) {
            CountyStation countyStation = toCountyStationDOAddNew(countyDto, taobaoUserId,operator);
            countyStationBO.addCountyStation(countyStation);
            countyDto.setId(countyStation.getId());
            // 同步菜鸟县仓
            syncNewCountyStationToCainiao(operator, countyDto, taobaoUserId);
            //新增绑定附件
            if(!ListUtils.isEmpty(countyDto.getAttachments())){
            	com.taobao.cun.common.operator.OperatorDto operatorDto =new com.taobao.cun.common.operator.OperatorDto();
            	operatorDto.setOperator(operator);
            	operatorDto.setOperatorType(com.taobao.cun.common.operator.OperatorTypeEnum.BUC);
            	operatorDto.setOperatorOrgId(1L);
            	criusAttachmentService.addAttachmentBatch(countyDto.getAttachments(), countyStation.getId(), AttachmentBizTypeEnum.COUNTY_STATION, operatorDto);
            }
            //自动绑定村淘组织和行政地址
            bindOrg2Address(countyStation,operator);
        } else {
            //修改县服务中心
            CountyStation old = countyStationMapper.selectByPrimaryKey(countyDto.getId());
            CountyStation countyStation = toCountyStationDOUpdate(operator, countyDto, old, taobaoUserId);
            countyStationMapper.updateByPrimaryKeySelective(countyStation);
            //如果是运营中 修改同步菜鸟驿站
            if ((CountyStationManageStatusEnum.OPERATING.equals(CountyStationManageStatusEnum.valueof(old.getManageStatus())))) {
                syncModifiedCountyStationToCainiao(old, countyDto);
            }
            //因为修改县服务中心，不知道有没有修改附件，一律删除，再新增
            if(!ListUtils.isEmpty(countyDto.getAttachments())) {
            	com.taobao.cun.common.operator.OperatorDto operatorDto =new com.taobao.cun.common.operator.OperatorDto();
            	operatorDto.setOperator(operator);
            	operatorDto.setOperatorType(com.taobao.cun.common.operator.OperatorTypeEnum.BUC);
            	operatorDto.setOperatorOrgId(1L);
            	AttachmentDeleteDto deletedDto=new AttachmentDeleteDto();
            	deletedDto.setObjectId(countyStation.getId());
            	deletedDto.setBizType( AttachmentBizTypeEnum.COUNTY_STATION);
            	deletedDto.copyOperatorDto(operatorDto);
            	criusAttachmentService.deleteAttachment(deletedDto);
            	criusAttachmentService.addAttachmentBatch(countyDto.getAttachments(), countyStation.getId(), AttachmentBizTypeEnum.COUNTY_STATION, operatorDto);
            }
        }
        return countyDto;
	}
	
	public static void validateSaveCountyStationParam(CountyDto countyDto) {
        Validate.notNull(countyDto, "countyDto is null");
        Validate.notEmpty(countyDto.getName(), "countyDto.name is empty");
        Validate.notNull(countyDto.getParentId(), "countyDto.parentId is null");
        Validate.notEmpty(countyDto.getProvince(), "countyDto.province is empty");
        Validate.notEmpty(countyDto.getCity(), "countyDto.city is empty");

        String logisticsOperator = countyDto.getLogisticsOperator();
        //筹划中县点，可能未传
        if (StringUtils.isEmpty(logisticsOperator)) {
            return;
        }
        int length = logisticsOperator.split(",").length;
        if (length > 4) {
            throw new ParamException("物流联系人不能超过4个!");
        }
    }
	
	 private void converDetail(CountyDto countyDto) {
	        String province = countyDto.getProvince();
	        String provinceDetail = countyDto.getProvinceDetail();
	        if (StringUtils.isNotEmpty(province) && StringUtils.isEmpty(provinceDetail)) {
	            DivisionVO divisionVO = chinaDivisionManager.getDivisionById(Long.parseLong(province));
	            if (divisionVO != null) {
	            	countyDto.setProvinceDetail(divisionVO.getDivisionAbbName());
	            }
	        }

	        String city = countyDto.getCity();
	        String cityDetail = countyDto.getCityDetail();
	        if (StringUtils.isNotEmpty(city) && StringUtils.isEmpty(cityDetail)) {
	            DivisionVO divisionVO = chinaDivisionManager.getDivisionById(Long.parseLong(city));
	            if (divisionVO != null) {
	            	countyDto.setCityDetail(divisionVO.getDivisionAbbName());
	            }
	        }

	        String county = countyDto.getCounty();
	        String countyDetail = countyDto.getCountyDetail();
	        if (StringUtils.isNotEmpty(county) && StringUtils.isEmpty(countyDetail)) {
	            DivisionVO divisionVO = chinaDivisionManager.getDivisionById(Long.parseLong(county));
	            if (divisionVO != null) {
	            	countyDto.setCountyDetail(divisionVO.getDivisionAbbName());
	            }
	        }
	        String town = countyDto.getTown();
	        String townDetail = countyDto.getTownDetail();
	        if (StringUtils.isNotEmpty(town) && StringUtils.isEmpty(townDetail)) {
	            DivisionVO divisionVO = chinaDivisionManager.getDivisionById(Long.parseLong(town));
	            if (divisionVO != null) {
	            	countyDto.setTownDetail(divisionVO.getDivisionName());
	            }
	        }
	    }
	 
	/**
	 * 校验大区组织下，是否已经有同名的县服务中心
	 *
	 * @param countyStationDto
	 */
	private void sameNameValidate(CountyDto countyDto) {
		CountyStationExample example = new CountyStationExample();
		Criteria c = example.createCriteria();
		c.andIsDeletedEqualTo("n").andParentIdEqualTo(countyDto.getParentId())
				.andNameEqualTo(countyDto.getName());
		List<CountyStation> countys = countyStationMapper
				.selectByExample(example);
		if (CollectionUtils.isNotEmpty(countys)) {
			boolean exist = false;
			for (CountyStation old : countys) {
				// 新增
				if (countyDto.getId() == null
						&& old.getName().equals(countyDto.getName())) {
					exist = true;
					break;
				}// 编辑
				else if (countyDto.getId() != null
						&& !old.getId().equals(countyDto.getId())) {
					exist = true;
					break;
				}
			}
			if (exist) {
				throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE,"县服务中心不能重名");
			}
		}
	}

	 private void bindCuntaoCountyOrg(String operator, CountyDto countyDto) {
	        //没有绑定过组织
	        if (countyDto.getOrgId() == null || countyDto.getOrgId() <= 0) {
	            //校验父组织
	        	validateParentOrg(countyDto);
	            //查询大区下已经存在的同名县服务中心组织
	            CuntaoOrg existCuntaoOrg = findCunTaoOrg(countyDto.getName(), countyDto.getParentId());
	            //因为前面已经做过同名校验
	            if (null != existCuntaoOrg) {
	            	countyDto.setOrgId(existCuntaoOrg.getId());
	            } else {
	                Long cuntaoOrgId = createCuntaoOrg(countyDto,operator);
	                countyDto.setOrgId(cuntaoOrgId);
	            }
	        } else {
	            //更新组织树中的名称和parentId等
	        	updateCuntaoOrg(countyDto,operator);
	            //清楚缓存
	            tairCache.invalid("cuntao_orgid_1");
	        }
	    }

	/**
	 * 校验父组织是否存在
	 *
	 * @param context
	 * @param countyStationDto
	 */
	private void validateParentOrg(CountyDto countyDto) {
		CuntaoOrg org=cuntaoOrgMapper.selectByPrimaryKey(countyDto.getParentId());
		if(org==null){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"查询大区异常"+countyDto.getParentId());
		}
	}
	
	/**
     * 查询 县运营中心 组织
     *
     * @return
     */
    private CuntaoOrg findCunTaoOrg(String name, long parentId) {
    	CuntaoOrgExample example=new CuntaoOrgExample();
    	com.taobao.cun.auge.dal.domain.CuntaoOrgExample.Criteria c = example.createCriteria();
		c.andIsDeletedEqualTo("n").andNameEqualTo(name).andParentIdEqualTo(parentId);
        List<CuntaoOrg> list = cuntaoOrgMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 创建 县运营中心 组织节点
     *
     * @param countyStationDto
     * @return
     */
    private Long createCuntaoOrg(CountyDto countyDto,String operator) {
        CuntaoOrg addCuntaoOrg = new CuntaoOrg();
        addCuntaoOrg.setName(countyDto.getName());
        addCuntaoOrg.setDeptPro(CuntaoOrgDeptProEnum.COUNTRY_SERVICE_CENTER.getDesc());
        addCuntaoOrg.setParentId(countyDto.getParentId());
        addCuntaoOrg.setOrgRangeType("county");
        addCuntaoOrg.setTempParentId(countyDto.getSpecialTeamId());
        if (CountyStationManageModelEnum.SELF.equals(countyDto.getManageModel())) {
            addCuntaoOrg.setOrgType(CuntaoOrgTypeEnum.SELF_SUPPORT.getCode());
        } else {
            addCuntaoOrg.setOrgType(CuntaoOrgTypeEnum.NO_SELF_SUPPORT.getCode());
        }
        Long id = cuntaoOrgBO.addOrg(addCuntaoOrg, operator);
        return id;
    }
    
    private void updateCuntaoOrg(CountyDto countyDto,String operator) {
        CuntaoOrg cuntaoOrg = new CuntaoOrg();
        	cuntaoOrg.setId(countyDto.getOrgId());
        	cuntaoOrg.setName(countyDto.getName());
            //获取新的父组织
        	CuntaoOrg parentOrgDo = cuntaoOrgMapper.selectByPrimaryKey(countyDto.getParentId());
        	cuntaoOrg.setFullIdPath(parentOrgDo.getFullIdPath() + "/" + countyDto.getOrgId());
        	cuntaoOrg.setFullNamePath(parentOrgDo.getFullNamePath() + "/" + countyDto.getName());
        	cuntaoOrg.setFullOrderPath(parentOrgDo.getFullOrderPath() + "/0");
        	cuntaoOrg.setParentId(countyDto.getParentId());
        	cuntaoOrg.setGmtModified(new Date());
        	cuntaoOrg.setModifier(operator);
            
            CuntaoOrg team = cuntaoOrgMapper.selectByPrimaryKey(countyDto.getSpecialTeamId()); 
            if(team != null){
            	cuntaoOrg.setTempFullIdPath(team.getTempFullIdPath() + "/" + countyDto.getOrgId());
            	cuntaoOrg.setTempFullNamePath(team.getTempFullNamePath() + "/" + countyDto.getName());
            	cuntaoOrg.setTempParentId(countyDto.getSpecialTeamId());
            }
            cuntaoOrgMapper.updateByPrimaryKeySelective(cuntaoOrg);
    }

    private Long getTaobaoUserId(String taobaoNick) {
        if (taobaoNick != null) {
            ResultDO<BaseUserDO> baseUserDO = uicReadServiceClient.getBaseUserByNick(taobaoNick);
            if (baseUserDO == null || baseUserDO.getModule() == null || !baseUserDO.isSuccess()) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"not find taobaoUserID: " + taobaoNick);
            }
            return baseUserDO.getModule().getUserId();
        }
        return null;
    }
    
    public static CountyStation toCountyStationDOAddNew(CountyDto countyDto,Long taobaoUserId,String operator) {
        if (countyDto.getManageModel() == null) {
        	countyDto.setManageModel(CountyStationManageModelEnum.SELF);
        }
        if (countyDto.getManageStatus() == null) {
        	countyDto.setManageStatus(CountyStationManageStatusEnum.PLANNING);
        }
        CountyStation countyStation = new CountyStation();
        BeanUtils.copyProperties(countyDto, countyStation);
        countyStation.setCreator(operator);
        countyStation.setModifier(operator);
        countyStation.setGmtCreate(new Date());
        countyStation.setGmtModified(new Date());
        countyStation.setIsDeleted("n");
        countyStation.setManageModel(countyDto.getManageModel().getCode());
        countyStation.setManageStatus(countyDto.getManageStatus().getCode());
        if (taobaoUserId != null) {
        	countyDto.setTaobaoUserId(taobaoUserId);
            countyStation.setTaobaoUserId(new Long(taobaoUserId.toString()));
        }
        if (null != countyDto.getOrgId()) {
            countyStation.setOrgId(countyDto.getOrgId());
        }
        countyStation.setGmtStartOperation(countyDto.getStartOperationTime());
        countyStation.setLeaseType(countyDto.getLeaseTypeEnum() == null ? null : countyDto.getLeaseTypeEnum().getCode());
        countyStation.setWaterPayment(StringUtils.isEmpty(countyDto.getWaterPayment())? "0" : countyDto.getWaterPayment());
        countyStation.setElectricPayment(StringUtils.isEmpty(countyDto.getElectricPayment())? "0" : countyDto.getElectricPayment());
        countyStation.setPropertyPayment(StringUtils.isEmpty(countyDto.getPropertyPayment())? "0" : countyDto.getPropertyPayment());
        return countyStation;
    }
    
    private void syncNewCountyStationToCainiao(String operator, CountyDto countyDto, Long taobaoUserId) {
        CuntaoCainiaoStationRel cuntaoCainiaoStationRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(
        		countyDto.getId(), CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
        if (null != cuntaoCainiaoStationRel) {
            return;
        }
        CaiNiaoStationDto stationDto = toNewCaiNiaoStationDto(countyDto);
        Long caiNiaostationId = caiNiaoAdapter.addCountyByOrg(stationDto);
        if (caiNiaostationId == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"同步菜鸟驿站失败");
        } else {
        	CuntaoCainiaoStationRelDto relDO = new CuntaoCainiaoStationRelDto();
            relDO.setObjectId(countyDto.getId());
            relDO.setCainiaoStationId(caiNiaostationId);
            relDO.setType(CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
            relDO.setOperator(operator);
            cuntaoCainiaoStationRelBO.insertCuntaoCainiaoStationRel(relDO);
        }
    }
    
    /**
     * StationDto 转换，新建服务中县服务中心时，物流操作人员，采用邮件方式通知菜鸟，不通过API方式
     *
     * @param countyStationDto
     * @return
     */
    private CaiNiaoStationDto toNewCaiNiaoStationDto(CountyDto countyDto) {
    	CaiNiaoStationDto stationDto = new CaiNiaoStationDto();
        stationDto.setStationName(countyDto.getName());
        stationDto.setStationAddress(convertToStationAddress(countyDto));
        stationDto.setContact(countyDto.getEmployeeName());
        stationDto.setMobile(countyDto.getLogisticsPhone());
        stationDto.setTaobaoUserId(countyDto.getTaobaoUserId());
        stationDto.setStationType(4);
        stationDto.setLoginId(countyDto.getLogisticsOperator());
        stationDto.setStationId(countyDto.getId());
        return stationDto;
    }
    
    private  Address convertToStationAddress(CountyDto countyDto) {
        Address address = new Address();
        address.setProvince(countyDto.getProvince());
        address.setProvinceDetail(countyDto.getProvinceDetail());
        address.setCity(countyDto.getCity());
        address.setCityDetail(countyDto.getCityDetail());
        address.setCounty(countyDto.getCounty());
        address.setCountyDetail(countyDto.getCountyDetail());
        address.setTown(countyDto.getTown());
        address.setTownDetail(countyDto.getTownDetail());
        address.setAddressDetail(countyDto.getAddressDetail());
        address.setLat(countyDto.getLat());
        address.setLng(countyDto.getLng());
        return address;
    }

    /**
     * 绑定村淘组织和行政地址
     *
     * @param countyStation
     * @param context
     */
    private void bindOrg2Address(CountyStation countyStation, String operator) {
        CuntaoOrgAdminAddress cuntaoOrgAdminAddressDO = buildCuntaoOrgAdminAddressDO(countyStation);
        //如果市、县都不存在，则不绑定
        if (null == cuntaoOrgAdminAddressDO) {
            return;
        }
        // 行政地址名称容易变化，只按code查询。
        CuntaoOrgAdminAddressExample example= new CuntaoOrgAdminAddressExample(); 
        com.taobao.cun.auge.dal.domain.CuntaoOrgAdminAddressExample.Criteria c=example.createCriteria();
        c.andIsDeletedEqualTo("n").andAddressCodeEqualTo(cuntaoOrgAdminAddressDO.getAddressCode());
        List<CuntaoOrgAdminAddress> addresses=cuntaoOrgAdminAddressMapper.selectByExample(example);
        //已经绑定了村淘组织,则修改为新的绑定  村淘组织 ： 行政地址=1：n，但是一个行政地址只能绑定一个组织
        if (addresses.size()>0) {
        	cuntaoOrgAdminAddressDO =addresses.get(0);
            //修改为新的村淘组织
            cuntaoOrgAdminAddressDO.setCuntaoOrgId(countyStation.getOrgId());
            cuntaoOrgAdminAddressDO.setModifier(operator);
            cuntaoOrgAdminAddressMapper.updateByPrimaryKey(cuntaoOrgAdminAddressDO);
        } else {
            cuntaoOrgAdminAddressDO.setCuntaoOrgId(countyStation.getOrgId());
            cuntaoOrgAdminAddressDO.setGmtCreate(new Date());
            cuntaoOrgAdminAddressDO.setModifier(operator);
            cuntaoOrgAdminAddressDO.setCreator(operator);
            cuntaoOrgAdminAddressDO.setGmtModified(new Date());
            cuntaoOrgAdminAddressDO.setIsDeleted("n");
            //绑定组织和行政地址
            cuntaoOrgAdminAddressMapper.insert(cuntaoOrgAdminAddressDO);
            //激活因该地区未开通的合伙人
            activeRefusedPartner(countyStation,operator);
        }
    }
    
    private void activeRefusedPartner(CountyStation countyStation,String operator) {
    	DefaultAddress address =new DefaultAddress();
        if (StringUtil.isNotEmpty(countyStation.getProvince())) {
        	address.setProvince(countyStation.getProvince());
        }
        if (StringUtil.isNotEmpty(countyStation.getCity())) {
        	address.setCity(countyStation.getCity());
        }
        if (StringUtil.isNotEmpty(countyStation.getCounty())) {
        	address.setCounty(countyStation.getCounty());
        }
        partnerApplyService.activeRefusedPartner(address,operator);
    }

    
    /**
     * 只有存在市或者县时，才自动关联
     * @param countyStation
     * @return
     */
    private CuntaoOrgAdminAddress buildCuntaoOrgAdminAddressDO(CountyStation countyStation) {
        CuntaoOrgAdminAddress cuntaoOrgAdminAddressDO = new CuntaoOrgAdminAddress();

        if (StringUtil.isNotEmpty(countyStation.getCounty())) {
            cuntaoOrgAdminAddressDO.setAddressCode(countyStation.getCounty());
            cuntaoOrgAdminAddressDO.setAddressName(countyStation.getCountyDetail());
        } else if (StringUtil.isNotEmpty(countyStation.getCity())) {
            cuntaoOrgAdminAddressDO.setAddressCode(countyStation.getCity());
            cuntaoOrgAdminAddressDO.setAddressName(countyStation.getCityDetail());
        } else {
            return null;
        }

        return cuntaoOrgAdminAddressDO;
    }
    
    private CountyStation toCountyStationDOUpdate(String operator, CountyDto countyDto,CountyStation old,Long taobaoUserId) {
        if (countyDto.getManageModel() == null) {
        	countyDto.setManageModel(CountyStationManageModelEnum.SELF);
        }
        CountyStation countyStation = new CountyStation();
        BeanUtils.copyProperties(countyDto, countyStation);
        countyStation.setModifier(operator);
        countyStation.setLeaseType(countyDto.getLeaseTypeEnum() == null ? null : countyDto.getLeaseTypeEnum().getCode());
        countyStation.setWaterPayment(StringUtils.isEmpty(countyDto.getWaterPayment())? "0" : countyDto.getWaterPayment());
        countyStation.setElectricPayment(StringUtils.isEmpty(countyDto.getElectricPayment())? "0" : countyDto.getElectricPayment());
        countyStation.setPropertyPayment(StringUtils.isEmpty(countyDto.getPropertyPayment())? "0" : countyDto.getPropertyPayment());
        countyStation.setManageModel(countyDto.getManageModel().getCode());
        if (countyDto.getManageStatus() != null) {
            countyStation.setManageStatus(countyDto.getManageStatus().getCode());
        }
        if (!CountyStationManageStatusEnum.OPERATING
                .equals(CountyStationManageStatusEnum.valueof(old.getManageStatus()))&&taobaoUserId != null) {
        	 countyStation.setTaobaoUserId(taobaoUserId);
        }
        if (null != countyDto.getOrgId()) {
            countyStation.setOrgId(countyDto.getOrgId());
        }
        countyStation.setGmtStartOperation(countyDto.getStartOperationTime());
        countyStation.setId(old.getId());
        return countyStation;
    }
    
    /**
     * 修改运营中的县运营中心数据同步至菜鸟
     *
     * @param old
     * @param countyStationDto
     */
    private void syncModifiedCountyStationToCainiao(CountyStation old,  CountyDto countyDto) {
    	CuntaoCainiaoStationRel rel= cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(old.getId(), CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
        if (rel == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"cuntaoCainiaoStationRelDao.getCuntaoCainiaoStationRelByCountyStationId is null");
        }
        //地址变更了，才发邮件,即使CountyStationDto中地址变更了
        if(needToSendAddressUpdatedEmail(old, countyDto)) {
            sendUpdateAddressEmail(countyDto, old);
        }
    }
    
  //县服务中心，地址是否变化
    private boolean needToSendAddressUpdatedEmail(CountyStation old, CountyDto countyDO) {
        if (isAddressChanged(old.getProvince(), countyDO.getProvince())) {
            return true;
        }

        if (isAddressChanged(old.getCity(), countyDO.getCity())) {
            return true;
        }

        if (isAddressChanged(old.getCounty(), countyDO.getCounty())) {
            return true;
        }
        if (isAddressChanged(old.getTown(), countyDO.getTown())) {
            return true;
        }

        if (isAddressChanged(old.getAddressDetail(), countyDO.getAddressDetail())) {
            return true;
        }
        return false;
    }
    
    private static boolean isAddressChanged(String oldAddress, String newAddress) {
        return (null != oldAddress && !oldAddress.equals(newAddress)) || (null == oldAddress && null != newAddress);
    }
    
    
    private void sendUpdateAddressEmail(CountyDto countyDto, CountyStation old) {
        Map<String, Object> params = new HashMap<String, Object>(8);
        params.put("originAddress", convertToStationAddressString(countyDto));
        params.put("curAddress", convertToStationAddressString(old));
        params.put("orgName", StringUtils.isEmpty(countyDto.getName()) ? old.getName() : countyDto.getName());
        params.put("orgType", "县服务中心");
        List<String> mailList = findEmailReceiverFromResource("station_address_change_receivers");
        MailSendDto mailSendDto =new MailSendDto();
        mailSendDto.setMailAddress(mailList);
        mailSendDto.setMessageType(MESSAGE_TYPE_ID);  
        mailSendDto.setSourceId(SOURCE_ID);
        mailSendDto.setTemplateId(TEMPLATE_ID);
        mailSendDto.setContentMap(params);
        messageService.sendMail(mailSendDto);
    }
    
    
    private  List<String> findEmailReceiverFromResource(String resourceType) {
    	 List<AppResourceDto> appResourceDOs = appResourceService.queryAppResourceList(resourceType);
        if (null == appResourceDOs || 0 == appResourceDOs.size() || null == appResourceDOs.get(0)) {
            return Collections.<String>emptyList();
        }
        return convertResourceToAccounts(appResourceDOs.get(0));
    }
    
    private List<String> convertResourceToAccounts(AppResourceDto appResourceDO) {
        if (null == appResourceDO || StringUtils.isEmpty(appResourceDO.getValue())) {
            return Collections.<String>emptyList();
        }
        String[] workNos = appResourceDO.getValue().split(",");
        if (ArrayUtils.isEmpty(workNos)) {
            return Collections.<String>emptyList();
        } else {
            return emailAddress(Arrays.<String>asList(workNos));
        }
    }
    
    private List<String> emailAddress(List<String> workNos) {
    	Map<String, EmpInfoDto> empInfoListVo = emp360Adapter.getEmpInfoByWorkNos(workNos);
        List<String> mailList = new ArrayList<String>(workNos.size());
        for (EmpInfoDto emp : empInfoListVo.values()) {
            String loginAccount = StringUtils.trim(emp.getLoginAccount());
            if (StringUtils.isNotEmpty(loginAccount)) {
                mailList.add(loginAccount + "@alibaba-inc.com");
            }
        }
        return mailList;
    }
    
    private Map<String,String> mailParam(String templateId,String sourceId,String messageTypeId){
    	Map<String,String> param = new HashMap<String, String>();
    	param.put("templateId", templateId);
    	param.put("sourceId", sourceId);
    	param.put("messageTypeId", messageTypeId);
    	return param;
    }
	private String convertToStationAddressString(CountyDto countyDto) {
		StringBuilder address = new StringBuilder();
		address.append(
				StringUtil.isBlank(countyDto.getProvinceDetail()) ? " "
						: countyDto.getProvinceDetail())
				.append(StringUtil.isBlank(countyDto.getCityDetail()) ? " "
						: countyDto.getCityDetail())
				.append(StringUtil.isBlank(countyDto.getCountyDetail()) ? " "
						: countyDto.getCountyDetail())
				.append(StringUtil.isBlank(countyDto.getTownDetail()) ? " "
						: countyDto.getTownDetail())
				.append(StringUtil.isBlank(countyDto.getAddressDetail()) ? " "
						: countyDto.getAddressDetail());
		return address.toString();
	}

	private String convertToStationAddressString(CountyStation countyStation) {
		StringBuilder address = new StringBuilder();
		address.append(
				StringUtil.isBlank(countyStation.getProvinceDetail()) ? " "
						: countyStation.getProvinceDetail())
				.append(StringUtil.isBlank(countyStation.getCityDetail()) ? " "
						: countyStation.getCityDetail())
				.append(StringUtil.isBlank(countyStation.getCountyDetail()) ? " "
						: countyStation.getCountyDetail())
				.append(StringUtil.isBlank(countyStation.getTownDetail()) ? " "
						: countyStation.getTownDetail())
				.append(StringUtil.isBlank(countyStation.getAddressDetail()) ? " "
						: countyStation.getAddressDetail());
		return address.toString();
	}

	
	public CountyDto startOperate(String operator,CountyDto countyDto){
	        validateStartOperateParam(countyDto);
	        countyDto.setManageStatus(CountyStationManageStatusEnum.OPERATING);
	        return saveCountyStation(operator, countyDto);
	}

	@Override
	public List<CountyDto> getCountyStationByCity(String cityCode) {
		Validate.notNull(cityCode, "cityCode is null");
		CountyStationExample example = new CountyStationExample();
		Criteria c = example.createCriteria();
		c.andIsDeletedEqualTo("n").andManageStatusEqualTo("OPERATING")
				.andCityEqualTo(cityCode);
		List<CountyStation> stations = countyStationMapper
				.selectByExample(example);

		if (CollectionUtils.isEmpty(stations)) {
			return Collections.<CountyDto>emptyList();

		}
		List<CountyDto> rst = new ArrayList<CountyDto>();
		for (CountyStation cs : stations) {
			CountyDto dto = toCountyDto(cs);
			rst.add(dto);
		}
		return rst;
	}
	
	
	private  void validateStartOperateParam(CountyDto countyDto) {
        Validate.notNull(countyDto, "countyDto is null");
        Validate.notEmpty(countyDto.getName(), "countyDto.name is empty");
        Validate.notEmpty(countyDto.getProvince(), "countyDto.province is empty");
        Validate.notEmpty(countyDto.getCity(), "countyDto.city is empty");
        Validate.notEmpty(countyDto.getAddressDetail(), "countyDto.addressDetail is empty");
        Validate.notNull(countyDto.getParentId(), "countyDto.parentId is null");
        Validate.notEmpty(countyDto.getAcreage(), "countyDto.acreage is empty");
        Validate.notNull(countyDto.getWarehouseNum(), "countyDto.warehouseNum is null");
        Validate.notEmpty(countyDto.getLeasingModel(), "countyDto.leasingModel is empty");
        Validate.notEmpty(countyDto.getOfficeDetail(), "countyDto.officeDetail is empty");
    }

}
