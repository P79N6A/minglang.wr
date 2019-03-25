package com.taobao.cun.auge.cuncounty.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Splitter;

/**
 * 政府签约信息
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyGovContractDto {
    private Long id;
    /**
     * 县服务中心ID
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
     * 补贴金额
     */
    private String allowance;

    /**
     * 宣传
     */
    private String publicity;

    private String attachments;
    
    public List<String> getAttachmentArray(){
    	if(attachments == null) {
    		return new ArrayList<String>();
    	}
    	return Splitter.on(";").splitToList(attachments);
    }

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

	public String getAllowance() {
		return allowance;
	}

	public void setAllowance(String allowance) {
		this.allowance = allowance;
	}

	public String getPublicity() {
		return publicity;
	}

	public void setPublicity(String publicity) {
		this.publicity = publicity;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

}