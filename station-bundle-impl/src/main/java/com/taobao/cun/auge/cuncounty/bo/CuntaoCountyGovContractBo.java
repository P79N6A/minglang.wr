package com.taobao.cun.auge.cuncounty.bo;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyGovContractDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovContractEditDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovProtocolAddDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyGovContractMapper;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;

/**
 * 政府签约信息
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyGovContractBo {
	@Resource
	private CuntaoCountyGovProtocolBo cuntaoCountyGovProtocolBo;
	@Resource
	private CuntaoCountyGovContractMapper cuntaoCountyGovContractMapper;
	@Resource
	private CuntaoCountyExtMapper cuntaoCountyExtMapper;
	
	@Transactional(rollbackFor=Throwable.class)
	public void save(CuntaoCountyGovContractEditDto cuntaoCountyGovContractEditDto) {
		CuntaoCountyGovProtocolAddDto cuntaoCountyGovProtocolAddDto = BeanConvertUtils.convert(CuntaoCountyGovProtocolAddDto.class, cuntaoCountyGovContractEditDto);
		cuntaoCountyGovProtocolBo.save(cuntaoCountyGovProtocolAddDto);
		cuntaoCountyGovContractMapper.insert(BeanConvertUtils.convert(cuntaoCountyGovContractEditDto));
	}
	
	Optional<CuntaoCountyGovContractDto> getCuntaoCountyGovContract(Long countyId){
		return Optional.ofNullable(BeanConvertUtils.convert(CuntaoCountyGovContractDto.class,cuntaoCountyExtMapper.getCuntaoCountyGovContract(countyId)));
	}
}
