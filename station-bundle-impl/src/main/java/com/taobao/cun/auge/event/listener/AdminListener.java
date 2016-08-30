package com.taobao.cun.auge.event.listener;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.ar.model.StationLocation;
import com.taobao.cun.ar.scene.station.param.PartnerLifecycleOnDegradeCallbackParam;
import com.taobao.cun.ar.scene.station.param.PartnerLifecycleOnEnterCallbackParam;
import com.taobao.cun.ar.scene.station.param.PartnerLifecycleOnQuitCallbackParam;
import com.taobao.cun.ar.scene.station.param.StationLifecycleOnStartCallbackParam;
import com.taobao.cun.ar.scene.station.service.PartnerLifecycleCallbackService;
import com.taobao.cun.ar.scene.station.service.StationLifecycleCallbackService;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceTypeChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("adminListener")
@EventSub({ EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, EventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT })
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

	@Autowired
	StationBO stationBO;

	@Override
	public void onMessage(Event event) {
		if (event.getValue() instanceof PartnerInstanceStateChangeEvent) {
			processStateChangeEvent(event);
		} else if (event.getValue() instanceof PartnerInstanceTypeChangeEvent) {
			processTypeChangeEvent(event);
		}

	}

	private void processTypeChangeEvent(Event event) {
		PartnerInstanceTypeChangeEvent typeChangeEvent = (PartnerInstanceTypeChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(typeChangeEvent));
		// 合伙人降级为淘帮手
		if (PartnerInstanceTypeChangeEnum.TP_DEGREE_2_TPA.equals(typeChangeEvent.getTypeChangeEnum())) {
			PartnerLifecycleOnDegradeCallbackParam param = new PartnerLifecycleOnDegradeCallbackParam();
			param.setGmtEnd(DateUtil.getCurrentDate());
			param.setPartnerUserId(typeChangeEvent.getParentTaobaoUserId());
			param.setUserId(typeChangeEvent.getTaobaoUserId());
			partnerLifecycleCallbackService.onDegrade(param);
		}

		logger.info("Finished to handle event." + JSON.toJSONString(typeChangeEvent));
	}

	private void processStateChangeEvent(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(stateChangeEvent));

		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();

		Long taobaoUserId = stateChangeEvent.getTaobaoUserId();
		PartnerInstanceTypeEnum partnerType = stateChangeEvent.getPartnerType();
		Long instanceId = stateChangeEvent.getPartnerInstanceId();
		Long stationId = stateChangeEvent.getStationId();

		// 村拍档，不处理
		if (PartnerInstanceTypeEnum.TPV.equals(partnerType)) {
			logger.info("tpv not handle.");
			return;
		}

		// 已停业
		if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
			addQuitRelation(partnerType, taobaoUserId, stationId, instanceId);
		}

		// 合伙人变成装修中，淘帮手进入服务中
		if ((PartnerInstanceStateChangeEnum.START_DECORATING.equals(stateChangeEnum)
				&& PartnerInstanceTypeEnum.TP.equals(partnerType))
				|| (PartnerInstanceStateChangeEnum.START_SERVICING.equals(stateChangeEnum)
						&& PartnerInstanceTypeEnum.TPA.equals(partnerType))) {
			addOpenRelation(partnerType, taobaoUserId, stationId, instanceId);
		}

		logger.info("Finished to handle event." + JSON.toJSONString(stateChangeEvent));

	}

	private void addOpenRelation(PartnerInstanceTypeEnum partnerType, Long taobaoUserId, Long stationId,
			Long instanceId) {
		try {
			// 这里增加合伙人关系写入
			logger.info("addOpenRelation start,instanceId=" + instanceId);
			// 村点服务关系建立
			StationLifecycleOnStartCallbackParam startCallbackParam = buildOnStartParam(stationId);
			stationLifecycleCallbackService.onStart(startCallbackParam);
			// 合伙人服务关系建立
			PartnerLifecycleOnEnterCallbackParam onEnterParam = buildOnEnterParam(partnerType, taobaoUserId, stationId,
					instanceId);
			partnerLifecycleCallbackService.onEnter(onEnterParam);
			logger.info("addOpenRelation start,instanceId=" + instanceId);
		} catch (Throwable e) {
			logger.error("addOpenRelation exception,instanceId=" + instanceId, e);
		}
	}

	/**
	 * 构建合伙人关系参数
	 * 
	 * @param onEnterParam
	 *            合伙人正式入驻服务的回调方法参数
	 * @param applyDetailDto
	 *            站点申请单信息
	 * @return
	 */
	private PartnerLifecycleOnEnterCallbackParam buildOnEnterParam(PartnerInstanceTypeEnum partnerType,
			Long taobaoUserId, Long stationId, Long instanceId) {
		PartnerLifecycleOnEnterCallbackParam onEnterParam = new PartnerLifecycleOnEnterCallbackParam();

		onEnterParam.setGmtStart(DateUtil.getCurrentDate());

		onEnterParam.setPartnerUserId(taobaoUserId);
		// 如果是淘帮手用户，则需要重新查询对应合伙人的数据
		boolean isTPA = PartnerInstanceTypeEnum.TPA.equals(partnerType);
		if (isTPA) {
			// 如果是淘帮手需要重置partnerUserId
			onEnterParam.setPartnerUserId(findParentPartnerTaobaoUserId(instanceId));
		}
		onEnterParam.setIsTpa(isTPA);
		onEnterParam.setStationId(stationId);
		onEnterParam.setUserId(taobaoUserId);

		return onEnterParam;
	}

	private StationLifecycleOnStartCallbackParam buildOnStartParam(Long stationId) {

		StationLifecycleOnStartCallbackParam startCallbackParam = new StationLifecycleOnStartCallbackParam();

		startCallbackParam.setGmtStart(DateUtil.getCurrentDate());
		// 目前都是独占的,后续如果改造的话,需要根据需求处理
		startCallbackParam.setIsExclusive(Boolean.FALSE);

		Station station = stationBO.getStationById(stationId);

		StationLocation location = new StationLocation();

		location.setCity(station.getCityDetail());
		location.setCityCode(station.getCity());
		location.setCounty(station.getCountyDetail());
		location.setCountyCode(station.getCounty());
		location.setProvince(station.getProvinceDetail());
		location.setProvinceCode(station.getProvince());
		location.setTown(station.getTownDetail());
		location.setTownCode(station.getTown());
		location.setVillage(station.getVillageDetail());
		location.setVillageCode(station.getVillage());

		startCallbackParam.setLocation(location);
		startCallbackParam.setStationId(stationId);

		return startCallbackParam;
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
