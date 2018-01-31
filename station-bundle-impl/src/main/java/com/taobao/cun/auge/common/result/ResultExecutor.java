package com.taobao.cun.auge.common.result;

import com.taobao.cun.auge.common.exception.AugeBizException;
import com.taobao.eagleeye.EagleEye;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResultExecutor<T> {

    private static Logger logger = LoggerFactory.getLogger(ResultExecutor.class);
    private Result<T> _result = Result.of(true);

    public Result<T> execute() {
        
        try {
            beforeExecute();

            if (_result.isSuccess()) {
                T result = doExecute();
                _result.setModule(result);
            }
            afterExecute();
        } catch (AugeBizException e) {
            _result.addErrorInfo(e.getErrorInfos());
        }  catch (Exception e) {
            logger.error("traceId:" + EagleEye.getTraceId(), e);
            _result.addErrorInfo(com.taobao.cun.auge.error.CommonErrorInfo.AUGE_ERR_SYSTEM);
        }

        return _result;
    }

    

    protected void writeLog(AugeBizException e) {
    	
    }

    
    protected abstract T doExecute() throws Exception;

    protected void afterExecute() throws Exception {
    }

    protected void beforeExecute() throws Exception {
    }

    protected Result<T> getResult() {
        return _result;
    }

}
