package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.station.dto.ProtocolDto;
import com.taobao.cun.auge.station.enums.ProtocolGroupTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

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
	 */
	public ProtocolDto getValidProtocol(ProtocolTypeEnum type);
	/**
	 * 查询所有状态的协议id
	 * @param types
	 * @return
	 */
    public List<Long> getAllProtocolId(List<ProtocolTypeEnum> types);
    
    /**
     * 通过协议租得到协议类型
     * @return
     */
    public List<ProtocolTypeEnum> getProtocolTypeByGroupType(ProtocolGroupTypeEnum groupTypeEnum);



}
