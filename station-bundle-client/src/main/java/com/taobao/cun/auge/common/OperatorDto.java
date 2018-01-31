package com.taobao.cun.auge.common;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

/**
 * 
 * @author quanzhu.wangqz
 * 
 */
public class OperatorDto implements Serializable {

	private static final long serialVersionUID = 2739466182498801975L;
	

	public static final String DEFAULT_OPERATOR = "system";

	@NotNull(message = "operator not null")
	private String operator;

	@NotNull(message = "operatorType not null")
	private OperatorTypeEnum operatorType;

	private Long operatorOrgId;

	public OperatorDto() {
	}

	public OperatorDto(String operator, OperatorTypeEnum operatorType) {
		this.operator = operator;
		this.operatorType = operatorType;
	}

	public OperatorDto(String operator, OperatorTypeEnum operatorType, Long operatorOrgId) {
		this.operator = operator;
		this.operatorType = operatorType;
		this.operatorOrgId = operatorOrgId;
	}

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

	public void validateOperator() throws AugeBusinessException {
		if (operator == null || operator.length() == 0) {
			throw new IllegalArgumentException("OPERATOR_IS_NULL");
		}
	}

	public void validateOperatorType() throws AugeBusinessException {
		if (operatorType == null) {
			throw new IllegalArgumentException("OPERATORTYPE_IS_NULL");
		}
	}

	public void validateOperatorOrgId() throws AugeBusinessException {
		if (null == operatorOrgId || operatorOrgId <= 0L) {
			throw new IllegalArgumentException("OPERATORORGID_IS_NULL");
		}
	}

	public void copyOperatorDto(OperatorDto source) {
		this.setOperator(source.getOperator());
		this.setOperatorOrgId(source.getOperatorOrgId());
		this.setOperatorType(source.getOperatorType());
	}

	public static OperatorDto defaultOperator() {
		OperatorDto operator = new OperatorDto();
		operator.setOperator(DEFAULT_OPERATOR);
		operator.setOperatorType(OperatorTypeEnum.SYSTEM);

		return operator;
	}
}
