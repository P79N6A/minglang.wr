package com.taobao.cun.auge.station.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.TbkDgNewuserOrderGetRequest;
import com.taobao.api.response.TbkDgNewuserOrderGetResponse;
import com.taobao.cun.auge.common.exception.AugeSystemException;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.UnionNewuserOrder;
import com.taobao.cun.auge.dal.domain.UnionNewuserOrderExample;
import com.taobao.cun.auge.dal.mapper.UnionNewuserOrderMapper;
import com.taobao.cun.auge.station.bo.PartnerAdzoneBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.NewuserOrderInitRequest;
import com.taobao.cun.auge.station.dto.NewuserOrderInitResponse;
import com.taobao.cun.auge.station.dto.PartnerAdzoneInfoDto;
import com.taobao.cun.auge.station.service.PartnerAdzoneService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.union.api.client.service.EntryService;
import com.taobao.union.common.RpcResult;
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

    @Autowired
    PartnerAdzoneBO partnerAdzoneBO;
    @Autowired
    PartnerInstanceBO partnerInstanceBO;
    @Autowired
    EntryService entryService;
    @Autowired
    UnionNewuserOrderMapper unionNewuserOrderMapper;

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
        String updateDate = DateFormatUtils.format(new Date().getTime(), "yyyyMMdd");
        try {
            logger.info("start TbkDgNewuserOrderGetRequest,pageNo = {} , activityId = {}", pageNO, activityId);
            TbkDgNewuserOrderGetResponse rsp = client.execute(req);
            if (rsp.isSuccess()) {
                TbkDgNewuserOrderGetResponse.Data data = rsp.getResults().getData();
                response.setHasNext(data.getHasNext());
                data.getResults().forEach(res -> {
                    UnionNewuserOrder order = convertFromTbkNewuserOrder(res);
                    order.setStatDate(request.getStatDate());
                    order.setUpdateDate(updateDate);
                    Date now = new Date();
                    order.setGmtCreate(now);
                    order.setGmtModified(now);
                    unionNewuserOrderMapper.insertSelective(order);
                });
            } else {
                response.setSuccess(false);
                logger.error("TbkDgNewuserOrderGetRequestError:" + JSON.toJSONString(rsp));
            }
        } catch (ApiException e) {
            logger.info("start TbkDgNewuserOrderGetRequest,pageNo = {} , activityId = {}", pageNO, activityId);
            response.setSuccess(false);
        }

        return response;
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

    @Override
    public void deleteNewuserOrder(String activityId, String statDate) {
        UnionNewuserOrderExample example = new UnionNewuserOrderExample();
        example.createCriteria().andStatDateEqualTo(statDate).andActivityIdEqualTo(activityId);
        unionNewuserOrderMapper.deleteByExample(example);
    }

    private UnionNewuserOrder convertFromTbkNewuserOrder(TbkDgNewuserOrderGetResponse.MapData data) {
        UnionNewuserOrder order = new UnionNewuserOrder();
        order.setActivityId(data.getActivityId());
        order.setActivityType(data.getActivityType());
        order.setAdzoneId(data.getAdzoneId());
        order.setBindCardTime(data.getBindCardTime());
        order.setBizDate(data.getBizDate());
        order.setBuyTime(data.getBuyTime());
        order.setIsCardSave(data.getIsCardSave());
        order.setMobile(data.getMobile());
        order.setOrderTkType(data.getOrderTkType());
        order.setRegisterTime(data.getRegisterTime());
        order.setStatus(data.getStatus());
        order.setTbTradeParentId(data.getStatus());
        return order;
    }
}
