package com.taobao.cun.auge.configuration;

/**
 * Created by xiao on 18/11/5.
 */
public class DiamondFactory {

    private static final String TRANS_DATA_ID = "trans";

    private static final String TRANS_GROUP_ID = "whiteList";

    public static String getTransDiamondConfig() {
        return getDiamondConfig(TRANS_GROUP_ID, TRANS_DATA_ID);
    }

    private static String getDiamondConfig(String groupId, String dataId) {
        return DiamondHelper.getConfig(groupId, dataId);
    }

}
