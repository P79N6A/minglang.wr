package com.taobao.cun.auge.configuration;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.taobao.tddl.client.sequence.SequenceDao;
import com.taobao.tddl.client.sequence.impl.DefaultSequenceDao;
import com.taobao.tddl.client.sequence.impl.GroupSequence;

@Configuration
public class TddlConfiguration {
	@Bean(initMethod="init")
    public SequenceDao groupSequenceDao(DataSource dataSource) {
		DefaultSequenceDao sequenceDao = new DefaultSequenceDao();
		sequenceDao.setDataSource(dataSource);
		sequenceDao.setTableName("cuntao_sequence");
        return sequenceDao;
    }
	
	@Bean
	public GroupSequence inventoryStoreCodeSequence(SequenceDao sequenceDao){
		GroupSequence sequence = new GroupSequence();
		sequence.setSequenceDao(sequenceDao);
		sequence.setName("inventory_store_code");
		return sequence;
	}
	
	@Bean
	public GroupSequence orgEndorOrgIdSequence(SequenceDao sequenceDao){
		GroupSequence sequence = new GroupSequence();
		sequence.setSequenceDao(sequenceDao);
		sequence.setName("store_endor_org_id");
		return sequence;
	}
}
