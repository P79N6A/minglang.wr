package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;

/**
 * 合作伙伴保证金冻结金额配置
 * @author zhenhuan.zhangzh
 *
 */
@RefreshScope
@Component
public class FrozenMoneyAmountConfig {

	@Value("${tp.frozenMoneyAmount:13000}")
	Double TPFrozenMoneyAmount;
	
	@Value("${tpa.frozenMoneyAmount:1}")
	Double TPAFrozenMoneyAmount;

	@Value("${tpt.frozenMoneyAmount:30000}")
	Double TPTFrozenMoneyAmount;
	
	@Value("${tps.frozenMoneyAmount:20000}")
	Double TPSFrozenMoneyAmount;

	public Double getTPFrozenMoneyAmount() {
		return TPFrozenMoneyAmount;
	}

	public void setTPFrozenMoneyAmount(Double tPFrozenMoneyAmount) {
		TPFrozenMoneyAmount = tPFrozenMoneyAmount;
	}

	public Double getTPAFrozenMoneyAmount() {
		return TPAFrozenMoneyAmount;
	}

	public void setTPAFrozenMoneyAmount(Double tPAFrozenMoneyAmount) {
		TPAFrozenMoneyAmount = tPAFrozenMoneyAmount;
	}

	public Double getTPTFrozenMoneyAmount() {
		return TPTFrozenMoneyAmount;
	}

	public void setTPTFrozenMoneyAmount(Double tPTFrozenMoneyAmount) {
		TPTFrozenMoneyAmount = tPTFrozenMoneyAmount;
	}

	
	public Double getFrozenMoneyByType(String type){
		if(PartnerInstanceTypeEnum.TP.getCode().equals(type)){
			return TPFrozenMoneyAmount;
		}
		if(PartnerInstanceTypeEnum.TPA.getCode().equals(type)){
			return TPAFrozenMoneyAmount;
		}
		if(PartnerInstanceTypeEnum.TPT.getCode().equals(type)){
			return TPTFrozenMoneyAmount;
		}
		if(PartnerInstanceTypeEnum.TPS.getCode().equals(type)){
			return TPSFrozenMoneyAmount;
		}
		throw new IllegalArgumentException(type+" not defined frozenMoney");
	}
}
