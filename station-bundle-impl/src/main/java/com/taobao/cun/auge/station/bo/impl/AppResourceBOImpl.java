package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.AppResourceExample;
import com.taobao.cun.auge.dal.domain.AppResourceExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AppResourceMapper;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("appResourceBO")
public class AppResourceBOImpl implements AppResourceBO {

	@Autowired
	AppResourceMapper appResourceMapper;

	@Override
	public List<AppResource> queryAppResourceList(String type) {
		ValidateUtils.notNull(type);
		AppResourceExample example = new AppResourceExample();
		Criteria criteria = example.createCriteria();
		criteria.andTypeEqualTo(type);
		criteria.andIsDeletedEqualTo("n");
		return appResourceMapper.selectByExample(example);
	}

	@Override
	public AppResource queryAppResource(String type, String key) {
		ValidateUtils.notNull(type);
		ValidateUtils.notNull(key);
		AppResourceExample example = new AppResourceExample();
		Criteria criteria = example.createCriteria();
		criteria.andTypeEqualTo(type);
		criteria.andIsDeletedEqualTo("n");
		criteria.andNameEqualTo(key);
		return ResultUtils.selectOne(appResourceMapper.selectByExample(example));
	}

	@Override
	public String queryAppResourceValue(String type, String key) {
		AppResource app = this.queryAppResource(type, key);
		if (app != null) {
			return app.getValue();
		}
		return "";
	}

	@Override
	public String queryAppValueNotAllowNull(String type, String key) {
		String value=queryAppResourceValue(type,key);
		if(StringUtils.isEmpty(value)){
			throw new AugeServiceException("query resource null:type:"+type+",key:"+key);
		}else{
			return value;
		}
	}

}
