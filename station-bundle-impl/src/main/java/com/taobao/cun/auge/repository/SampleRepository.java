package com.taobao.cun.auge.repository;

import com.taobao.cun.auge.dal.domain.Sample;
import org.springframework.stereotype.Repository;

@Repository
public class SampleRepository {

	public Long save(Sample sample){
		sample.setId(1L);
		System.out.println("save repository");
		return sample.getId();
	}
}
