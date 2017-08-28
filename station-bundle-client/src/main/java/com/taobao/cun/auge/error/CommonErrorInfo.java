package com.taobao.cun.auge.error;

import com.taobao.cun.auge.common.result.ErrorInfo;

public interface CommonErrorInfo {

    ErrorInfo AUGE_ERR_INVALID_PARAM = ErrorInfo.of("AUGE_ERR_INVALID_PARAM", "auge.error_invalid_param", "参数有误，请检查确认");

    ErrorInfo AUGE_ERR_SYSTEM = ErrorInfo.of("AUGE_ERR_SYSTEM", "auge.system_error", "系统异常");


}
