package com.taobao.cun.auge.cuncounty.dto;

import java.util.Date;

/**
 * 政府协议
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyGovProtocolDto {
    private Long id;

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
    private Date gmtProtocolStart;

    /**
     * 协议结束时间
     */
    private Date gmtProtocolEnd;

    /**
     * 协议附件
     */
    private String attachments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getGmtProtocolStart() {
		return gmtProtocolStart;
	}

	public void setGmtProtocolStart(Date gmtProtocolStart) {
		this.gmtProtocolStart = gmtProtocolStart;
	}

	public Date getGmtProtocolEnd() {
		return gmtProtocolEnd;
	}

	public void setGmtProtocolEnd(Date gmtProtocolEnd) {
		this.gmtProtocolEnd = gmtProtocolEnd;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
}