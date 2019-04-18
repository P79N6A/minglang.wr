package com.taobao.cun.auge.punish.service.impl;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.client.result.ResultModel;
import com.taobao.cun.auge.punish.PartnerPunishService;
import com.taobao.cun.auge.punish.dto.ViolationPunishInfoDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.ruledata.domain.AuthKey;
import com.taobao.ruledata.domain.result.SimpleResult;
import com.taobao.ruledata.domain.rightcenter.PunishValue;
import com.taobao.ruledata.domain.rightcenter.RcRetMessage;
import com.taobao.ruledata.service.rightcenter.RightQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("partnerPunishService")
@HSFProvider(serviceInterface = PartnerPunishService.class)
public class PartnerPunishServiceImpl implements PartnerPunishService {


    @Value("{rightcentor.punish.right.id}")
    private String PUNISH_RIGHT_ID;

    @Value("{rightcentor.punish.app.secret}")
    private String APP_SECRET;


    @Autowired
    private RightQueryService rightQueryService;


    @Override
    public ViolationPunishInfoDto getVoilationPunishInfoDto(Long taobaoUserId) {

        if(taobaoUserId == null){
            return null;
        }
       return getVoilationPunishInfoDtoByUserId(taobaoUserId);
    }

    @Override
    public ResultModel<ViolationPunishInfoDto> getVoilationPunishInfoDto4Mobile(Long taobaoUserId) {

        if(taobaoUserId == null){
            return null;
        }
        ResultModel<ViolationPunishInfoDto> resultModel = new ResultModel<>();
        try{
            ViolationPunishInfoDto violationPunishInfoDto = getVoilationPunishInfoDtoByUserId(taobaoUserId);
            resultModel.setSuccess(true);
            resultModel.setResult(violationPunishInfoDto);
        }catch (Exception ex){
            resultModel.setSuccess(false);
        }
        return resultModel;
    }


    private ViolationPunishInfoDto getVoilationPunishInfoDtoByUserId(Long taobaoUserId){
        AuthKey authKey = new AuthKey();
        authKey.setAppName("auge");
        authKey.setSecret(APP_SECRET);
        //objectType:1代表卖家，2代表身份证
        SimpleResult<RcRetMessage> simpleResult = rightQueryService.objectHasRight(1, taobaoUserId.toString(), Integer.valueOf(PUNISH_RIGHT_ID), authKey);
        ViolationPunishInfoDto violationPunishInfoDto = null;
        if(simpleResult.isSuccess()){
            List<PunishValue> punishValueList = simpleResult.getData().getPunishValueList();
            violationPunishInfoDto = new ViolationPunishInfoDto();
            for (PunishValue punishValue : punishValueList) {
                if(punishValue.getIndicateName().equals("严重违规总分数")){
                    violationPunishInfoDto.setSeriousViolationPoints(Integer.valueOf(punishValue.getValue().toString()));
                }else if(punishValue.getIndicateName().equals("严重违规总分数")){
                    violationPunishInfoDto.setGeneralViolationPoints(Integer.valueOf(punishValue.getValue().toString()));
                }else if(punishValue.getIndicateName().equals("自然年违规次数")){
                    violationPunishInfoDto.setTotalIllegalCount(Integer.valueOf(punishValue.getValue().toString()));
                }
            }

        }
        return violationPunishInfoDto;
    }


    public static void main(String[] args) {

        ViolationPunishInfoDto violationPunishInfoDto = new ViolationPunishInfoDto();
        violationPunishInfoDto.setGeneralViolationPoints(15);
        violationPunishInfoDto.setSeriousViolationPoints(19);
        violationPunishInfoDto.setTotalIllegalCount(10);
        System.out.println(JSON.toJSONString(violationPunishInfoDto));
    }
}
