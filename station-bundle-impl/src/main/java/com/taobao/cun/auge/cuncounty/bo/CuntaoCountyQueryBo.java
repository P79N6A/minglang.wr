package com.taobao.cun.auge.cuncounty.bo;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.taobao.cun.auge.contactrecord.bo.CuntaoGovContactRecordQueryBo;
import com.taobao.cun.auge.cuncounty.dto.*;
import com.taobao.cun.auge.cuncounty.tag.CountyTagUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.common.PageOutput;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.cuncounty.vo.CuntaoCountyListItemVO;
import com.taobao.cun.auge.dal.domain.CuntaoCounty;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.user.dto.CuntaoUserOrgVO;
import com.taobao.cun.auge.user.dto.UserRoleEnum;
import com.taobao.cun.auge.user.service.CuntaoUserOrgService;

@Component
public class CuntaoCountyQueryBo {
	@Resource
	private CuntaoCountyGovContactBo cuntaoCountyGovContactBo;
	@Resource
	private CuntaoCountyGovContractBo cuntaoCountyGovContractBo;
	@Resource
	private CuntaoCountyOfficeBo cuntaoCountyOfficeBo;
	@Resource
	private CainiaoCountyBo cainiaoCountyBo;
	@Resource
	private CuntaoCountyBo cuntaoCountyBo;
	@Resource
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	@Resource
	private CuntaoCountyExtMapper cuntaoCountyExtMapper;
	@Resource
	private CuntaoUserOrgService cuntaoUserOrgService;
	@Resource
	private CainiaoCountyRemoteBo cainiaoCountyRemoteBo;
	@Resource
	private CuntaoGovContactRecordQueryBo cuntaoGovContactRecordQueryBo;
	
	/**
	 * 县点基本信息
	 * @param countyId
	 * @return
	 */
	public CuntaoCountyDto getCuntaoCounty(Long countyId) {
		return cuntaoCountyBo.getCuntaoCounty(countyId);
	}
	
	/**
	 * 根据ORGID查询县点基本信息
	 * @param orgId
	 * @return
	 */
	public CuntaoCountyDto getCuntaoCountyByOrgId(Long orgId) {
		CuntaoCounty cuntaoCounty = cuntaoCountyExtMapper.getCuntaoCountyByOrgId(orgId);
		CuntaoCountyDto cuntaoCountyDto = BeanConvertUtils.convert(CuntaoCountyDto.class, cuntaoCounty);
		cuntaoCountyDto.setState(cuntaoCounty.getState());
		return cuntaoCountyDto;
	}
	/**
	 * 查询详情
	 * @param countyId
	 * @return
	 */
	public CuntaoCountyDetailDto getCuntaoCountyDetail(Long countyId) {
		CuntaoCountyDetailDto cuntaoCountyDetailDto = new CuntaoCountyDetailDto();
		//县服务中心基础信息
		CuntaoCountyDto cuntaoCountyDto = cuntaoCountyBo.getCuntaoCounty(countyId);
		cuntaoCountyDetailDto.setCuntaoCountyDto(cuntaoCountyDto);
		//县点所属组织信息
		cuntaoCountyDetailDto.setCuntaoCountyOrgDto(createCuntaoCountyOrg(cuntaoCountyDto.getOrgId()));
		//签约信息
		cuntaoCountyDetailDto.setCuntaoCountyGovContractDto(cuntaoCountyGovContractBo.getCuntaoCountyGovContract(countyId));
		//政府联系人
		cuntaoCountyDetailDto.setCuntaoCountyGovContacts(cuntaoCountyGovContactBo.getCuntaoCountyGovContacts(countyId));
		//菜鸟县仓
		cuntaoCountyDetailDto.setCainiaoCountyDto(cainiaoCountyBo.getCainiaoCountyDto(countyId));
		//办公场地
		cuntaoCountyDetailDto.setCuntaoCountyOfficeDto(cuntaoCountyOfficeBo.getCuntaoCountyOffice(countyId));
		return cuntaoCountyDetailDto;
	}
	
	public List<CuntaoCountyStateCountDto> groupCountyByState(CuntaoCountyCondition condition) {
		if(Strings.isNullOrEmpty(condition.getFullIdPath())) {
			CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getCuntaoOrg(condition.getOrgId());
			condition.setFullIdPath(cuntaoOrgDto.getFullIdPath());
		}
		List<Map<String, Object>> list = cuntaoCountyExtMapper.groupByState(condition);
		List<CuntaoCountyStateCountDto> result = Lists.newArrayList();
		list.forEach(v->{
			String state = (String) v.get("state");
			Long num = (Long) v.get("num");
			CuntaoCountyStateCountDto dto = new CuntaoCountyStateCountDto();
			dto.setNum(num);
			dto.setCuntaoCountyState(CuntaoCountyStateEnum.valueof(state));
			result.add(dto);
		});
		return result;
	}
	
	private CuntaoCountyOrgDto createCuntaoCountyOrg(Long orgId) {
		return BeanConvertUtils.convert(CuntaoCountyOrgDto.class, cuntaoOrgServiceClient.getCuntaoOrg(orgId));
	}
	
	public PageOutput<CuntaoCountyListItem> query(CuntaoCountyCondition condition){
		if(Strings.isNullOrEmpty(condition.getFullIdPath())) {
			CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getCuntaoOrg(condition.getOrgId());
			condition.setFullIdPath(cuntaoOrgDto.getFullIdPath());
		}
		if(Strings.isNullOrEmpty(condition.getFullIdPath())) {
			throw new IllegalArgumentException("orgId 或者 fullIdPath 至少有一个不为空");
		}
		int total = cuntaoCountyExtMapper.count(condition);
		List<CuntaoCountyListItem> cuntaoCountyListItems = null;
		
		if(total > 0) {
			cuntaoCountyListItems = convert(cuntaoCountyExtMapper.query(condition));
		}
		
		return new PageOutput<CuntaoCountyListItem>(condition.getPage(), condition.getPageSize(), total, cuntaoCountyListItems);
	}

	private List<CuntaoCountyListItem> convert(List<CuntaoCountyListItemVO> cuntaoCountyListItemVOs) {
		List<CuntaoCountyListItem> cuntaoCountyListItems = Lists.newArrayList();
		if(CollectionUtils.isEmpty(cuntaoCountyListItemVOs)){
			return cuntaoCountyListItems;
		}
		List<Long> countyIds = Lists.newArrayList();

		Map<Long, CountyOrgInfo> countyOrgInfos = Maps.newHashMap();
		for(CuntaoCountyListItemVO vo : cuntaoCountyListItemVOs) {
			countyIds.add(vo.getId());
			
			CountyOrgInfo countyOrgInfo = new CountyOrgInfo();
			countyOrgInfo.countyId = vo.getId();
			countyOrgInfo.countyOrgId = vo.getOrgId();
			//区域运营中心组织ID
			countyOrgInfo.areaOrgId = cuntaoOrgServiceClient.getAncestor(vo.getOrgId(), OrgRangeType.SPECIALTEAM).getId();
			//省组织ID
			countyOrgInfo.provinceOrgId = cuntaoOrgServiceClient.getAncestor(vo.getOrgId(), OrgRangeType.PROVINCE).getId();
			countyOrgInfos.put(vo.getId(), countyOrgInfo);
			
			CuntaoCountyListItem cuntaoCountyListItem = BeanConvertUtils.convert(CuntaoCountyListItem.class, vo);
			cuntaoCountyListItem.setProtocolStartDate(DateUtil.format(vo.getGmtProtocolStartDate(), "yyyy-MM-dd"));
			cuntaoCountyListItem.setState(vo.getState());
			cuntaoCountyListItem.setProtocolEndDate(DateUtil.format(vo.getGmtProtocolEndDate(), "yyyy-MM-dd"));
			cuntaoCountyListItem.setOperateDate(DateUtil.format(vo.getOperateDate(), "yyyy-MM-dd"));
			initTags(cuntaoCountyListItem, vo);
			cuntaoCountyListItems.add(cuntaoCountyListItem);
		}
		
		initOffice(cuntaoCountyListItems, countyIds);
		initLeader(cuntaoCountyListItems, countyOrgInfos);
		return cuntaoCountyListItems;
	}

	private void initTags(CuntaoCountyListItem cuntaoCountyListItem, CuntaoCountyListItemVO cuntaoCountyListItemVO) {
		cuntaoCountyListItem.setCountyTags(CountyTagUtils.convert(cuntaoCountyListItemVO.getTags()));
	}

	private void initLeader(List<CuntaoCountyListItem> cuntaoCountyListItems, Map<Long, CountyOrgInfo> countyOrgInfos) {
		List<Long> orgIds = Lists.newArrayList();
		countyOrgInfos.values().forEach(v->{
			orgIds.add(v.countyOrgId);
			orgIds.add(v.areaOrgId);
			orgIds.add(v.provinceOrgId);
		});
		
		List<CuntaoUserOrgVO> cuntaoUserOrgVOs = cuntaoUserOrgService.getCuntaoOrgUsers(orgIds, Lists.newArrayList(
				UserRoleEnum.COUNTY_LEADER.getCode(),
				UserRoleEnum.TEAM_LEADER.getCode(),
				UserRoleEnum.PROVINCE_LEADER.getCode()));
		
		Map<Long, List<CuntaoUserOrgVO>> cuntaoUserOrgVOMap = Maps.newHashMap();
		cuntaoUserOrgVOs.forEach(c->{
			List<CuntaoUserOrgVO> list = cuntaoUserOrgVOMap.get(c.getOrgId());
			if(list == null) {
				list = Lists.newArrayList();
				cuntaoUserOrgVOMap.put(c.getOrgId(), list);
			}
			list.add(c);
		});
		
		cuntaoCountyListItems.forEach(item->{
			CountyOrgInfo countyOrgInfo = countyOrgInfos.get(item.getId());
			item.setCountyLeaders(cuntaoUserOrgVOMap.get(countyOrgInfo.countyOrgId));
			item.setAreaLeaders(cuntaoUserOrgVOMap.get(countyOrgInfo.areaOrgId));
			item.setProvinceLeaders(cuntaoUserOrgVOMap.get(countyOrgInfo.provinceOrgId));
		});
	}

	private void initOffice(List<CuntaoCountyListItem> cuntaoCountyListItems, List<Long> countyIds) {
		List<CuntaoCountyOfficeDto> offices = cuntaoCountyOfficeBo.getCuntaoCountyOffices(countyIds);
		Map<Long, CuntaoCountyOfficeDto> officeMap = Maps.newHashMap();
		for(CuntaoCountyOfficeDto office : offices) {
			officeMap.put(office.getCountyId(), office);
		}
		for(CuntaoCountyListItem item : cuntaoCountyListItems) {
			CuntaoCountyOfficeDto office = officeMap.get(item.getId());
			if(office != null) {
				item.setOfficeAddress(office.getAddress());
				item.setProvinceName(office.getProvinceName());
				item.setCityName(office.getCityName());
				item.setCountyName(office.getCountyName());
				item.setTownName(office.getTownName());
			}
		}
	}
	
	public List<CainiaoWarehouseDto> getCainiaoWarehouseByCountyId(Long countyId){
		return cainiaoCountyRemoteBo.getCainiaoWarehouseByCountyId(countyId);
	}
	
	class CountyOrgInfo{
		Long countyId;
		
		Long provinceOrgId;
		
		Long areaOrgId;
		
		Long countyOrgId;
	}
}
