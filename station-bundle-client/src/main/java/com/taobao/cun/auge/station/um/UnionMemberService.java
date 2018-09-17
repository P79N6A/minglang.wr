package com.taobao.cun.auge.station.um;

import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.station.um.dto.UnionMemberAddDto;
import com.taobao.cun.auge.station.um.dto.UnionMemberCheckDto;
import com.taobao.cun.auge.station.um.dto.UnionMemberStateChangeDto;
import com.taobao.cun.auge.station.um.dto.UnionMemberUpdateDto;

/**
 * 优盟管理服务
 *
 * @author haihu.fhh
 */
public interface UnionMemberService {

    /**
     * 入驻之前，校验优盟账号，合法性
     *
     * @param checkDto
     * @return
     */
    AliPaymentAccountDto checkUnionMember(UnionMemberCheckDto checkDto);

    /**
     * 新增优盟,但不开通
     *
     * @param addDto
     * @return
     */
    Long addUnionMember(UnionMemberAddDto addDto);

    /**
     * 修改优盟
     *
     * @param updateDto
     */
    void updateUnionMember(UnionMemberUpdateDto updateDto);

    /**
     * 开通或关闭优盟
     *
     * @param stateChangeDto
     */
    void openOrCloseUnionMember(UnionMemberStateChangeDto stateChangeDto);

}
