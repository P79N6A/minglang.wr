package com.taobao.cun.auge.alipay.service;

import com.taobao.cun.auge.alipay.dto.AlipayFaceToFaceOrderInfo;
import com.taobao.cun.auge.alipay.dto.AlipayFaceToFacePaymentResult;

public interface AlipayFaceToFacePaymentService {


    public AlipayFaceToFacePaymentResult createTwoLevelMerchants(Long taobaoUserId);


    public AlipayFaceToFaceOrderInfo getAlipayFaceToFaceOrderInfoByTaobaoUserId(Long taobaoUserId) throws Exception;


    public void updateAlipayFaceToFaceOrderInfoByTaobaoUserId(Long taobaoUserId) throws Exception;


}
