package com.taobao.cun.auge.fence.dto.job;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * 菜鸟站点（县仓、菜鸟站点）停业，要关闭相关围栏
 *
 * @author chengyu.zhoucy
 */
public class CainiaoStationCloseFenceInstanceJob extends FenceInstanceJob{
    @NotEmpty(message="围栏类型不能为空")
    private List<String> fenceTypes;

    public List<String> getFenceTypes() {
        return fenceTypes;
    }

    public void setFenceTypes(List<String> fenceTypes) {
        this.fenceTypes = fenceTypes;
    }
}
