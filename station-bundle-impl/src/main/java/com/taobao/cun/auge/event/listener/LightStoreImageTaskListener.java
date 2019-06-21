package com.taobao.cun.auge.event.listener;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.client.operator.DefaultOperatorEnum;
import com.taobao.cun.auge.store.bo.StoreWriteV2BO;
import com.taobao.cun.auge.store.service.StoreReadService;
import com.taobao.cun.auge.task.dto.TaskInstanceEventDto;
import com.taobao.cun.auge.task.enums.TaskInstanceEventEnum;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * 轻店图片审批任务 通过监听
 */
@Component("lightStoreImageTaskListener")
@EventSub({"CUN_TASK_INSTANCE_EVENT"})
public class LightStoreImageTaskListener implements EventListener{

    private static final Logger logger = LoggerFactory.getLogger(LightStoreImageTaskListener.class);
    private static final String BUSI_TYPE_CODE="LIGHT_STORE";

    @Autowired
    private StoreReadService storeReadService;

    @Autowired
    private StoreWriteV2BO storeWriteV2BO;

    @Override
    public void onMessage(Event event) {
        logger.info("receive event.event=" + JSON.toJSONString(event.getValue()));

        TaskInstanceEventDto eventDto = JSON.parseObject((String) event.getValue(), TaskInstanceEventDto.class);
        TaskInstanceEventEnum eventType = eventDto.getEventType();
        String userId = eventDto.getUserId();
        String userType = eventDto.getUserType();
        Long taskInstanceId = eventDto.getTaskInstanceId();
        String busiTypeCode = eventDto.getBusiTypeCode();

        if (!DefaultOperatorEnum.HAVANA.getCode().equals(userType)) {
            logger.warn("lightStoreTask only support havana user type");
            return;
        }

        if (TaskInstanceEventEnum.COMPLETED.equals(eventType) && BUSI_TYPE_CODE.equals(busiTypeCode)) {
            List<String> imageList = storeReadService.getSubImageFromTask(taskInstanceId);
            storeWriteV2BO.initLightStore(Long.parseLong(userId),taskInstanceId);
        }
    }
}
