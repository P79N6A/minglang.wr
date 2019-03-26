package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CainiaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyOfficeDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovContactAddDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyUpdateDto;
import com.taobao.cun.auge.validator.BeanValidator;

@Component
public class CuntaoCountyPredicate {
	@Resource
	private CuntaoCountyWhitenameBo cuntaoCountyWhitenameBo;
	@Resource
	private CuntaoCountyOfficeBo cuntaoCountyOfficeBo;
	@Resource
	private CainiaoCountyBo cainiaoCountyBo;
	@Resource
	private CuntaoOrgAdminAddressBo cuntaoOrgAdminAddressBo;
	
	void checkCreateCounty(String countyCode) {
		CuntaoCountyWhitenameDto cuntaoCountyWhitenameDto = cuntaoCountyWhitenameBo.getCuntaoCountyWhitenameByCountyCode(countyCode);
		if(cuntaoCountyWhitenameDto != null) {
			if(cuntaoCountyWhitenameDto.getCountyId() > 0) {
				throw new RuntimeException(cuntaoCountyWhitenameDto.getCountyName() + "已经开了县点");
			}
		}else {
			throw new RuntimeException("该县不在白名单里，暂不能开县");
		}
		
		if(cuntaoOrgAdminAddressBo.isExistCountyCode(countyCode)) {
			throw new RuntimeException("分发地址已经存在：" + countyCode);
		}
	}

	public void checkUpdateCounty(CuntaoCountyUpdateDto cuntaoCountyUpdateDto) {
		//校验政府签约信息
		if(cuntaoCountyUpdateDto.getCuntaoCountyGovContractEditDto() == null) {
			throw new IllegalArgumentException("政府签约信息不能为空");
		}
		BeanValidator.validateWithThrowable(cuntaoCountyUpdateDto.getCuntaoCountyGovContractEditDto());
		
		//校验政府联系人
		if(CollectionUtils.isEmpty(cuntaoCountyUpdateDto.getCuntaoCountyGovContactAddDtos())){
			throw new IllegalArgumentException("政府联系人不能为空");
		}
		
		if(cuntaoCountyUpdateDto.getCuntaoCountyGovContactAddDtos().size() < 2){
			throw new IllegalArgumentException("至少要有两位政府联系人");
		}
		
		for(CuntaoCountyGovContactAddDto dto : cuntaoCountyUpdateDto.getCuntaoCountyGovContactAddDtos()) {
			BeanValidator.validateWithThrowable(dto);
		}
		
		//办公场地校验，如果之前已经有办公场地了，那么更新的时候也必须有办公场地，不允许为空
		CuntaoCountyOfficeDto cuntaoCountyOfficeDto = cuntaoCountyOfficeBo.getCuntaoCountyOffice(cuntaoCountyUpdateDto.getCountyId());
		if(cuntaoCountyOfficeDto != null) {
			if(cuntaoCountyUpdateDto.getCuntaoCountyOfficeEditDto() == null) {
				throw new IllegalArgumentException("办公场地信息不能为空");
			}
		}
		
		if(cuntaoCountyUpdateDto.getCuntaoCountyOfficeEditDto() != null) {
			BeanValidator.validateWithThrowable(cuntaoCountyUpdateDto.getCuntaoCountyOfficeEditDto());
		}
		
		//校验菜鸟县仓，如果之前已经有菜鸟县仓了，那么更新的时候也必须有菜鸟县仓，不允许为空
		CainiaoCountyDto cainiaoCountyDto = cainiaoCountyBo.getCainiaoCountyDto(cuntaoCountyUpdateDto.getCountyId());
		if(cainiaoCountyDto != null) {
			if(cuntaoCountyUpdateDto.getCainiaoCountyEditDto() == null) {
				throw new IllegalArgumentException("菜鸟县仓信息不能为空");
			}
		}
		if(cuntaoCountyUpdateDto.getCainiaoCountyEditDto() != null) {
			BeanValidator.validateWithThrowable(cuntaoCountyUpdateDto.getCainiaoCountyEditDto());
		}
	}
}
