package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExt;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExtExample;

public interface PartnerProtocolRelExtMapper {
	
	List<PartnerProtocolRelExt> selectPartnerProtocolsByExample(PartnerProtocolRelExtExample example);
}
