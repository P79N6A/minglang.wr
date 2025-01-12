package com.taobao.cun.auge.common.utils;

import java.util.Collection;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;

/**
 * 检验参数工具类
 * @author quanzhu.wangqz
 *
 */
public class ValidateUtils {

	
	public static void notNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("PARAM_IS_NULL");
        }
    }
	
	public static void notEmpty(Collection collection) {
        if (collection == null || collection.size() == 0) {
        	throw new IllegalArgumentException("PARAM_IS_NULL");
        }
    }
	
	public static void validateParam(OperatorDto operatorDto) {
		notNull(operatorDto);
		operatorDto.validateOperator();

		if (OperatorTypeEnum.BUC.equals(operatorDto.getOperatorType())) {
			operatorDto.validateOperatorOrgId();
		}

		operatorDto.validateOperatorType();
    }
}
