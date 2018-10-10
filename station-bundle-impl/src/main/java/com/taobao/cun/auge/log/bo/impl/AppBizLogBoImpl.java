package com.taobao.cun.auge.log.bo.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.dal.domain.AppBizLog;
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
}
