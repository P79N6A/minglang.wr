package com.taobao.cun.auge.fence.jingwei;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;
import com.alibaba.middleware.jingwei.client.custom.UpdateEvent;

import com.taobao.cun.auge.alilang.jingwei.JingweiTask;
import com.taobao.cun.auge.configuration.ProductCondition;
import com.taobao.cun.auge.fence.dto.job.StationUpdateFenceInstanceJob;
import com.taobao.cun.auge.fence.service.FenceInstanceJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 18/7/12.
 */
@Component
@Conditional(ProductCondition.class)
public class StationChangeJingweiTask extends JingweiTask {

    @Value("${jingwei.taskid.fence}")
    private String taskId;

    private Client client;

    @Value("#{'${jingwei.fence.fields}'.split(',')}")
    private List<String> fenceFields;

    private static final String PRIMARY_KEY = "id";

    private static final String MODIFIER = "modifier";

    @Autowired
    private FenceInstanceJobService jobService;

    @Override
    public void start() {
        client = ClientFactory.create(taskId);
        client.registerMessageListener(new SimpleMessageListener() {
            @Override
            public Result onReceiveMessage(List<EventMessage> messages) {
                for (EventMessage msg : messages) {
                    if (msg instanceof UpdateEvent) {
                        UpdateEvent event = (UpdateEvent) msg;
                        List<String> modifiedFieldNames = event.getModifiedFieldNames();
                        modifiedFieldNames.retainAll(fenceFields);
                        if (modifiedFieldNames.size() > 0) {
                            event.getRowDataMaps().forEach(i -> submitJob(i));
                        }
                    }
                }
                return Result.ACK_AND_NEXT;
            }
        });
        client.startTask();
    }

    public void submitJob(Map<String, Serializable> dataMap) {
        Long id = (Long) dataMap.get(PRIMARY_KEY);
        String modifier = (String) dataMap.get(MODIFIER);
        StationUpdateFenceInstanceJob job
            = new StationUpdateFenceInstanceJob();
        job.setStationId(id);
        job.setCreator(modifier);
        jobService.createJob(job);
    }

}
