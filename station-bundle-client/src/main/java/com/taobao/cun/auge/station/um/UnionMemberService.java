package com.taobao.cun.auge.station.um;

import com.taobao.cun.auge.common.OperatorDto;
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
     * 新增优盟
     *
     * @param addDto
     * @return 返回优盟门店id
     */
    Long addUnionMember(UnionMemberAddDto addDto);

    /**
     * 修改优盟，只有所属村小二可以操作
     *
     * @param updateDto
     */
    void updateUnionMember(UnionMemberUpdateDto updateDto);

    /**
     * 开通或关闭优盟，只有所属村小二可以操作
     *
     * @param stateChangeDto
     */
    void openOrCloseUnionMember(UnionMemberStateChangeDto stateChangeDto);

    /**
     * 删除优盟
     *
     * @param stationId
     * @param operatorDto
     */
    void deleteUnionMember(Long stationId, OperatorDto operatorDto);

    /**
     * 测试接口，不可对外暴露
     * @param stationNums
     */
    @Deprecated
    public void updateUmstationNum(String stationNums);
}
