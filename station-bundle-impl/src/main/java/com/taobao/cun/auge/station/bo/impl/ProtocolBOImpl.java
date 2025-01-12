package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.common.utils.BeanCopyUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Protocol;
import com.taobao.cun.auge.dal.domain.ProtocolExample;
import com.taobao.cun.auge.dal.domain.ProtocolExample.Criteria;
import com.taobao.cun.auge.dal.mapper.ProtocolMapper;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.dto.ProtocolDto;
import com.taobao.cun.auge.station.enums.ProtocolGroupTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("protocolBO")
public class ProtocolBOImpl implements ProtocolBO {

	
	@Autowired
	ProtocolMapper protocolMapper;

	@Override
	public ProtocolDto getValidProtocol(ProtocolTypeEnum type) {
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
		Protocol protocol = protocols.get(0);
		ProtocolDto dto =new ProtocolDto();
		BeanCopyUtils.copyNotNullProperties(protocol, dto);
		return dto;
	}


	@Override
	public List<Long> getAllProtocolId(List<ProtocolTypeEnum> types)
			{
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
			ProtocolGroupTypeEnum groupTypeEnum) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Protocol getProtocolById(Long id) {
		return protocolMapper.selectByPrimaryKey(id);
	}
}
