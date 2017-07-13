package com.taobao.cun.auge.validator;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 采用JSR-303规范对Bean校验
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class BeanValidator {

	private static Validator staticValidator;
	@Autowired
	private Validator validator;

	@PostConstruct
	public void init() {
		BeanValidator.staticValidator = validator;
	}

	/**
	 * 校验Bean，返回校验错误，如果返回的list为空，则表示没有错误
	 * 
	 * @param object
	 * @param groups
	 * @return
	 */
	public static <T> ValidateResult validate(T object, Class<?>... groups) {
		List<String> errors = Lists.newArrayList();

		Set<ConstraintViolation<T>> set = staticValidator.validate(object, groups);
		for (ConstraintViolation<T> c : set) {
			errors.add(c.getMessage());
		}
		return new ValidateResult(object.getClass().getCanonicalName(), errors);
	}

	/**
	 * 将校验结果以异常方式抛出
	 * 
	 * @param object
	 * @param groups
	 * @throws BeanValidateException
	 */
	public static <T> void validateWithThrowable(T object, Class<?>... groups){
		ValidateResult validateResult = validate(object, groups);

		if (validateResult.hasError()) {
			BeanValidateException validateException =  new BeanValidateException(validateResult.getClassName(), validateResult.getErrors());
			throw new AugeBusinessException("param error",validateException.getMessage());
		}
	}

}
