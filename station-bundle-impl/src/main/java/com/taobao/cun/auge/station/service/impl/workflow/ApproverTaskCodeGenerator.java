package com.taobao.cun.auge.station.service.impl.workflow;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by xujianhui on 17/2/22.
 *
 * @author xujianhui
 * @date 2017/02/22
 */
public class ApproverTaskCodeGenerator {

    private static final String ORG = "ORG";
    private static final String SPLITER_CHAR = "#";

    public static String generateOrgAclTaskCode(String orgId, String aclRoleCode) {
        JSONArray result = new JSONArray();
        result.add(0, ORG + SPLITER_CHAR + orgId + SPLITER_CHAR + aclRoleCode);
        return result.toString();
    }
}
