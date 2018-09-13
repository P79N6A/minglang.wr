package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.alipay.dto.AlipayRiskScanData;
import com.taobao.cun.auge.alipay.dto.AlipayRiskScanResult;
import com.taobao.cun.auge.alipay.service.AlipayRiskScanService;
import com.taobao.cun.auge.station.bo.TaobaoAccountBo;
import com.taobao.namelist.model.NamelistResult;
import com.taobao.namelist.param.NamelistMatchParam;
import com.taobao.namelist.service.NamelistMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("taobaoAccountBo")
public class TaobaoAccountBoImpl implements TaobaoAccountBo {

    private static final Logger logger = LoggerFactory.getLogger(TaobaoAccountBo.class);

    @Autowired
    private NamelistMatchService namelistMatchService;

    @Autowired
    private AlipayRiskScanService alipayRiskScanService;

    @Override
    public boolean isTaobaoBuyerOrSellerBlack(Long taobaoUserId) {
        if (null == taobaoUserId) {
            return Boolean.FALSE;
        }
        // 黑白名单
        NamelistMatchParam buyer = new NamelistMatchParam();
        buyer.setIdentifier("sm"); // 买家名单表标识
        buyer.setValue(String.valueOf(taobaoUserId));// 名单表里的类型的值
        NamelistMatchParam seller = new NamelistMatchParam();
        seller.setIdentifier("R_172"); // 买家名单表标识
        seller.setValue(String.valueOf(taobaoUserId));// 名单表里的类型的值
        List<NamelistMatchParam> list = new ArrayList<NamelistMatchParam>();
        list.add(buyer);
        list.add(seller);
        Map<NamelistMatchParam, NamelistResult<Boolean>> map = namelistMatchService.batchMatch(list);
        NamelistResult<Boolean> bResult = map.get(buyer);
        // 买家黑名单
        if (null != bResult && bResult.isSuccess() && bResult.getValue()) {
            return Boolean.TRUE;
        }
        NamelistResult<Boolean> sResult = map.get(seller);
        // 卖家黑名单
        if (null != sResult && sResult.isSuccess() && sResult.getValue()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean isAlipayRiskUser(Long taobaoUserId) {
        AlipayRiskScanResult risk = alipayRiskScanService.checkEntryRisk(taobaoUserId);
        if (risk != null && risk.isSuccess()) {
            AlipayRiskScanData riskData = risk.getData();
            return riskData.isRisk();
        }
        logger.error("alipayRiskScanService error", risk.getErrorMsg());
        // 如果支付宝效验异常暂时放过
        return false;
    }


}
