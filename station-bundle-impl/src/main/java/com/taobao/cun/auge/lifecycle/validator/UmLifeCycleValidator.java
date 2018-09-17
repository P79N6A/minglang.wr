package com.taobao.cun.auge.lifecycle.validator;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.validate.PartnerValidator;
import com.taobao.cun.auge.station.validate.UmStationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public  class UmLifeCycleValidator {

    @Autowired
    PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private LifeCycleValidator lifeCycleValidator;

    public void validateSettling(PartnerInstanceDto partnerInstanceDto) throws AugeBusinessException {
        ValidateUtils.validateParam(partnerInstanceDto);
        ValidateUtils.notNull(partnerInstanceDto.getStationDto());
        ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
        ValidateUtils.notNull(partnerInstanceDto.getType());
        StationDto stationDto = partnerInstanceDto.getStationDto();
        PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
        UmStationValidator.validateStationInfo(stationDto);
        PartnerValidator.validatePartnerInfo(partnerDto);
        //校验合作店名称是否合法
        lifeCycleValidator.stationModelBusCheck(partnerInstanceDto);

        // 判断手机号是否已经被使用
        // 逻辑变更只判断入驻中、装修中、服务中，退出中用户
        if (!partnerInstanceBO.judgeMobileUseble(partnerDto.getTaobaoUserId(), null, partnerDto.getMobile())) {
            throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "该手机号已被使用");
        }
    }
}
