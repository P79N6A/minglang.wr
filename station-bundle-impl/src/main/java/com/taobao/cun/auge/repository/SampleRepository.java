package com.taobao.cun.auge.repository;

import org.springframework.stereotype.Repository;

import com.taobao.cun.auge.dal.domain.Sample;

@Repository
public class SampleRepository {

	public Long save(Sample sample){
		sample.setId(1l);
		System.out.println("save repository");
		return sample.getId();
	}
}
