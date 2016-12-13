package com.taobao.cun.auge.evaluate.enums;

/**
 * Created by xujianhui on 16/12/13.
 */
public enum TaskNodeAuditStatus {
    AUDIT_PASS,
    AUDIT_REFUSE
    ;

    public static boolean isAudited(String auditStatus) {
        return AUDIT_PASS.name().equals(auditStatus) || AUDIT_REFUSE.name().equals(auditStatus);
    }
}
