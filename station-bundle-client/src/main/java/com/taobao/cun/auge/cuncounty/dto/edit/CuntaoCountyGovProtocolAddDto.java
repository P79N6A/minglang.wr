package com.taobao.cun.auge.cuncounty.dto.edit;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * 添加协议
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyGovProtocolAddDto {
	/**
	 * 县服务中心
	 */
    private Long countyId;
    
    /**
     * 协议编号
     */
    private String serialNum;
    
    /**
     * 签约主体
     */
    private String signatory;

    /**
     * 协议开始时间
     */
    private Date gmtStart;

    /**
     * 协议结束时间
     */
    private Date gmtEnd;

    /**
     * 协议附件
     */
    private String attachments;
    
    private String operator;
    
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getSignatory() {
		return signatory;
	}

	public void setSignatory(String signatory) {
		this.signatory = signatory;
	}

	public Date getGmtStart() {
		return gmtStart;
	}

	public void setGmtStart(Date gmtStart) {
		this.gmtStart = gmtStart;
	}

	public Date getGmtEnd() {
		return gmtEnd;
	}

	public void setGmtEnd(Date gmtEnd) {
		this.gmtEnd = gmtEnd;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public boolean isContentSame(CuntaoCountyGovProtocolAddDto cuntaoCountyProtocolDto) {
		return EqualsBuilder.reflectionEquals(this, cuntaoCountyProtocolDto, "operator");
	}
}