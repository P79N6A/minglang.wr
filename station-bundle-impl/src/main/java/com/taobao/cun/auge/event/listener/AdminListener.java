package com.taobao.cun.auge.event.listener;

import java.util.Date;

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
import com.taobao.cun.auge.event.ChangeTPEvent;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("adminListener")
@EventSub({EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, EventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT
        , EventConstant.CHANGE_TP_EVENT})
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
        } else if (event.getValue() instanceof ChangeTPEvent){
            processChangeTPEvent(event);
        }
    }

    /**
     * 处理变更合伙人事件
     * @param event
     */
    private void processChangeTPEvent(Event event){
        ChangeTPEvent changeTPEvent = (ChangeTPEvent) event.getValue();
        Long instanceId = changeTPEvent.getInstanceId();
        Long stationId = changeTPEvent.getStationId();
        Long taobaoUserId = getTaobaoUserIdByStationId(stationId);
        //解除旧合伙人关系,绑定新合伙人关系
        quitTPARelation(taobaoUserId, stationId, getTaobaoUserIdByStationId(changeTPEvent.getOldParentStationId()));
        addOpenRelation(PartnerInstanceTypeEnum.TPA, taobaoUserId, stationId, instanceId);
    }

	/**
	 * 处理类型变更事件
	 * @param event
	 */
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
			// 淘帮手升级为合伙人
		} else if (PartnerInstanceTypeChangeEnum.TPA_UPGRADE_2_TP.equals(typeChangeEvent.getTypeChangeEnum())) {
			// 淘帮手实例
			PartnerStationRel tpaInstance = partnerInstanceBO.findPartnerInstanceById(typeChangeEvent.getPartnerInstanceId());
			Long taobaoUserId = tpaInstance.getTaobaoUserId();
			Long stationId = tpaInstance.getStationId();
			Long parentStationId = tpaInstance.getParentStationId();
			// 合伙人实例
			PartnerStationRel tpInstance = partnerInstanceBO.findPartnerInstanceByStationId(parentStationId);
			Long parentTaobaoUserId = tpInstance.getTaobaoUserId();

			// 解除旧合伙人关系,绑定新合伙人关系
			quitTPARelation(taobaoUserId, stationId, parentTaobaoUserId);
			//撤销升级
		}else if (PartnerInstanceTypeChangeEnum.CANCEL_TPA_UPGRADE_2_TP.equals(typeChangeEvent.getTypeChangeEnum())) {
			// 淘帮手实例
			PartnerStationRel tpaInstance = partnerInstanceBO.findPartnerInstanceById(typeChangeEvent.getPartnerInstanceId());
			Long taobaoUserId = tpaInstance.getTaobaoUserId();
			Long stationId = tpaInstance.getStationId();

			// 建立和原来合伙人的关系
		    addOpenRelation(PartnerInstanceTypeEnum.TPA, taobaoUserId, stationId, tpaInstance.getId());
		}

		logger.info("Finished to handle event." + JSON.toJSONString(typeChangeEvent));
	}

    /**
     * 处理状态变更事件
     * @param event
     */
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
        
        //已停业回到服务中，淘帮手和 合伙人 都可以
        if ((PartnerInstanceStateChangeEnum.CLOSE_TO_SERVICE.equals(stateChangeEnum)
                && PartnerInstanceTypeEnum.TP.equals(partnerType))
                || (PartnerInstanceStateChangeEnum.CLOSE_TO_SERVICE.equals(stateChangeEnum)
                && PartnerInstanceTypeEnum.TPA.equals(partnerType))) {
            addOpenRelation(partnerType, taobaoUserId, stationId, instanceId);
        }

        logger.info("Finished to handle event." + JSON.toJSONString(stateChangeEvent));

    }

    private void addOpenRelation(PartnerInstanceTypeEnum partnerType, Long taobaoUserId, Long stationId,
                                 Long instanceId) {
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
    }

    /**
     * 构建合伙人关系参数
     *
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
     */
    private void addQuitRelation(PartnerInstanceTypeEnum partnerType, Long taobaoUserId, Long stationId,
                                 Long instanceId) {
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
    }

    /**
     * 解除旧合伙人关系,绑定新合伙人关系
     */
    private void quitTPARelation(Long taobaoUserId, Long stationId, Long parentTaobaoUserId) {
            logger.info("quitTPARelation start,stationId=" + stationId);
            Date gmtEnd = DateUtil.getCurrentDate();

            PartnerLifecycleOnQuitCallbackParam onquitCallbackParam = new PartnerLifecycleOnQuitCallbackParam();
            onquitCallbackParam.setGmtEnd(gmtEnd);
            onquitCallbackParam.setIsTpa(true);
            onquitCallbackParam.setPartnerUserId(parentTaobaoUserId);
            onquitCallbackParam.setStationId(stationId);
            onquitCallbackParam.setUserId(taobaoUserId);
            partnerLifecycleCallbackService.onQuit(onquitCallbackParam);
            stationLifecycleCallbackService.onStop(stationId, gmtEnd);
            logger.info("quitTPARelation end,stationId=" + stationId);
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
        return getTaobaoUserIdByStationId(parentStationId);
    }

    private Long getTaobaoUserIdByStationId(Long stationId) {
        PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
        Long partnerId = instance.getPartnerId();
        Partner partner = partnerBO.getPartnerById(partnerId);
        return partner.getTaobaoUserId();
    }

}
