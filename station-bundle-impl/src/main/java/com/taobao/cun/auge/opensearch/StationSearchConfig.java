package com.taobao.cun.auge.opensearch;

import com.taobao.csp.switchcenter.annotation.AppSwitch;
import com.taobao.csp.switchcenter.annotation.NameSpace;
import com.taobao.csp.switchcenter.bean.Switch;

@NameSpace(nameSpace="CUNTAO_STATION_SEARCH")
public class StationSearchConfig {
	@AppSwitch(des="最大的访问次数",valueDes="最多20次",level= Switch.Level.p2)
    public static  int USER_VISIT_TIMES=20;

    @AppSwitch(des="月搜索次数", valueDes = "最多50次", level = Switch.Level.p2)
    public static int SEARCH_VISIT_MONTH_TIME = 50;

    @AppSwitch(des="日搜索次数", valueDes = "最多10次", level = Switch.Level.p2)
    public static int SEARCH_VISIT_DAY_TIME = 10;
}
