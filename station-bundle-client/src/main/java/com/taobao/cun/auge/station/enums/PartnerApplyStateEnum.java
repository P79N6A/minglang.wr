package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiao on 16/8/29.
 */
public class PartnerApplyStateEnum implements Serializable {

    public static final PartnerApplyStateEnum STATE_APPLY_WAIT = new PartnerApplyStateEnum("STATE_APPLY_WAIT", "待初审");
    public static final PartnerApplyStateEnum STATE_APPLY_REFUSE = new PartnerApplyStateEnum("STATE_APPLY_REFUSE",
            "初审未通过");// V2.5以前审核拒绝在Ｖ2.5开始要当成初审未通过
    public static final PartnerApplyStateEnum STATE_APPLY_INTERVIEW = new PartnerApplyStateEnum("STATE_APPLY_INTERVIEW",
            "待面试");
    public static final PartnerApplyStateEnum STATE_APPLY_NOTIFY = new PartnerApplyStateEnum("STATE_APPLY_NOTIFY",
            "面试通知已发送");
    public static final PartnerApplyStateEnum STATE_APPLY_SIGNED = new PartnerApplyStateEnum("STATE_APPLY_SIGNED",
            "面试已签到");
    public static final PartnerApplyStateEnum STATE_APPLY_NOT_SIGNED = new PartnerApplyStateEnum(
            "STATE_APPLY_NOT_SIGNED", "面试未签到");
    public static final PartnerApplyStateEnum STATE_APPLY_REFUSE_INTERVIEW = new PartnerApplyStateEnum(
            "STATE_APPLY_REFUSE_INTERVIEW", "面试未通过");
    public static final PartnerApplyStateEnum STATE_APPLY_SUCC = new PartnerApplyStateEnum("STATE_APPLY_SUCC", "面试通过");// V2.5以前审核通过在Ｖ2.5开始要当成面试通过
    public static final PartnerApplyStateEnum STATE_APPLY_COOPERATION = new PartnerApplyStateEnum(
            "STATE_APPLY_COOPERATION", "已合作"); // 已签约

    private static final Map<String, PartnerApplyStateEnum> mappings = new HashMap<String, PartnerApplyStateEnum>();

    static {
        mappings.put("STATE_APPLY_WAIT", STATE_APPLY_WAIT);
        mappings.put("STATE_APPLY_REFUSE", STATE_APPLY_REFUSE);
        mappings.put("STATE_APPLY_INTERVIEW", STATE_APPLY_INTERVIEW);
        mappings.put("STATE_APPLY_NOTIFY", STATE_APPLY_NOTIFY);
        mappings.put("STATE_APPLY_SIGNED", STATE_APPLY_SIGNED);
        mappings.put("STATE_APPLY_NOT_SIGNED", STATE_APPLY_NOT_SIGNED);
        mappings.put("STATE_APPLY_SUCC", STATE_APPLY_SUCC);
        mappings.put("STATE_APPLY_REFUSE_INTERVIEW", STATE_APPLY_REFUSE_INTERVIEW);
        mappings.put("STATE_APPLY_COOPERATION", STATE_APPLY_COOPERATION);
    }

    public static List<PartnerApplyStateEnum> enums() {
        List<PartnerApplyStateEnum> enums = new ArrayList<PartnerApplyStateEnum>();
        enums.add(STATE_APPLY_WAIT);
        enums.add(STATE_APPLY_REFUSE);
        enums.add(STATE_APPLY_INTERVIEW);
        enums.add(STATE_APPLY_NOTIFY);
        enums.add(STATE_APPLY_SIGNED);
        enums.add(STATE_APPLY_NOT_SIGNED);
        enums.add(STATE_APPLY_SUCC);
        enums.add(STATE_APPLY_REFUSE_INTERVIEW);
        enums.add(STATE_APPLY_COOPERATION);
        return enums;
    }

    /**
     * 可以面试的状态
     *
     * @return
     */
    public static List<PartnerApplyStateEnum> getCanInerviewStates() {
        List<PartnerApplyStateEnum> enums = new ArrayList<PartnerApplyStateEnum>();
        enums.add(STATE_APPLY_INTERVIEW);
        enums.add(STATE_APPLY_NOTIFY);
        enums.add(STATE_APPLY_NOT_SIGNED);
        enums.add(STATE_APPLY_SIGNED);
        return enums;
    }

    public static String[] getCanInterviewStateCodes() {
        List<PartnerApplyStateEnum> states = PartnerApplyStateEnum.getCanInerviewStates();
        List<String> codes = new ArrayList<String>(states.size());
        for (PartnerApplyStateEnum state : states) {
            codes.add(state.getCode());
        }
        return codes.toArray(new String[codes.size()]);
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
        if (obj == null)
            return false;
        if (!(obj instanceof PartnerApplyStateEnum))
            return false;
        PartnerApplyStateEnum objType = (PartnerApplyStateEnum) obj;
        return objType.getCode().equals(this.getCode());
    }

    public static PartnerApplyStateEnum valueof(String code) {
        if (code == null)
            return null;
        return mappings.get(code);
    }

    public PartnerApplyStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public PartnerApplyStateEnum() {
    }
}
