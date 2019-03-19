package com.taobao.cun.auge.cuncounty.bo;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyAddDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCounty;
import com.taobao.cun.auge.dal.domain.CuntaoOrg;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyMapper;
import com.taobao.cun.auge.org.bo.CuntaoOrgBO;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.dto.org.enums.CuntaoOrgDeptProEnum;
import com.taobao.cun.dto.org.enums.CuntaoOrgTypeEnum;

@Component
public class CuntaoCountyWriteBo {
	@Resource
	private CuntaoCountyWhitenameBo cuntaoCountyWhitenameBo;
	@Resource
	private CuntaoCountyPredicate cuntaoCountyPredicate;
	@Resource
	private CuntaoOrgBO cuntaoOrgBO;
	@Resource
	private CuntaoCountyMapper cuntaoCountyMapper;
	
	@Transactional(rollbackFor=Throwable.class)
	public Long createCuntaoCounty(CuntaoCountyAddDto cuntaoCountyAddDto) {
		cuntaoCountyPredicate.checkCreateCounty(cuntaoCountyAddDto.getCountyCode());
		//创建组织
		Long orgId = createCuntaoOrg(cuntaoCountyAddDto);
		//创建县服务中心
		return doCreateCuntaoCounty(cuntaoCountyAddDto, orgId);
	}
	
	
	
	private Long createCuntaoOrg(CuntaoCountyAddDto cuntaoCountyAddDto) {
		CuntaoOrg addCuntaoOrg = new CuntaoOrg();
        addCuntaoOrg.setName(cuntaoCountyAddDto.getName());
        addCuntaoOrg.setDeptPro(CuntaoOrgDeptProEnum.COUNTRY_SERVICE_CENTER.getDesc());
        addCuntaoOrg.setParentId(cuntaoCountyAddDto.getParentOrgId());
        addCuntaoOrg.setOrgRangeType(OrgRangeType.COUNTY.type);
        addCuntaoOrg.setTempParentId(cuntaoCountyAddDto.getParentOrgId());
        addCuntaoOrg.setOrgType(CuntaoOrgTypeEnum.SELF_SUPPORT.getCode());
        return cuntaoOrgBO.addOrg(addCuntaoOrg, cuntaoCountyAddDto.getOperator());
	}

	private Long doCreateCuntaoCounty(CuntaoCountyAddDto cuntaoCountyAddDto, Long orgId) {
		Optional<CuntaoCountyWhitenameDto> optional = cuntaoCountyWhitenameBo.getCuntaoCountyWhitenameByCountyCode(cuntaoCountyAddDto.getCountyCode());
		CuntaoCounty record = BeanConvertUtils.convert(cuntaoCountyAddDto, orgId, optional.get());
		cuntaoCountyMapper.insert(record);
		cuntaoCountyWhitenameBo.updateCountyId(optional.get().getId(), record.getId());
		return record.getId();
	}
}
