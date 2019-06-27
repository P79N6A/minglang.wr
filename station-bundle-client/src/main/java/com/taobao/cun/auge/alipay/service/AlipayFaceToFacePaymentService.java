package com.taobao.cun.auge.alipay.service;

import com.taobao.cun.auge.alipay.dto.AlipayFaceToFacePaymentResult;

public interface AlipayFaceToFacePaymentService {


    public AlipayFaceToFacePaymentResult createTwoLevelMerchants(Long taobaoUserId);


}
