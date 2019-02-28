package com.taobao.cun.auge.station.um;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.condition.UnionMemberPageCondition;
import com.taobao.cun.auge.station.convert.UnionMemberConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.um.dto.UnionMemberDto;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 优盟查询服务
 *
 * @author haihu.fhh
 */
@Service("unionMemberQueryService")
@HSFProvider(serviceInterface = UnionMemberQueryService.class, clientTimeout = 10000)
public class UnionMemberQueryServiceImpl implements UnionMemberQueryService {

    @Autowired
    private PartnerInstanceQueryService partnerInstanceQueryService;

    /**
     * 由于优盟的状态名称和村小二不同，所以需要转化一下
     *
     * @param stationId
     * @param operatorDto
     * @return
     */
    @Override
    public UnionMemberDto getUnionMember(Long stationId, OperatorDto operatorDto) {
        BeanValidator.validateWithThrowable(operatorDto);
        if (null == stationId || 0L == stationId) {
            throw new AugeSystemException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "stationId is null");
        }
        PartnerInstanceDto instanceDto = partnerInstanceQueryService.queryInfo(stationId, operatorDto);

        OperatorTypeEnum operatorTypeEnum = operatorDto.getOperatorType();
        //村小二查询时，只能查询自己的优盟
        if (OperatorTypeEnum.HAVANA.equals(operatorTypeEnum)) {
            Long parentTaobaoUserId = Long.valueOf(operatorDto.getOperator());
            //所属村小二
            PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getActivePartnerInstance(
                    parentTaobaoUserId);

            if (null == partnerInstanceDto) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "村小二不存在或者已经退出");
            }

            Long parentStationId = partnerInstanceDto.getStationId();
            if (null != parentStationId && !parentStationId.equals(instanceDto.getParentStationId())) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "不能查询非自己名下的优盟店");
            }
        }
        return UnionMemberConverter.convert(instanceDto);
    }

    @Override
    public PageDto<UnionMemberDto> queryByPage(UnionMemberPageCondition pageCondition) {
        BeanValidator.validateWithThrowable(pageCondition);

        OperatorTypeEnum operatorTypeEnum = pageCondition.getOperatorType();
        //村小二端来查询
        if (OperatorTypeEnum.HAVANA.equals(operatorTypeEnum) && null == pageCondition.getParentStationId()) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "只能查询自己名下的优盟店");
        }

        PartnerInstancePageCondition instancePageCondition = UnionMemberConverter.convert(pageCondition);
        PageDto<PartnerInstanceDto> instanceDtoPageDto = partnerInstanceQueryService.queryByPage(instancePageCondition);
        return UnionMemberConverter.convert(instanceDtoPageDto);
    }
}
