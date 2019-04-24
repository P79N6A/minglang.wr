package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.cuncounty.dto.CainiaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CainiaoCountyEditDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyAddDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyUpdateDto;
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
	@Resource
	private CuntaoCountyGovContactBo cuntaoCountyGovContactBo;
	@Resource
	private CuntaoCountyGovProtocolBo cuntaoCountyGovProtocolBo;
	@Resource
	private CuntaoCountyGovContractBo cuntaoCountyGovContractBo;
	@Resource
	private CuntaoCountyOfficeBo cuntaoCountyOfficeBo;
	@Resource
	private CainiaoCountyBo cainiaoCountyBo;
	@Resource
	private CuntaoOrgAdminAddressBo cuntaoOrgAdminAddressBo;
	@Resource
	private CountyActionLogBo countyActionLogBo;
	
	@Transactional(rollbackFor=Throwable.class)
	public Long createCuntaoCounty(CuntaoCountyAddDto cuntaoCountyAddDto) {
		cuntaoCountyPredicate.checkCreateCounty(cuntaoCountyAddDto.getCountyCode());
		//创建组织
		Long orgId = createCuntaoOrg(cuntaoCountyAddDto);
		//创建县服务中心
		CuntaoCounty cuntaoCounty = doCreateCuntaoCounty(cuntaoCountyAddDto, orgId);
		//创建菜鸟仓
		createCainiaoCounty(cuntaoCounty, cuntaoCountyAddDto.getOperator());
		//更新白名单
		cuntaoCountyWhitenameBo.updateCountyId(cuntaoCounty.getCountyCode(), cuntaoCounty.getId());
		//报名分发地址
		CuntaoCountyDto cuntaoCountyDto = BeanConvertUtils.convert(CuntaoCountyDto.class, cuntaoCounty);
		cuntaoOrgAdminAddressBo.create(cuntaoCountyDto, cuntaoCountyAddDto.getOperator());
		//激活分发
		cuntaoOrgAdminAddressBo.activeRefusedPartner(cuntaoCountyDto, cuntaoCountyAddDto.getOperator());
		//记录创建日志
		countyActionLogBo.addCreateLog(cuntaoCounty.getId(), cuntaoCounty.getCreator());
		return cuntaoCounty.getId();
	}
	
	@Transactional(rollbackFor=Throwable.class)
	public void updateCuntaoCounty(CuntaoCountyUpdateDto cuntaoCountyUpdateDto) {
		cuntaoCountyPredicate.checkUpdateCounty(cuntaoCountyUpdateDto);
		
		//添加联系人
		cuntaoCountyGovContactBo.batchSave(cuntaoCountyUpdateDto.getCuntaoCountyGovContactAddDtos());
		
		//添加政府签约信息
		cuntaoCountyGovContractBo.save(cuntaoCountyUpdateDto.getCuntaoCountyGovContractEditDto());
		
		//添加办公场地信息
		cuntaoCountyOfficeBo.save(cuntaoCountyUpdateDto.getCuntaoCountyOfficeEditDto());
		
		//添加菜鸟县仓信息
		cainiaoCountyBo.save(cuntaoCountyUpdateDto.getCainiaoCountyEditDto());
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

	private CuntaoCounty doCreateCuntaoCounty(CuntaoCountyAddDto cuntaoCountyAddDto, Long orgId) {
		CuntaoCountyWhitenameDto cuntaoCountyWhitenameDto = cuntaoCountyWhitenameBo.getCuntaoCountyWhitenameByCountyCode(cuntaoCountyAddDto.getCountyCode());
		CuntaoCounty cuntaoCounty = BeanConvertUtils.convert(cuntaoCountyAddDto, orgId, cuntaoCountyWhitenameDto);
		cuntaoCountyMapper.insert(cuntaoCounty);
		return cuntaoCounty;
	}
	
	/**
	 * 创建一个菜鸟县仓记录，非政府仓，也不会做同步到菜鸟的操作
	 */
	private void createCainiaoCounty(CuntaoCounty cuntaoCounty, String operator) {
		CainiaoCountyEditDto cainiaoCountyEditDto = BeanConvertUtils.convert(CainiaoCountyEditDto.class, cuntaoCounty);
		cainiaoCountyEditDto.setAddress(cuntaoCounty.getName());
		cainiaoCountyEditDto.setStoreType(CainiaoCountyDto.STORE_TYPE_CAINIAO);;
		cainiaoCountyEditDto.setOperator(operator);
		cainiaoCountyEditDto.setCountyId(cuntaoCounty.getId());
		cainiaoCountyBo.save(cainiaoCountyEditDto);
	}
}