package com.taobao.cun.auge.bail.service.impl;

import com.taobao.cun.auge.bail.BailService;
import com.taobao.cun.settle.bail.dto.*;
import com.taobao.cun.settle.bail.enums.BailChannelEnum;
import com.taobao.cun.settle.bail.enums.UserTypeEnum;
import com.taobao.cun.settle.bail.service.CuntaoNewBailService;
import com.taobao.cun.settle.common.model.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
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

    @Autowired
    private CuntaoNewBailService cuntaoNewBailService;

    @Override
    public ResultModel<Boolean> isUserSignBail(Long taobaoUserId, String alipayId, UserTypeEnum userTypeEnum) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(userTypeEnum);
        return cuntaoNewBailService.isUserSignBail(taobaoUserId, alipayId, userTypeEnum);
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
        return cuntaoNewBailService.buildSignBailUrl(signDto);
    }

    @Override
    public ResultModel<Boolean> unfreezeUserBail(CuntaoUnFreezeBailDto cuntaoUnFreezeBailDto) {
        String outOrderId = cuntaoUnFreezeBailDto.getOutOrderId() + "UNFREEZE";
        cuntaoUnFreezeBailDto.setOutOrderId(outOrderId);
        return cuntaoNewBailService.unfreezeUserBail(cuntaoUnFreezeBailDto);
    }

    @Override
    public ResultModel<Boolean> freezeUserBail(CuntaoFreezeBailDto cuntaoFreezeBailDto) {
        return cuntaoNewBailService.freezeUserBail(cuntaoFreezeBailDto);
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
