package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import org.springframework.stereotype.Component;

@Component("umStrategy")
public class UmStrategy extends CommonStrategy implements PartnerInstanceStrategy{
    @Override
    public void applySettle(PartnerInstanceDto partnerInstanceDto) {

    }

    @Override
    public void applySettleNewly(PartnerInstanceDto partnerInstanceDto) {

    }

    @Override
    public void settleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto, PartnerStationRel rel) {

    }

    @Override
    public void applyQuit(QuitStationApplyDto quitDto, PartnerInstanceTypeEnum typeEnum) {

    }

    @Override
    public void handleDifferQuitAuditPass(Long partnerInstanceId) {

    }

    @Override
    public void quit(PartnerInstanceQuitDto partnerInstanceQuitDto) {

    }

    @Override
    public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto, PartnerStationRel rel) {

    }

    @Override
    public void validateExistChildrenForQuit(PartnerStationRel instance) {

    }

    @Override
    public void validateClosePreCondition(PartnerStationRel partnerStationRel) {

    }

    @Override
    public Boolean validateUpdateSettle(Long instanceId) {
        return null;
    }

    @Override
    public void startClosing(Long instanceId, String stationName, OperatorDto operatorDto) {

    }

    @Override
    public void startQuiting(Long instanceId, String stationName, OperatorDto operatorDto) {

    }

    @Override
    public void validateAssetBack(Long instanceId) {

    }

    @Override
    public void validateOtherPartnerQuit(Long instanceId) {

    }

    @Override
    public void startService(Long instanceId, Long taobaoUserId, OperatorDto operatorDto) {

    }
}
