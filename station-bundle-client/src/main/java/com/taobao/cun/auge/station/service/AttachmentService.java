package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.dto.AttachementDeleteDto;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementTypeIdEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

import java.util.List;

/**
 * Created by xiao on 16/12/22.
 */
public interface AttachmentService {

    public List<AttachementDto> getAttachmentList(Long objectId, AttachementBizTypeEnum bizTypeEnum, AttachementTypeIdEnum attachementTypeId) throws AugeServiceException;

    public void addAttachmentBatch(List<AttachementDto> attachmentDtoList,Long objectId,AttachementBizTypeEnum bizTypeEnum, AttachementTypeIdEnum attachmentTypeId,OperatorDto operatorDto) throws AugeServiceException;

    /**
	 * 批量修改,   先删除,后新增
	 * @param attachementDtoList
	 * @param objectId
	 * @param bizTypeEnum
	 * @throws AugeServiceException
	 */
	public List<Long> modifyAttachementBatch(List<AttachementDto> attachementDtoList,Long objectId,AttachementBizTypeEnum bizTypeEnum,OperatorDto operatorDto) throws AugeServiceException;
	
	/**
	 * 批量修改,   先删除,后新增
	 * @param attachementDtoList
	 * @param objectId
	 * @param bizTypeEnum
	 * @throws AugeServiceException
	 */
	public void modifyAttachementBatch(List<AttachementDto> attachementDtoList,Long objectId,AttachementBizTypeEnum bizTypeEnum, AttachementTypeIdEnum attachementTypeId,OperatorDto operatorDto) throws AugeServiceException;
	
	/**
	 * 批量新增附件
	 * @param attachementDtoList
	 * @param objectId
	 * @param bizTypeEnum
	 * @throws AugeServiceException
	 */
	public List<Long> addAttachementBatch(List<AttachementDto> attachementDtoList,Long objectId,AttachementBizTypeEnum bizTypeEnum,OperatorDto operatorDto) throws AugeServiceException;
	
	/**
	 * 查询附件
	 * @param objectId
	 * @param bizTypeEnum
	 * @return
	 * @throws AugeServiceException
	 */
	public List<AttachementDto>  getAttachementList(Long objectId,AttachementBizTypeEnum bizTypeEnum) throws AugeServiceException;
	
	/**
	 * 删除附件
	 * @param attachementDeleteDto
	 * @return
	 * @throws AugeServiceException
	 */
	public void deleteAttachement(AttachementDeleteDto attachementDeleteDto) throws AugeServiceException;
}