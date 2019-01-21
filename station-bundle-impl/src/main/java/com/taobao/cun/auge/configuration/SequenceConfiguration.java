package com.taobao.cun.auge.configuration;

import com.taobao.tddl.client.sequence.impl.GroupSequence;
import com.taobao.tddl.client.sequence.impl.GroupSequenceDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SequenceConfiguration {

    @Bean(name = "sequenceDao", initMethod = "init")
    public GroupSequenceDao sequenceDao(@Value("${spring.tddl.app}") String appName, @Value("${spring.tddl.group}") String groupKey) {
        GroupSequenceDao dao = new GroupSequenceDao();
        dao.setAppName(appName);
        dao.setDbGroupKeys(Collections.singletonList(groupKey));
        dao.setInnerStep(1);
        //单库
        dao.setDscount(1);
        dao.setTableName("cuntao_sequence");
        return dao;
    }

    @Bean(name = "stationNewCustomerSequence", initMethod = "init")
    public GroupSequence stationNewCustomerSequence(GroupSequenceDao groupSequenceDao) {
        GroupSequence groupSequence = new GroupSequence();
        groupSequence.setSequenceDao(groupSequenceDao);
        groupSequence.setName("station_new_customer");
        return groupSequence;
    }
}
