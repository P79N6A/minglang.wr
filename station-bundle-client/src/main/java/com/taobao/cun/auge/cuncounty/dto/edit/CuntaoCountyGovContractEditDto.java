package com.taobao.cun.auge.cuncounty.dto.edit;

import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.time.DateUtils;
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

    @NotNull(message="政府签约信息:协议开始时间不能为空")
    private Date gmtProtocolStart;

    @NotNull(message="政府签约信息:协议结束时间不能为空")
    private Date gmtProtocolEnd;

    @Digits(integer=Integer.MAX_VALUE,fraction=2,message="政府签约信息:专项资金必须为数字，最多保留两位小数")
	@Min(value=0,message="政府签约信息:专项资金大于等于0")
    private String allowance;

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

	public Date getGmtProtocolStart() {
		return gmtProtocolStart;
	}

	public void setGmtProtocolStart(Date gmtProtocolStart) {
		this.gmtProtocolStart = DateUtils.truncate(gmtProtocolStart, Calendar.DATE);
	}

	public Date getGmtProtocolEnd() {
		return gmtProtocolEnd;
	}

	public void setGmtProtocolEnd(Date gmtProtocolEnd) {
		this.gmtProtocolEnd = DateUtils.truncate(gmtProtocolEnd, Calendar.DATE);
	}

	public String getAllowance() {
		return allowance;
	}

	public void setAllowance(String allowance) {
		this.allowance = allowance;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

}