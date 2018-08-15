package com.taobao.cun.auge.testuser;

import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by zhenhuan.zhangzh on 17/2/28.
 */

@Service("testUserService")
@HSFProvider(serviceInterface= TestUserService.class)
public class TestUserServiceImpl implements TestUserService{
	
	@Autowired
	private TestUserRuleMananger testUserRuleMananger;
	
	@Autowired
	private TestUserInfoManager testUserInfoManager;
    @Override
    public boolean isTestUser(Long taobaoUserId, String bizCode,boolean allMatch) {
    	Assert.notNull(taobaoUserId);
    	Assert.notNull(bizCode);
    	return testUserRuleMananger.run(bizCode, taobaoUserId, allMatch);
    }

	@Override
	public Map<String, String> getTestUserConfig(Long taobaoUserId, String bizCode) {
		return testUserInfoManager.getUserConfig(bizCode, taobaoUserId);
	}

	@Override
	public Result<UserMatchInfo> getUserMatchInfo(Long taobaoUserId) {
		Result<UserMatchInfo> result = new Result<UserMatchInfo>();
		try {
			UserMatchInfo userMatchInfo = new UserMatchInfo();
			boolean bigElec = this.isTestUser(taobaoUserId, "bigElec", true);
			boolean smallElec = this.isTestUser(taobaoUserId, "smallElec", true);
			boolean fmcg = this.isTestUser(taobaoUserId, "fmcg", true);
			userMatchInfo.setBigElecUser(bigElec);
			userMatchInfo.setSmallElecUser(smallElec);
			userMatchInfo.setFmcgUser(fmcg);
			result.setModule(userMatchInfo);
			return result;
		} catch (Exception e) {
			result.addErrorInfo(ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常"));
		}
		return result;
	}

    
}
