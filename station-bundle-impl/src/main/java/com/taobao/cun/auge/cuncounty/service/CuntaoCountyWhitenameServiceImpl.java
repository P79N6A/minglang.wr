package com.taobao.cun.auge.cuncounty.service;

import java.util.List;

import javax.annotation.Resource;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyWhitenameBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyWhitenameAddDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 开县白名单
 * 
 * @author chengyu.zhoucy
 *
 */
@HSFProvider(serviceInterface = CuntaoCountyWhitenameService.class)
public class CuntaoCountyWhitenameServiceImpl implements CuntaoCountyWhitenameService {
	@Resource
	private CuntaoCountyWhitenameBo cuntaoCountyWhitenameBo;
	
	@Override
	public List<CuntaoCountyWhitenameDto> getCuntaoCountyWhitenames() {
		return cuntaoCountyWhitenameBo.getCuntaoCountyWhitenames();
	}

	@Override
	public void addCuntaoCountyWhitename(CuntaoCountyWhitenameAddDto cuntaoCountyWhitenameAddDto) {
		cuntaoCountyWhitenameBo.addCuntaoCountyWhitename(cuntaoCountyWhitenameAddDto);
	}

	@Override
	public void delete(Long id, String operator) {
		cuntaoCountyWhitenameBo.delete(id, operator);
	}

	@Override
	public void toggle(Long id, String operator) {
		cuntaoCountyWhitenameBo.toggle(id, operator);
	}

	@Override
	public PageDto<CuntaoCountyWhitenameDto> query(CuntaoCountyWhitenameCondition condition) {
		return cuntaoCountyWhitenameBo.query(condition);
	}

	@Override
	public List<CuntaoCountyWhitenameDto> getCuntaoCountyWhitenamesByCodes(List<String> codes) {
		return cuntaoCountyWhitenameBo.getCuntaoCountyWhitenamesByCodes(codes);
	}

}
