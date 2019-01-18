package com.taobao.cun.auge.configuration;

import com.taobao.tddl.client.sequence.impl.GroupSequence;
import com.taobao.tddl.client.sequence.impl.GroupSequenceDao;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SequenceConfiguration {

    @Bean(name = "stationNewCustomerSequence", initMethod = "init")
    public GroupSequence stationNewCustomerSequence(GroupSequenceDao groupSequenceDao) {
        GroupSequence groupSequence = new GroupSequence();
        groupSequence.setSequenceDao(groupSequenceDao);
        groupSequence.setName("station_new_customer");
        return groupSequence;
    }
}
