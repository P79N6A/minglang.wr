package com.taobao.cun.auge.fence.dto;

import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * Created by xiao on 18/7/4.
 */
public class FenceBatchOpDto extends OperatorDto {

    /**
     * 批量覆盖(关闭站点原有相同类型围栏)
     */
    public static final String BATCH_OVERRIDE = "BATCH_OVERRIDE";

    /**
     * 批量叠加(保留站点原围栏)
     */
    public static final String BATCH_NEW = "BATCH_NEW";

    /**
     * 批量解除站点及围栏关系
     */
    public static final String BATCH_DELETE = "BATCH_DELETE";

    private List<Long> templateIdList;

    private FenceBatchOpCondition condition;

    private String opType;

    public List<Long> getTemplateIdList() {
        return templateIdList;
    }

    public void setTemplateIdList(List<Long> templateIdList) {
        this.templateIdList = templateIdList;
    }

    public FenceBatchOpCondition getCondition() {
        return condition;
    }

    public void setCondition(FenceBatchOpCondition condition) {
        this.condition = condition;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    class FenceBatchOpCondition {

        private Long orgId;

        private String province;

        private String city;

        private String county;

        private String town;

        private String stationType;

        private String isOnTown;

        private String stationStatus;

        public Long getOrgId() {
            return orgId;
        }

        public void setOrgId(Long orgId) {
            this.orgId = orgId;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getStationType() {
            return stationType;
        }

        public void setStationType(String stationType) {
            this.stationType = stationType;
        }

        public String getIsOnTown() {
            return isOnTown;
        }

        public void setIsOnTown(String isOnTown) {
            this.isOnTown = isOnTown;
        }

        public String getStationStatus() {
            return stationStatus;
        }

        public void setStationStatus(String stationStatus) {
            this.stationStatus = stationStatus;
        }
    }

}
