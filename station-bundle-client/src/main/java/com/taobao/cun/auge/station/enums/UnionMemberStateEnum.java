package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 优盟状态
 *
 * @author haihu.fhh
 */
public class UnionMemberStateEnum implements Serializable {

    private static final long serialVersionUID = 4887324018125491747L;
    // 已开通
    public static final UnionMemberStateEnum SERVICING = new UnionMemberStateEnum("SERVICING", "已开通");

    // 未开通
    public static final UnionMemberStateEnum CLOSED = new UnionMemberStateEnum("CLOSED", "未开通");

    private static final Map<String, UnionMemberStateEnum> MAPPINGS = new HashMap<String, UnionMemberStateEnum>();

    static {
        MAPPINGS.put("SERVICING", SERVICING);
        MAPPINGS.put("CLOSED", CLOSED);
    }

    private String code;
    private String desc;

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
        if (!(obj instanceof UnionMemberStateEnum)) {
            return false;
        }
        UnionMemberStateEnum objType = (UnionMemberStateEnum)obj;
        return objType.getCode().equals(this.getCode());
    }

    public UnionMemberStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UnionMemberStateEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return MAPPINGS.get(code);
    }
}
