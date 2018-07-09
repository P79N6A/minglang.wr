package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 县服务中心 经营状态枚举
 * @author quanzhu.wangqz
 *
 */
public class CountyStationManageStatusEnum implements Serializable {
	private static final Map<String, CountyStationManageStatusEnum> mappings = new HashMap<String, CountyStationManageStatusEnum>();


    private static final long serialVersionUID = -2325045809951918393L;

    private String code;
    private String desc;

	public static final CountyStationManageStatusEnum PLANNING = new CountyStationManageStatusEnum("PLANNING", "筹划中");
	public static final CountyStationManageStatusEnum OPERATING = new CountyStationManageStatusEnum("OPERATING", "运营中");
	public static final CountyStationManageStatusEnum STOP_OPERATION = new CountyStationManageStatusEnum(	"STOP_OPERATION", "停止运营");

    static {
    	mappings.put("PLANNING", PLANNING);
		mappings.put("OPERATING", OPERATING);
		mappings.put("STOP_OPERATION", STOP_OPERATION);
    }

    public CountyStationManageStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public CountyStationManageStatusEnum() {

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

    public static CountyStationManageStatusEnum valueof(String code) {
        if (code == null) {
            return null;
        }
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CountyStationManageStatusEnum other = (CountyStationManageStatusEnum) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }
}
