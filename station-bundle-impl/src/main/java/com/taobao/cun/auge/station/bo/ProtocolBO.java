package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.enums.ProtocolTargetBizTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

public interface ProtocolBO {

	/**
	 * 签署协议，协议的签署时间、生效时间为当前时间
	 * 
	 * @param taobaoUserId
	 *            签署人淘宝id
	 * @param type
	 *            协议类型
	 * @param businessId
	 *            业务主键id
	 * @param bizType
	 *            业务类型
	 */
	public void signProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId,
			ProtocolTargetBizTypeEnum bizType);

	/**
	 * 解除已经签署的协议。例如合伙人退出协议，小二不同意后，重新进入服务中，需要删除已签署协议。
	 * 
	 * @param taobaoUserId
	 * @param type
	 * @param operator
	 *            操作人，小二工号
	 */
	public void cancelProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId,
			ProtocolTargetBizTypeEnum bizType, String operator);

}
