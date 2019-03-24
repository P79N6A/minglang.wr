package com.taobao.cun.auge.cuncounty.bo;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.auge.client.address.DefaultAddress;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.dal.domain.CuntaoOrgAdminAddress;
import com.taobao.cun.auge.dal.domain.CuntaoOrgAdminAddressExample;
import com.taobao.cun.auge.dal.mapper.CuntaoOrgAdminAddressMapper;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;

/**
 * 报名分发地址
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoOrgAdminAddressBo {
	@Resource
	private CuntaoOrgAdminAddressMapper cuntaoOrgAdminAddressMapper;
	@Resource
	private PartnerApplyService partnerApplyService;
	
	public void create(CuntaoCountyDto cuntaoCountyDto, String operator) {
		CuntaoOrgAdminAddressExample example = new CuntaoOrgAdminAddressExample();
		example.createCriteria().andAddressCodeEqualTo(cuntaoCountyDto.getCountyCode()).andIsDeletedEqualTo("n");
		if(cuntaoOrgAdminAddressMapper.countByExample(example) == 0) {
			cuntaoOrgAdminAddressMapper.insert(createCuntaoOrgAdminAddressAddDto(cuntaoCountyDto, operator));
		}
	}
	
	public void activeRefusedPartner(CuntaoCountyDto cuntaoCountyDto,String operator) {
    	DefaultAddress address =new DefaultAddress();
    	address.setProvince(cuntaoCountyDto.getProvinceCode());
        if (StringUtil.isNotEmpty(cuntaoCountyDto.getCityCode())) {
        	address.setCity(cuntaoCountyDto.getCityCode());
        }
        if (StringUtil.isNotEmpty(cuntaoCountyDto.getCountyCode())) {
        	address.setCounty(cuntaoCountyDto.getCountyCode());
        }
        partnerApplyService.activeRefusedPartner(address,operator);
    }
	
	private CuntaoOrgAdminAddress createCuntaoOrgAdminAddressAddDto(CuntaoCountyDto cuntaoCountyDto, String operator) {
		CuntaoOrgAdminAddress dto = new CuntaoOrgAdminAddress();
		dto.setCuntaoOrgId(cuntaoCountyDto.getOrgId());
		dto.setAddressCode(cuntaoCountyDto.getCountyCode());
		dto.setAddressName(cuntaoCountyDto.getCountyName());
		dto.setCreator(operator);
		dto.setModifier(operator);
		dto.setGmtCreate(new Date());
		dto.setGmtModified(new Date());
		dto.setIsDeleted("n");
		return dto;
	}
}
