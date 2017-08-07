package com.taobao.cun.auge.user.service;


public interface CuntaoUserOrgService {
	 /**
     * 校验工号是否与村淘组织相匹配
     * 
     * @param empId 员工工号
     * @param cuntaoFullIdPath 村淘组织id路径
     * @return 工号与组织匹配时，返回true，反之，false
     */
    public Boolean checkOrg(String empId, String cuntaoFullIdPath);
}
