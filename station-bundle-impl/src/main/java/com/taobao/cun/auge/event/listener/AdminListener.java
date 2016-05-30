package com.taobao.cun.auge.event.listener;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.ar.scene.station.param.PartnerLifecycleOnQuitCallbackParam;
import com.taobao.cun.ar.scene.station.service.PartnerLifecycleCallbackService;
import com.taobao.cun.ar.scene.station.service.StationLifecycleCallbackService;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("adminListener")
@EventSub(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT)
public class AdminListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(AdminListener.class);

	@Autowired
	private PartnerLifecycleCallbackService partnerLifecycleCallbackService;

	@Autowired
	private StationLifecycleCallbackService stationLifecycleCallbackService;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerBO partnerBO;

	@Override
	public void onMessage(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();

		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();

		Long taobaoUserId = stateChangeEvent.getTaobaoUserId();
		PartnerInstanceTypeEnum partnerType = stateChangeEvent.getPartnerType();
		Long instanceId = stateChangeEvent.getPartnerInstanceId();
		Long stationId = stateChangeEvent.getStationId();

		//已停业
		if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
			addQuitRelation(partnerType, taobaoUserId, stationId, instanceId);
		}
	}

	/**
	 * 添加服务记录及关系
	 * 
	 * @param context
	 * @param stationDetailDto
	 */
	private void addQuitRelation(PartnerInstanceTypeEnum partnerType, Long taobaoUserId, Long stationId,
			Long instanceId) {
		try {
			logger.info("addQuitRelation start,stationId=" + stationId);
			Date gmtEnd = DateUtil.getCurrentDate();

			PartnerLifecycleOnQuitCallbackParam onquitCallbackParam = new PartnerLifecycleOnQuitCallbackParam();
			onquitCallbackParam.setGmtEnd(gmtEnd);
			// 判断是否是淘帮手
			boolean isTPA = PartnerInstanceTypeEnum.TPA.equals(partnerType);
			onquitCallbackParam.setIsTpa(isTPA);

			onquitCallbackParam.setPartnerUserId(taobaoUserId);
			if (isTPA) {
				onquitCallbackParam.setPartnerUserId(findParentPartnerTaobaoUserId(instanceId));
			}

			onquitCallbackParam.setStationId(stationId);
			onquitCallbackParam.setUserId(taobaoUserId);
			partnerLifecycleCallbackService.onQuit(onquitCallbackParam);
			stationLifecycleCallbackService.onStop(stationId, gmtEnd);
			logger.info("addQuitRelation end,stationId=" + stationId);
		} catch (Throwable e) {
			logger.error("addRelation exception,stationId=" + stationId, e);
		}
	}

	/**
	 * 查询淘帮手的合伙人的taobaoUserId
	 * 
	 * @param instanceId
	 * @return
	 */
	private Long findParentPartnerTaobaoUserId(Long instanceId) {
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		Long parentStationId = instance.getParentStationId();
		PartnerStationRel parentInstance = partnerInstanceBO.findPartnerInstanceByStationId(parentStationId);
		Long partnerId = parentInstance.getPartnerId();
		Partner partner = partnerBO.getPartnerById(partnerId);

		return partner.getTaobaoUserId();
	}

}
