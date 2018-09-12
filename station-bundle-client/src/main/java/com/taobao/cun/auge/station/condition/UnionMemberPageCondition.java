package com.taobao.cun.auge.station.condition;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.UnionMemberStateEnum;

/**
 * 优盟分页查询条件
 *
 * @author haihu.fhh
 */
public class UnionMemberPageCondition extends OperatorDto {

    private static final long serialVersionUID = 5885214194585033618L;

    /**
     * 所属村站id
     */
    private Long parentStationId;

    /**
     * 合作店名称
     */
    private String stationName;

    /**
     * 合作店编号
     */
    private String stationNum;

    /**
     * 合作店状态
     */
    private UnionMemberStateEnum state;

    /**
     * 所属组织path
     */
    private String orgIdPath;

    /**
     * 淘宝nick
     */
    private String taobaoNick;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public UnionMemberStateEnum getState() {
        return state;
    }

    public void setState(UnionMemberStateEnum state) {
        this.state = state;
    }

    public Long getParentStationId() {
        return parentStationId;
    }

    public void setParentStationId(Long parentStationId) {
        this.parentStationId = parentStationId;
    }

    public String getStationNum() {
        return stationNum;
    }

    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
    }

    public String getOrgIdPath() {
        return orgIdPath;
    }

    public void setOrgIdPath(String orgIdPath) {
        this.orgIdPath = orgIdPath;
    }

    public String getTaobaoNick() {
        return taobaoNick;
    }

    public void setTaobaoNick(String taobaoNick) {
        this.taobaoNick = taobaoNick;
    }
}
