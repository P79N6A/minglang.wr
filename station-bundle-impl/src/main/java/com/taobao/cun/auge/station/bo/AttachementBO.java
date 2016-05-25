package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 附件基础服务类
 * @author quanzhu.wangqz
 *
 */
public interface AttachementBO {
	
	public Long addAttachement(AttachementDto attachementDto)   throws AugeServiceException;
	

}
