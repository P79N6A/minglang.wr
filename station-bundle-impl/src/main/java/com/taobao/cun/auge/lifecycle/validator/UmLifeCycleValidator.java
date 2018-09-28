package com.taobao.cun.auge.lifecycle.validator;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.KFCServiceConfig;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.um.dto.UnionMemberUpdateDto;
import com.taobao.cun.auge.station.validate.PartnerValidator;
import com.taobao.cun.auge.station.validate.StationValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haihu.fhh
 */
@Component
public class UmLifeCycleValidator {

    @Autowired
    PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private PartnerInstanceQueryService partnerInstanceQueryService;

    @Autowired
    private KFCServiceConfig kfcServiceConfig;

    @Autowired
    private StationBO stationBO;

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
        Long taobaoUserId = partnerDto.getTaobaoUserId();
        Address address = stationDto.getAddress();
        //不可重复入驻
        PartnerInstanceDto piDto = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
        if (piDto != null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "该账号已经合作，不能重复添加");
        }
        //优盟姓名
        String umName = partnerDto.getName();
        //优盟门店名称校验
        String stationName = stationDto.getName();
        StationValidator.nameFormatCheck(stationName);
        //校验优盟门店名称是否有违禁词汇
        checkStationNameKfc(stationName);
        //校验优盟店名称中是否包含优盟姓名
        if (stationName.contains(umName)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "优盟店名称不可以包含优盟姓名");
        }
        //校验同一个省域内，村站名称是否重复
        checkStationNameDuplicate(stationName, address.getProvince());

        //校验地址字符长度等
        StationValidator.addressFormatCheck(address);
        //校验合作店地址是否包含违禁词
        checkAdressKfc(address.getAddressDetail());
        //校验优盟店地址中是否包含优盟姓名
        if (address.getAddressDetail().contains(umName)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "优盟店地址不可以包含优盟姓名");
        }
        //校验优盟信息
        PartnerValidator.validatePartnerInfo(partnerDto);

        // 判断手机号是否已经被使用
        // 逻辑变更只判断入驻中、装修中、服务中，退出中用户
        if (!partnerInstanceBO.judgeMobileUseble(taobaoUserId, null, partnerDto.getMobile())) {
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
    public void validateUpdate(UnionMemberUpdateDto updateDto, String umName) {
        String stationName = updateDto.getStationName();
        Address address = updateDto.getAddress();
        if (StringUtils.isNotBlank(stationName)) {
            //校验优盟门店名称字符和长度
            StationValidator.nameFormatCheck(stationName);
            if (stationName.contains(umName)) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "优盟店名称不可以包含优盟姓名");
            }
            //校验优盟门店名称是否有违禁词汇
            checkStationNameKfc(stationName);
            //校验同一个省域内，村站名称是否重复
            checkStationNameDuplicate(stationName, address.getProvince());
        }
        //地址变更后，校验新的地址，字符长度校验
        StationValidator.addressFormatCheck(address);
        //校验合作店地址是否包含违禁词
        checkAdressKfc(address.getAddressDetail());

        if (address.getAddressDetail().contains(umName)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "优盟店地址不可以包含优盟姓名");
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

    /**
     * 校验站点名称中是否有违禁词
     *
     * @param stationName
     */
    public void checkStationNameKfc(String stationName) {
        if (kfcServiceConfig.isProhibitedWord(stationName)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,
                "优盟店名称包含违禁词汇：" + kfcServiceConfig.kfcCheck(stationName).get("word"));
        }
    }

    /**
     * 校验地址中是否有违禁词
     *
     * @param addressDetail
     */
    public void checkAdressKfc(String addressDetail) {
        if (kfcServiceConfig.isProhibitedWord(addressDetail)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,
                "地址包含违禁词汇：" + kfcServiceConfig.kfcCheck(addressDetail).get("word"));
        }
    }

    /**
     * 判断服务站名同一省内是否存在
     */
    public void checkStationNameDuplicate(String newStationName, String province) {
        int count = stationBO.getSameNameInProvinceCnt(newStationName, province);
        if (count > 0) {
            throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "优盟店名称同一省域不能重复");
        }
    }
}
