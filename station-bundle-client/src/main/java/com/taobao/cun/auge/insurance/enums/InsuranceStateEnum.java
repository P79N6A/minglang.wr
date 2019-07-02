package com.taobao.cun.auge.insurance.enums;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class InsuranceStateEnum implements Serializable {

  /*
       * ��ʼ INIT(0),
     * �ѳ���  ISSUED(1),
     * �˱���ֹ SURRENDERRED(2),
     * ��ȡ�� CANNELLED(3),
     * ��֧�� PAID(4),
     * ������ֹ EXPIRED(5),
     * �Ѻ˱� UNDERWROTE(8),
     * ����Ч EFFECTED(9),
     * ���˿� REFUNDED(10),
*/

    private Integer code;

    private String desc;

    public static final InsuranceStateEnum insuranceStateINIT = new InsuranceStateEnum(0, "INIT");
    public static final InsuranceStateEnum insuranceStateISSUED = new InsuranceStateEnum(1, "ISSUED");
    public static final InsuranceStateEnum insuranceStateSURRENDERRED = new InsuranceStateEnum(2, "SURRENDERRED");
    public static final InsuranceStateEnum insuranceStateCANNELLED = new InsuranceStateEnum(3, "CANNELLED");
    public static final InsuranceStateEnum insuranceStatePAID = new InsuranceStateEnum(4, "PAID");
    public static final InsuranceStateEnum insuranceStateEXPIRED = new InsuranceStateEnum(5, "EXPIRED");
    public static final InsuranceStateEnum insuranceStateUNDERWROTE = new InsuranceStateEnum(8, "UNDERWROTE");
    public static final InsuranceStateEnum insuranceStateEFFECTED = new InsuranceStateEnum(9, "EFFECTED");
    public static final InsuranceStateEnum insuranceStateREFUNDED = new InsuranceStateEnum(10, "REFUNDED");

    private static final Map<Integer, InsuranceStateEnum> mappings = new HashMap<Integer, InsuranceStateEnum>();

    static {
        mappings.put(0, insuranceStateINIT);
        mappings.put(1, insuranceStateISSUED);
        mappings.put(2, insuranceStateSURRENDERRED);
        mappings.put(3, insuranceStateCANNELLED);
        mappings.put(4, insuranceStatePAID);
        mappings.put(5, insuranceStateEXPIRED);
        mappings.put(8, insuranceStateUNDERWROTE);
        mappings.put(9, insuranceStateEFFECTED);
        mappings.put(10, insuranceStateREFUNDED);

    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private InsuranceStateEnum() {

    }

    private InsuranceStateEnum(Integer code,String desc) {

    }

    public static InsuranceStateEnum valueof(Integer code) {
        if (code == null) {
            return null;
        }
        return mappings.get(code);
    }


}
