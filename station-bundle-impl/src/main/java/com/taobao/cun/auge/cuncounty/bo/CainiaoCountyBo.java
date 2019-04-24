package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CainiaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.cuncounty.dto.edit.CainiaoCountyEditDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CainiaoCounty;
import com.taobao.cun.auge.dal.mapper.CainiaoCountyMapper;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;

/**
 * 菜鸟县仓
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CainiaoCountyBo {
	@Resource
	private CuntaoCountyExtMapper cuntaoCountyExtMapper;
	@Resource
	private CainiaoCountyMapper cainiaoCountyMapper;
	@Resource
	private CuntaoCountyBo cuntaoCountyBo;
	@Resource
	private CainiaoCountyRemoteBo cainiaoCountyRemoteBo;
	
	void save(CainiaoCountyEditDto cainiaoCountyEditDto) {
		//如果没有菜鸟县仓信息则直接返回
		if(cainiaoCountyEditDto == null) {
			return;
		}
		CainiaoCounty cainiaoCounty = cuntaoCountyExtMapper.getCainiaoCounty(cainiaoCountyEditDto.getCountyId());
		CuntaoCountyDto cuntaoCountyDto = cuntaoCountyBo.getCuntaoCounty(cainiaoCountyEditDto.getCountyId());
		if(cainiaoCounty == null) {
			insert(cainiaoCountyEditDto, cuntaoCountyDto);
		}else {
			update(cainiaoCounty, cainiaoCountyEditDto, cuntaoCountyDto);
		}
	}

	private void update(CainiaoCounty cainiaoCounty, CainiaoCountyEditDto cainiaoCountyEditDto, CuntaoCountyDto cuntaoCountyDto) {
		CainiaoCounty newCainiaoCounty = BeanConvertUtils.convert(cainiaoCountyEditDto);
		newCainiaoCounty.setCreator(cainiaoCounty.getCreator());
		newCainiaoCounty.setGmtCreate(cainiaoCounty.getGmtCreate());
		newCainiaoCounty.setId(cainiaoCounty.getId());
		newCainiaoCounty.setCainiaoCountyId(cainiaoCounty.getCainiaoCountyId());
		cainiaoCountyMapper.updateByPrimaryKeySelective(newCainiaoCounty);
		//待开业后,如果地址发生变化，需要更新菜鸟县仓（目前只是发送邮件通知）
		if(isSyncCainiaoCountyState(cuntaoCountyDto) && isCainiaoAddressChanged(cainiaoCounty, cainiaoCountyEditDto)) {
			cainiaoCountyRemoteBo.updateCainiaoCounty(
					cuntaoCountyDto,
					BeanConvertUtils.convert(CainiaoCountyDto.class, cainiaoCounty), 
					BeanConvertUtils.convert(CainiaoCountyDto.class, newCainiaoCounty));
		}
	}

	private void insert(CainiaoCountyEditDto cainiaoCountyEditDto, CuntaoCountyDto cuntaoCountyDto) {
		CainiaoCounty cainiaoCounty = BeanConvertUtils.convert(cainiaoCountyEditDto);
		cainiaoCountyMapper.insert(cainiaoCounty);
		
		//在待开业之前，需要审批过了之后才能同步到菜鸟，否则在插入时就同步到菜鸟
		if(isSyncCainiaoCountyState(cuntaoCountyDto)) {
			cuntaoCountyExtMapper.updateCainiaoCountyId(cuntaoCountyDto.getId(), cainiaoCountyRemoteBo.createCainiaoCounty(cuntaoCountyDto.getId()));
		}
	}
	
	CainiaoCountyDto getCainiaoCountyDto(Long countyId) {
		CainiaoCountyDto cainiaoCountyDto = BeanConvertUtils.convert(CainiaoCountyDto.class, cuntaoCountyExtMapper.getCainiaoCounty(countyId));
		if(cainiaoCountyDto != null) {
			cainiaoCountyDto.setAttachmentVOList(BeanConvertUtils.convertAttachmentVO(cainiaoCountyDto.getAttachments()));
		}
		return cainiaoCountyDto;
	}
	
	private boolean isSyncCainiaoCountyState(CuntaoCountyDto cuntaoCountyDto) {
		return cuntaoCountyDto.getState().getCode().equals(CuntaoCountyStateEnum.WAIT_OPEN.getCode()) 
				|| cuntaoCountyDto.getState().getCode().equals(CuntaoCountyStateEnum.OPENING.getCode());
	}
	
	/**
	 * 菜鸟县仓地址是否发生变化
	 * @param oldCainiaoCountyDto
	 * @param newCainiaoCountyDto
	 * @return
	 */
	private boolean isCainiaoAddressChanged(CainiaoCounty cainiaoCounty, CainiaoCountyEditDto cainiaoCountyEditDto) {
		return !new EqualsBuilder()
				.append(cainiaoCounty.getProvinceCode(), cainiaoCountyEditDto.getProvinceCode())
				.append(cainiaoCounty.getCityCode(), cainiaoCountyEditDto.getCityCode())
				.append(cainiaoCounty.getCountyCode(), cainiaoCountyEditDto.getCountyCode())
				.append(cainiaoCounty.getTownCode(), cainiaoCountyEditDto.getTownCode())
				.append(cainiaoCounty.getAddress(), cainiaoCountyEditDto.getAddress())
				.isEquals();
	}
	
	void updateCainiaoCountyStoreType(Long id, String storeType, String operator) {
		cuntaoCountyExtMapper.updateCainiaoCountyStoreType(id, storeType, operator);
	}
	
	
}
