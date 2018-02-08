package com.taobao.cun.auge.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.eagleeye.EagleEye;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 未来重构异常所使用
 * 业务异常，异常信息，直接可以显示给用户的
 */
public class AugeBizException extends RuntimeException {

	private static final long serialVersionUID = -6479393095526687858L;

	private List<ErrorInfo> errors = Lists.newArrayList();

	private final List<Object> runtimeParameters = Lists.newArrayList();

	public AugeBizException(ErrorInfo errorInfo) {
		errors.add(errorInfo);
	}

	public AugeBizException(List<ErrorInfo> errorInfos) {
		this.errors.addAll(errorInfos);
	}

	public AugeBizException(ErrorInfo errorInfo, Throwable t) {
		super(t);
		errors.add(errorInfo);
	}

	public static AugeBizException of(ErrorInfo errorInfo, Object... params) {
		AugeBizException exp = new AugeBizException(errorInfo);
		for (Object param : params) {
			exp.addRuntimeParameter(param);
		}
		return exp;
	}

	public static AugeBizException of(List<ErrorInfo> errorInfos, Object... params) {
		AugeBizException exp = new AugeBizException(errorInfos);
		for (Object param : params) {
			exp.addRuntimeParameter(param);
		}
		return exp;
	}

	public AugeBizException() {
		super();
	}

	public AugeBizException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return StringUtils.join(errors.stream().map(ErrorInfo::getErrorCode).collect(Collectors.toList()), ",");
	}

	public List<ErrorInfo> getErrorInfos() {
		return ImmutableList.copyOf(errors);
	}

	public AugeBizException addRuntimeParameter(Object o) {
		runtimeParameters.add(o);
		return this;
	}

	public AugeBizException addRuntimeParameters(Object... objs) {
		for (Object o : objs) {
			runtimeParameters.add(o);
		}
		return this;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("\n").append(EagleEye.getTraceId());
		if (CollectionUtils.isNotEmpty(runtimeParameters)) {
			sb.append("\nParameters:");
			for (Object object : runtimeParameters) {
				sb.append("\n\t" + object);
			}
		}
		if (CollectionUtils.isNotEmpty(this.getErrorInfos())) {
			sb.append("\nErrorInfos:");
			for (ErrorInfo code : this.getErrorInfos()) {
				sb.append("\n\t" + code);
			}
		}
		sb.append("\n").append(getClass().getName());
		if (sb.length() > 0) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

}