package com.taobao.cun.auge.common.result;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ErrorInfo implements Serializable {

    private static final long serialVersionUID = 9016055773696829356L;

    /**
     * 内部错误码, 多用于日志
     */
    private final String errorCode;

    /**
     * 外部错误码, 一般用于提供出去的Top接口, top平台识别
     */
    private final String outerErrorCode;

    private final String subErrorCode;

    /**
     * 错误文案, 用于直接提示给用户
     */
    private final String errorMessage;

    private ErrorInfo(String errorCode, String outerErrorCode, String subErrorCode, String errorMessage) {

        if (StringUtils.isBlank(errorCode)) {
            this.errorCode = StringUtils.EMPTY;
        } else {
            this.errorCode = StringUtils.isBlank(subErrorCode) ? errorCode.trim()
                : (errorCode.trim() + ":" + subErrorCode.trim());
        }

        if (StringUtils.isBlank(outerErrorCode)) {
            this.outerErrorCode = StringUtils.EMPTY;
        } else {
            this.outerErrorCode = StringUtils.isBlank(subErrorCode) ? outerErrorCode.trim() : (outerErrorCode.trim()
                + ":" + subErrorCode.trim());
        }

        this.subErrorCode = StringUtils.isBlank(subErrorCode) ? StringUtils.EMPTY : subErrorCode.trim();

        this.errorMessage = StringUtils.isBlank(errorMessage) ? StringUtils.EMPTY : errorMessage.trim();

    }

    public static ErrorInfo of(String errorCode, String outerErrorCode, String errorMessage) {

        return of(errorCode, outerErrorCode, null, errorMessage);
    }

    public static ErrorInfo of(String errorCode, String outerErrorCode, String subErrorCode, String errorMessage) {

//        Validate.isTrue(StringUtils.isNotEmpty(errorCode) || StringUtils.isNotEmpty(outerErrorCode),
//            "Argument errorCode and outerErrorCode can't both be empty.");

        return new ErrorInfo(errorCode, outerErrorCode, subErrorCode, errorMessage);
    }

    public ErrorInfo of(Object... errorMessageArgs) {
        return of(this.getErrorCode(), this.getOuterErrorCode(), this.getSubErrorCode(),
            String.format(this.getErrorMessage(), errorMessageArgs));
    }

    public ErrorInfo join(String msg) {
        return of(this.getErrorCode(), this.getOuterErrorCode(), this.getSubErrorCode(),
            (StringUtils.isBlank(this.getErrorMessage()) ? msg : (this.getErrorMessage() + ":" + msg)));
    }

    public ErrorInfo subError(String subErrorCode) {
        return of(this.getErrorCode(), this.getOuterErrorCode(), subErrorCode, this.getErrorMessage());
    }

    public ErrorInfo subError(String subErrorCode, String errorMessage) {
        return of(this.getErrorCode(), this.getOuterErrorCode(), subErrorCode,
            StringUtils.isNotBlank(errorMessage) ? errorMessage : this.getErrorMessage());
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getOuterErrorCode() {
        return outerErrorCode;
    }

    public String getSubErrorCode() {
        return subErrorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof ErrorInfo)) && EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }
}
