package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.station.dto.ProtocolDto;
import com.taobao.cun.auge.station.enums.ProtocolGroupTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 协议表基础服务
 * @author quanzhu.wangqz
 *
 */
public interface ProtocolBO {
	
	/**
	 * 查询有效的协议id
	 * @param type
	 * @return
	 * @throws AugeServiceException
	 */
	public ProtocolDto getValidProtocol(ProtocolTypeEnum type) throws AugeServiceException;
	/**
	 * 查询所有状态的协议id
	 * @param types
	 * @return
	 * @throws AugeServiceException
	 */
    public List<Long> getAllProtocolId(List<ProtocolTypeEnum> types) throws AugeServiceException;
    
    /**
     * 通过协议租得到协议类型
     * @return
     * @throws AugeServiceException
     */
    public List<ProtocolTypeEnum> getProtocolTypeByGroupType(ProtocolGroupTypeEnum groupTypeEnum) throws AugeServiceException;



}
