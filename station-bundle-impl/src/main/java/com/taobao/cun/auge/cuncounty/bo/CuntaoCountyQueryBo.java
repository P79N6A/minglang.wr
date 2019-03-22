package com.taobao.cun.auge.cuncounty.bo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageOutput;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDetailDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyOrgDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.cuncounty.vo.CuntaoCountyListItemVO;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
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
	
	private CuntaoCountyOrgDto createCuntaoCountyOrg(Long orgId) {
		return BeanConvertUtils.convert(CuntaoCountyOrgDto.class, cuntaoOrgServiceClient.getCuntaoOrg(orgId));
	}

	public PageOutput<CuntaoCountyListItem> query(CuntaoCountyCondition condition){
		if(Strings.isNullOrEmpty(condition.getFullIdPath())) {
			CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getCuntaoOrg(condition.getOrgId());
			condition.setFullIdPath(cuntaoOrgDto.getFullIdPath());
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
		
		List<Long> countyIds = Lists.newArrayList();
		List<Long> countyOrgIds = Lists.newArrayList();
		List<Long> areaOrgIds = Lists.newArrayList();
		List<Long> provinceOrgIds = Lists.newArrayList();
		for(CuntaoCountyListItemVO vo : cuntaoCountyListItemVOs) {
			countyIds.add(vo.getId());
			countyOrgIds.add(vo.getOrgId());
			//区域运营中心组织ID
			CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getAncestor(vo.getOrgId(), OrgRangeType.SPECIALTEAM);
			areaOrgIds.add(cuntaoOrgDto.getId());
			//省组织ID
			cuntaoOrgDto = cuntaoOrgServiceClient.getAncestor(vo.getOrgId(), OrgRangeType.PROVINCE);
			provinceOrgIds.add(cuntaoOrgDto.getId());
			
			CuntaoCountyListItem cuntaoCountyListItem = BeanConvertUtils.convert(CuntaoCountyListItem.class, vo);
			cuntaoCountyListItem.setCuntaoCountyState(CuntaoCountyStateEnum.valueof(vo.getState()));
			cuntaoCountyListItem.setProtocolStartDate(DateUtil.format(vo.getGmtProtocolStartDate(), "yyyy-MM-dd"));
			cuntaoCountyListItem.setProtocolEndDate(DateUtil.format(vo.getGmtProtocolEndDate(), "yyyy-MM-dd"));
			cuntaoCountyListItems.add(cuntaoCountyListItem);
		}
		
		return cuntaoCountyListItems;
	}
}
