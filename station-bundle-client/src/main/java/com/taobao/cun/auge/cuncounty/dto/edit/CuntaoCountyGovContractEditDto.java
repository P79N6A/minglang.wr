package com.taobao.cun.auge.cuncounty.dto.edit;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 政府签约信息
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyGovContractEditDto {
    @NotNull(message="政府签约信息:县服务中心不能为空")
    private Long countyId;

    @NotBlank(message="政府签约信息:协议编号不能为空")
    private String serialNum;

    @NotBlank(message="政府签约信息:签约主体不能为空")
    private String signatory;

    @NotBlank(message="政府签约信息:协议开始时间不能为空")
    private Date gmtStart;

    @NotBlank(message="政府签约信息:协议结束时间不能为空")
    private Date gmtEnd;

    @NotBlank(message="政府签约信息:资金补贴不能为空")
    private String allowance;

    @NotBlank(message="政府签约信息:宣传支持不能为空")
    private String publicity;
    
    @NotBlank(message="政府签约信息:协议不能为空")
    private String attachments;
    
    @NotBlank(message="政府签约信息:操作人不能为空")
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