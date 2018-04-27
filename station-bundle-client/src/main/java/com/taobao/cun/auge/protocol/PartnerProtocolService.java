package com.taobao.cun.auge.protocol;

import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

public interface PartnerProtocolService {

	/**
	 * 确认合伙人协议
	 * @param taobaoUserId
	 * @param protocol
	 * @return
	 */
	Result<Boolean> confirmPartnerProtocol(Long taobaoUserId,ProtocolTypeEnum protocol);
	
	
	/**
	 * 是否确人合伙人协议
	 * @param taobaoUserId
	 * @param protocol
	 * @return
	 */
	Result<Boolean> isConfirmPartnerProtocol(Long taobaoUserId,ProtocolTypeEnum protocol);
	
}
