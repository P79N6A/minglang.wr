package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.dal.domain.Protocol;
import com.taobao.cun.auge.dal.domain.ProtocolExample;
import com.taobao.cun.auge.dal.domain.ProtocolExample.Criteria;
import com.taobao.cun.auge.dal.mapper.ProtocolMapper;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.enums.ProtocolStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

@Service("protocolBO")
public class ProtocolBOImpl implements ProtocolBO {

	@Autowired
	ProtocolMapper protocolMapper;


	/**
	 * 根据协议类型，查询协议id
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public Long findProtocolId(ProtocolTypeEnum type) {
		ProtocolExample example = new ProtocolExample();
		
		Criteria criteria = example.createCriteria();
		
		criteria.andIsDeletedEqualTo("n");
		criteria.andTypeEqualTo(type.getCode());
		criteria.andStateEqualTo(ProtocolStateEnum.VALID.getCode());

		List<Protocol> protocols = protocolMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(protocols)){
			return null;
		}
		return protocols.get(0).getId();
	}
}
