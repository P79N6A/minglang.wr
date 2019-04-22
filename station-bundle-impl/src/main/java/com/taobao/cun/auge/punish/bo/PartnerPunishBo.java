package com.taobao.cun.auge.punish.bo;

import com.taobao.cun.auge.client.result.ResultModel;
import com.taobao.cun.auge.punish.dto.ViolationPunishInfoDto;

import java.util.List;
import java.util.Map;

public interface PartnerPunishBo {


    /**
     * 根据淘宝userId查询当前用户的违规处罚扣分等信息
     * @param taobaoUserId
     * @return
     */
    ViolationPunishInfoDto getVoilationPunishInfoDto(Long taobaoUserId);


    /**
     * 根据淘宝userIds批量查询当前用户的违规处罚扣分等信息
     * @param taobaoUserIds
     * @return
     */
    Map<Long,ViolationPunishInfoDto>  getVoilationPunishInfoDtoByuserIds(List<Long> taobaoUserIds);


    /**
     * 根据淘宝userId查询当前用户的违规处罚扣分等信息（无线专用）
     * @param taobaoUserId
     * @return
     */
    ResultModel<ViolationPunishInfoDto> getVoilationPunishInfoDto4Mobile(Long taobaoUserId);



}
