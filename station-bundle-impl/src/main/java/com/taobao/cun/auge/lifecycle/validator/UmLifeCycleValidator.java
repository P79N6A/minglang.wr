package com.taobao.cun.auge.lifecycle.validator;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.security.ProhibitedWordChecker;
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

/**
 * @author haihu.fhh
 */
@Component
public class UmLifeCycleValidator {

    @Autowired
    private StationBO stationBO;

    @Autowired
    PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private PartnerInstanceQueryService partnerInstanceQueryService;

    @Resource
	private ProhibitedWordChecker prohibitedWordChecker;
	
    //优盟描述最多50个字
    private static final int UM_DESC_MAX_SIZE = 50;

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
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "该账号已经合作，不能重复使用");
        }
        //优盟姓名
        String umName = partnerDto.getName();
        //优盟门店名称校验
        String stationName = stationDto.getName();
        StationValidator.umStationNameCheck(stationName);
        //校验优盟门店名称是否有违禁词汇
        checkStationNameKfc(stationName);
        //校验优盟店名称中是否包含优盟姓名
        if (stationName.contains(umName)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "优盟店名称不可以包含姓名等信息");
        }

        // 判断同一省不能重复村站名
        checkStationNameDuplicate(null, stationName, address.getProvince());

        //校验地址字符长度等
        StationValidator.addressFormatCheck(address);
        //校验合作店地址是否包含违禁词
        checkAdressKfc(address.getAddressDetail());
        //校验优盟店地址中是否包含优盟姓名
        if (address.getAddressDetail().contains(umName)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "优盟店地址不可以包含姓名等信息");
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
            StationValidator.umStationNameCheck(stationName);
            if (stationName.contains(umName)) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "优盟店名称不可以包含姓名等信息");
            }
            //校验优盟门店名称是否有违禁词汇
            checkStationNameKfc(stationName);

            // 判断同一省不能重复村站名
            checkStationNameDuplicate(updateDto.getStationId(), stationName, address.getProvince());
        }
        //地址变更后，校验新的地址，字符长度校验
        StationValidator.addressFormatCheck(address);
        //校验合作店地址是否包含违禁词
        checkAdressKfc(address.getAddressDetail());

        if (address.getAddressDetail().contains(umName)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "优盟店地址不可以包含姓名等信息");
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
        if (StringUtils.isNotBlank(desc) && desc.length() > UM_DESC_MAX_SIZE) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "优盟描述不能超过50位");
        }
    }

    /**
     * 校验站点名称中是否有违禁词
     *
     * @param stationName
     */
    public void checkStationNameKfc(String stationName) {
        if (prohibitedWordChecker.hasProhibitedWord(stationName)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,
                "优盟店名称包含违禁词汇：" + prohibitedWordChecker.getProhibitedWord(stationName).get());
        }
    }

    /**
     * 校验地址中是否有违禁词
     *
     * @param addressDetail
     */
    public void checkAdressKfc(String addressDetail) {
        if (prohibitedWordChecker.hasProhibitedWord(addressDetail)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,
                "地址包含违禁词汇：" + prohibitedWordChecker.getProhibitedWord(addressDetail).get());
        }
    }

    /**
     * 判断优盟服务站名同一省内是否存在
     */
    public void checkStationNameDuplicate(Long stationId, String newStationName, String province) {
        String oldName = null;
        if (stationId != null) {
            Station oldStation = stationBO.getStationById(stationId);
            oldName = oldStation.getName();
        }
        if (!StringUtils.equals(oldName, newStationName)) {
            int count = stationBO.getSameNameInProvinceCnt(newStationName, province);
            if (count > 0) {
                throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "优盟店名称同一省域不能重复");
            }
        }
    }
}
