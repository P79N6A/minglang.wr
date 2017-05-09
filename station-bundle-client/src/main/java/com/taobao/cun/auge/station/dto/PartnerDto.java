package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.taobao.cun.attachment.dto.AttachmentDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;

public class PartnerDto extends OperatorDto implements Serializable{

	private static final long serialVersionUID = -6662885547380230732L;
	
	/**
	 *  合伙人id
	 */
	private Long id;
	  /**
     * 姓名
     */
	@NotNull
    private String name;

    /**
     * 支付宝账号
     */
    private String alipayAccount;

    /**
     * 淘宝user_id
     */
    private Long taobaoUserId;

    /**
     * 淘宝nick
     */
    private String taobaoNick;

    /**
     * 身份证号码
     */
    private String idenNum;

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

    /**
     * 状态：temp:暂存，normal:正常
     */
    private PartnerStateEnum state;
    
//    private List<AttachementDto> attachements;
    
    private List<AttachmentDto> attachments;
    
    /**
     * 是否固点
     */
    private String solidPoint;
    
    /**
     * 可租赁门店面积
     */
    private BigDecimal leaseArea; 
    
    private String aliLangUserId;
    
    private String flowerName;
    private Date birthday;
    
    
    
    public String getFlowerName() {
		return flowerName;
	}

	public void setFlowerName(String flowerName) {
		this.flowerName = flowerName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAliLangUserId() {
		return aliLangUserId;
	}

	public void setAliLangUserId(String aliLangUserId) {
		this.aliLangUserId = aliLangUserId;
	}

	public BigDecimal getLeaseArea() {
		return leaseArea;
	}

	public void setLeaseArea(BigDecimal leaseArea) {
		this.leaseArea = leaseArea;
	}

	public String getSolidPoint() {
		return solidPoint;
	}

	public void setSolidPoint(String solidPoint) {
		this.solidPoint = solidPoint;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getIdenNum() {
		return idenNum;
	}

	public void setIdenNum(String idenNum) {
		this.idenNum = idenNum;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


//	public List<AttachementDto> getAttachements() {
//		return attachements;
//	}
//
//	public void setAttachements(List<AttachementDto> attachements) {
//		this.attachements = attachements;
//	}

	public PartnerBusinessTypeEnum getBusinessType() {
		return businessType;
	}

	public void setBusinessType(PartnerBusinessTypeEnum businessType) {
		this.businessType = businessType;
	}

	public PartnerStateEnum getState() {
		return state;
	}

	public void setState(PartnerStateEnum state) {
		this.state = state;
	}

	public List<AttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDto> attachments) {
		this.attachments = attachments;
	}
}
