package com.taobao.cun.auge.common.utils;

import java.util.Collection;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

/**
 * 检验参数工具类
 * @author quanzhu.wangqz
 *
 */
public class ValidateUtils {

	public static void notNull(Object object,CommonExceptionEnum commonExceptionEnum) {
        if (object == null) {
            throw new AugeServiceException(commonExceptionEnum);
        }
    }
	
	public static void notNull(Object object) {
        if (object == null) {
            throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
        }
    }
	
	public static void notEmpty(Collection collection) {
        if (collection == null || collection.size() == 0) {
        	throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
        }
    }
	
	public static void validateParam(OperatorDto operatorDto) {
		notNull(operatorDto);
		operatorDto.validateOperator();
		operatorDto.validateOperatorOrgId();
		operatorDto.validateOperatorType();
    }
}
