package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.station.dto.AttachementDeleteDto;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
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
	
	/**
	 * 批量新增附件
	 * @param attachementDto
	 * @param objectId
	 * @param bizTypeEnum
	 * @throws AugeServiceException
	 */
	public void addAttachementBatch(List<AttachementDto> attachementDtoList,Long objectId,AttachementBizTypeEnum bizTypeEnum,String operator) throws AugeServiceException;
	
	/**
	 * 批量修改,   先删除,后新增
	 * @param attachementDtoList
	 * @param objectId
	 * @param bizTypeEnum
	 * @throws AugeServiceException
	 */
	public void modifyAttachementBatch(List<AttachementDto> attachementDtoList,Long objectId,AttachementBizTypeEnum bizTypeEnum,String operator) throws AugeServiceException;
	
	/**
	 * 查询附件
	 * @param objectId
	 * @param bizTypeEnum
	 * @return
	 * @throws AugeServiceException
	 */
	public List<AttachementDto>  selectAttachementList(Long objectId,AttachementBizTypeEnum bizTypeEnum) throws AugeServiceException;
	

}
