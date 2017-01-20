package com.taobao.cun.auge.bail.service.impl;

import com.taobao.cun.auge.bail.BailService;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.settle.bail.dto.*;
import com.taobao.cun.settle.bail.enums.BailChannelEnum;
import com.taobao.cun.settle.bail.enums.UserTypeEnum;
import com.taobao.cun.settle.bail.service.CuntaoNewBailService;
import com.taobao.cun.settle.common.model.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by xujianhui on 16/12/27.
 */
@Service("bailService")
@HSFProvider(serviceInterface = BailService.class, clientTimeout=15000)
public class BailServiceImpl implements BailService {

    private static final Logger logger = LoggerFactory.getLogger(BailServiceImpl.class);

    @Autowired
    private CuntaoNewBailService cuntaoNewBailService;

    @Override
    public ResultModel<Boolean> isUserSignBail(Long taobaoUserId, String alipayId, UserTypeEnum userTypeEnum) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(userTypeEnum);
        try {
            ResultModel<Boolean> result = cuntaoNewBailService.isUserSignBail(taobaoUserId, alipayId, userTypeEnum);
            if(result!=null && !result.isSuccess()){
                logger.error("BailServiceImpl bailService process fail, alipayId:{}, ,message:{}, errorMsg:{}", alipayId, result.getMessage(), result.getException());
            }
            return result;
        }catch (Exception e){
            logger.error("BailServiceImpl bailService process error, alipayId:"+alipayId, e);
            throw new AugeServiceException("UserSignBail Exception:" + alipayId);
        }
    }

    @Override
    public ResultModel<String> buildSignBailUrl(Long taobaoUserId, UserTypeEnum userTypeEnum, String returnUrl, BailChannelEnum channel) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(userTypeEnum);
        Assert.notNull(channel);
        Assert.notNull(returnUrl);
        CuntaoBailSignDto signDto = new CuntaoBailSignDto();
        signDto.setTaobaoUserId(taobaoUserId);
        signDto.setUserTypeEnum(userTypeEnum);
        signDto.setReturnUrl(returnUrl);
        signDto.setBailChannelEnum(channel);
        try{
            ResultModel<String> result = cuntaoNewBailService.buildSignBailUrl(signDto);
            if(result!=null && !result.isSuccess()){
                logger.error("BailServiceImpl bailService process fail, taobaoUserId:{}, ,message:{}, errorMsg:{}", taobaoUserId, result.getMessage(), result.getException());
            }
            return result;
        }catch (Exception e){
            logger.error("BailServiceImpl bailService process error, taobaoUserId:"+taobaoUserId, e);
            throw new AugeServiceException("buildSignBailUrl Exception, taobaoUserId:" + taobaoUserId);
        }
    }

    @Override
    public ResultModel<Boolean> unfreezeUserBail(CuntaoUnFreezeBailDto cuntaoUnFreezeBailDto) {
        String outOrderId = cuntaoUnFreezeBailDto.getOutOrderId() + "UNFREEZE";
        cuntaoUnFreezeBailDto.setOutOrderId(outOrderId);
        try {
            ResultModel<Boolean> result = cuntaoNewBailService.unfreezeUserBail(cuntaoUnFreezeBailDto);
            if(result!=null && !result.isSuccess()){
                logger.error("BailServiceImpl bailService process fail, taobaoUserId:{}, ,message:{}, errorMsg:{}", cuntaoUnFreezeBailDto.getTaobaoUserId(), result.getMessage(), result.getException());
            }
            return result;
        }catch (Exception e) {
            logger.error("BailServiceImpl bailService process error, taobaoUserId:"+cuntaoUnFreezeBailDto.getTaobaoUserId(), e);
            throw new AugeServiceException("unfreezeUserBail Exception, taobaoUserId:" + cuntaoUnFreezeBailDto.getTaobaoUserId());
        }
    }

    @Override
    public ResultModel<Boolean> freezeUserBail(CuntaoFreezeBailDto cuntaoFreezeBailDto) {
        Assert.notNull(cuntaoFreezeBailDto);
        Assert.notNull(cuntaoFreezeBailDto.getTaobaoUserId());
        Assert.notNull(cuntaoFreezeBailDto.getAmount());
        try{
            ResultModel<Boolean> result = cuntaoNewBailService.freezeUserBail(cuntaoFreezeBailDto);
            if(result!=null && !result.isSuccess()){
                logger.error("BailServiceImpl bailService process fail, alipayId:{}, ,message:{}, errorMsg:{}", cuntaoFreezeBailDto.getAlipayId(), result.getMessage(), result.getException());
            }
            return result;
        }catch (Exception e){
            logger.error("BailServiceImpl bailService process error, alipayId:"+cuntaoFreezeBailDto.getAlipayId(), e);
            throw new AugeServiceException("freezeUserBail Exception:" + cuntaoFreezeBailDto.getAlipayId());
        }
    }

    @Override
    public ResultModel<Boolean> transferUserBail(CuntaoTransferBailDto cuntaoTransferBailDto) {
        return cuntaoNewBailService.transferUserBail(cuntaoTransferBailDto);
    }

    @Override
    public ResultModel<Boolean> transferUserBailForPunish(CuntaoTransferBailForPunishDto cuntaoTransferBailForPunishDto) {
        return cuntaoNewBailService.transferUserBailForPunish(cuntaoTransferBailForPunishDto);
    }

    @Override
    public ResultModel<Boolean> checkAlipaySign(Map<String, String> params) {
        Assert.notEmpty(params);
        return cuntaoNewBailService.checkAlipaySign(params);
    }

    @Override
    public ResultModel<Boolean> fillSignInfo(Map<String, String> params) {
        Assert.notEmpty(params);
        return cuntaoNewBailService.fillSignInfo(params);
    }

    @Override
    public ResultModel<String> queryUserFreezeAmount(Long taobaoUserId, UserTypeEnum userTypeEnum) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(userTypeEnum);
        return cuntaoNewBailService.queryUserFreezeAmount(taobaoUserId, userTypeEnum);
    }
}
