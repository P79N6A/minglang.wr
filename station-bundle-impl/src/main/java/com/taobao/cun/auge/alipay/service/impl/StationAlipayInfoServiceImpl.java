package com.taobao.cun.auge.alipay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.alipay.bo.StationAlipayInfoBO;
import com.taobao.cun.auge.alipay.dto.StationAlipayInfoDto;
import com.taobao.cun.auge.alipay.service.StationAlipayInfoService;
import com.taobao.cun.auge.dal.domain.StationAlipayInfo;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.mtop.api.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service("stationAlipayInfoService")
@HSFProvider(serviceInterface= StationAlipayInfoService.class, clientTimeout = 10000)
public class StationAlipayInfoServiceImpl implements StationAlipayInfoService {

    private static final Logger logger = LoggerFactory.getLogger(StationAlipayInfoServiceImpl.class);

    public static final String METHOD_ZFC_REJECTED="ant.merchant.expand.indirect.zft.rejected";

    public static final String METHOD_ZFC_PASSED="ant.merchant.expand.indirect.zft.passed";

    public static final String SUCCESS="success";

    public static final String FAIL="fail";

    @Autowired
    private StationAlipayInfoBO stationAlipayInfoBO;

    @Override
    public StationAlipayInfoDto getStationAlipayInfoByTaobaoUserId(String taobaoUserId) {

        StationAlipayInfo stationAlipayInfo= stationAlipayInfoBO.getStationAlipayInfo(taobaoUserId);
        return convertToDto(stationAlipayInfo);
    }

    @Override
    public Long saveStationAlipayInfo(StationAlipayInfoDto stationAlipayInfoDto) {

        return stationAlipayInfoBO.addStationAlipayInfo(convertTo(stationAlipayInfoDto));
    }

    @Override
    public void updateStationAlipayInfo(StationAlipayInfoDto stationAlipayInfoDto) {

        stationAlipayInfoBO.updateStationAlipayInfo(convertTo(stationAlipayInfoDto));
    }


    @Override
    public String dealZftMessage(Map<String, String> params) {

        if(params==null){
            return FAIL;
        }
        String msgMethod=params.get("msg_method");
        if(METHOD_ZFC_REJECTED.equals(msgMethod)){
          String bizContent=params.get("biz_content");
          JSONObject object= JSONObject.parseObject(bizContent);
          String externalId=object.getString("external_id");
          String reason=object.getString("reason");
          if(StringUtil.isNotBlank(externalId)){
              logger.info("dealZftMessage,the method=ant.merchant.expand.indirect.zft.rejected, external_id="+externalId+",reason="+reason);
              String taobaoUserId=externalId.substring(7);
              StationAlipayInfo stationAlipayInfo= stationAlipayInfoBO.getStationAlipayInfo(taobaoUserId);
              if(stationAlipayInfo!=null){
                  stationAlipayInfo.setAuditStatus("rejected");
                  stationAlipayInfo.setAuditMemo(reason);
                  stationAlipayInfo.setGmtModified(new Date());
                  stationAlipayInfoBO.updateStationAlipayInfo(stationAlipayInfo);
                  logger.info("dealZftMessage,the method=ant.merchant.expand.indirect.zft.rejected, external_id="+externalId+",reason="+reason+",update success");
              }
          }
          return SUCCESS;
        }
        else if(METHOD_ZFC_PASSED.equals(msgMethod)){
            String bizContent=params.get("biz_content");
            JSONObject object= JSONObject.parseObject(bizContent);
            String externalId=object.getString("external_id");
            String memo=object.getString("memo");
            String smid=object.getString("smid");
            if(StringUtil.isNotBlank(externalId)){
                logger.info("dealZftMessage,the method=ant.merchant.expand.indirect.zft.passed, external_id="+externalId+",smid="+smid+",memo="+memo);
                String taobaoUserId=externalId.substring(7);
                StationAlipayInfo stationAlipayInfo= stationAlipayInfoBO.getStationAlipayInfo(taobaoUserId);
                if(stationAlipayInfo!=null){
                    stationAlipayInfo.setAuditStatus("passed");
                    stationAlipayInfo.setAuditMemo(memo);
                    stationAlipayInfo.setGmtModified(new Date());
                    stationAlipayInfo.setSmid(smid);
                    stationAlipayInfoBO.updateStationAlipayInfo(stationAlipayInfo);
                    logger.info("dealZftMessage,the method=ant.merchant.expand.indirect.zft.passed, external_id="+externalId+",smid="+smid+",memo="+memo+",update success");
                }
            }
            return SUCCESS;
        }
        return FAIL;
    }

    private StationAlipayInfoDto convertToDto(StationAlipayInfo stationAlipayInfo){

        if(stationAlipayInfo==null){
            return null;
        }
        StationAlipayInfoDto stationAlipayInfoDto=new StationAlipayInfoDto();
        BeanUtils.copyProperties(stationAlipayInfo,stationAlipayInfoDto);
        return stationAlipayInfoDto;
    }

    private StationAlipayInfo convertTo(StationAlipayInfoDto stationAlipayInfoDto){

        if(stationAlipayInfoDto==null){
            return null;
        }
        StationAlipayInfo stationAlipayInfo=new StationAlipayInfo();
        BeanUtils.copyProperties(stationAlipayInfoDto,stationAlipayInfo);
        return stationAlipayInfo;
    }
}
