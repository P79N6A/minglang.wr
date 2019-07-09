package com.taobao.cun.auge.fence.service;

import java.util.List;

public interface CainiaoFenceMgrService {
    /**
     * 获取菜鸟围栏
     * @param cainiaoFenceId
     */
    String getCainiaoFences(Long cainiaoFenceId);

    void check();
}
