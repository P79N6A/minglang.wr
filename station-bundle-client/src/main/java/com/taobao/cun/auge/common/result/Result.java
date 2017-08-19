package com.taobao.cun.auge.common.result;


import java.util.List;

public class Result<T> extends BaseResult<T> {

    private static final long serialVersionUID = -8670001196386385694L;

    public static <T> Result<T> of(T module) {

        Result<T> result = new Result<T>();

        result.setModule(module);

        return result;

    }

    public static <T> Result<T> of(boolean success) {

        Result<T> result = new Result<T>();

        result.setSuccess(success);

        return result;

    }

    public static <T> Result<T> of(ErrorInfo errorInfo) {

        Result<T> result = new Result<T>();

        result.setSuccess(false);

        result.addErrorInfo(errorInfo);

        return result;

    }

    public static <T> Result<T> of(List<ErrorInfo> errorInfos) {

        Result<T> result = new Result<T>();

        result.setSuccess(false);

        result.addErrorInfo(errorInfos);

        return result;

    }

    public void merge(Result<T> result) {

        if (!result.isSuccess()) {
            setSuccess(false);
        }

        setModule(result.getModule());
        errorInfos.addAll(result.getErrorInfos());

    }

}
