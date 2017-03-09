package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 附件表 类型id枚举
 * @author quanzhu.wangqz
 *
 */
public class AttachementTypeIdEnum implements Serializable {

    private static final long serialVersionUID = -2325045809951918493L;
    
    public static final Map<Long, AttachementTypeIdEnum> mappings = new HashMap<Long, AttachementTypeIdEnum>();
    

    public static final AttachementTypeIdEnum IDCARD_IMG = new AttachementTypeIdEnum(1L, "身份证扫描件");
	public static final AttachementTypeIdEnum CURRENT_STATUS_IMG = new AttachementTypeIdEnum(2L, "现状照片");
	public static final AttachementTypeIdEnum AUDIT_IMG = new AttachementTypeIdEnum(3L, "审批文件");
	
	public static final AttachementTypeIdEnum RENTAL_PROTOCOL = new AttachementTypeIdEnum(5L, "租赁协议");
	public static final AttachementTypeIdEnum LESSOR_IDCARD = new AttachementTypeIdEnum(6L, "出租人身份证");
	public static final AttachementTypeIdEnum PROPERTY_PROVE = new AttachementTypeIdEnum(7L, "房产证明");
	public static final AttachementTypeIdEnum GOV_PROTOCOL = new AttachementTypeIdEnum(8L, "政府协议");
	public static final AttachementTypeIdEnum TPA_OPERATION_STUFF = new AttachementTypeIdEnum(9L, "淘帮手经营材料");
	
	public static final AttachementTypeIdEnum STATION_DECORATE_SYYJ = new AttachementTypeIdEnum(10L, "服务站装修记录-室外远景");
	public static final AttachementTypeIdEnum STATION_DECORATE_SYJJ = new AttachementTypeIdEnum(11L, "服务站装修记录-室外近景");
	public static final AttachementTypeIdEnum STATION_DECORATE_SNT = new AttachementTypeIdEnum(12L, "服务站装修记录-室内图");
    public static final AttachementTypeIdEnum STATION_DECORATE_FRAME = new AttachementTypeIdEnum(13L, "服务站装修记录-框架图");
    public static final AttachementTypeIdEnum STATION_DECORATE_LOGO = new AttachementTypeIdEnum(14L, "服务站装修记录-LOGO墙图");

	public static final AttachementTypeIdEnum WISDOM_COUNTY_APPLY = new AttachementTypeIdEnum(15L, "报名表");
	public static final AttachementTypeIdEnum WISDOM_COUNTY_FUNCTION = new AttachementTypeIdEnum(16L, "服务功能确认表");

	public static final AttachementTypeIdEnum CUNGOV_CONSULT_IMAGE = new AttachementTypeIdEnum(17L, "举报投诉照片");

	public static final AttachementTypeIdEnum INCENTIVE_PROGRAM_COMMON = new AttachementTypeIdEnum(20L, "激励方案通用附件");
	public static final AttachementTypeIdEnum INCENTIVE_PROGRAM_GOVAUDIT = new AttachementTypeIdEnum(21L, "激励方案政府审批附件");

    static {
        mappings.put(1L, IDCARD_IMG);
        mappings.put(2L, CURRENT_STATUS_IMG);
        mappings.put(3L, AUDIT_IMG);
        mappings.put(5L, RENTAL_PROTOCOL);
        mappings.put(6L, LESSOR_IDCARD);
        mappings.put(7L, PROPERTY_PROVE);
        mappings.put(8L, GOV_PROTOCOL);
        mappings.put(9L, TPA_OPERATION_STUFF);
        
        mappings.put(10L, STATION_DECORATE_SYYJ);
        mappings.put(11L, STATION_DECORATE_SYJJ);
        mappings.put(12L, STATION_DECORATE_SNT);
        mappings.put(13L, STATION_DECORATE_FRAME);
        mappings.put(14L, STATION_DECORATE_LOGO);
        
        mappings.put(15L, WISDOM_COUNTY_APPLY);
        mappings.put(16L, WISDOM_COUNTY_FUNCTION);

        mappings.put(17L, CUNGOV_CONSULT_IMAGE);

        mappings.put(20L, INCENTIVE_PROGRAM_COMMON);
        mappings.put(21L, INCENTIVE_PROGRAM_GOVAUDIT);
    }
    
    private Long code;
    private String desc;

    public AttachementTypeIdEnum(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AttachementTypeIdEnum() {

    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static AttachementTypeIdEnum valueof(Long code) {
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
        AttachementTypeIdEnum other = (AttachementTypeIdEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
