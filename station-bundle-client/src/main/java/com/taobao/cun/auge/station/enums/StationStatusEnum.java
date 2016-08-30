package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haihu.fhh on 2016/5/4.
 */
public class StationStatusEnum implements Serializable {

    private static final long serialVersionUID = -1197977282708260567L;

    // 暂存
    public static final StationStatusEnum TEMP = new StationStatusEnum("TEMP", "暂存");

    // 新
    public static final StationStatusEnum NEW = new StationStatusEnum("NEW", "新");
    
    //作废
    public static final StationStatusEnum INVALID  = new StationStatusEnum("INVALID", "无效");

    

    // 装修中 当冻结资金完成之后，村点申请单状态变为装修中，当装修完成之后，需要小二点击开业，才进入服务中
    public static final StationStatusEnum DECORATING = new StationStatusEnum("DECORATING", "装修中");

    // 服务中
    public static final StationStatusEnum SERVICING = new StationStatusEnum("SERVICING", "服务中");

    // 停业申请中
    public static final StationStatusEnum CLOSING = new StationStatusEnum("CLOSING", "停业申请中");

    // 已停业
    public static final StationStatusEnum CLOSED = new StationStatusEnum("CLOSED", "已停业");

    // 退出申请中
    public static final StationStatusEnum QUITING = new StationStatusEnum("QUITING", "退出申请中");

    // 已退出
    public static final StationStatusEnum QUIT = new StationStatusEnum("QUIT", "已退出");

    private static final Map<String, StationStatusEnum> mappings = new HashMap<String, StationStatusEnum>();

    static {
        mappings.put("TEMP", TEMP);
        mappings.put("NEW", NEW);
        mappings.put("INVALID", INVALID);
        mappings.put("DECORATING", DECORATING);
        mappings.put("SERVICING", SERVICING);
        mappings.put("CLOSING", CLOSING);
        mappings.put("CLOSED", CLOSED);
        mappings.put("QUITING", QUITING);
        mappings.put("QUIT", QUIT);
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
        if (!(obj instanceof StationStatusEnum))
            return false;
        StationStatusEnum objType = (StationStatusEnum) obj;
        return objType.getCode().equals(this.getCode());
    }

    public StationStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static StationStatusEnum valueof(String code) {
        if (code == null)
            return null;
        return mappings.get(code);
    }

    /**
     * 获取有效的村点状态，
     *
     * @return
     */
    public static String[] getValidStatusArray() {
        ArrayList<String> states = new ArrayList<String>();
        states.add(NEW.getCode());
        states.add(DECORATING.getCode());
        states.add(SERVICING.getCode());
        states.add(CLOSING.getCode());
        states.add(CLOSED.getCode());
        states.add(QUITING.getCode());
        return states.toArray(new String[0]);
    }

    /**
     * 获取有效的村点状态，
     *
     * @return
     */
    public static List<StationStatusEnum> getValidStatusList() {
        List<StationStatusEnum> statuses = new ArrayList<StationStatusEnum>();
        statuses.add(NEW);
        statuses.add(DECORATING);
        statuses.add(SERVICING);
        statuses.add(CLOSING);
        statuses.add(CLOSED);
        statuses.add(QUITING);
        return statuses;
    }

    /**
     * 获取村点开始服务之后的有效状态，
     *
     * @return
     */
    public static List<StationStatusEnum> getInServiceStatusList() {
        List<StationStatusEnum> statuses = new ArrayList<StationStatusEnum>();
        statuses.add(DECORATING);
        statuses.add(SERVICING);
        statuses.add(CLOSING);
        statuses.add(CLOSED);
        statuses.add(QUITING);
        return statuses;
    }

    /**
     * 获取村点开始服务之后的有效状态
     *
     * @return
     */
    public static String[] getInServiceStatusArray() {
        ArrayList<String> statuses = new ArrayList<String>();
        statuses.add(DECORATING.getCode());
        statuses.add(SERVICING.getCode());
        statuses.add(CLOSING.getCode());
        statuses.add(CLOSED.getCode());
        statuses.add(QUITING.getCode());
        return statuses.toArray(new String[0]);
    }

    /**
     * 获取可以删除的状态
     *
     * @return
     */
    public static List<String> getCanDeleteStatus() {
        List<String> listValidStatus = new ArrayList<String>();

        listValidStatus.add(TEMP.getCode());

        return listValidStatus;
    }

    /**
     * 获取可以重新入驻的状态
     *
     * @return
     */
    public static List<String> getCanReSettledStatuses() {
        List<String> statuses = new ArrayList<String>();

        statuses.add(CLOSED.getCode());

        return statuses;
    }

    /**
     * 获取所有的状态
     *
     * @return
     */
    public static List<StationStatusEnum> getAllStatusesList() {
        ArrayList<StationStatusEnum> statuses = new ArrayList<StationStatusEnum>();
        statuses.add(TEMP);
        statuses.add(NEW);
        statuses.add(DECORATING);
        statuses.add(SERVICING);
        statuses.add(CLOSING);
        statuses.add(CLOSED);
        statuses.add(QUITING);
        statuses.add(QUIT);

        return statuses;
    }

    /**
     * 获取所有的状态
     *
     * @return
     */
    public static String[] getAllStatusArray() {
        ArrayList<String> statuses = new ArrayList<String>();

        statuses.add(TEMP.getCode());
        statuses.add(NEW.getCode());
        statuses.add(DECORATING.getCode());
        statuses.add(SERVICING.getCode());
        statuses.add(CLOSING.getCode());
        statuses.add(CLOSED.getCode());
        statuses.add(QUITING.getCode());
        statuses.add(QUIT.getCode());

        return statuses.toArray(new String[0]);
    }

    @SuppressWarnings("unused")
    private StationStatusEnum() {

    }
}
