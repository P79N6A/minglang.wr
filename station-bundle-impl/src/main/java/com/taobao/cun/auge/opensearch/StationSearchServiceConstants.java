package com.taobao.cun.auge.opensearch;

import com.taobao.csp.switchcenter.annotation.AppSwitch;
import com.taobao.csp.switchcenter.annotation.NameSpace;
import com.taobao.csp.switchcenter.bean.Switch;

@NameSpace(nameSpace = "StationSearch")
public class StationSearchServiceConstants {
	@AppSwitch(des = "名称搜索开始取数据的索引", valueDes = "名称搜索开始取数据的索引", level = Switch.Level.p2)
	public static int QUERY_BY_NAME_FETCH_START = 0;
	@AppSwitch(des = "名称搜索取数据的大小", valueDes = "名称搜索取数据的大小", level = Switch.Level.p2)
	public static int QUERY_BY_NAME_FETCH_SIZE = 20;
	@AppSwitch(des = "经纬度搜索开始取数据的索引", valueDes = "经纬度搜索开始取数据的索引", level = Switch.Level.p2)
	public static int QUERY_BY_DISTANCE_FETCH_START = 0;
	@AppSwitch(des = "经纬度搜索取数据的大小", valueDes = "经纬度搜索开始取数据的大小", level = Switch.Level.p2)
	public static int QUERY_BY_DISTANCE_FETCH_SIZE = 10;
	@AppSwitch(des = "服务站名称搜索的黑名单", level = Switch.Level.p2)
	public static String STATION_SEARCH_BLACK_STRING = ",服务,服务站,";

}
