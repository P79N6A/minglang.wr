package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.StationNewCustomerDailyTaskDto;

/**
 * 门店新用户服务
 *
 * @author haihu.fhh
 */
public interface StationNewCustomerService {

    /**
     * 同步新客户
     *
     * @param taskDto
     */
    public void syncNewCustomer(StationNewCustomerDailyTaskDto taskDto);

}
