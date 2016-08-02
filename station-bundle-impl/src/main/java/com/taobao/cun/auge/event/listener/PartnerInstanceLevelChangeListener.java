package com.taobao.cun.auge.event.listener;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerInstanceLevelChangeEvent;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by jingxiao.gjx on 2016/8/2.
 */
@Component("partnerInstanceLevelChangeListener")
@EventSub(EventConstant.PARTNER_INSTANCE_LEVEL_CHANGE_EVENT)
public class PartnerInstanceLevelChangeListener implements EventListener {


	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceLevelChangeListener.class);

	@Override
	public void onMessage(Event event) {
		PartnerInstanceLevelChangeEvent stateChangeEvent = (PartnerInstanceLevelChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(stateChangeEvent));
		// TODO 记录流转日志
	}
}
