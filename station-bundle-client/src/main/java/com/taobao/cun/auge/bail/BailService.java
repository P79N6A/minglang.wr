package com.taobao.cun.auge.bail;

import java.util.Map;

import com.taobao.cun.settle.bail.dto.CuntaoFreezeBailDto;
import com.taobao.cun.settle.bail.dto.CuntaoTransferBailDto;
import com.taobao.cun.settle.bail.dto.CuntaoTransferBailForPunishDto;
import com.taobao.cun.settle.bail.dto.CuntaoUnFreezeBailDto;
import com.taobao.cun.settle.bail.enums.BailChannelEnum;
import com.taobao.cun.settle.bail.enums.UserTypeEnum;
import com.taobao.cun.settle.common.model.ResultModel;

/**
 * Created by xujianhui on 16/12/27.
 */
public interface BailService {
    /**
     * 用户保证金是否签约
     * @param alipayId 支付账户Id:16位数字字符串,对应BasePaymentAccountDO中的accountNo;淘宝域绑定后缀是“0156”，1688域绑定后缀无“0156”
     * @return
     */
    public ResultModel<Boolean> isUserSignBail(Long taobaoUserId, String alipayId, UserTypeEnum userTypeEnum);

    /**
     * 生成用户保证金签约地址
     * @return
     */
    @Deprecated
    public ResultModel<String> buildSignBailUrl(Long taobaoUserId, UserTypeEnum userTypeEnum, String returnUrl, BailChannelEnum channel);

    /**
     * 生成用户保证金签约地址
     * @return
     */
    public ResultModel<String> buildSignBailUrl(Long taobaoUserId, String outOrderNo, UserTypeEnum userTypeEnum, String returnUrl, BailChannelEnum channel);


    /**
     * 保证金解冻接口
     * @param cuntaoUnFreezeBailDto 可以使用BaiDtoBuilder.generateUnfreezeBailDto构建
     * @return
     */
    public ResultModel<Boolean> unfreezeUserBail(CuntaoUnFreezeBailDto cuntaoUnFreezeBailDto);

    /**
     * 保证金冻结接口
     * @param cuntaoFreezeBailDto 可以使用BaiDtoBuilder.generateFreezeBailDto构建
     * @return
     */
    public ResultModel<Boolean> freezeUserBail(CuntaoFreezeBailDto cuntaoFreezeBailDto);

    /**
     * 保证金转移接口
     * @param cuntaoTransferBailDto
     * @return
     */
    public ResultModel<Boolean> transferUserBail(CuntaoTransferBailDto cuntaoTransferBailDto);
    /**
     * 保证金转移接口－处罚
     * @param cuntaoTransferBailForPunishDto
     * @return
     */
    public ResultModel<Boolean> transferUserBailForPunish(CuntaoTransferBailForPunishDto cuntaoTransferBailForPunishDto);

    /**
     * 支付宝回调签名验证
     * @param params
     * @return
     */
    public ResultModel<Boolean> checkAlipaySign(Map<String,String> params);

    /**
     * 补充签约信息(协议号) return_url和notify_url的post请求里需要调用此接口
     * @return
     */
    public ResultModel<Boolean> fillSignInfo(Map<String, String> params);

    /**
     * 查询用户保证金余额
     * @param taobaoUserId
     * @return
     */
    public ResultModel<String> queryUserFreezeAmount(Long taobaoUserId,UserTypeEnum userTypeEnum);
    
    /**
     * 解冻铺货保证金
     * @param cuntaoUnFreezeBailDto
     * @return
     */
    public ResultModel<Boolean> unfreezeUserReplenishBail(Long partnerInstanceId);
    
    /**
     * 冻结铺货金
     * @param partnerInstanceId
     * @return
     */
    public Boolean freezeUserReplenishBail(Long partnerInstanceId);

}