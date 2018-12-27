package com.taobao.cun.auge.log.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.dal.domain.AppBizLog;
import com.taobao.cun.auge.dal.domain.AppBizLogExample;
import com.taobao.cun.auge.dal.domain.AppBizLogExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AppBizLogMapper;
import com.taobao.cun.auge.log.ExtAppBizLog;
import com.taobao.cun.auge.log.SimpleAppBizLog;
import com.taobao.cun.auge.log.bo.AppBizLogBo;
import com.taobao.cun.common.util.BeanCopy;
import com.taobao.cun.crius.oss.client.FileStoreService;

/**
 * 记录应用日志
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AppBizLogBoImpl implements AppBizLogBo {
	@Resource
	private AppBizLogMapper appBizLogMapper;
	@Resource
	private FileStoreService fileStoreService;
	
	@Override
	public void addLog(SimpleAppBizLog simpleAppBizLog) {
		AppBizLog appBizLog = BeanCopy.copy(AppBizLog.class, simpleAppBizLog);
		appBizLog.setGmtCreate(new Date());
		appBizLogMapper.insert(appBizLog);
	}

	@Override
	public void addLog(ExtAppBizLog extAppBizLog) {
		SimpleAppBizLog simpleAppBizLog = BeanCopy.copy(SimpleAppBizLog.class, extAppBizLog);
		String fileName = "app_biz_log_" + System.currentTimeMillis() + ".json";
		fileStoreService.saveFile(fileName, fileName, JSON.toJSONString(extAppBizLog).getBytes(),"application/json");
		simpleAppBizLog.setMessage("http://crius.cn-hangzhou.oss-cdn.aliyun-inc.com/" + fileName);
		addLog(simpleAppBizLog);
	}

	@Override
	public List<SimpleAppBizLog> queryLog(SimpleAppBizLog simpleAppBizLog) {
		AppBizLogExample appBizLogExample = new AppBizLogExample();
		Criteria criteria = appBizLogExample.createCriteria();
		if(simpleAppBizLog.getBizKey() != null) {
			criteria.andBizKeyEqualTo(simpleAppBizLog.getBizKey());
		}
		if(simpleAppBizLog.getBizType() != null) {
			criteria.andBizTypeEqualTo(simpleAppBizLog.getBizType());
		}
		if(simpleAppBizLog.getCreator() != null) {
			criteria.andCreatorEqualTo(simpleAppBizLog.getCreator());
		}
		if(simpleAppBizLog.getState() != null) {
			criteria.andStateEqualTo(simpleAppBizLog.getState());
		}
		
		List<AppBizLog> appBizLogList = appBizLogMapper.selectByExample(appBizLogExample);
		List<SimpleAppBizLog> simpleAppBizLogList = new ArrayList<SimpleAppBizLog>();
		for(AppBizLog appBizLog : appBizLogList) {
			simpleAppBizLogList.add(BeanCopy.copy(SimpleAppBizLog.class, appBizLog));
		}
		
		return simpleAppBizLogList; 
	}

	@Override
	public void deleteLog(SimpleAppBizLog simpleAppBizLog) {
		AppBizLogExample appBizLogExample = new AppBizLogExample();
		Criteria criteria = appBizLogExample.createCriteria();
		if(simpleAppBizLog.getBizKey() != null) {
			criteria.andBizKeyEqualTo(simpleAppBizLog.getBizKey());
		}
		if(simpleAppBizLog.getBizType() != null) {
			criteria.andBizTypeEqualTo(simpleAppBizLog.getBizType());
		}
		if(simpleAppBizLog.getCreator() != null) {
			criteria.andCreatorEqualTo(simpleAppBizLog.getCreator());
		}
		if(simpleAppBizLog.getState() != null) {
			criteria.andStateEqualTo(simpleAppBizLog.getState());
		}
		
		appBizLogMapper.deleteByExample(appBizLogExample);		
	}

	@Override
	public void updateLog(SimpleAppBizLog updateAppBizLog,SimpleAppBizLog whereAppBizLog) {
		
		AppBizLog appBizLog = BeanCopy.copy(AppBizLog.class, updateAppBizLog);
		
		
		AppBizLogExample appBizLogExample = new AppBizLogExample();
		Criteria criteria = appBizLogExample.createCriteria();
		if(whereAppBizLog.getBizKey() != null) {
			criteria.andBizKeyEqualTo(whereAppBizLog.getBizKey());
		}
		if(whereAppBizLog.getBizType() != null) {
			criteria.andBizTypeEqualTo(whereAppBizLog.getBizType());
		}
		if(whereAppBizLog.getCreator() != null) {
			criteria.andCreatorEqualTo(whereAppBizLog.getCreator());
		}
		if(whereAppBizLog.getState() != null) {
			criteria.andStateEqualTo(whereAppBizLog.getState());
		}
		
		appBizLogMapper.updateByExampleSelective(appBizLog, appBizLogExample);	
	}
	

}
