package com.taobao.cun.auge.cuncounty.bo;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyGovProtocolDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovProtocolAddDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovProtocol;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovProtocolExample;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyGovProtocolMapper;

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
	/**
	 * 保存协议
	 * @param cuntaoCountyProtocolAddDto
	 */
	void save(CuntaoCountyGovProtocolAddDto cuntaoCountyGovProtocolAddDto) {
		Optional<CuntaoCountyGovProtocolDto> optional = getValidProtocol(cuntaoCountyGovProtocolAddDto.getCountyId());
		if(optional.isPresent()) {
			CuntaoCountyGovProtocolAddDto old = BeanConvertUtils.convert(CuntaoCountyGovProtocolAddDto.class, optional.get());
			if(old.isContentSame(cuntaoCountyGovProtocolAddDto)) {
				return;
			}
		}
		cuntaoCountyGovProtocolMapper.insert(BeanConvertUtils.convert(cuntaoCountyGovProtocolAddDto));
	}
	
	Optional<CuntaoCountyGovProtocolDto> getValidProtocol(Long countyId) {
		CuntaoCountyGovProtocolExample example = new CuntaoCountyGovProtocolExample();
		example.createCriteria().andCountyIdEqualTo(countyId).andIsDeletedEqualTo("n").andStateEqualTo("valid");
		List<CuntaoCountyGovProtocol> result = cuntaoCountyGovProtocolMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(result)) {
			return Optional.empty();
		}else {
			return Optional.of(BeanConvertUtils.convert(CuntaoCountyGovProtocolDto.class, result.get(0)));
		}
	}
}
