package com.taobao.cun.auge.qualification.service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.alibaba.pm.sc.api.Result;
import com.alibaba.pm.sc.portal.api.constants.ResultCode;
import com.alibaba.pm.sc.portal.api.quali.spi.FormValidator;
import com.alibaba.pm.sc.portal.api.quali.spi.dto.FormValidateRequest;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("cuntaoQualificationFormValidator")
@RefreshScope
@HSFProvider(serviceInterface= FormValidator.class,serviceVersion="${cuntaoQualificationFormValidator.version}")
public class CuntaoQualificationFormValidator implements FormValidator{
	private static final Logger logger = LoggerFactory.getLogger(CuntaoQualificationFormValidator.class);
	@Value("#{'${c2bBizScopeKeyWords}'.split(',')}")
	private List<String> c2bBizScopeKeyWords;

	@Override
	public Result<Void> validate(FormValidateRequest request) {
		Result<Void> result = new Result<Void>();
		c2bBizScopeKeyWords = c2bBizScopeKeyWords.stream().filter(value -> value!=null).collect(Collectors.toList());
		try {
			if(request.getContent()==null){
				result.setCode(ResultCode.FORM_VALIDATE_FAIL.getCode());
				result.setMessage("资质内容不存在");
				return result;
			}
			String bizScope = (String)request.getContent().get("operateScope");
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
					return Result.result(ResultCode.SUCCESS);
				}
			}
		} catch (Exception e) {
			logger.error("CuntaoQualificationFormValidator error!",e);
			return Result.result(ResultCode.FORM_VALIDATE_EXECUTE_FAIL);
		}
		String bizScopeKeyWords = String.join(",",c2bBizScopeKeyWords);
		result.setMessage("经营范围必须包含其中一项["+bizScopeKeyWords+"]");
		result.setCode(ResultCode.FORM_VALIDATE_FAIL.getCode());
		return result;
	}

}
