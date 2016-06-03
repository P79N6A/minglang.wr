package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Protocol;
import com.taobao.cun.auge.dal.domain.ProtocolExample;
import com.taobao.cun.auge.dal.domain.ProtocolExample.Criteria;
import com.taobao.cun.auge.dal.mapper.ProtocolMapper;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.enums.ProtocolGroupTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Service("protocolBO")
public class ProtocolBOImpl implements ProtocolBO {

	@Autowired
	ProtocolMapper protocolMapper;

	@Override
	public Long getValidProtocolId(ProtocolTypeEnum type) throws AugeServiceException {
		ValidateUtils.notNull(type);
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


	@Override
	public List<Long> getAllProtocolId(List<ProtocolTypeEnum> types)
			throws AugeServiceException {
		ValidateUtils.notEmpty(types);
		
		ProtocolExample example = new ProtocolExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andTypeIn(getTypeCode(types));
		List<Protocol> protocols = protocolMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(protocols)){
			return null;
		}
		
		return bulidIds(protocols);
	}
	
	private List<Long> bulidIds(List<Protocol> protocols) {
		List<Long> res = new ArrayList<Long>();
		for (Protocol protocol : protocols) {
			res.add(protocol.getId());
		}
		return res;
	}
	
	private List<String>  getTypeCode(List<ProtocolTypeEnum> types) {
		List<String> res = new ArrayList<String>();
		for (ProtocolTypeEnum typeEnums : types) {
			res.add(typeEnums.getCode());
		}
		return res;
	}


	@Override
	public List<ProtocolTypeEnum> getProtocolTypeByGroupType(
			ProtocolGroupTypeEnum groupTypeEnum) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}
}
