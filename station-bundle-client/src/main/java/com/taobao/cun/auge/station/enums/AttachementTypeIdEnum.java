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
    
    private static final Map<Long, AttachementTypeIdEnum> mappings = new HashMap<Long, AttachementTypeIdEnum>();
    

    public static final AttachementTypeIdEnum IDCARD_IMG = new AttachementTypeIdEnum(1L, "身份证扫描件");
	public static final AttachementTypeIdEnum CURRENT_STATUS_IMG = new AttachementTypeIdEnum(2L, "现状照片");
	public static final AttachementTypeIdEnum AUDIT_IMG = new AttachementTypeIdEnum(3L, "审批文件");
	
	public static final AttachementTypeIdEnum RENTAL_PROTOCOL = new AttachementTypeIdEnum(5L, "租赁协议");
	public static final AttachementTypeIdEnum LESSOR_IDCARD = new AttachementTypeIdEnum(6L, "出租人身份证");
	public static final AttachementTypeIdEnum PROPERTY_PROVE = new AttachementTypeIdEnum(7L, "房产证明");
	public static final AttachementTypeIdEnum GOV_PROTOCOL = new AttachementTypeIdEnum(8L, "政府协议");


    static {
        mappings.put(1L, IDCARD_IMG);
        mappings.put(2L, CURRENT_STATUS_IMG);
        mappings.put(3L, AUDIT_IMG);
        mappings.put(5L, RENTAL_PROTOCOL);
        mappings.put(6L, LESSOR_IDCARD);
        mappings.put(7L, PROPERTY_PROVE);
        mappings.put(8L, GOV_PROTOCOL);
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
