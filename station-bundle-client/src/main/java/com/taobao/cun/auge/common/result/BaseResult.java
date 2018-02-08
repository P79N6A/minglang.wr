package com.taobao.cun.auge.common.result;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 3426791699617741590L;

    private boolean success = true;

    private T module;

    protected final List<ErrorInfo> errorInfos = Lists.newArrayList();

    protected final List<ErrorInfo> warningInfos = Lists.newArrayList();

    public List<String> getErrorMessage() {

        List<String> errorMessages = Lists.newArrayListWithExpectedSize(errorInfos.size());

        for (ErrorInfo errorInfo : errorInfos) {
            if (StringUtils.isNotEmpty(errorInfo.getErrorMessage())) {
                errorMessages.add(errorInfo.getErrorMessage());
            }
        }

        return errorMessages;

    }

    public List<String> getWarningMessage() {

        List<String> warningMessages = Lists.newArrayListWithExpectedSize(warningInfos.size());

        for (ErrorInfo errorInfo : warningInfos) {
            if (StringUtils.isNotEmpty(errorInfo.getErrorMessage())) {
                warningMessages.add(errorInfo.getErrorMessage());
            }
        }

        return warningMessages;

    }

    public List<String> getErrorCodes() {

        List<String> errorMessages = Lists.newArrayListWithExpectedSize(errorInfos.size());

        for (ErrorInfo errorInfo : errorInfos) {
            if (StringUtils.isNotEmpty(errorInfo.getErrorCode())) {
                errorMessages.add(errorInfo.getErrorCode());
            }
        }

        return errorMessages;
    }

    public List<String> getOuterErrorCodes() {

        List<String> errorMessages = Lists.newArrayListWithExpectedSize(errorInfos.size());

        for (ErrorInfo errorInfo : errorInfos) {
            if (StringUtils.isNotEmpty(errorInfo.getOuterErrorCode())) {
                errorMessages.add(errorInfo.getOuterErrorCode());
            }
        }

        return errorMessages;
    }

    public void addErrorInfo(ErrorInfo errorInfo) {

        success = false;

        errorInfos.add(errorInfo);
    }

    public void addErrorInfo(List<ErrorInfo> errorInfos) {

        if (errorInfos.isEmpty()) {
            return;
        }

        success = false;

        this.errorInfos.addAll(errorInfos);
    }

    public void addWarningInfo(ErrorInfo errorInfo) {

        success = true;

        warningInfos.add(errorInfo);
    }

    public void addWarningInfo(List<ErrorInfo> errorInfos) {

        if (errorInfos.isEmpty()) {
            return;
        }

        success = true;

        this.warningInfos.addAll(errorInfos);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getModule() {
        return module;
    }

    public void setModule(T module) {
        this.module = module;
    }

    public List<ErrorInfo> getErrorInfos() {
        return errorInfos;
    }

    public List<ErrorInfo> getWarningInfos() {
        return warningInfos;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof BaseResult)) && EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }
}
