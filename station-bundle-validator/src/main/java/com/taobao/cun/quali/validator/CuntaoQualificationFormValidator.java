package com.taobao.cun.quali.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.pm.sc.api.Result;
import com.alibaba.pm.sc.portal.api.quali.spi.FormValidator;
import com.alibaba.pm.sc.portal.api.quali.spi.dto.FormValidateRequest;

public class CuntaoQualificationFormValidator implements FormValidator{
	
	private static List<String> c2bBizScopeKeyWords = new ArrayList<String>();
	
	static {
		c2bBizScopeKeyWords.add("电子商务咨询");
		c2bBizScopeKeyWords.add("电子商务推广");
		c2bBizScopeKeyWords.add("电商咨询");
		c2bBizScopeKeyWords.add("电商推广");
		c2bBizScopeKeyWords.add("代购");
		c2bBizScopeKeyWords.add("网购");
		c2bBizScopeKeyWords.add("商务咨询");
		c2bBizScopeKeyWords.add("商务服务");
		c2bBizScopeKeyWords.add("营销推广");
	}
	
	private QualificationBuilder qualificationBuilder = new QualificationBuilder();
	

	@Override
	public Result<Void> validate(FormValidateRequest request) {
		Result<Void> result = new Result<Void>();
		for (Iterator<String> it = c2bBizScopeKeyWords.iterator(); it.hasNext();) {
			if (StringUtils.isEmpty(it.next())) {
				it.remove();
			}
		}
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
			return Result.result(11003, "FROM_VALIDATE_EXECUTE_FAIL");
		}
		String bizScopeKeyWords = c2bBizScopeKeyWords.stream().collect(Collectors.joining(","));
		result.setMessage("经营范围不合法，必须包含"+bizScopeKeyWords);
		result.setCode(11002);
		return result;
	}

}
