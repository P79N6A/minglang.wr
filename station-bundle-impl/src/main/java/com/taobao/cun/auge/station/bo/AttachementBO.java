package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.dto.AttachementDeleteDto;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 附件基础服务类
 * @author quanzhu.wangqz
 *
 */
public interface AttachementBO {
	
	/**
	 * 新增附件
	 * @param attachementDto
	 * @return
	 * @throws AugeServiceException
	 */
	public Long addAttachement(AttachementDto attachementDto) throws AugeServiceException;
	
	/**
	 * 删除附件
	 * @param attachementDeleteDto
	 * @return
	 * @throws AugeServiceException
	 */
	public void deleteAttachement(AttachementDeleteDto attachementDeleteDto) throws AugeServiceException;
	

}
