package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.station.dto.PartnerSopRltDto;

import java.util.Map;

/**
 * 提供给sop的专属接口
 * 将查询权限通过SOP前置到咨询端，以提升小二的解答能力，保障客户体验。
 */
public interface PartnerSopService {

    /**
     * 通过淘宝账号 查询 入驻状态，村点状态，县负责人等信息。
     * @param taobaoUserId
     * @return
     */
    public Result<PartnerSopRltDto> getPartnerInfo(Long taobaoUserId);

    /**
     * 通过淘宝账号 查询 入驻状态，村点状态，县负责人等信息。
     * @param taobaoNick
     * @return
     */
    public Result<PartnerSopRltDto> getPartnerInfoByTaobaoNick(String taobaoNick);
}
