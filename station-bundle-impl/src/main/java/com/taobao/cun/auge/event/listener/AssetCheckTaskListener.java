package com.taobao.cun.auge.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.asset.bo.AssetCheckTaskBO;
import com.taobao.cun.auge.asset.enums.AssetCheckTaskTaskTypeEnum;
import com.taobao.cun.auge.task.dto.TaskInteractionDto;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("assetCheckTaskListener")
@EventSub({"CUN_TASK_INTERACTION_INIT_V2"})
public class AssetCheckTaskListener implements EventListener {
	
	@Autowired
	private AssetCheckTaskBO assetCheckTaskBO;
	
	@Override
	public void onMessage(Event event) {
		if (event.getValue() instanceof String) {
			String event1 =  (String)event.getValue();
			TaskInteractionDto o = JSONObject.parseObject(event1,TaskInteractionDto.class);
			if("TP_ASSET_CHECK".equals(o.getBusiType())) {
				assetCheckTaskBO.initTaskForStation(AssetCheckTaskTaskTypeEnum.STATION_CHECK.getCode(), event1, Long.parseLong(o.getUserId()));
			}
		} 
	}
}
