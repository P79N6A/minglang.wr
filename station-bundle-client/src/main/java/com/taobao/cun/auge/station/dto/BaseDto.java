package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.OperatorTypeEnum;

/**
 * 
 * @author quanzhu.wangqz
 * 
 */
public class BaseDto implements Serializable {

	private static final long serialVersionUID = 2739466182498801975L;

	private String operator;

	private Long operatorOrgId;

	private OperatorTypeEnum OperatorType;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public Long getOperatorOrgId() {
		return operatorOrgId;
	}

	public void setOperatorOrgId(Long operatorOrgId) {
		this.operatorOrgId = operatorOrgId;
	}

	public OperatorTypeEnum getOperatorType() {
		return OperatorType;
	}

	public void setOperatorType(OperatorTypeEnum operatorType) {
		OperatorType = operatorType;
	}

	public boolean validateOperator() {
		return operator == null || operator.length() == 0;
	}

	public boolean validateOperatorType() {
		return OperatorType == null;
	}

	public boolean validateOperatorOrgId() {
		return operatorOrgId == null || operatorOrgId <= 0l;
	}
}
	
