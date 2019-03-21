package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyMapper;

/**
 * 县服务中心
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyBo {
	@Resource
	private CuntaoCountyMapper cuntaoCountyMapper;
	
	CuntaoCountyDto getCuntaoCounty(Long id) {
		return BeanConvertUtils.convert(CuntaoCountyDto.class, cuntaoCountyMapper.selectByPrimaryKey(id));
	}
}
