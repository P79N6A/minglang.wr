package com.taobao.cun.auge.lifecycle.validator;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.um.dto.UnionMemberUpdateDto;
import com.taobao.cun.auge.station.validate.PartnerValidator;
import com.taobao.cun.auge.station.validate.StationValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UmLifeCycleValidator {

    @Autowired
    PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private LifeCycleValidator lifeCycleValidator;

    /**
     * 优盟入驻前置校验
     *
     * @param partnerInstanceDto
     */
    public void validateSettling(PartnerInstanceDto partnerInstanceDto) {
        ValidateUtils.validateParam(partnerInstanceDto);
        ValidateUtils.notNull(partnerInstanceDto.getStationDto());
        ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
        ValidateUtils.notNull(partnerInstanceDto.getType());
        StationDto stationDto = partnerInstanceDto.getStationDto();
        PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
        //优盟门店名称校验
        StationValidator.nameFormatCheck(stationDto.getName());
        //校验地址字符长度等
        StationValidator.addressFormatCheck(stationDto.getAddress());
        //校验优盟门店名称和地址是否包含违禁词
        //校验地址中是否包含村小二姓名
        //校验优盟门店名称是否重复
        lifeCycleValidator.stationModelBusCheck(partnerInstanceDto);
        //校验优盟信息
        PartnerValidator.validatePartnerInfo(partnerDto);

        // 判断手机号是否已经被使用
        // 逻辑变更只判断入驻中、装修中、服务中，退出中用户
        if (!partnerInstanceBO.judgeMobileUseble(partnerDto.getTaobaoUserId(), null, partnerDto.getMobile())) {
            throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "该手机号已被使用");
        }

        //校验描述
        validateDescription(stationDto.getDescription());
    }

    /**
     * 优盟变更前置校验
     *
     * @param updateDto
     */
    public void validateUpdate(UnionMemberUpdateDto updateDto) {
        String stationName = updateDto.getStationName();
        if (StringUtils.isNotBlank(stationName)) {
            //校验优盟门店名称字符和长度
            StationValidator.nameFormatCheck(stationName);
            //校验优盟门店名称是否有违禁词汇
            lifeCycleValidator.checkStationNameKfc(stationName);
        }
        Address address = updateDto.getAddress();
        if (null != address) {
            //地址变更后，校验新的地址，字符长度校验
            StationValidator.addressFormatCheck(address);
            //校验合作店名称、地址是否包含违禁词
            lifeCycleValidator.checkAdressKfc(address.getAddressDetail());
        }
        //校验描述
        validateDescription(updateDto.getDescription());
    }

    /**
     * 校验描述
     *
     * @param desc
     */
    private void validateDescription(String desc) {
        if (StringUtils.isNotBlank(desc) && desc.length() > 50) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "优盟描述不能超过50位");
        }
    }
}
