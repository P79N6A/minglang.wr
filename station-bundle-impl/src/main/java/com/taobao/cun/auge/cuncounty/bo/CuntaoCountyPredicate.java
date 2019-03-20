package com.taobao.cun.auge.cuncounty.bo;

import java.util.Optional;

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
	
	void checkCreateCounty(String countyCode) {
		Optional<CuntaoCountyWhitenameDto> optional = cuntaoCountyWhitenameBo.getCuntaoCountyWhitenameByCountyCode(countyCode);
		if(optional.isPresent()) {
			CuntaoCountyWhitenameDto cuntaoCountyWhitenameDto = optional.get();
			if(cuntaoCountyWhitenameDto.getCountyId() > 0) {
				throw new RuntimeException(cuntaoCountyWhitenameDto.getCountyName() + "已经开了县点");
			}
		}else {
			throw new RuntimeException("该县不在白名单里，暂不能开县");
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
		
		//办公场地校验，如果之前已经有办公场地了，那么更新的时候也必须有办公场地，不允许删除
		Optional<CuntaoCountyOfficeDto> ccoOptional = cuntaoCountyOfficeBo.getCuntaoCountyOffice(cuntaoCountyUpdateDto.getCountyId());
		if(ccoOptional.isPresent()) {
			if(cuntaoCountyUpdateDto.getCuntaoCountyOfficeEditDto() == null) {
				throw new IllegalArgumentException("不允许删除办公场地信息");
			}
		}
		
		if(cuntaoCountyUpdateDto.getCuntaoCountyOfficeEditDto() != null) {
			BeanValidator.validateWithThrowable(cuntaoCountyUpdateDto.getCuntaoCountyOfficeEditDto());
		}
		
		//校验菜鸟县仓
		Optional<CainiaoCountyDto> ccdOptional = cainiaoCountyBo.getCainiaoCountyDto(cuntaoCountyUpdateDto.getCountyId());
		if(ccdOptional.isPresent()) {
			if(cuntaoCountyUpdateDto.getCainiaoCountyEditDto() == null) {
				throw new IllegalArgumentException("不允许删除菜鸟县仓");
			}
		}
		if(cuntaoCountyUpdateDto.getCainiaoCountyEditDto() != null) {
			BeanValidator.validateWithThrowable(cuntaoCountyUpdateDto.getCainiaoCountyEditDto());
		}
	}
}
