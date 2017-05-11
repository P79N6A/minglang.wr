package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;

import com.taobao.cun.attachment.dto.AttachmentDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerBusinessTypeEnum;

/**
 * 更新使用中的合伙人信息
 * @author quanzhu.wangqz
 *
 */
public class PartnerUpdateServicingDto extends OperatorDto implements Serializable{

	private static final long serialVersionUID = -6662885547380230732L;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * email
     */
    private String email;

    /**
     * 经营类型全职： fulltime 兼职：partime
     */
    private PartnerBusinessTypeEnum businessType;

    /**
     * 现状描述
     */
    private String description;

    private List<AttachmentDto> attachments;

	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public PartnerBusinessTypeEnum getBusinessType() {
		return businessType;
	}


	public void setBusinessType(PartnerBusinessTypeEnum businessType) {
		this.businessType = businessType;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public List<AttachmentDto> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<AttachmentDto> attachments) {
		this.attachments = attachments;
	}
}
