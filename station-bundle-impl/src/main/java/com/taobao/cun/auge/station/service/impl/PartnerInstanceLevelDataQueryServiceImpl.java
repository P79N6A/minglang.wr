package com.taobao.cun.auge.station.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ali.com.google.common.base.Function;
import com.ali.com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.BeanCopyUtils;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthDtoV2;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthStatDateDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthTrendDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthTrendDtoV2;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.service.interfaces.PartnerInstanceLevelDataQueryService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.data.service.PartnerInstanceLevelDataService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.util.CalendarUtil;

@Service("partnerInstanceLevelDataQueryService")
@HSFProvider(serviceInterface = PartnerInstanceLevelDataQueryService.class, clientTimeout = 7000)
public class PartnerInstanceLevelDataQueryServiceImpl implements PartnerInstanceLevelDataQueryService {

    private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceLevelDataQueryServiceImpl.class);
    private static final int DEFAULT_GROWTH_STAT_DAYS = 180;
    private static final int DEFAULT_GROWTH_TREND_STAT_DAYS = 30;

    @Autowired
    PartnerInstanceBO partnerInstanceBO;
    
    @Autowired
    PartnerInstanceLevelDataService partnerInstanceLevelDataService;
    
    @Override
    public PartnerInstanceLevelGrowthDto getPartnerInstanceLevelGrowthData(Long taobaoUserId) {
        return getGrowthData(taobaoUserId, PartnerInstanceLevelGrowthDto.class, new DataFetcher<com.taobao.cun.crius.data.service.dto.PartnerInstanceLevelGrowthDto>(){
            @Override
            public ResultModel<com.taobao.cun.crius.data.service.dto.PartnerInstanceLevelGrowthDto> getPartnerInstanceLevelGrowthData(Long taobaoUserId, Long stationId, String statDate) {
                return partnerInstanceLevelDataService.getPartnerInstanceLevelGrowthData(taobaoUserId, stationId, statDate);
            }
        });
    }

    @Override
    public List<PartnerInstanceLevelGrowthTrendDto> getPartnerInstanceLevelGrowthTrendData(Long taobaoUserId, String statDate) {
        return getTrendData(taobaoUserId, statDate, PartnerInstanceLevelGrowthTrendDto.class,  new DataListFetcher<com.taobao.cun.crius.data.service.dto.PartnerInstanceLevelGrowthTrendDto>(){
            @Override
            public ResultModel<List<com.taobao.cun.crius.data.service.dto.PartnerInstanceLevelGrowthTrendDto>> getPartnerInstanceLevelTrendData(Long taobaoUserId, Long stationId, String startDate, String endDate) {
                return partnerInstanceLevelDataService.getPartnerInstanceLevelGrowthTrendData(taobaoUserId, stationId, startDate, endDate);
            }
        });
    }
    
    @Override
    public PartnerInstanceLevelGrowthDtoV2 getPartnerInstanceLevelGrowthDataV2(Long taobaoUserId) {
        return getGrowthData(taobaoUserId, PartnerInstanceLevelGrowthDtoV2.class, new DataFetcher<com.taobao.cun.crius.data.service.dto.PartnerInstanceLevelGrowthDtoV2>(){
            @Override
            public ResultModel<com.taobao.cun.crius.data.service.dto.PartnerInstanceLevelGrowthDtoV2> getPartnerInstanceLevelGrowthData(Long taobaoUserId, Long stationId, String statDate) {
                return partnerInstanceLevelDataService.getPartnerInstanceLevelGrowthDataV2(taobaoUserId, stationId, statDate);
            }
        });
    }

    @Override
    public List<PartnerInstanceLevelGrowthTrendDtoV2> getPartnerInstanceLevelGrowthTrendDataV2(Long taobaoUserId, String statDate) {
        return getTrendData(taobaoUserId, statDate, PartnerInstanceLevelGrowthTrendDtoV2.class,  new DataListFetcher<com.taobao.cun.crius.data.service.dto.PartnerInstanceLevelGrowthTrendDtoV2>(){
            @Override
            public ResultModel<List<com.taobao.cun.crius.data.service.dto.PartnerInstanceLevelGrowthTrendDtoV2>> getPartnerInstanceLevelTrendData(Long taobaoUserId, Long stationId, String startDate, String endDate) {
                return partnerInstanceLevelDataService.getPartnerInstanceLevelGrowthTrendDataV2(taobaoUserId, stationId, startDate, endDate);
            }
        });
    }
    
    private <T, S> T getGrowthData(Long taobaoUserId, Class<T> clazz, DataFetcher<S> df) {
            PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
            if (null == instance || !PartnerInstanceTypeEnum.TP.getCode().equals(instance.getType())) {
                return null;
            }
            // 今天的数据没有则取昨天的数据
            List<PartnerInstanceLevelGrowthStatDateDto> statDateList = getRecentStatDateList();
            for (PartnerInstanceLevelGrowthStatDateDto statDate : statDateList) {
                ResultModel<S> result = df.getPartnerInstanceLevelGrowthData(instance.getTaobaoUserId(), instance.getStationId(), statDate.getStatDate());
                checkResult(result, "getPartnerInstanceLevelGrowthData");
                if (null == result.getResult()) {
                    continue;
                }
                T dto = BeanUtils.instantiate(clazz);
                BeanCopyUtils.copyNotNullProperties(result.getResult(), dto);
                BeanCopyUtils.copyNotNullProperties(statDate, dto);
                return dto;
            }
            if (PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState())) {
                logger.warn("PartnerInstanceLevelGrowthData not exists " + taobaoUserId);
            }
            return null;
    }

    private <T,S> List<T> getTrendData(Long taobaoUserId, String statDate, Class<T> clazz, DataListFetcher<S> dataFetcher) {
            PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
            if (null == instance || !PartnerInstanceTypeEnum.TP.getCode().equals(instance.getType())) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            if (StringUtils.isBlank(statDate)) {
                statDate = CalendarUtil.formatDate(calendar.getTime(), CalendarUtil.DATE_FMT_3);
            }
            calendar.add(Calendar.DATE, (-1 * DEFAULT_GROWTH_TREND_STAT_DAYS));
            String statStartDate = CalendarUtil.formatDate(calendar.getTime(), CalendarUtil.DATE_FMT_3);
            ResultModel<List<S>> result = dataFetcher.getPartnerInstanceLevelTrendData(taobaoUserId, instance.getStationId(), statStartDate, statDate);
            checkResult(result, "getPartnerInstanceLevelGrowthTrendData");
            if (CollectionUtils.isEmpty(result.getResult())) {
                return null;
            }
            List<T> list = Lists.transform(result.getResult(),
                    new Function<S, T>() {
                        @Override
                        public T apply(S input) {
                            try{
                                T dto = clazz.newInstance();
                                BeanCopyUtils.copyNotNullProperties(input, dto);
                                return dto;
                            }catch(Exception e){
                                logger.error("copy properties exception!", e);
                                return null;
                            }
                        }
                    });

            return list;
    }
    
    /**
     * 获得最近两天的时间参数 statDate=yesterday/the day before
     * yesterday;statEndDate=statDate;statStartDate=statDate-
     * DEFAULT_GROWTH_STAT_DAYS day
     * 
     * @return
     */
    private List<PartnerInstanceLevelGrowthStatDateDto> getRecentStatDateList() {
        List<PartnerInstanceLevelGrowthStatDateDto> list = Lists.newArrayList();
        for (int i = 1; i <= 2; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1 * i);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            list.add(buildPartnerInstanceLevelGrowthStatDateDto(calendar));
        }
        return list;
    }

    private PartnerInstanceLevelGrowthStatDateDto buildPartnerInstanceLevelGrowthStatDateDto(Calendar calendar) {
        PartnerInstanceLevelGrowthStatDateDto dto = new PartnerInstanceLevelGrowthStatDateDto();
        Date stateDate = calendar.getTime();
        dto.setStatDate(CalendarUtil.formatDate(stateDate, CalendarUtil.DATE_FMT_3));
        dto.setStatEndDate(stateDate);

        calendar.add(Calendar.DATE, (-1 * DEFAULT_GROWTH_STAT_DAYS));
        Date statStartDate = calendar.getTime();
        dto.setStatStartDate(statStartDate);
        return dto;
    }
    
    private String getErrorMessage(String methodName, String param, String error) {
        StringBuilder sb = new StringBuilder();
        sb.append("PartnerInstanceQueryService-Error|").append(methodName).append("(.param=").append(param).append(").")
                .append("errorMessage:").append(error);
        return sb.toString();
    }

    
    private <T> void checkResult(ResultModel<T> rm, String msg) {
        if (!rm.isSuccess()) {
            if (rm.getException() != null) {
                throw rm.getException();
            } else {
                throw new AugeBusinessException("get ResultModel failed: " + msg);
            }
        }
    }

    public static interface DataFetcher<T> {
        ResultModel<T> getPartnerInstanceLevelGrowthData(Long taobaoUserId, Long stationId, String statDate);
    }
    
    public static interface DataListFetcher<T> {
        ResultModel<List<T>> getPartnerInstanceLevelTrendData(Long taobaoUserId, Long stationId, String statDate, String endDate);
    }
}
