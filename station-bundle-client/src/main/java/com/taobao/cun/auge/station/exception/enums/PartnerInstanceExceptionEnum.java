package com.taobao.cun.auge.station.exception.enums;

import java.util.HashMap;
import java.util.Map;

public class PartnerInstanceExceptionEnum extends CommonExceptionEnum  {

	protected static final Map<String, PartnerInstanceExceptionEnum> mappings = new HashMap<String, PartnerInstanceExceptionEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    protected String code;
    protected String desc;
    
    public static final PartnerInstanceExceptionEnum DEGRADE_PARTNER_TYPE_FAIL = new PartnerInstanceExceptionEnum("DEGRADE_PARTNER_TYPE_FAIL", "请选择合伙人进行降级操作");
    public static final PartnerInstanceExceptionEnum DEGRADE_PARTNER_STATE_FAIL = new PartnerInstanceExceptionEnum("DEGRADE_PARTNER_STATE_FAIL", "只有状态为(装修中，服务中，停业申请中，已停业)才能降级");
    public static final PartnerInstanceExceptionEnum DEGRADE_PARTNER_HAS_TPA = new PartnerInstanceExceptionEnum("DEGRADE_PARTNER_HAS_TPA", "该合伙人下面还有淘帮手，请解绑所有淘帮手再做降级");
    public static final PartnerInstanceExceptionEnum DEGRADE_PARTNER_TAOBAOUSERID_ERROR = new PartnerInstanceExceptionEnum("DEGRADE_PARTNER_TAOBAOUSERID_ERROR", "淘宝账号输入有误，请检查确认后再做降级");
    public static final PartnerInstanceExceptionEnum DEGRADE_PARTNER_TAOBAOUSERID_SAME = new PartnerInstanceExceptionEnum("DEGRADE_PARTNER_TAOBAOUSERID_SAME", "需要降级的合伙人与归属合伙人不能为同一人");
    public static final PartnerInstanceExceptionEnum DEGRADE_TARGET_PARTNER_NOT_SERVICING = new PartnerInstanceExceptionEnum("DEGRADE_TARGET_PARTNER_NOT_SERVICING", "归属合伙人状态不是服务中，请确认后再做降级");
    public static final PartnerInstanceExceptionEnum DEGRADE_TARGET_PARTNER_TYPE_NOT_TP = new PartnerInstanceExceptionEnum("DEGRADE_TARGET_PARTNER_TYPE_NOT_TP", "归属合伙人类型必须为合伙人");
    public static final PartnerInstanceExceptionEnum DEGRADE_TARGET_PARTNER_HAS_TPA_MAX = new PartnerInstanceExceptionEnum("DEGRADE_TARGET_PARTNER_HAS_TPA_MAX", "所归属的合伙人的淘帮手超过限额，不允许绑定");
    public static final PartnerInstanceExceptionEnum DEGRADE_PARTNER_ORG_NOT_SAME = new PartnerInstanceExceptionEnum("DEGRADE_PARTNER_ORG_NOT_SAME", "需要降级的合伙人与归属合伙人组织不在同一个县，请检查确认后再做降级");
    
	public static final PartnerInstanceExceptionEnum PARTNER_INSTANCE_STATUS_CHANGED = new PartnerInstanceExceptionEnum(
			"PARTNER_INSTANCE_STATUS_CHANGED", "合伙人状态已变更");
	
    static {
        mappings.put("DEGRADE_PARTNER_TYPE_FAIL", DEGRADE_PARTNER_TYPE_FAIL);
        mappings.put("DEGRADE_PARTNER_STATE_FAIL", DEGRADE_PARTNER_STATE_FAIL);
        mappings.put("DEGRADE_PARTNER_HAS_TPA", DEGRADE_PARTNER_HAS_TPA);
        mappings.put("DEGRADE_PARTNER_TAOBAOUSERID_ERROR", DEGRADE_PARTNER_TAOBAOUSERID_ERROR);
        mappings.put("DEGRADE_PARTNER_TAOBAOUSERID_SAME", DEGRADE_PARTNER_TAOBAOUSERID_SAME);
        mappings.put("DEGRADE_TARGET_PARTNER_NOT_SERVICING", DEGRADE_TARGET_PARTNER_NOT_SERVICING);
        mappings.put("DEGRADE_TARGET_PARTNER_TYPE_NOT_TP", DEGRADE_TARGET_PARTNER_TYPE_NOT_TP);
        mappings.put("DEGRADE_TARGET_PARTNER_HAS_TPA_MAX", DEGRADE_TARGET_PARTNER_HAS_TPA_MAX);
        mappings.put("DEGRADE_PARTNER_ORG_NOT_SAME", DEGRADE_PARTNER_ORG_NOT_SAME);
        mappings.put("PARTNER_INSTANCE_STATUS_CHANGED", PARTNER_INSTANCE_STATUS_CHANGED);
    }

    public PartnerInstanceExceptionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public PartnerInstanceExceptionEnum() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static PartnerInstanceExceptionEnum valueof(String code) {
        if (code == null)
            return null;
        return mappings.get(code);
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PartnerInstanceExceptionEnum other = (PartnerInstanceExceptionEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
    	return this.code +"|" +this.desc;
    }
}
