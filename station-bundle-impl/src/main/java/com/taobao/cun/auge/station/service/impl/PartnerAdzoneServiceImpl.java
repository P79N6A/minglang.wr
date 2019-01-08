package com.taobao.cun.auge.station.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgNewuserOrderGetRequest;
import com.taobao.api.response.TbkDgNewuserOrderGetResponse;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.cache.TairCache;
import com.taobao.cun.auge.common.exception.AugeSystemException;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.UnionNewuserOrder;
import com.taobao.cun.auge.dal.domain.UnionNewuserOrderExample;
import com.taobao.cun.auge.dal.mapper.UnionNewuserOrderMapper;
import com.taobao.cun.auge.station.bo.PartnerAdzoneBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.*;
import com.taobao.cun.auge.station.service.PartnerAdzoneService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.union.api.client.service.EntryService;
import com.taobao.union.common.RpcResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

@Service("partnerAdzoneService")
@HSFProvider(serviceInterface = PartnerAdzoneService.class)
public class PartnerAdzoneServiceImpl implements PartnerAdzoneService {

    private static final Logger logger = LoggerFactory.getLogger(PartnerAdzoneServiceImpl.class);
    private static final String PID_CACHE_PRE = "PID_";

    @Autowired
    PartnerAdzoneBO partnerAdzoneBO;
    @Autowired
    PartnerInstanceBO partnerInstanceBO;
    @Autowired
    EntryService entryService;
    @Autowired
    UnionNewuserOrderMapper unionNewuserOrderMapper;
    @Autowired
    AppResourceService appResourceService;
    @Autowired
    TairCache tairCache;

    @Value("${taobao.union.app.key}")
    private String appKey;
    @Value("${taobao.union.site.id}")
    private String siteId;
    @Value("${taobao.union.tb.num}")
    private Long tbNumId;
    @Value("${taobao.union.app.secret}")
    private String appSecret;
    @Value("${taobao.union.app.url}")
    private String appUrl;
    private static final String PARTNER_ADZONE_ERROR = "PARTNER_ADZONE_ERROR:";

    private static final String CREATE_ADZONE_QUERY_ID = "adzone.create";
    private static final String CONFIG_UNION_NEWUSER_TYPE = "union_newuser";
    private static final String CONFIG_UNION_NEWUSER_CURRENT_UPDATE_DATE = "current_update_date_";
    private static final String CONFIG_UNION_NEWUSER_ACTIVITY_ID = "activity_id_";

    @Override
    public String createAdzone(Long taobaoUserId) {
        Assert.notNull(taobaoUserId, "taobaoUserId is null");
        PartnerStationRel instance = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
        if (null == instance || null == instance.getStationId()) {
            logger.error(PARTNER_ADZONE_ERROR + "createAdzoneError(instance is null): {} ", taobaoUserId);
            return null;
        }
        Long stationId = instance.getStationId();
        String unionPid = getUnionPid(taobaoUserId, stationId);
        if (null != unionPid) {
            return unionPid;
        }

        return createAdzone(taobaoUserId, stationId);
    }

    public String createAdzoneWithoutStationId(Long taobaoUserId){
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("appkey", appKey);
        variables.put("siteId", siteId);
        variables.put("tbNumId", tbNumId);
        variables.put("adzoneName", taobaoUserId);
        RpcResult<Object> result = entryService.get(CREATE_ADZONE_QUERY_ID, variables);
        if (!result.isSuccess()) {
            logger.error(PARTNER_ADZONE_ERROR + "create adzone without station_id error: {}, {}", JSON.toJSONString(variables),
                    result.toString());
            throw new AugeSystemException(result.toString());
        }
        Map data = (Map)result.getData();
        String pid = (String)data.get("pid");
        return pid;
    }

    private String createAdzone(Long taobaoUserId, Long stationId) {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("appkey", appKey);
        variables.put("siteId", siteId);
        variables.put("tbNumId", tbNumId);
        variables.put("adzoneName", taobaoUserId);
        RpcResult<Object> result = entryService.get(CREATE_ADZONE_QUERY_ID, variables);
        if (!result.isSuccess()) {
            logger.error(PARTNER_ADZONE_ERROR + "create adzone error: {}, {}", JSON.toJSONString(variables),
                result.toString());
            throw new AugeSystemException(result.toString());
        }
        Map data = (Map)result.getData();
        String pid = (String)data.get("pid");
        partnerAdzoneBO.addAdzone(taobaoUserId, stationId, pid);
        return pid;
    }

    @Override
    public String getUnionPid(Long taobaoUserId, Long stationId) {
        Assert.notNull(taobaoUserId, "taobaoUserId is null");
        String cacheKey = PID_CACHE_PRE + taobaoUserId + (stationId == null ? "" : stationId);
        String pid = tairCache.get(cacheKey);
        if (null != pid) {
            return pid;
        }
        if (stationId == null) {
            PartnerStationRel instance = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
            if (null == instance || null == instance.getStationId()) {
                logger.error("getUnionPidError(instance is null): {} ", taobaoUserId);
                return null;
            }
            stationId = instance.getStationId();
        }
        pid = partnerAdzoneBO.getUnionPid(taobaoUserId, stationId);
        if (StringUtils.isNotBlank(pid)) {
            tairCache.put(cacheKey, pid, 3600);
        }
        return pid;
    }

    @Override
    public PartnerAdzoneInfoDto getPartnerAdzoneInfoByPid(String pid) {
        return partnerAdzoneBO.getPartnerAdzoneInfoByPid(pid);
    }

    @Override
    public NewuserOrderInitResponse initNewUserOrder(NewuserOrderInitRequest request) {
        NewuserOrderInitResponse response = new NewuserOrderInitResponse();
        response.setSuccess(true);
        TaobaoClient client = new DefaultTaobaoClient(appUrl, appKey, appSecret);
        TbkDgNewuserOrderGetRequest req = new TbkDgNewuserOrderGetRequest();
        Long pageNO = request.getPageNo();
        String activityId = request.getActivityId();
        req.setActivityId(activityId);
        req.setPageNo(pageNO);
        req.setPageSize(request.getPageSize());
        //req.setAdzoneId();
        //req.setStartTime();
        //req.setEndTime();
        if (null == request.getUpdateDate()) {
            String updateDate = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
            request.setUpdateDate(updateDate);
        }
        try {
            logger.info("start TbkDgNewuserOrderGetRequest,pageNo = {} , activityId = {}", pageNO, activityId);
            TbkDgNewuserOrderGetResponse rsp = client.execute(req);
            if (rsp.isSuccess()) {
                TbkDgNewuserOrderGetResponse.Data data = rsp.getResults().getData();
                response.setHasNext(data.getHasNext());
                data.getResults().forEach(res -> {
                    UnionNewuserOrder order = convertFromTbkNewuserOrder(res);
                    order.setStatDate(request.getStatDate());
                    order.setUpdateDate(request.getUpdateDate());
                    Date now = new Date();
                    order.setGmtCreate(now);
                    order.setGmtModified(now);
                    unionNewuserOrderMapper.insertSelective(order);
                });
            } else {
                response.setSuccess(false);
                logger.error(PARTNER_ADZONE_ERROR + "TbkDgNewuserOrderGetRequestError:" + JSON.toJSONString(rsp));
            }
        } catch (ApiException e) {
            logger.info(PARTNER_ADZONE_ERROR + "start TbkDgNewuserOrderGetRequest,pageNo = {} , activityId = {}",
                pageNO, activityId);
            response.setSuccess(false);
        }

        return response;
    }

    @Override
    public void deleteNewuserOrder(String activityId, String updateDate) {
        Assert.notNull(activityId, "activityId is null");
        Assert.notNull(updateDate, "updateDate is null");

        String currentUpdateDate = appResourceService.queryAppResourceValue(CONFIG_UNION_NEWUSER_TYPE,
            CONFIG_UNION_NEWUSER_CURRENT_UPDATE_DATE + updateDate.substring(0, 6));
        if (currentUpdateDate != null && currentUpdateDate.equalsIgnoreCase(updateDate)) {
            return;
        }
        UnionNewuserOrderExample example = new UnionNewuserOrderExample();
        example.createCriteria().andUpdateDateEqualTo(updateDate).andActivityIdEqualTo(activityId);
        unionNewuserOrderMapper.deleteByExample(example);
    }

    @Override
    public NewuserOrderStat getNewuserOrderStat(Long taobaoUserId, Long stationId, String statDate) {
        Assert.notNull(taobaoUserId, "taobaoUserId is null");
        Assert.notNull(statDate, "statDate is null");
        String unionPid = getUnionPid(taobaoUserId, stationId);
        if (StringUtils.isBlank(unionPid)) {
            logger.error("getNewuserOrderStatError, invalid param {}, {}", taobaoUserId, stationId);
            NewuserOrderStat stat = new NewuserOrderStat();
            stat.setTaobaoUserId(taobaoUserId);
            stat.setStatDate(statDate);
            return stat;
        }
        Long adzoneId = Long.parseLong(unionPid.split("_")[3]);

        String currentUpdateDate = appResourceService.queryAppResourceValue(CONFIG_UNION_NEWUSER_TYPE,
            CONFIG_UNION_NEWUSER_CURRENT_UPDATE_DATE + statDate);
        NewuserOrderStat stat = new NewuserOrderStat();
        if (null != currentUpdateDate) {
            stat = partnerAdzoneBO.getNewuserOrderStat(adzoneId, statDate, currentUpdateDate);
        }
        stat.setTaobaoUserId(taobaoUserId);
        stat.setStatDate(statDate);
        return stat;
    }

    @Override
    public void initAllNewUserOrder(NewuserOrderInitRequest request) {
        Long pageNO = request.getPageNo();
        Long pageSize = 20L;
        Long endNo = request.getPageSize();
        request.setPageSize(pageSize);
        while (true) {
            request.setPageNo(pageNO);
            NewuserOrderInitResponse response = initNewUserOrder(request);
            if (response.getSuccess()) {
                if (response.getHasNext()) {
                    pageNO++;
                } else {
                    logger.error("----------------initAllNewUserOrderEnd");
                    break;
                }
            } else {
                logger.error("----------------initAllNewUserOrderError");
                break;
            }
        }
    }

    private UnionNewuserOrder convertFromTbkNewuserOrder(TbkDgNewuserOrderGetResponse.MapData data) {
        UnionNewuserOrder order = new UnionNewuserOrder();
        order.setActivityId(data.getActivityId());
        order.setActivityType(data.getActivityType());
        order.setAdzoneId(data.getAdzoneId());
        order.setBindCardTime(data.getBindCardTime());
        order.setBindTime(data.getBindTime());
        order.setBizDate(data.getBizDate());
        order.setBuyTime(data.getBuyTime());
        order.setIsCardSave(null == data.getIsCardSave() ? 0 : data.getIsCardSave());
        order.setMobile(data.getMobile());
        order.setOrderTkType(data.getOrderTkType());
        order.setRegisterTime(data.getRegisterTime());
        order.setStatus(data.getStatus());
        order.setTbTradeParentId(data.getTbTradeParentId());
        order.setLoginTime(data.getLoginTime());
        return order;
    }

}
