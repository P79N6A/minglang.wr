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
	
	@Autowired
	private QualificationBuilder qualificationBuilder;
	

	@Override
	public Result<Void> validate(FormValidateRequest request) {
		Result<Void> result = new Result<Void>();
		try {
			if(request.getContent()==null){
				result.setCode(11002);
				result.setMessage("资质内容不存在");
				return result;
			}
			
			String bizScope = qualificationBuilder.getContent(request.getContent(), qualificationBuilder.getBizScope());
			if(StringUtils.isEmpty(bizScope)){
				result.setCode(11002);
				result.setMessage("经营范围不存在");
				return result;
			}
			if(CollectionUtils.isEmpty(c2bBizScopeKeyWords)){
				return Result.result(1,"SUCCESS");
			}
			for (Iterator<String> iterator = c2bBizScopeKeyWords.iterator(); iterator.hasNext();) {
				String keyword =  iterator.next();
				if(bizScope.contains(keyword)){
					return Result.result(1,"SUCCESS");
				}
			}
		} catch (Exception e) {
			logger.error("CuntaoQualificationFormValidator error!",e);
			return Result.result(11003, "FROM_VALIDATE_EXECUTE_FAIL");
		}
		String bizScopeKeyWords = c2bBizScopeKeyWords.stream().collect(Collectors.joining(","));
		result.setMessage("经营范围不合法，必须包含"+bizScopeKeyWords);
		result.setCode(11002);
		return result;
	}

}
