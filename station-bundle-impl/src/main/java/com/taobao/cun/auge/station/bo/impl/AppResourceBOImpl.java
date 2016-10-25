package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.AppResourceExample;
import com.taobao.cun.auge.dal.domain.AppResourceExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AppResourceMapper;
import com.taobao.cun.auge.station.bo.AppResourceBO;

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
    public boolean configAppResource(String type, String key, String value,  boolean isDelete, String configurePerson) {
        ValidateUtils.notNull(type);
        ValidateUtils.notNull(key);
        ValidateUtils.notNull(value);
        ValidateUtils.notNull(configurePerson);        
        AppResource existResource = queryAppResource(type, key);
        if(existResource!=null){
            AppResource newResource = toAppResource(type, key, value, configurePerson, isDelete);
            newResource.setId(existResource.getId());
            newResource.setGmtCreate(existResource.getGmtCreate());
            int count = appResourceMapper.updateByPrimaryKey(newResource);
            return count > 0;
        }else{
            AppResource newResource = toAppResource(type, key, value, configurePerson, isDelete);
            newResource.setCreator(configurePerson);
            newResource.setGmtCreate(new Date());
            int id = appResourceMapper.insert(newResource);
            return id > 0;
        }
    }

    private AppResource toAppResource(String type, String key, String value, String configurePerson, boolean isDeleted) {
        AppResource resource = new AppResource();
        resource.setGmtModified(new Date());
        resource.setValue(value);
        if(isDeleted){
            resource.setIsDeleted("y");
        }else{
            resource.setIsDeleted("n");
        }
        resource.setModifier(configurePerson);
        resource.setType(type);
        resource.setName(key);
        return resource;
    }

    @Override
    public Map<String, AppResource> queryAppResourceMap(String type) {
        List<AppResource> appResourceList = queryAppResourceList(type);
        if(CollectionUtils.isEmpty(appResourceList)){
            return Maps.newHashMap();
        }
        Map<String, AppResource> resourceMap = Maps.newHashMap();
        for(AppResource resource:appResourceList){
            if(resource!=null && resource.getName()!=null){
                resourceMap.put(resource.getName(), resource);
            }
        }
        return resourceMap;
    }
}
