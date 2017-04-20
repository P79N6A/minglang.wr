package com.taobao.cun.auge.station.service.impl.levelaudit;

import java.util.Calendar;
import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.dal.domain.PartnerInstanceLevel;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceLevelBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.service.interfaces.PartnerInstanceLevelService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by xujianhui on 17/4/20.
 */
@Service("partnerInstanceLevelService")
@HSFProvider(serviceInterface = PartnerInstanceLevelService.class)
public class PartnerInstanceLevelServiceImpl implements PartnerInstanceLevelService {

    private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceLevelServiceImpl.class);

    private static final int DEFAULT_EVALUATE_INTERVAL = 6;

    @Autowired
    StationBO stationBO;

    @Autowired
    PartnerInstanceLevelBO partnerInstanceLevelBO;

    @Autowired
    PartnerInstanceBO partnerInstanceBO;

    @Override
    public boolean initPartnerInstanceLevel(Long partnerInstanceId, String operator) {
        Assert.notNull(partnerInstanceId);
        PartnerStationRel instance  =partnerInstanceBO.findPartnerInstanceById(partnerInstanceId);
        if (instance == null || instance.getOpenDate() == null) {
            throw new AugeServiceException("找不到村小二实例或开业时间为空!");
        }
        if (!PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState())) {
            return false;
        }

        PartnerInstanceLevel instanceLevel = partnerInstanceLevelBO.getPartnerInstanceLevelByPartnerInstanceId(partnerInstanceId);
        if (instanceLevel!=null) {
            throw new AugeServiceException("该村小二层级信息已存在!");
        }

        PartnerInstanceLevelDto dto = new PartnerInstanceLevelDto();
        dto.setPartnerInstanceId(instance.getId());
        dto.setTaobaoUserId(instance.getTaobaoUserId());
        dto.setStationId(instance.getStationId());
        dto.setCurrentLevel(PartnerInstanceLevelEnum.S_4);
        dto.setEvaluateBy(operator);
        dto.setEvaluateDate(instance.getOpenDate());
        Calendar c = getNextEvaluateDate(instance.getOpenDate());
        dto.setNextEvaluateDate(c.getTime());
        dto.copyOperatorDto(OperatorDto.defaultOperator());
        Station station = stationBO.getStationById(instance.getStationId());
        dto.setCountyOrgId(station.getApplyOrg());
        partnerInstanceLevelBO.addPartnerInstanceLevel(dto);
        return true;
    }

    public static Calendar getNextEvaluateDate(Date openDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(openDate);
        c.add(Calendar.MONTH, DEFAULT_EVALUATE_INTERVAL);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (day > 1) {
            c.add(Calendar.MONTH, 1);
        }
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c;
    }
}
