package com.taobao.cun.auge.station.bo;

import java.util.Date;

import com.taobao.cun.auge.station.dto.PartnerProtocolRelDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

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
	 * @throws AugeServiceException
	 */
	public Long addPartnerProtocolRel(PartnerProtocolRelDto PartnerProtocolRelDto) throws AugeServiceException;
	
	/**
	 * 删除协议关系
	 * @param partnerProtocolRelDeleteDto
	 * @throws AugeServiceException
	 */
	public void deletePartnerProtocolRel(PartnerProtocolRelDeleteDto partnerProtocolRelDeleteDto) throws AugeServiceException;
	
	
	/**
	 * 查询签署的协议
	 * @param type
	 * @param objectId
	 * @param targetType
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerProtocolRelDto  getPartnerProtocolRelDto(ProtocolTypeEnum type,Long objectId,PartnerProtocolRelTargetTypeEnum targetType)  throws AugeServiceException;
	
	public void signProtocol(Long objectId, Long taobaoUserId, ProtocolTypeEnum type, Date confirmTime, Date startTime, Date endTime,
			String operator, PartnerProtocolRelTargetTypeEnum targetType);
}
