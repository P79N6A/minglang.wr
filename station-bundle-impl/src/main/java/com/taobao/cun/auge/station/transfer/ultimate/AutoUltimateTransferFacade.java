package com.taobao.cun.auge.station.transfer.ultimate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.log.ExtAppBizLog;
import com.taobao.cun.auge.log.LogContent;
import com.taobao.cun.auge.log.bo.AppBizLogBo;
import com.taobao.cun.auge.station.transfer.CountyStationTransferBo;

/**
 * N + 75自动交接，从biz_action_log中找出县点交接时间在N+75后一天的县点
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AutoUltimateTransferFacade {
	@Resource
	private BaseUltimateTransferBo autoUltimateTransferBo;
	
	@Resource
	private AppBizLogBo appBizLogBo;
	
	@Resource
	private CountyStationTransferBo countyStationTransferBo;
	
	@Value("${auto.transfer.day}")
	private int autoTransferDay;
	
	public void transfer() {
		Date date = DateUtils.addDays(DateUtils.truncate(new Date(), Calendar.DATE), -1 * autoTransferDay);
		
		List<Long> countyIds = countyStationTransferBo.getAutoTransferableCountyIds(date);
		
		for(Long countyId : countyIds) {
			try {
				autoUltimateTransferBo.transfer(countyId, "auto", 0L);
			}catch(Throwable t) {
				ExtAppBizLog extAppBizLog = new ExtAppBizLog();
				extAppBizLog.setBizType("county_auto_trans");
				extAppBizLog.setBizKey(countyId);
				extAppBizLog.setCreator("system");
				extAppBizLog.setState("ERROR");
				LogContent logContent = new LogContent();
				logContent.setException(t);
				extAppBizLog.setLogContents(Lists.newArrayList(logContent));
				appBizLogBo.addLog(extAppBizLog);
			}
		}
	}
}
