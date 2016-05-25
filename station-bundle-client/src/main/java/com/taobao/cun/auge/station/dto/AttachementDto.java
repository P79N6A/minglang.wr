package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementTypeIdEnum;

/**
 * 附件dto
 * @author quanzhu.wangqz
 *
 */
public class AttachementDto  extends BaseDto implements Serializable{

	private static final long serialVersionUID = -2236478973303620397L;
	
	/**
	 * 主键id
	 */
	private Long id;
	/**
     * 外部的存储id
     */
    private String fsId;

    /**
     * 附件标题
     */
    private String title;

    /**
     * 文件类型,比如txt,jpg
     */
    private String fileType;

    /**
     * 附件描述
     */
    private String description;

    /**
     * 附件类型id
     */
    private AttachementTypeIdEnum attachementTypeId;

    /**
     * 外部id,如station_apply表id
     */
    private Long objectId;

    /**
     * 业务类型
     */
    private AttachementBizTypeEnum bizType;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFsId() {
		return fsId;
	}

	public void setFsId(String fsId) {
		this.fsId = fsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public AttachementTypeIdEnum getAttachementTypeId() {
		return attachementTypeId;
	}

	public void setAttachementTypeId(AttachementTypeIdEnum attachementTypeId) {
		this.attachementTypeId = attachementTypeId;
	}

	public AttachementBizTypeEnum getBizType() {
		return bizType;
	}

	public void setBizType(AttachementBizTypeEnum bizType) {
		this.bizType = bizType;
	}
}
