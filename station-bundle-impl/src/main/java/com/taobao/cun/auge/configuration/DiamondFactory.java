package com.taobao.cun.auge.configuration;

/**
 * Created by xiao on 18/11/5.
 */
public class DiamondFactory {

    private static final String TRANS_DATA_ID = "trans";

    private static final String TRANS_GROUP_ID = "whiteList";

    private static final String INSURANCE_DATA_ID = "insurance";

    private static final String INSURANCE_GROUP_ID = "whiteList";

    public static String getTransDiamondConfig() {
        return getDiamondConfig(TRANS_GROUP_ID, TRANS_DATA_ID);
    }

    public static String getInsuranceDiamondConfig() {
        return getDiamondConfig(INSURANCE_GROUP_ID, INSURANCE_DATA_ID);
    }

    private static String getDiamondConfig(String groupId, String dataId) {
        return DiamondHelper.getConfig(groupId, dataId);
    }

}
