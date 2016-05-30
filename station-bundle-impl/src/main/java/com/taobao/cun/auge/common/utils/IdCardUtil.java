package com.taobao.cun.auge.common.utils;

import org.apache.commons.lang.StringUtils;

public class IdCardUtil {
    public static final String idCardNoHide(String idCard) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(idCard) && idCard.length() > 2) {
            String start = idCard.charAt(0) + "";
            String end = idCard.charAt(idCard.length() - 1) + "";

            sb.append(start);
            for (int i = 0; i < idCard.length() - 2; i++) {
                sb.append("*");
            }
            sb.append(end);
        }
        return sb.toString();
    }
}
