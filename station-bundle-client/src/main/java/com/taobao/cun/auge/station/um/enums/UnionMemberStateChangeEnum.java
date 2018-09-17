package com.taobao.cun.auge.station.um.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UnionMemberStateChangeEnum implements Serializable {

    private static final long serialVersionUID = -722700951409528045L;

    // 状态变更类型
    private ChangeEnum type;
    // 描述
    private String description;
    // 变更后合伙人实例状态
    private UnionMemberStateEnum unionMemberState;
    // 变更前合伙人实例状态
    private UnionMemberStateEnum preUnionMemberState;

    public enum ChangeEnum {
        START_SETTLING,
        START_SERVICING,
        CLOSED,
        CLOSE_TO_SERVICE
    }

    public static final UnionMemberStateChangeEnum
        START_SERVICING = new UnionMemberStateChangeEnum(
        ChangeEnum.START_SERVICING,
        "进入服务中 : '未开通'-> '已开通'", UnionMemberStateEnum.SERVICING, UnionMemberStateEnum.SETTLING);

    public static final UnionMemberStateChangeEnum
        CLOSED = new UnionMemberStateChangeEnum(
        ChangeEnum.CLOSED,
        "关闭 : '已开通' -> '已关闭'", UnionMemberStateEnum.CLOSED, UnionMemberStateEnum.SERVICING);

    public static final UnionMemberStateChangeEnum
        CLOSE_TO_SERVICE = new UnionMemberStateChangeEnum(
        ChangeEnum.CLOSE_TO_SERVICE,
        "重新开通 :'已关闭' -> '已开通'", UnionMemberStateEnum.SERVICING, UnionMemberStateEnum.CLOSED);

    private static final Map<UnionMemberStateChangeEnum.ChangeEnum, UnionMemberStateChangeEnum>
        mappings = new HashMap<UnionMemberStateChangeEnum.ChangeEnum, UnionMemberStateChangeEnum>();

    static {
        mappings.put(UnionMemberStateChangeEnum.ChangeEnum.START_SERVICING, START_SERVICING);
        mappings.put(UnionMemberStateChangeEnum.ChangeEnum.CLOSED, CLOSED);
        mappings.put(UnionMemberStateChangeEnum.ChangeEnum.CLOSE_TO_SERVICE, CLOSE_TO_SERVICE);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UnionMemberStateEnum getUnionMemberState() {
        return unionMemberState;
    }

    public void setUnionMemberState(UnionMemberStateEnum unionMemberState) {
        this.unionMemberState = unionMemberState;
    }

    public UnionMemberStateEnum getPreUnionMemberState() {
        return preUnionMemberState;
    }

    public void setPreUnionMemberState(UnionMemberStateEnum preUnionMemberState) {
        this.preUnionMemberState = preUnionMemberState;
    }

    public UnionMemberStateChangeEnum.ChangeEnum getType() {
        return type;
    }

    public void setType(UnionMemberStateChangeEnum.ChangeEnum type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UnionMemberStateChangeEnum)) {
            return false;
        }
        UnionMemberStateChangeEnum
            objType = (UnionMemberStateChangeEnum)obj;
        return objType.getType().equals(this.getType());
    }

    public UnionMemberStateChangeEnum(ChangeEnum type, String description, UnionMemberStateEnum unionMemberState,
                                      UnionMemberStateEnum preUnionMemberState) {
        this.type = type;
        this.description = description;
        this.unionMemberState = unionMemberState;
        this.preUnionMemberState = preUnionMemberState;
    }

    public static UnionMemberStateChangeEnum valueof(
        UnionMemberStateChangeEnum.ChangeEnum type) {
        if (type == null) {
            return null;
        }
        return mappings.get(type);
    }

    public UnionMemberStateChangeEnum() {

    }
}
