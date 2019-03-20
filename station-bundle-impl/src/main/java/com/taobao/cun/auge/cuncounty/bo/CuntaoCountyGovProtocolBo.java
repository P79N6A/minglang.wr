package com.taobao.cun.auge.cuncounty.bo;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyGovProtocolDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovProtocolAddDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovProtocol;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyGovProtocolMapper;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;

/**
 * 县服务中心协议
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyGovProtocolBo {
	@Resource
	private CuntaoCountyGovProtocolMapper cuntaoCountyGovProtocolMapper;
	@Resource
	private CuntaoCountyExtMapper cuntaoCountyExtMapper;
	/**
	 * 保存协议
	 * @param cuntaoCountyProtocolAddDto
	 */
	void save(CuntaoCountyGovProtocolAddDto cuntaoCountyGovProtocolAddDto) {
		if(!isExists(cuntaoCountyGovProtocolAddDto)) {
			//先失效掉历史协议
			invalid(cuntaoCountyGovProtocolAddDto.getCountyId(), cuntaoCountyGovProtocolAddDto.getOperator());
			//添加新协议
			cuntaoCountyGovProtocolMapper.insert(BeanConvertUtils.convert(cuntaoCountyGovProtocolAddDto));
		}
	}

	private boolean isExists(CuntaoCountyGovProtocolAddDto cuntaoCountyGovProtocolAddDto) {
		CuntaoCountyGovProtocol cuntaoCountyGovProtocol = cuntaoCountyExtMapper.getCuntaoCountyGovProtocol(cuntaoCountyGovProtocolAddDto.getCountyId());
		if(cuntaoCountyGovProtocol != null) {
			CuntaoCountyGovProtocolAddDto old = BeanConvertUtils.convert(CuntaoCountyGovProtocolAddDto.class, cuntaoCountyGovProtocol);
			if(old.isContentSame(cuntaoCountyGovProtocolAddDto)) {
				return true;
			}
		}
		return false;
	}
	
	private void invalid(Long countyId, String operator) {
		cuntaoCountyExtMapper.invalidProtocols(countyId, operator);
	}

	Optional<CuntaoCountyGovProtocolDto> getValidProtocol(Long countyId) {
		return Optional.of(BeanConvertUtils.convert(CuntaoCountyGovProtocolDto.class, cuntaoCountyExtMapper.getCuntaoCountyGovProtocol(countyId)));
	}
}
