package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class PartnerSopRltDto implements Serializable {

    private static final long serialVersionUID = -5458194521053835714L;

    /**
     * 招募状态描述
     */
    private String partnerApplyStateDesc;

    /**
     * 村小二实例状态描述
     */
    private String partnerIsntanceStateDesc;

    /**
     * 县小二名字
     */
    private String CountyLeaderName;

    /**
     * 区域经理名字
     */
    private String teamLeaderName;

    public String getPartnerApplyStateDesc() {
        return partnerApplyStateDesc;
    }

    public void setPartnerApplyStateDesc(String partnerApplyStateDesc) {
        this.partnerApplyStateDesc = partnerApplyStateDesc;
    }

    public String getPartnerIsntanceStateDesc() {
        return partnerIsntanceStateDesc;
    }

    public void setPartnerIsntanceStateDesc(String partnerIsntanceStateDesc) {
        this.partnerIsntanceStateDesc = partnerIsntanceStateDesc;
    }

    public String getCountyLeaderName() {
        return CountyLeaderName;
    }

    public void setCountyLeaderName(String countyLeaderName) {
        CountyLeaderName = countyLeaderName;
    }

    public String getTeamLeaderName() {
        return teamLeaderName;
    }

    public void setTeamLeaderName(String teamLeaderName) {
        this.teamLeaderName = teamLeaderName;
    }
}
