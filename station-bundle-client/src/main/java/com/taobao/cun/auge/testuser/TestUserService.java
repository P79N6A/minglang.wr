package com.taobao.cun.auge.testuser;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.common.result.Result;

/**
 * Created by zhenhuan.zhangzh on 17/2/28.
 */
public interface TestUserService {

    boolean isTestUser(Long taobaoUserId,String bizCode,boolean allMatch);
    
    Map<String,String> getTestUserConfig(Long taobaoUserId,String bizCode);
    
    Result<UserMatchInfo> getUserMatchInfo(Long taobaoUserId);

    public void updatePartnerApplyStatusToAddressPass(List<Long> ids);
}
