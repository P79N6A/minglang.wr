package com.taobao.cun.auge.punish.service.impl;

import com.taobao.cun.auge.client.result.ResultModel;
import com.taobao.cun.auge.punish.PartnerPunishService;
import com.taobao.cun.auge.punish.bo.PartnerPunishBo;
import com.taobao.cun.auge.punish.dto.ViolationPunishInfoDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("partnerPunishService")
@HSFProvider(serviceInterface = PartnerPunishService.class)
public class PartnerPunishServiceImpl implements PartnerPunishService {


    @Autowired
    private PartnerPunishBo partnerPunishBo;


    @Override
    public ViolationPunishInfoDto getVoilationPunishInfoDto(Long taobaoUserId) {

        return partnerPunishBo.getVoilationPunishInfoDto(taobaoUserId);
    }

    @Override
    public ResultModel<ViolationPunishInfoDto> getVoilationPunishInfoDto4Mobile(Long taobaoUserId) {

        return partnerPunishBo.getVoilationPunishInfoDto4Mobile(taobaoUserId);
    }

    @Override
    public Map<Long, ViolationPunishInfoDto> getVoilationPunishInfoDtoByuserIds(List<Long> taobaoUserIds) {
        return partnerPunishBo.getVoilationPunishInfoDtoByuserIds(taobaoUserIds);
    }
}
