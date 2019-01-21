package com.taobao.cun.auge.qualification.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.pm.sc.api.Result;
import com.alibaba.pm.sc.portal.api.constants.ResultCode;
import com.alibaba.pm.sc.portal.api.quali.spi.FormValidator;
import com.alibaba.pm.sc.portal.api.quali.spi.dto.FormValidateRequest;
import com.taobao.cun.auge.configuration.DiamondFactory;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service("cuntaoQualificationFormValidator")
@RefreshScope
@HSFProvider(serviceInterface= FormValidator.class,serviceVersion="${cuntaoQualificationFormValidator.version}")
public class CuntaoQualificationFormValidator implements FormValidator{
	private static final Logger logger = LoggerFactory.getLogger(CuntaoQualificationFormValidator.class);
//	@Value("#{'${c2bBizScopeKeyWords}'.split(',')}")
//	private List<String> c2bBizScopeKeyWords;
	
	@Value("${isCheckBizScope}")
	private boolean isCheckBizScope;
	

	@Override
	public Result<Void> validate(FormValidateRequest request) {
		Result<Void> result = new Result<Void>();
		if(!isCheckBizScope){
			return Result.result(ResultCode.SUCCESS);
		}
//		c2bBizScopeKeyWords = c2bBizScopeKeyWords.stream().filter(value -> value!=null).collect(Collectors.toList());
		List<String> c2bBizScopeKeyWords= JSON.parseObject(DiamondFactory.getBizScopeKeyWordsDiamondConfig(), new TypeReference<List<String>>() {});
		 
		try {
			String bizScope = (String)request.getContentByName("operateScope");
			logger.info("CuntaoQualificationFormValidator validate param:" + bizScope);
			if(StringUtils.isEmpty(bizScope)){
				result.setCode(ResultCode.FORM_VALIDATE_FAIL.getCode());
				result.setMessage("没有获取到经营范围");
				return result;
			}
			if(CollectionUtils.isEmpty(c2bBizScopeKeyWords)){
				return Result.result(ResultCode.SUCCESS);
			}
			for (Iterator<String> iterator = c2bBizScopeKeyWords.iterator(); iterator.hasNext();) {
				String keyword =  iterator.next();
				if(bizScope.contains(keyword)){
					logger.info("CuntaoQualificationFormValidator validate sucess, scope:" + bizScope);
					return Result.result(ResultCode.SUCCESS);
				}
			}
			logger.info("CuntaoQualificationFormValidator validate failed, scope:" + bizScope);
		} catch (Exception e) {
			logger.error("CuntaoQualificationFormValidator error!",e);
			return Result.result(ResultCode.FORM_VALIDATE_EXECUTE_FAIL);
		}
		String bizScopeKeyWords = String.join(",",c2bBizScopeKeyWords);
		result.setMessage("经营范围必须包含其中一项["+bizScopeKeyWords+"]");
		result.setCode(ResultCode.FORM_VALIDATE_FAIL.getCode());
		return result;
	}
//	public static void main(String[] args) {
//		
//		//List<String> c2bBizScopeKeyWords= JSON.parseObject("["+"有偿帮助服务"+","+"代买"+"]", new TypeReference<List<String>>() {});
//		System.out.println("["+"有偿帮助服务"+","+"代买"+"]");
//
//	}

}
