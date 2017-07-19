package com.taobao.cun.auge.station.bo;

import java.util.Date;

import com.taobao.cun.auge.station.dto.PartnerProtocolRelDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

public interface PartnerProtocolRelBO {
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
	public void signProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long objectId,
			PartnerProtocolRelTargetTypeEnum targetType);

	/**
	 * 解除已经签署的协议。例如合伙人退出协议，小二不同意后，重新进入服务中，需要删除已签署协议。
	 * 
	 * @param taobaoUserId
	 * @param type
	 * @param operator
	 *            操作人，小二工号
	 */
	public void cancelProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId,
			PartnerProtocolRelTargetTypeEnum targetType, String operator);
	
	
	/**
	 * 新增协议关系
	 * @param PartnerProtocolRelDto
	 * @
	 */
	public Long addPartnerProtocolRel(PartnerProtocolRelDto PartnerProtocolRelDto) ;
	
	/**
	 * 删除协议关系
	 * @param partnerProtocolRelDeleteDto
	 * @
	 */
	public void deletePartnerProtocolRel(PartnerProtocolRelDeleteDto partnerProtocolRelDeleteDto) ;
	
	
	/**
	 * 查询签署的协议
	 * @param type
	 * @param objectId
	 * @param targetType
	 * @return
	 * @
	 */
	public PartnerProtocolRelDto  getPartnerProtocolRelDto(ProtocolTypeEnum type,Long objectId,PartnerProtocolRelTargetTypeEnum targetType)  ;
	
	public PartnerProtocolRelDto  getPartnerProtocolRelDto(Long objectId,PartnerProtocolRelTargetTypeEnum targetType,Long protocolId)  ;
	
	public PartnerProtocolRelDto  getLastPartnerProtocolRelDtoByTaobaoUserId(Long taobaoUserId,ProtocolTypeEnum type,PartnerProtocolRelTargetTypeEnum targetType)  ;

			
	public void signProtocol(Long objectId, Long taobaoUserId, ProtocolTypeEnum type, Date confirmTime, Date startTime, Date endTime,
			String operator, PartnerProtocolRelTargetTypeEnum targetType);
}
