package com.taobao.cun.auge.testuser;

/**
 * Created by zhenhuan.zhangzh on 17/2/28.
 */
public interface TestUserService {

    boolean isTestUser(Long taobaoUserId,String bizCode,boolean allMatch);
}
