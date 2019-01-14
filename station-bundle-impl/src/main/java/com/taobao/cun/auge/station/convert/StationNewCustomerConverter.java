package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.dal.domain.StationNewCustomer;
import com.taobao.cun.auge.station.dto.StationNewCustomerDailyTaskDto;

public final class StationNewCustomerConverter {

    private StationNewCustomerConverter() {

    }

    public static StationNewCustomer convert(StationNewCustomerDailyTaskDto taskDto) {
        if (null == taskDto) {
            return null;
        }

        StationNewCustomer newCustomer = new StationNewCustomer();

        newCustomer.setTaobaoUserId(taskDto.getTaobaoUserId());
        newCustomer.setMobile(taskDto.getMobile());
        newCustomer.setStationId(taskDto.getStationId());
        newCustomer.setAdzoneId(taskDto.getAdzoneId());
        newCustomer.setPreInterestTime(taskDto.getPreInterestTime());
        newCustomer.setRealInterestTime(taskDto.getRealInterestTime());
        newCustomer.setFinishCpaTime(taskDto.getFinishCpaTime());
        newCustomer.setCpaConstraintTime(taskDto.getCpaConstraintTime());
        newCustomer.setStatus(taskDto.getStatus());
//        newCustomer.setRateBeginTime();
//        newCustomer.setRateEndTime();
//        newCustomer.setTagAddTime();
        newCustomer.setRisk(taskDto.getRisk());
        newCustomer.setMs(taskDto.getMs());

        return newCustomer;
    }
}
