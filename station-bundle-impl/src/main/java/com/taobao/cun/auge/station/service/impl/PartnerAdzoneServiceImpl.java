package com.taobao.cun.auge.station.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.common.exception.AugeSystemException;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerAdzoneBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerAdzoneInfoDto;
import com.taobao.cun.auge.station.service.PartnerAdzoneService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.union.api.client.service.EntryService;
import com.taobao.union.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

@Service("partnerAdzoneService")
@HSFProvider(serviceInterface = PartnerAdzoneService.class)
public class PartnerAdzoneServiceImpl implements PartnerAdzoneService {

    private static final Logger logger = LoggerFactory.getLogger(PartnerAdzoneServiceImpl.class);

    @Autowired
    PartnerAdzoneBO partnerAdzoneBO;
    @Autowired
    PartnerInstanceBO partnerInstanceBO;
    @Autowired
    EntryService entryService;

    @Value("${taobao.union.app.key}")
    private String appKey;
    @Value("${taobao.union.site.id}")
    private String siteId;
    @Value("${taobao.union.tb.num}")
    private Long tbNumId;
    private static final String CREATE_ADZONE_QUERY_ID = "adzone.create";

    @Override
    public String createAdzone(Long taobaoUserId) {
        Assert.notNull(taobaoUserId, "taobaoUserId is null");
        PartnerStationRel instance = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
        if (null == instance || null == instance.getStationId()) {
            logger.error("createAdzoneError(instance is null): {} ", taobaoUserId);
            return null;
        }
        Long stationId = instance.getStationId();
        String unionPid = getUnionPid(taobaoUserId, stationId);
        if (null != unionPid) {
            return unionPid;
        }

        return createAdzone(taobaoUserId, stationId);
    }

    private String createAdzone(Long taobaoUserId, Long stationId) {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("appkey", appKey);
        variables.put("siteId", siteId);
        variables.put("tbNumId", tbNumId);
        variables.put("adzoneName", taobaoUserId);
        RpcResult<Object> result = entryService.get(CREATE_ADZONE_QUERY_ID, variables);
        if (!result.isSuccess()) {
            logger.error("create adzone erroor: {}, {}", JSON.toJSONString(variables), result.toString());
            throw new AugeSystemException(result.toString());
        }
        Map data = (Map) result.getData();
        String pid = (String) data.get("pid");
        partnerAdzoneBO.addAdzone(taobaoUserId, stationId, pid);
        return pid;
    }

    @Override
    public String getUnionPid(Long taobaoUserId, Long stationId) {
        Assert.notNull(taobaoUserId, "taobaoUserId is null");
        if (stationId == null) {
            PartnerStationRel instance = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
            if (null == instance || null == instance.getStationId()) {
                logger.error("getUnionPidError(instance is null): {} ", taobaoUserId);
                return null;
            }
            stationId = instance.getStationId();
        }
        return partnerAdzoneBO.getUnionPid(taobaoUserId, stationId);
    }

    @Override
    public PartnerAdzoneInfoDto getPartnerAdzoneInfoByPid(String pid) {
        return partnerAdzoneBO.getPartnerAdzoneInfoByPid(pid);
    }
}
