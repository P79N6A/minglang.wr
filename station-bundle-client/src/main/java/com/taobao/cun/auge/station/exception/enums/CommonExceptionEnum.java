package com.taobao.cun.auge.station.exception.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommonExceptionEnum implements Serializable {

	protected static final Map<String, CommonExceptionEnum> mappings = new HashMap<String, CommonExceptionEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    protected String code;
    protected String desc;

    public static final CommonExceptionEnum PARAM_IS_NULL = new CommonExceptionEnum("PARAM_IS_NULL", "参数不能为空");
    public static final CommonExceptionEnum SYSTEM_ERROR = new CommonExceptionEnum("SYSTEM_ERROR", "系统异常");
    public static final CommonExceptionEnum OPERATOR_IS_NULL = new CommonExceptionEnum("OPERATOR_IS_NULL", "操作人不能为空");
    public static final CommonExceptionEnum OPERATORTYPE_IS_NULL = new CommonExceptionEnum("OPERATORTYPE_IS_NULL", "操作人类型不能为空");
    public static final CommonExceptionEnum OPERATORORGID_IS_NULL = new CommonExceptionEnum("OPERATORORGID_IS_NULL", "操作人组织不能为空");

    static {
        mappings.put("PARAM_IS_NULL", PARAM_IS_NULL);
        mappings.put("SYSTEM_ERROR", SYSTEM_ERROR);
        mappings.put("OPERATOR_IS_NULL", OPERATOR_IS_NULL);
        mappings.put("OPERATORTYPE_IS_NULL", OPERATORTYPE_IS_NULL);
        mappings.put("OPERATORORGID_IS_NULL", OPERATORORGID_IS_NULL);
    }

    public CommonExceptionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public CommonExceptionEnum() {

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

    public static CommonExceptionEnum valueof(String code) {
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
        CommonExceptionEnum other = (CommonExceptionEnum) obj;
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
