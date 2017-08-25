package com.taobao.cun.auge.flowRecord.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CuntaoFlowRecordTargetTypeEnum implements Serializable {
	
	private static final long serialVersionUID = 1173751851665155410L;
	
	private String code;
	private String desc;
	
	public static final CuntaoFlowRecordTargetTypeEnum STATION_QUIT = new CuntaoFlowRecordTargetTypeEnum("stationQuitRecord","村点退出");
	public static final CuntaoFlowRecordTargetTypeEnum STATION_STATUS_CHANGE = new CuntaoFlowRecordTargetTypeEnum("stationStatusChange","村点状态转移");
	public static final CuntaoFlowRecordTargetTypeEnum STATION_FORCED_CLOSURE = new CuntaoFlowRecordTargetTypeEnum("stationForcedClosure","村点强制清退");
	public static final CuntaoFlowRecordTargetTypeEnum ASSET_SIGN = new CuntaoFlowRecordTargetTypeEnum("assetSign","村点资产签收");
	public static final CuntaoFlowRecordTargetTypeEnum ASSET_CHECK = new CuntaoFlowRecordTargetTypeEnum("assetCheck","村点资产盘点");
	//资产申请business code
	public static final CuntaoFlowRecordTargetTypeEnum ASSET_APPLY = new CuntaoFlowRecordTargetTypeEnum("assetApply","县资产申请");
	
	public static final CuntaoFlowRecordTargetTypeEnum ASSET_FLOW_EDIT = new CuntaoFlowRecordTargetTypeEnum("assetFlowEdit","县资产申请单修改");
	
	//合伙人申请信息数据导出流程  business code
	public static final CuntaoFlowRecordTargetTypeEnum PARTNER_APPLY_DOWNLOAD = new CuntaoFlowRecordTargetTypeEnum("partnerApplyDownload","合伙人申请信息数据导出");
	
	public static final CuntaoFlowRecordTargetTypeEnum STATION = new CuntaoFlowRecordTargetTypeEnum("STATION","村点日志");
	
	public static final CuntaoFlowRecordTargetTypeEnum PARTNER_INSTANCE = new CuntaoFlowRecordTargetTypeEnum("PARTNER_INSTANCE","合伙人实例");

	public static final CuntaoFlowRecordTargetTypeEnum WISDOM_COUNTY_APPLY = new CuntaoFlowRecordTargetTypeEnum("WISDOM_COUNTY_APPLY","智慧县域报名");
	
	public static final CuntaoFlowRecordTargetTypeEnum SANTONG_DZWL = new CuntaoFlowRecordTargetTypeEnum("SANTONG_DZWL","三通电子围栏");

	public static final CuntaoFlowRecordTargetTypeEnum ASSET_BUY = new CuntaoFlowRecordTargetTypeEnum("ASSET_BUY","资产回购标记");

	public static final Map<String, CuntaoFlowRecordTargetTypeEnum> mappings = new HashMap<String, CuntaoFlowRecordTargetTypeEnum>();
	
	static {
		mappings.put("stationQuit", STATION_QUIT);
		mappings.put("stationStatusChange", STATION_STATUS_CHANGE);
		mappings.put("stationForcedClosure", STATION_FORCED_CLOSURE);
		mappings.put("assetSign", ASSET_SIGN);
		mappings.put("assetCheck", ASSET_CHECK);
		mappings.put("ASSET_FLOW_EDIT", ASSET_FLOW_EDIT);
		mappings.put("assetApply", ASSET_APPLY);
		mappings.put("partnerApplyDownload", PARTNER_APPLY_DOWNLOAD);
		mappings.put("STATION", STATION);
		mappings.put("PARTNER_INSTANCE", PARTNER_INSTANCE);		
		mappings.put("WISDOM_COUNTY_APPLY", WISDOM_COUNTY_APPLY);
		mappings.put("SANTONG_DZWL", SANTONG_DZWL);
		mappings.put("ASSET_BUY", ASSET_BUY);
	}

	private CuntaoFlowRecordTargetTypeEnum() {}
	
	private CuntaoFlowRecordTargetTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
		if (!(obj instanceof CuntaoFlowRecordTargetTypeEnum)) {
            return false;
        }
		CuntaoFlowRecordTargetTypeEnum objType = (CuntaoFlowRecordTargetTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public static CuntaoFlowRecordTargetTypeEnum valueof(String code) {
		if (code==null) {
            return null;
        }
		return mappings.get(code);
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}