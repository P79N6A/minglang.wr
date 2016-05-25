package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

/**
 * 
 * @author quanzhu.wangqz
 * 
 */
public class BaseDto implements Serializable {

	private static final long serialVersionUID = 2739466182498801975L;

	private String operator;

	private Long operatorOrgId;

	private OperatorTypeEnum operatorType;

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
		return operatorType;
	}

	public void setOperatorType(OperatorTypeEnum operatorType) {
		this.operatorType = operatorType;
	}

	public void validateOperator() throws AugeServiceException{
		if (operator == null || operator.length() == 0) {
			throw new AugeServiceException(CommonExceptionEnum.OPERATOR_IS_NULL);
		}
	}

	public void validateOperatorType() throws AugeServiceException{
		if (operatorType == null) {
			throw new AugeServiceException(CommonExceptionEnum.OPERATORTYPE_IS_NULL);
		}
	}

	public void validateOperatorOrgId() throws AugeServiceException{
		if (operatorOrgId == null || operatorOrgId < 0L) {
			throw new AugeServiceException(CommonExceptionEnum.OPERATORORGID_IS_NULL);
		}
	}
}
	
